package com.tvc.datvetaumobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import Service.*;
import Object.*;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText etEmail, etPassword, etConfirmPassword, etName, etDateOfBirth;
    private Button btnRegister;
    private ImageButton btnBack;
    private Context context = this;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private FirebaseDatabase database;
    private Calendar calendar;
    private ShowDatePicker showDatePicker;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker.showDatePickerDialog();
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
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String withoutSpaces = charSequence.toString().replace(" ", "");
                    etConfirmPassword.setText(withoutSpaces);
                    etConfirmPassword.setSelection(withoutSpaces.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }
    private void init(){
        builder = new AlertDialog.Builder(context);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(context);
        etEmail = findViewById(R.id.etEmail_Register);
        etPassword = findViewById(R.id.etPassword_Register);
        etConfirmPassword = findViewById(R.id.etConfirmPassword_Register);
        etName = findViewById(R.id.etName_Register);
        etDateOfBirth = findViewById(R.id.etBirthDay_Register);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack_Register);
        calendar = Calendar.getInstance();
        showDatePicker = new ShowDatePicker(context, calendar, etDateOfBirth);
    }
    private void registerUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString();
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
        || name.isEmpty() || dateOfBirth.isEmpty()){
            Toast.makeText(context, "Hãy nhập đủ thông tin tài khoản!!!",
                    Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword)){
            Toast.makeText(context, "Mật khẩu nhập lại không khớp!!!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
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
                        User user = new User(mUser.getUid(), name,
                                1, dateOfBirth, mUser.getEmail());
                        //Tạo thông tin tài khoản cho user trên realtime database
                        DatabaseReference databaseReference = database.getReference("Users");
                        databaseReference.child(mUser.getUid()).setValue(user);

                        builder.setMessage("Đăng ký tài khoản thành công");
                        alertDialog = builder.create();
                        alertDialog.show();
                        clearInformation();
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
    private void clearInformation(){
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
        etName.setText("");
        etDateOfBirth.setText("");
    }

}