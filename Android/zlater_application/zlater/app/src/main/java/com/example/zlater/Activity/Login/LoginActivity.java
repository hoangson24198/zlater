package com.example.zlater.Activity.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zlater.Activity.MainActivity;
import com.example.zlater.Activity.TutorialActivity;
import com.example.zlater.Model.Responses.UserResponse;
import com.example.zlater.Model.User;
import com.example.zlater.R;
import com.example.zlater.Service.remote.PolyFitService;
import com.example.zlater.Service.remote.RetrofitClient;
import com.example.zlater.Utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private EditText edt_username;
    private EditText edt_password;
    private TextView tvSignUp;
    private LinearLayout btn_Login;
    private PolyFitService polyFitService;
    private ProgressDialog progressDialog;

    private void connectView() {
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_Login = findViewById(R.id.btn_Login);
        tvSignUp = findViewById(R.id.tvSignUp);
        btn_Login.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        connectView();
        Retrofit retrofit = RetrofitClient.getInstance();
        polyFitService = retrofit.create(PolyFitService.class);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    private void loginUser(String userName, String password) {
        if(edt_username.getText().toString().equals("admin@polyfit.com")){
            Toast.makeText(this, "Admin can't login in this app", Toast.LENGTH_SHORT).show();
        }else {
            showProgressDialog();
            String token = FirebaseInstanceId.getInstance().getToken();
            User user = new User(userName, password,true,token);
            Call<UserResponse> calledLogin = polyFitService.loginUser(user);
            calledLogin.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful()) {
                        UserResponse userResponse = response.body();
                        if (userResponse.getStatus() == 0) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
                            SharedPreferences.Editor editor = getSharedPreferences(Constants.LOGIN, MODE_PRIVATE).edit();
                            Intent i;
                            if (sharedPreferences.getBoolean("isFirstTime", false)) {
                                i = new Intent(LoginActivity.this, MainActivity.class);
                            } else {
                                editor.putBoolean("isFirstTime", true);
                                i = new Intent(LoginActivity.this, TutorialActivity.class);
                            }
                            editor.putString("username", userName);
                            editor.putString("password", password);
                            editor.putInt("id", userResponse.getObject().getId());
//                    editor.putString("token", userResponse.getResponse());
                            dismissProgressDialog();
                            editor.apply();
                            startActivity(i);
                            Toast.makeText(LoginActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Login:
                String userName = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Snackbar.make(view, "Tài khoản và mật khẩu không được rỗng", Snackbar.LENGTH_SHORT).show();
                } else {
                    loginUser(userName, password);
                }
                break;
            case R.id.tvSignUp:
                startActivity(new Intent(LoginActivity.this, StepOneSignUpActivity.class));
                break;
        }
    }

    private void showProgressDialog() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

}
