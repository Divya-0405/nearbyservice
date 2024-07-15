package com.gatewaysolution.nearbyservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gatewaysolution.nearbyservice.Model.login;
import com.gatewaysolution.nearbyservice.Utils.ApiClient;
import com.gatewaysolution.nearbyservice.Utils.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    Button loginBtn;
    TextView forgetpwd, register;
    EditText email, pwd;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_btnlogin);
        forgetpwd = findViewById(R.id.login_forgetpassword);
        email = findViewById(R.id.login_email);
        pwd = findViewById(R.id.login_password);
        register = findViewById(R.id.login_btn_register);
        loadingBar = new ProgressDialog(Login.this);

        loginBtn.setOnClickListener(view -> {
            loadingBar.setTitle("Login User");
            loadingBar.setMessage("Please Wait while Validate the Details");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //get input details
            String emailInput = email.getText().toString();
            String pwdInput = pwd.getText().toString();
            if (checkemail()) {
                try {
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<login> call = apiInterface.login_staff(emailInput, pwdInput);
                    call.enqueue(new Callback<login>() {
                        @Override
                        public void onResponse(Call<login> call, Response<login> response) {
                            loadingBar.dismiss(); // Dismiss loading bar on response
                            if (response.isSuccessful()) {
                                login ls = response.body();
                                if (ls.getStatus().equalsIgnoreCase("1")) {
                                    Intent intent = new Intent(Login.this, UserHome.class);
                                    intent.putExtra("uid", ls.getStfId());
                                    intent.putExtra("rcode", ls.getScode());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    email.setText("");
                                    pwd.setText("");
                                    Toast.makeText(getApplicationContext(), "Username or Password is Incorrect", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<login> call, Throwable t) {
                            loadingBar.dismiss(); // Dismiss loading bar on failure
                            Toast.makeText(getApplicationContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception ex) {
                    loadingBar.dismiss(); // Dismiss loading bar on exception
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (emailInput.equals("admin") && pwdInput.equals("admin")) {
                    loadingBar.dismiss(); // Dismiss loading bar for admin login
                    Toast.makeText(getApplicationContext(), "Admin Login success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, AdminHome.class);
                    startActivity(intent);
                    finish();
                } else {
                    loadingBar.dismiss(); // Dismiss loading bar for incorrect credentials
                    Toast.makeText(getApplicationContext(), "Username or Password is Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotosignup(View view) {
        Intent intent = new Intent(getApplicationContext(), Signup.class);
        startActivity(intent);
        finish();
    }

    private boolean checkemail() {
        String emailInput = email.getText().toString();
        return !emailInput.equalsIgnoreCase("admin");
    }
}
