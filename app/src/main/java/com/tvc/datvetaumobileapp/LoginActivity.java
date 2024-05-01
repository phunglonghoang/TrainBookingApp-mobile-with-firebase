package com.tvc.datvetaumobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import Object.*;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnLoginAdmin;
    private TextView txtRegister, txtForgotPassword;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private Context context = this;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String withoutSpaces = charSequence.toString().replace(" ", "");
                    etPassword.setText(withoutSpaces);
                    etPassword.setSelection(withoutSpaces.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAdmin();
            }
        });
    }

    private void init(){
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail_Login);
        etPassword = findViewById(R.id.etPassword_Login);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginAdmin = findViewById(R.id.btnLoginAdmin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        progressDialog = new ProgressDialog(context);
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setPositiveButton("OK", null);

//        registerAdmin();
    }
    private void loginUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "Hãy nhập email và mật khẩu để đăng nhập",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        FirebaseUser mUser = firebaseAuth.getCurrentUser();
                        DatabaseReference databaseReference = database.getReference("Users").
                                child(mUser.getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                if(user.getUserRole() == 1){
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                                else {
                                    builder.setMessage("Tài khoản của bạn không phải tài khoản User");
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else{
                        String error = task.getException().getMessage().toString();
                        builder.setTitle(error);
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });
        }
    }

    private void loginAdmin(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(context, "Hãy nhập email và mật khẩu để đăng nhập",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                                DatabaseReference databaseReference = database.getReference("Users").
                                        child(mUser.getUid());
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        if(user.getUserRole() == 0){
                                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                        else {
                                            builder.setMessage("Tài khoản của bạn không phải tài khoản Admin");
                                            alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else{
                                String error = task.getException().getMessage().toString();
                                builder.setTitle(error);
                                alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    });
        }
    }
    private void registerAdmin(){
        String email = "admin@gmail.com"; // Email của tài khoản admin
        String password = "123456"; // Mật khẩu của tài khoản admin
        String name = "Admin"; // Tên của tài khoản admin
        String dateOfBirth = ""; // Ngày sinh của tài khoản admin

        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        builder.setTitle("Thông báo");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        if(task.isSuccessful()){
                            FirebaseUser mUser = firebaseAuth.getCurrentUser();
                            User adminUser = new User(mUser.getUid(), name, 0, dateOfBirth, mUser.getEmail());
                            // Tạo thông tin tài khoản cho admin trên realtime database
                            DatabaseReference databaseReference = database.getReference("Users");
                            databaseReference.child(mUser.getUid()).setValue(adminUser);

//                            builder.setMessage("Tạo tài khoản admin thành công");
//                            alertDialog = builder.create();
//                            alertDialog.show();
//
                        }
                        else{
                            String error = task.getException().getMessage().toString();
                            builder.setMessage(error);
                            alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
    }

}