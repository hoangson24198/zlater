package com.example.zlater.Activity.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zlater.Activity.MainActivity;
import com.example.zlater.Activity.TutorialActivity;
import com.example.zlater.Model.Responses.UserResponse;
import com.example.zlater.Model.User;
import com.example.zlater.R;
import com.example.zlater.Service.remote.ZlaterService;
import com.example.zlater.Service.remote.RetrofitClient;
import com.example.zlater.Utils.CheckInternetConnection;
import com.example.zlater.Utils.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kaopiz.kprogresshud.KProgressHUD;

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
    private Button btn_Login;
    private ZlaterService zlaterService;
    private KProgressHUD progressDialog;
    private String check;

    private void connectView() {
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_Login = findViewById(R.id.btn_Login);
        tvSignUp = findViewById(R.id.tvSignUp);
        btn_Login.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        connectView();
        Retrofit retrofit = RetrofitClient.getInstance();
        zlaterService = retrofit.create(ZlaterService.class);
        new CheckInternetConnection(this).checkConnection();
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
        if(edt_username.getText().toString().equals("admin@zlater.com")){
            Toast.makeText(this, "Admin can't login in this app", Toast.LENGTH_SHORT).show();
        }else {
            showProgressDialog();
            String token = FirebaseInstanceId.getInstance().getToken();
            User user = new User(userName, password,true,token);
            Call<UserResponse> calledLogin = zlaterService.loginUser(user);
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
                if (validateUsername(userName) || validatePassword(password)||TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)) {
                    loginUser(userName, password);
                }
                break;
            case R.id.tvSignUp:
                startActivity(new Intent(LoginActivity.this, StepOneSignUpActivity.class));
                break;
        }
    }

    private void showProgressDialog() {
        progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getText(R.string.please_wait).toString())
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private boolean validatePassword(String pass) {


        if (pass.length() < 4 || pass.length() > 20) {
            edt_password.setError("Password Must consist of 4 to 20 characters");
            return false;
        }
        return true;
    }

    private boolean validateUsername(String email) {

        if (email.length() < 4 || email.length() > 30) {
            edt_username.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!email.matches("^[A-za-z0-9.@]+")) {
            edt_username.setError("Only . and @ characters allowed");
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            edt_username.setError("Email must contain @ and .");
            return false;
        }
        return true;
    }

    TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 40) {
                edt_username.setError("Email Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                edt_username.setError("Only . and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                edt_username.setError("Enter Valid Email");
            }

        }

    };

    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            check = editable.toString();
            if (check.length() < 4 || check.length() > 20) {
                edt_password.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                edt_password.setError("Only @ special character allowed");
            }
        }
    };

}
