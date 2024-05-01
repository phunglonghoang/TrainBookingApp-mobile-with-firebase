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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Object.*;
import Service.*;

public class LapLichChuyenTauActivity extends AppCompatActivity {
    private TuyenDuong tuyenDuong;
    private Context context = this;
    private FirebaseDatabase database;
    private ImageButton btnBack;
    private Button btnLapLichCT;
    private EditText etTenCT, etGaDi, etGaDen, etSLGhe, etGiaVeH1, etGiaVeH2,
            etGioKhoiHanh, etTGDiChuyen, etNgayKhoiHanh;
    private Calendar calendar;
    private ShowDatePicker showDatePicker;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private List<ChuyenTau> chuyenTauList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_lich_chuyen_tau);
        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etNgayKhoiHanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker.showDatePickerDialogForFuture(3);
            }
        });
        etTenCT.setText("Tên chuyến tàu: " + tuyenDuong.getTenTuyenDuong());
        etGaDi.setText("Ga đi: " + tuyenDuong.getGaDi());
        etGaDen.setText("Ga đến: " + tuyenDuong.getGaDen());
        etSLGhe.setText("Số lượng ghế: " + String.valueOf(tuyenDuong.getSlGhe()));
        etGioKhoiHanh.setText("Giờ khởi hành: " + tuyenDuong.getGioKhoiHanh());
        etTGDiChuyen.setText("Duration: " + tuyenDuong.getThoiGianDiChuyen());
        NumberFormat format = NumberFormat.getCurrencyInstance(new
                Locale("vi", "VN"));
        String giaVeH1 = format.format(tuyenDuong.getGiaVeGiuongNam());
        String giaVeH2 = format.format(tuyenDuong.getGiaVeGheNgoi());
        etGiaVeH1.setText("Giá vé H1: " + giaVeH1);
        etGiaVeH2.setText("Giá vé H2: " + giaVeH2);

        btnLapLichCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ngayKhoiHanh = etNgayKhoiHanh.getText().toString();
                if(ngayKhoiHanh.isEmpty()){
                    Toast.makeText(context, "Hãy chọn ngày khởi hành để lập lịch chuyến tàu",
                            Toast.LENGTH_SHORT).show();
                }
                else if(checkChuyenTau(ngayKhoiHanh) == true){
                    Toast.makeText(context, "Đã tồn tại chuyến tàu",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    themChuyenTau(ngayKhoiHanh);
                }
            }
        });
    }
    private void init(){
        database = FirebaseDatabase.getInstance();
        Bundle bundle = getIntent().getExtras();
        tuyenDuong = (TuyenDuong) bundle.get("tuyenDuong");

        btnBack = findViewById(R.id.btnBack_ThemCT);
        btnLapLichCT = findViewById(R.id.btnLapLichCT);
        etTenCT = findViewById(R.id.etTenChuyenTau_ThemCT);
        etGaDi = findViewById(R.id.etGaDi_ThemCT);
        etGaDen = findViewById(R.id.etGaDen_ThemCT);
        etSLGhe = findViewById(R.id.etSLGhe_ThemCT);
        etGiaVeH1 = findViewById(R.id.etGiaVeGiuongNam_ThemCT);
        etGiaVeH2 = findViewById(R.id.etGiaVeGheNgoi_ThemCT);
        etGioKhoiHanh = findViewById(R.id.etGioKhoiHanh_ThemCT);
        etTGDiChuyen = findViewById(R.id.etTGDiChuyen_ThemCT);
        etNgayKhoiHanh = findViewById(R.id.etNgayKhoiHanh_ThemCT);

        calendar = Calendar.getInstance();
        showDatePicker = new ShowDatePicker(context, calendar, etNgayKhoiHanh);

        builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setPositiveButton("OK", null);

        progressDialog = new ProgressDialog(context);

        chuyenTauList = new ArrayList<>();
        layDSChuyenTau();
    }
    private void themChuyenTau(String ngayKhoiHanh){
        DatabaseReference reference = database.getReference("ChuyenTau");
        ChuyenTau chuyenTau = new ChuyenTau(ngayKhoiHanh, tuyenDuong);
        DatabaseReference newRef = reference.push();
        chuyenTau.setMaChuyenTau(newRef.getKey());
        progressDialog.show();
        newRef.setValue(chuyenTau).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    builder.setMessage("Thêm chuyến tàu thành công");
                    alertDialog = builder.create();
                    alertDialog.show();
                    etNgayKhoiHanh.setText("");
                }
                else {
                    builder.setMessage(task.getException().getMessage().toString());
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }
    private void layDSChuyenTau(){
        DatabaseReference reference = database.getReference("ChuyenTau");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(chuyenTauList != null){
                    chuyenTauList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChuyenTau chuyenTau = dataSnapshot.getValue(ChuyenTau.class);
                    chuyenTauList.add(chuyenTau);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean checkChuyenTau(String ngayKhoiHanh){
        for (ChuyenTau chuyenTau : chuyenTauList){
            if (chuyenTau.getNgayDi().equals(ngayKhoiHanh) && chuyenTau.getTuyenDuong().
                    getMaTuyenDuong().equals(tuyenDuong.getMaTuyenDuong())){
                return true;
            }
        }
        return false;
    }
}