package com.tvc.datvetaumobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import Fragment.*;

public class MainActivity extends AppCompatActivity {
    private FrameLayout frameLayoutUser;
    private TabLayout tabLayoutUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setFragment(new FragmentTraCuu());
        tabLayoutUser.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        setFragment(new FragmentTraCuu());
                        break;
                    case 1:
                        setFragment(new FragmentVeCuaToi());
                        break;
                    case 2:
                        setFragment(new FragmentTaiKhoan());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        frameLayoutUser = findViewById(R.id.frameLayoutUser);
        tabLayoutUser = findViewById(R.id.tabLayoutUser);
    }
    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayoutUser, fragment);
        transaction.commit();
    }
}