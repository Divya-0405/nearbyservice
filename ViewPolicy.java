package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gatewaysolution.nearbyservice.Model.get_policy;
import com.gatewaysolution.nearbyservice.Model.ulist;
import com.gatewaysolution.nearbyservice.Utils.ApiClient;
import com.gatewaysolution.nearbyservice.Utils.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPolicy extends AppCompatActivity {
    String uid;
    ImageView imgViewBack;
    private List<get_policy> getEmps=new ArrayList<>();
    TextView tvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_policy);
        Intent intenta = getIntent();
        uid = intenta.getStringExtra("uid");
        imgViewBack = findViewById(R.id.imgViewBack);
        tvDate = findViewById(R.id.tvDate);
        imgViewBack.setOnClickListener(v -> {
            // Validate fields before submitting
            onBackPressed();
        });
        loadEmployee();
    }

    private void loadEmployee() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<get_policy>> call = apiInterface.getpolicy(uid);
        call.enqueue(new Callback<List<get_policy>>() {
            @Override
            public void onResponse(Call<List<get_policy>> call, Response<List<get_policy>> response) {
                if (response.isSuccessful()) {
                    getEmps=response.body();
                    if (getEmps.size() > 0) {
                        tvDate.setText(" Name: "+getEmps.get(0).getName()+"\n Dob: "+getEmps.get(0).getDob()+"\n PlanType: "+getEmps.get(0).getPlanType()+"\n DueDate: "+getEmps.get(0).getDueDate()+"\n Registration No: "+getEmps.get(0).getRegistrationNo()+"\n Address: "+getEmps.get(0).getAddress()+"\n Contact Number: "+getEmps.get(0).getContactNumber());
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"No Data Available",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<get_policy>> call, Throwable t) {

            }
        });
    }
}