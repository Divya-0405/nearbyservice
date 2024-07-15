package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.gatewaysolution.nearbyservice.Adapter.EmployeeAdapter;
import com.gatewaysolution.nearbyservice.Model.ulist;
import com.gatewaysolution.nearbyservice.Utils.ApiClient;
import com.gatewaysolution.nearbyservice.Utils.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeList extends AppCompatActivity {
    AppCompatImageView imgViewBack,imgdownload ;
    private List<ulist> getEmps=new ArrayList<>();
    RecyclerView emp_list;
    ProgressDialog dialog;
    EmployeeAdapter employeeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        imgViewBack = findViewById(R.id.imgViewBack);
        imgdownload = findViewById(R.id.imgdownload);
        emp_list = findViewById(R.id.call_list);
        dialog = new ProgressDialog(EmployeeList.this);
        dialog.setCancelable(true);
        dialog.setMessage("Please wait ...");
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        loadEmployee();
    }

    private void loadEmployee() {
        try {
            dialog = ProgressDialog.show(EmployeeList.this, "Get Details", "Please wait...", true);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<List<ulist>> call = apiInterface.getUser();
            call.enqueue(new Callback<List<ulist>>() {
                @Override
                public void onResponse(Call<List<ulist>> call, Response<List<ulist>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        getEmps=response.body();
                        if (getEmps.size() > 0) {
                            Gson gson = new Gson();
                            String json = gson.toJson(getEmps);
                            Log.e("getEmps", json);
                            employeeAdapter = new EmployeeAdapter(getEmps, EmployeeList.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(EmployeeList.this, RecyclerView.VERTICAL, false);
                            emp_list.setLayoutManager(mLayoutManager);
                            emp_list.setAdapter(employeeAdapter);

                        } else {
                            emp_list.setAdapter(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<ulist>> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("el err",t.toString());
                }
            });

       }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }
    }
}