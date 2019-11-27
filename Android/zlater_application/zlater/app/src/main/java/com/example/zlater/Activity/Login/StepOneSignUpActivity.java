package com.example.zlater.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zlater.R;

public class StepOneSignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ic_closeSignUpStepOne;
    LinearLayout nextStepSignUp;

    EditText edtDisplayName, edtUsername, edtPassword, edtRetypePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_one_sign_up);
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
                if (edtDisplayName.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty() || edtRetypePassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!edtPassword.getText().toString().equals(edtRetypePassword.getText().toString())) {
                    Toast.makeText(this, "Password and retype password not matched", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra("displayName", edtDisplayName.getText().toString());
                    intent.putExtra("userName", edtUsername.getText().toString());
                    intent.putExtra("password", edtPassword.getText().toString());
                    startActivity(intent);
                }
                break;
        }
    }


}
