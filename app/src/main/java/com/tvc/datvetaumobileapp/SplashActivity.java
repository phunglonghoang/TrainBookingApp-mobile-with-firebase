package com.tvc.datvetaumobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import Object.*;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 3000);

    }
    private void nextActivity(){
        FirebaseUser mUser = firebaseAuth.getCurrentUser();

        if (mUser == null){
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        else{
            DatabaseReference databaseReference = database.getReference("Users").child(mUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user.getUserRole() == 0){
                        Intent intent = new Intent(SplashActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}