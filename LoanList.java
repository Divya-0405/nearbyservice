package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gatewaysolution.nearbyservice.Adapter.BankLoanAdapter;
import com.gatewaysolution.nearbyservice.Model.BankLoanDetails;
import com.gatewaysolution.nearbyservice.Model.ulist;
import com.gatewaysolution.nearbyservice.Utils.ApiClient;
import com.gatewaysolution.nearbyservice.Utils.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanList extends AppCompatActivity {
    private AppCompatButton btnSubmit,cancel;
    AppCompatImageView imgViewBack;
    RelativeLayout llActionbar;
    ProgressDialog dialog;
    private Spinner spLoanType;
    RecyclerView recyclerView;
    BankLoanAdapter bankLoanAdapter;
    private List<BankLoanDetails> bankLoanDetailsList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_list);
        llActionbar = findViewById(R.id.llActionbar);
        imgViewBack = findViewById(R.id.imgViewBack);
        cancel=findViewById(R.id.btnCancel);
        recyclerView=findViewById(R.id.recyclerView);
        spLoanType = findViewById(R.id.spLoan);
        btnSubmit = findViewById(R.id.btnSubmit);
        dialog = new ProgressDialog(LoanList.this);
        dialog.setCancelable(true);
        dialog.setMessage("Please wait ...");
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String loanType = spLoanType.getSelectedItem().toString().trim();
                if (loanType.equals("Select Loan Type")) {
                    Toast.makeText(getApplicationContext(), "Please select a valid loan type", Toast.LENGTH_SHORT).show();
                    spLoanType.requestFocus();
                    return;
                }
                bankLoanDetailsList.clear();
                getbankloan(loanType);
            }
        });

    }

    private void getbankloan(String loanType) {
        dialog = ProgressDialog.show(LoanList.this, "Get Details", "Please wait...", true);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<BankLoanDetails>> call = apiInterface.getBank(loanType);
        call.enqueue(new Callback<List<BankLoanDetails>>() {
            @Override
            public void onResponse(Call<List<BankLoanDetails>> call, Response<List<BankLoanDetails>> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    bankLoanDetailsList=response.body();
                    Gson gson = new Gson();
                    String json = gson.toJson(bankLoanDetailsList);
                    Log.e("getEmps", json);
                    if(bankLoanDetailsList.size()>0){
                        bankLoanAdapter = new BankLoanAdapter(bankLoanDetailsList, getApplicationContext());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LoanList.this, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(bankLoanAdapter);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No detail Available", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<BankLoanDetails>> call, Throwable t) {
                dialog.dismiss();
                Log.e("el err",t.toString());

            }
        });
    }
}