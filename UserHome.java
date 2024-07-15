package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class UserHome extends AppCompatActivity {
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Intent intenta = getIntent();
        uid = intenta.getStringExtra("uid");
        Log.e("uid",uid);
    }

    public void goback(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    public void gotohospital(View view) {
        Intent intent = new Intent(getApplicationContext(), Hospital.class);
        startActivity(intent);

    }

    public void gotobank(View view) {
        Intent intent = new Intent(getApplicationContext(), Banks.class);
        startActivity(intent);

    }

    public void gotobanklist(View view) {
        Intent intent = new Intent(getApplicationContext(), LoanList.class);
        startActivity(intent);
    }

    public void gotohealth(View view) {
        Intent intent = new Intent(getApplicationContext(), HealthInsurance.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}