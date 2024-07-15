package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gatewaysolution.nearbyservice.Adapter.hospitalAdapter;
import com.gatewaysolution.nearbyservice.Model.getLocationList;
import com.gatewaysolution.nearbyservice.Utils.ApiClient;
import com.gatewaysolution.nearbyservice.Utils.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Banks extends AppCompatActivity {
    EditText locationname,locationtype;
    AppCompatButton btnSubmit,btnCancel;
    ProgressDialog dialog;
    private List<getLocationList> slists = new ArrayList<>();
    com.gatewaysolution.nearbyservice.Adapter.hospitalAdapter hospitalAdapter;
    RecyclerView cat_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banks);
        locationname=findViewById(R.id.etEmailId);
        locationtype=findViewById(R.id.etpassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        cat_list=findViewById(R.id.rv_cate);
        dialog = new ProgressDialog(Banks.this);
        dialog.setCancelable(true);
        dialog.setMessage("Please wait ...");
        btnSubmit.setOnClickListener(view -> {
            String lname=locationname.getText().toString().trim();
            String  ltype=locationtype.getText().toString().trim();

            if(lname.equalsIgnoreCase("")){
                locationname.setError("Enter Location Name");
                // Toast.makeText(StudentEntroll.this, "Set Valid Date", Toast.LENGTH_SHORT).show();
            }
            else if(ltype.equalsIgnoreCase("")){
                locationtype.setError("Enter Bank Type");
                // Toast.makeText(StudentEntroll.this, "Set Valid Date", Toast.LENGTH_SHORT).show();
            }
            else{
                getCoordinatesFromLocation(lname,ltype);
            }
        });

        btnCancel.setOnClickListener(view -> {
            locationname.setText("");locationtype.setText("");
            slists.clear();
            cat_list.setAdapter(null);
        });
    }
    private void getCoordinatesFromLocation(String lname, String ltype) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(lname, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Use the location data (latitude and longitude)
                Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                double distance = 0.05;
                double left = longitude - distance;
                double right = longitude + distance;
                double top = latitude + distance;
                double bottom = latitude - distance;

                dialog = ProgressDialog.show(Banks.this, "get Details ", "Please wait...", true);
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<List<getLocationList>> call=apiInterface.getHospital(left+"",top+"",right+"",bottom+"",ltype);
                call.enqueue(new Callback<List<getLocationList>>() {
                    @Override
                    public void onResponse(Call<List<getLocationList>> call, Response<List<getLocationList>> response) {
                        if (response.isSuccessful()) {
                            dialog.dismiss();
                            slists=response.body();
                            Gson gson = new Gson();
                            String json = gson.toJson(slists);
                            Log.e("slists", json);
                            if(slists.size()>0){
                                hospitalAdapter=new hospitalAdapter(slists,getApplication());
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                                cat_list.setLayoutManager(mLayoutManager);
                                cat_list.setAdapter(hospitalAdapter);
                            }
                            else{
                                cat_list.setAdapter(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<getLocationList>> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("verify err", t.getMessage());
                    }
                });

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to get coordinates. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goback(View view) {
        onBackPressed();
    }
}