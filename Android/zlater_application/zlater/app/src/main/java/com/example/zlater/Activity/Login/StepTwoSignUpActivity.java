package com.example.zlater.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.zlater.Model.History;
import com.example.zlater.Model.Responses.HistoryResponse;
import com.example.zlater.Model.Responses.UserResponse;
import com.example.zlater.Model.User;
import com.example.zlater.R;
import com.example.zlater.Service.remote.ZlaterService;
import com.example.zlater.Service.remote.RetrofitClient;
import com.example.zlater.Utils.CheckInternetConnection;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

public class StepTwoSignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ic_backToStepOneSignUp;
    private Button btn_SignUpNow;
    private EditText edt_heigth, edt_weigth;
    private RadioButton rb_Male, rb_Female;
    private CheckBox cb_Accept;
    private ZlaterService zlaterService;
    KProgressHUD progressDialog;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_two_sign_up);
        Retrofit retrofit = RetrofitClient.getInstance();
        zlaterService = retrofit.create(ZlaterService.class);
        new CheckInternetConnection(this).checkConnection();
        connectView();
    }

    private void connectView() {
        ic_backToStepOneSignUp = findViewById(R.id.ic_backToStepOneSignUp);
        ic_backToStepOneSignUp.setOnClickListener(this);
        btn_SignUpNow = findViewById(R.id.btn_SignUpNow);
        btn_SignUpNow.setOnClickListener(this);
        edt_heigth = findViewById(R.id.edt_height);
        edt_weigth = findViewById(R.id.edt_weight);
        rb_Male = findViewById(R.id.rb_Male);
        rb_Female = findViewById(R.id.rb_female);
        cb_Accept = findViewById(R.id.cb_Accept);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_backToStepOneSignUp:
                super.onBackPressed();
                break;
            case R.id.btn_SignUpNow:

                if (edt_heigth.getText().toString().isEmpty() || edt_weigth.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter full your information", Toast.LENGTH_SHORT).show();
                } else if (!rb_Male.isChecked() && !rb_Female.isChecked()) {
                    Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
                } else if (!cb_Accept.isChecked()) {
                    Toast.makeText(this, "Please accept my policy", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle extras = getIntent().getExtras();
                    assert extras != null;
                    String displayName = extras.getString("displayName");
                    String userName = extras.getString("userName");
                    String password = extras.getString("password");
                    Float height = Float.valueOf(edt_heigth.getText().toString());
                    Float weight = Float.valueOf(edt_weigth.getText().toString());

                    int gender = 2;
                    if (rb_Male.isChecked()) {
                        gender = 1;
                    } else if (rb_Female.isChecked()) {
                        gender = 0;
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    String currentDate = formatter.format(date);
                    User user = new User(userName, password, displayName, weight, height, gender);
                    registerUser(user);
                }

                break;
        }
    }

    private void registerUser(User user) {
       showProgressDialog();
        Call<UserResponse> calledRegister = zlaterService.registerUser(user);
        calledRegister.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if(userResponse.getStatus() == 0) {
                    Log.e("HS:::","success");
//                    progressDialog.dismiss();
//                    startActivity(new Intent(StepTwoSignUpActivity.this, LoginActivity.class));
                    getUserByUserName(user.getUsername());
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(StepTwoSignUpActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("HS:::","failed"+call.request()+":::"+t.getMessage());
            }
        });
//        mSubscriptions.add(zlaterService.registerUser(displayName, userName, password, weight, height, gender, createAt)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        if (s.contains("User already exists")) {
//                            Toast.makeText(StepTwoSignUpActivity.this, "" + s, Toast.LENGTH_SHORT).show();
//                        } else {
////                            getUserByUserName(userName);
//                            startActivity(new Intent(StepTwoSignUpActivity.this, LoginActivity.class));
//
//                        }
//                    }
//                }));
    }



    private void addHistory(float user_bmi, int user_id) {
        History history=new History(user_bmi,user_id);
        Call<HistoryResponse> calledRegister = zlaterService.addHistory(history);
        calledRegister.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                HistoryResponse historyResponse = response.body();
                if(historyResponse.getStatus() == 0) {
                    Log.e("HS:::","success");
                    startActivity(new Intent(StepTwoSignUpActivity.this,LoginActivity.class));
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(StepTwoSignUpActivity.this, historyResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("HS:::","failed"+call.request()+":::"+t.getMessage());
            }
        });
    }

    private void getUserByUserName(String userName) {
        Call<UserResponse> userResponseCall = zlaterService.getUserByUserName(userName);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse.getStatus() == 0) {
                        User user = userResponse.getObject();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        addHistory(user.getBmi(),user.getId());
                    }
                } else {
                   Log.e("ERROR","ERROR");
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("ERROR","ERROR");
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        new CheckInternetConnection(this).checkConnection();
    }
}
