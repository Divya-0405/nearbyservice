package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gatewaysolution.nearbyservice.Model.StatusifInserted;
import com.gatewaysolution.nearbyservice.Utils.ApiClient;
import com.gatewaysolution.nearbyservice.Utils.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {
    AppCompatButton save,cancel;
    TextInputEditText name_text,age_text,pass_text,address_text,wart_text,rc_code_text;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        save=findViewById(R.id.btnApplyFilter);
        cancel=findViewById(R.id.btnCancel);
        rc_code_text=findViewById(R.id.rc_code_text);
        name_text=findViewById(R.id.name_text);
        age_text=findViewById(R.id.age_text);
        pass_text=findViewById(R.id.pass_text);
        address_text=findViewById(R.id.address_text);
        wart_text=findViewById(R.id.wart_text);
        save.setOnClickListener(view -> {
            String a,b,c,d,e,f ;
            a=name_text.getText().toString().trim();
            b=age_text.getText().toString().trim();
            c=pass_text.getText().toString().trim();
            e=wart_text.getText().toString().trim();
            d=address_text.getText().toString().trim();
            f=rc_code_text.getText().toString().trim();
            progressDialog = new ProgressDialog(Signup.this);
            progressDialog.setMessage("Add Details to Server...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<StatusifInserted> call = apiInterface.insert_user(a,c,b,d,e,f);
            call.enqueue(new Callback<StatusifInserted>() {
                @Override
                public void onResponse(Call<StatusifInserted> call, Response<StatusifInserted> response) {
                    progressDialog.dismiss();
                    StatusifInserted status = response.body();
                    Gson gson = new Gson();
                    String json = gson.toJson(status);
                    Log.e("insert", json);
                    if (status.getResult().getStatus().equals("1")) {
                        Toast.makeText(Signup.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(Signup.this, "Error saving data on server", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<StatusifInserted> call, Throwable t) {
                    Log.e("insert", t.toString());

                }
            });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void goback(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}