package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gatewaysolution.nearbyservice.Model.StatusifInserted;
import com.gatewaysolution.nearbyservice.Utils.ApiClient;
import com.gatewaysolution.nearbyservice.Utils.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankLoan extends AppCompatActivity {
    private AppCompatEditText etName, etPurpose, etLoanLength, etInterest;
    private Spinner spLoanType;
    private RadioGroup rgCreditCheck, rgCollateral;
    private AppCompatButton btnSubmit,cancel;
    AppCompatImageView imgViewBack,imgdownload ;
    RelativeLayout llActionbar;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_loan);
        llActionbar = findViewById(R.id.llActionbar);
        imgViewBack = findViewById(R.id.imgViewBack);
        cancel=findViewById(R.id.btnCancel);
        etName = findViewById(R.id.etName);
        etPurpose = findViewById(R.id.etPurpose);
        spLoanType = findViewById(R.id.sploan);
        etLoanLength = findViewById(R.id.etloan);
        etInterest = findViewById(R.id.etInterest);
        rgCreditCheck = findViewById(R.id.radio_credit_check);
        rgCollateral = findViewById(R.id.radio_collateral);
        btnSubmit = findViewById(R.id.btnSubmit);
        dialog = new ProgressDialog(BankLoan.this);
        dialog.setCancelable(true);
        dialog.setMessage("Please wait ...");
        btnSubmit.setOnClickListener(v -> submitBankLoan());
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear EditText fields
                etName.setText("");
                etPurpose.setText("");
                etLoanLength.setText("");
                etInterest.setText("");

                // Reset Spinner to default position
                spLoanType.setSelection(0); // Assuming index 0 is "Select Loan Type"

                // Clear RadioGroup selections
                rgCreditCheck.clearCheck();
                rgCollateral.clearCheck();

                // Optionally, remove error messages if any were set
                etName.setError(null);
                etPurpose.setError(null);
                etLoanLength.setError(null);
                etInterest.setError(null);

                // Optionally, reset focus to the top of the layout or any specific view
                findViewById(R.id.llActionbar).requestFocus();
            }
        });
    }

    private void submitBankLoan() {
        String bankName = etName.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();
        String loanType = spLoanType.getSelectedItem().toString().trim();
        String loanLength = etLoanLength.getText().toString().trim();
        String interestRate = etInterest.getText().toString().trim();
        String creditCheck = rgCreditCheck.getCheckedRadioButtonId() == -1 ? "" : ((RadioButton) findViewById(rgCreditCheck.getCheckedRadioButtonId())).getText().toString().trim();
        String collateral = rgCollateral.getCheckedRadioButtonId() == -1 ? "" : ((RadioButton) findViewById(rgCollateral.getCheckedRadioButtonId())).getText().toString().trim();

        // Validate fields
        if (bankName.isEmpty()) {
            etName.setError("Bank Name is required");
            etName.requestFocus();
            return;
        }
        if (purpose.isEmpty()) {
            etPurpose.setError("Purpose is required");
            etPurpose.requestFocus();
            return;
        }
        if (loanType.equals("Select Loan Type")) {
            Toast.makeText(this, "Please select a valid loan type", Toast.LENGTH_SHORT).show();
            spLoanType.requestFocus();
            return;
        }
        if (loanLength.isEmpty()) {
            etLoanLength.setError("Loan Length is required");
            etLoanLength.requestFocus();
            return;
        }
        if (interestRate.isEmpty()) {
            etInterest.setError("Interest Rate is required");
            etInterest.requestFocus();
            return;
        }
        if (creditCheck.isEmpty()) {
            Toast.makeText(this, "Please select a credit check option", Toast.LENGTH_SHORT).show();
            rgCreditCheck.requestFocus();
            return;
        }
        if (collateral.isEmpty()) {
            Toast.makeText(this, "Please select a collateral option", Toast.LENGTH_SHORT).show();
            rgCollateral.requestFocus();
            return;
        }


        sendBankLoanDetails(bankName, purpose, loanType, loanLength, interestRate, creditCheck, collateral);
    }

    private void sendBankLoanDetails(String bankName, String purpose, String loanType, String loanLength, String interestRate, String creditCheck, String collateral) {

        dialog = ProgressDialog.show(BankLoan.this, "Update Details", "Please wait...", true);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<StatusifInserted> call = apiInterface.insertLoanDetails(bankName, purpose, loanType, loanLength, interestRate, creditCheck, collateral);
        call.enqueue(new Callback<StatusifInserted>() {
            @Override
            public void onResponse(Call<StatusifInserted> call, Response<StatusifInserted> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    StatusifInserted status = response.body();
                    Gson gson = new Gson();
                    String json = gson.toJson(status);
                    Log.e("insert", json);
                    if (status.getResult().getStatus().equals("1")) {
                        Toast.makeText(getApplicationContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error saving data on server", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<StatusifInserted> call, Throwable t) {
                dialog.dismiss();
                Log.e("insert", t.toString());

            }
        });
    }
}