package com.example.zlater.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zlater.R;

public class LoginMethod extends AppCompatActivity implements View.OnClickListener {
    LinearLayout loginWithEmail;
    TextView tv_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_method);
        connectView();

    }

    private void connectView() {
        loginWithEmail = findViewById(R.id.loginWithEmail);
        loginWithEmail.setOnClickListener(this);
        tv_signUp = findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginWithEmail:
                startActivity(new Intent(LoginMethod.this, LoginActivity.class));
                break;
            case R.id.tv_signUp:
                startActivity(new Intent(LoginMethod.this, StepOneSignUpActivity.class));
                break;
        }
    }
}
