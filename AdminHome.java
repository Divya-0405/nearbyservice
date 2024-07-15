package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void goback(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    public void gotouser(View view) {
        Intent intent = new Intent(getApplicationContext(), EmployeeList.class);
        startActivity(intent);
    }

    public void gotobank(View view) {
        Intent intent = new Intent(getApplicationContext(), BankLoan.class);
        startActivity(intent);
    }
}