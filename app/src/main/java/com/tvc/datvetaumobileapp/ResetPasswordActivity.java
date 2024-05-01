package com.tvc.datvetaumobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnResetPassword;
    private EditText etEmail;
    private FirebaseAuth firebaseAuth;
    private Context context = this;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }
    private void init(){
        btnBack = findViewById(R.id.btnBack_ForgotPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        etEmail = findViewById(R.id.etEmail_ResetPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setPositiveButton("OK", null);
    }
    private void resetPassword(){
        String email = etEmail.getText().toString();
        if(email.trim().isEmpty()){
            Toast.makeText(context, "Hãy nhập email bạn muốn cài lại mật khẩu", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){
                        builder.setMessage("Gửi yêu cầu reset pasword đến email thành công, kiểm tra emal ể cài lại mật khẩu");
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else {
                        builder.setMessage(task.getException().getMessage().toString());
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });
        }
    }
}