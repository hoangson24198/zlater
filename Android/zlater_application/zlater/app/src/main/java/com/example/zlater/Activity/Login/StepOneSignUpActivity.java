package com.example.zlater.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.zlater.R;
import com.example.zlater.Utils.CheckInternetConnection;

public class StepOneSignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ic_closeSignUpStepOne;
    Button nextStepSignUp;
    String check;

    EditText edtDisplayName, edtUsername, edtPassword, edtRetypePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_one_sign_up);
        new CheckInternetConnection(this).checkConnection();
        connectView();
    }

    private void connectView() {
        ic_closeSignUpStepOne = findViewById(R.id.ic_closeSignUpStepOne);
        ic_closeSignUpStepOne.setOnClickListener(this);
        nextStepSignUp = findViewById(R.id.nextStepSignUp);
        nextStepSignUp.setOnClickListener(this);
        edtDisplayName = findViewById(R.id.edt_DisplayName);
        edtUsername = findViewById(R.id.edt_Username);
        edtPassword = findViewById(R.id.edt_Password);
        edtRetypePassword = findViewById(R.id.edt_ReTypePassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_closeSignUpStepOne:
                finish();
                break;
            case R.id.nextStepSignUp:
                Intent intent = new Intent(StepOneSignUpActivity.this, StepTwoSignUpActivity.class);
                if (validateUsername(edtUsername.getText().toString(),edtDisplayName.getText().toString())&& validatePassword(edtPassword.getText().toString(),edtRetypePassword.getText().toString())){
                    intent.putExtra("displayName", edtDisplayName.getText().toString());
                    intent.putExtra("userName", edtUsername.getText().toString());
                    intent.putExtra("password", edtPassword.getText().toString());
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CheckInternetConnection(this).checkConnection();
    }

    private boolean validatePassword(String pass,String retypePassword) {


        if (pass.length() < 4 || pass.length() > 20) {
            edtPassword.setError("Password Must consist of 4 to 20 characters");
            return false;
        }
        if (!pass.equals(retypePassword)){
            edtRetypePassword.setError("Password and retype password not matched");
            return false;
        }
        return true;
    }

    private boolean validateUsername(String email,String displayname) {

        if (email.length() < 4 || email.length() > 30) {
            edtUsername.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!email.matches("^[A-za-z0-9.@]+")) {
            edtUsername.setError("Only . and @ characters allowed");
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            edtUsername.setError("Email must contain @ and .");
            return false;
        }
        if (displayname.length()==0){
            edtDisplayName.setError("This field can't empty");
            return false;
        }
        return true;
    }

    TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            check = s.toString();

            if (check.length() < 4 || check.length() > 40) {
                edtUsername.setError("Email Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                edtUsername.setError("Only . and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                edtUsername.setError("Enter Valid Email");
            }
            if (check.length() == 0) {
                edtDisplayName.setError("This field can't empty");
                edtUsername.setError("This field can't empty");
                edtPassword.setError("This field can't empty");
                edtRetypePassword.setError("This field can't empty");
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
                edtPassword.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                edtPassword.setError("Only @ special character allowed");
            }
        }
    };
}
