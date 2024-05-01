package com.tvc.datvetaumobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditPasswordActivity extends AppCompatActivity {
    private Context context = this;
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private ImageButton btnBack;
    private Button btnChangePassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        init();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etCurrentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String withoutSpaces = charSequence.toString().replace(" ", "");
                    etCurrentPassword.setText(withoutSpaces);
                    etCurrentPassword.setSelection(withoutSpaces.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().contains(" ")) {
                    String withoutSpaces = charSequence.toString().replace(" ", "");
                    etNewPassword.setText(withoutSpaces);
                    etNewPassword.setSelection(withoutSpaces.length());
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

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if(currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(context, "Hãy nhập đủ thông tin mật khẩu", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!newPassword.equals(confirmPassword)){
                        Toast.makeText(context, "Mật khẩu nhập lại không trùng khớp", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.show();

                        builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông báo");
                        builder.setPositiveButton("OK", null);

                        AuthCredential authCredential = EmailAuthProvider.
                                getCredential(mUser.getEmail(), currentPassword);
                        mUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    mUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                builder.setMessage("Đổi mật khẩu thành công");
                                                alertDialog = builder.create();
                                                alertDialog.show();

                                                etCurrentPassword.setText("");
                                                etNewPassword.setText("");
                                                etConfirmPassword.setText("");
                                            }
                                            else {
                                                builder.setMessage(task.getException().getMessage().toString());
                                                alertDialog = builder.create();
                                                alertDialog.show();
                                            }
                                        }
                                    });
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
        });
    }
    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(context);
        etCurrentPassword = findViewById(R.id.etCurrentPassword_EditPasswordUser);
        etNewPassword = findViewById(R.id.etNewPassword_EditPasswordUser);
        etConfirmPassword = findViewById(R.id.etConfirmPassword_EditPasswordUser);
        btnBack = findViewById(R.id.btnBack_EditPasswordUser);
        btnChangePassword = findViewById(R.id.btnChangePassword_EditPasswordUser);
    }
}