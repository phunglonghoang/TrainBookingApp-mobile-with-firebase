package com.tvc.datvetaumobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Object.*;
public class ChuyenTauDetailActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private Context context = this;
    private TextView txtTenChuyenTau;
    private ImageButton btnBack;
    private RecyclerView rvChuyenTau;
    private List<ChuyenTau> dsChuyenTau;
    private ChuyenTauAdapter2 chuyenTauAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_tau_detail);
        init();
        String tenChuyenTau = "(" + dsChuyenTau.get(0).getTuyenDuong().getTenTuyenDuong() + ")";
        txtTenChuyenTau.setText(tenChuyenTau);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        rvChuyenTau.setLayoutManager(linearLayoutManager);
        rvChuyenTau.setAdapter(chuyenTauAdapter2);
    }
    private void init(){
        database = FirebaseDatabase.getInstance();
        dsChuyenTau = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dsChuyenTau = (ArrayList<ChuyenTau>) bundle.getSerializable("dsChuyenTau");
        txtTenChuyenTau = findViewById(R.id.txtTenCT_CTDetail);

        btnBack = findViewById(R.id.btnBack_CTDetail);
        rvChuyenTau = findViewById(R.id.rvChuyenTau_CTDetail);
        chuyenTauAdapter2 = new ChuyenTauAdapter2(context, dsChuyenTau);
    }
}