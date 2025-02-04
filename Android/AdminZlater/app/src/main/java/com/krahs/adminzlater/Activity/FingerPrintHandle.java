package com.krahs.adminzlater.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.krahs.adminzlater.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerPrintHandle extends FingerprintManager.AuthenticationCallback {
    private Context context;
    private ImageView fingerSuccess;
    EditText edtPassword;
    String password;

    public FingerPrintHandle(Context context, ImageView fingerSuccess,EditText edtPassword,String password) {
        this.context = context;
        this.fingerSuccess=fingerSuccess;
        this.edtPassword=edtPassword;
        this.password=password;
    }

    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal=new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){

            return;
        }
            fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,this,null);

    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        fingerSuccess.setImageResource(R.drawable.green_finger);
        edtPassword.setText(password);
        context.startActivity(new Intent(context,MainActivity.class));
        ((SplashScreenActivity)context).finish();

    }
}
