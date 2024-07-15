package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gatewaysolution.nearbyservice.Model.Result2;
import com.gatewaysolution.nearbyservice.Model.StatusifInserted;
import com.gatewaysolution.nearbyservice.Utils.ApiClient;
import com.gatewaysolution.nearbyservice.Utils.ApiInterface;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthInsurance extends AppCompatActivity {
    private EditText etName, etRegistrationNo, etContactNumber, etEmail, etAddress;
    private Spinner spPlanType;
    private Button btnSubmit,btnCancel;
    ImageView imgViewBack;
    TextView tvDueDate, etDob;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_insurance);
        Intent intenta = getIntent();
        uid = intenta.getStringExtra("uid");
        Log.e("HealthInsurance",uid);
        imgViewBack = findViewById(R.id.imgViewBack);
        etName = findViewById(R.id.etsname);
        etDob = findViewById(R.id.tvDate);
        tvDueDate = findViewById(R.id.tvDueDate);
        spPlanType = findViewById(R.id.spsem);
        etRegistrationNo = findViewById(R.id.ettname);
        etContactNumber = findViewById(R.id.etPhoneNum);
        etEmail = findViewById(R.id.etEmailId);
        etAddress = findViewById(R.id.etpassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etDob);
            }
        });

        tvDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2(tvDueDate);
            }
        });
        imgViewBack.setOnClickListener(v -> {
            // Validate fields before submitting
           onBackPressed();
        }); btnSubmit.setOnClickListener(v -> {
            // Validate fields before submitting
            if (validateForm()) {
                // All fields are valid, proceed to submit
                submitDetails();
            }
        }); btnCancel.setOnClickListener(v -> {
            // Validate fields before submitting
            Intent intent = new Intent(getApplicationContext(), ViewPolicy.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });
    }

    public void goback(View view) {
        onBackPressed();
        finish();
    }

    private void submitDetails() {

        String name = etName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String planType = spPlanType.getSelectedItem().toString();
        String registrationNo = etRegistrationNo.getText().toString().trim();
        String contactNumber = etContactNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String tdate = tvDueDate.getText().toString().trim();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Result2> call = apiInterface.insertHealthInsurance(
                uid,
                name,
                dob,
                planType,
                registrationNo,
                contactNumber,
                email,
                address,
                tdate
        );

        call.enqueue(new Callback<Result2>() {
            @Override
            public void onResponse(Call<Result2> call, Response<Result2> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result2 result = response.body();
                    if ("1".equals(result.getStatus())) {
                        // Handle successful insertion
                        Toast.makeText(HealthInsurance.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                       onBackPressed();
                    } else {
                        // Handle server response indicating failure
                        Toast.makeText(HealthInsurance.this, "Error saving data on server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle network or unexpected errors
                    Toast.makeText(getApplicationContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result2> call, Throwable t) {
                // Handle network errors or exceptions
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HealthInsurance", "Error: " + t.getMessage());
            }
        });

    }
    private boolean validateForm() {
        boolean isValid = true;

        String name = etName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String planType = spPlanType.getSelectedItem().toString();
        String registrationNo = etRegistrationNo.getText().toString().trim();
        String contactNumber = etContactNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String dueDate = tvDueDate.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter your name");
            isValid = false;
        }

        if (TextUtils.isEmpty(dob)) {
            etDob.setError("Select your date of birth");
            isValid = false;
        }

        if (TextUtils.isEmpty(planType) || planType.equals("Select Plan Type")) {
            Toast.makeText(this, "Please select a valid plan type", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (TextUtils.isEmpty(registrationNo)) {
            etRegistrationNo.setError("Enter registration number");
            isValid = false;
        }

        if (TextUtils.isEmpty(contactNumber)) {
            etContactNumber.setError("Enter contact number");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter email");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            isValid = false;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Enter address");
            isValid = false;
        }

        if (TextUtils.isEmpty(dueDate)) {
            tvDueDate.setError("Select due date");
            isValid = false;
        }

        return isValid;
    }
    public void showDatePickerDialog(final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; // Change as needed
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textView.setText(sdf.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void showDatePickerDialog2(final TextView tv) {
        // Get current date as default date for the DatePickerDialog
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected day in the TextView
                        String selectedDate = String.valueOf(dayOfMonth);
                        tv.setText(selectedDate);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}