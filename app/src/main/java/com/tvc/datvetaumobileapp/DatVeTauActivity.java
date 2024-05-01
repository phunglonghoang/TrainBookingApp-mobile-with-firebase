package com.tvc.datvetaumobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Object.*;

public class DatVeTauActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private Context context = this;
    private ScrollView svToaTau;
    private LinearLayout layoutToaTau;
    private ImageButton btnBack;
    private Button btnDatVe;
    private TextView txtTenChuyenTau, txtNgayGioXuatPhat, txtSLGheDat, txtTongTien;
    private ChuyenTau chuyenTau;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;
    private List<String> dsGheDaDat;
    private int selectedSeatCount = 0;
    float tongTien = 0;
    private List<TextView> dsGheDaChon;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_ve_tau);
        init();
        setUpDSGhe();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txtTenChuyenTau.setText(chuyenTau.getTuyenDuong().getTenTuyenDuong());
        txtNgayGioXuatPhat.setText(chuyenTau.getNgayDi() + " " + chuyenTau.getTuyenDuong().getGioKhoiHanh());

        btnDatVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSeatCount == 0) {
                    Toast.makeText(context, "Hãy chọn ghế muốn đặt!", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_thanhtoan);

                    Window window = dialog.getWindow();
                    if (window == null) {
                        return;
                    }
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    WindowManager.LayoutParams layoutParams = window.getAttributes();
                    layoutParams.gravity = Gravity.CENTER;
                    window.setAttributes(layoutParams);
                    dialog.setCancelable(true);

                    EditText etTongTien = dialog.findViewById(R.id.etTienThanhToan_DialogThanhToan);
                    NumberFormat format = NumberFormat.getCurrencyInstance(new
                            Locale("vi", "VN"));
                    String formattedTongTien = format.format(tongTien);
                    etTongTien.setText(formattedTongTien);

                    EditText etPassword = dialog.findViewById(R.id.etPassword_DialogThanhToan);
                    Button btnThanhToan = dialog.findViewById(R.id.btnThanhToan_DialogThanhToan);
                    btnThanhToan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String passWord = etPassword.getText().toString().trim();
                            if (passWord.isEmpty()) {
                                etPassword.setError("Hãy nhập mật khẩu");
                                etPassword.requestFocus();
                            } else {
                                progressDialog.show();
                                AuthCredential authCredential = EmailAuthProvider.getCredential(mUser.getEmail(), passWord);
                                mUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            for (TextView item : dsGheDaChon) {
                                                LocalDate currentDate = LocalDate.now();
                                                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                                String formattedDate = currentDate.format(formatterDate);

                                                LocalTime currentTime = LocalTime.now();
                                                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
                                                String formattedTime = currentTime.format(formatterTime);

                                                datVeTau(formattedDate, formattedTime,
                                                        "Ghe Ngoi", item.getText().toString(), mUser.getUid(), chuyenTau,
                                                        chuyenTau.getTuyenDuong().getGiaVeGheNgoi());
                                            }
                                            dialog.dismiss();
                                            selectedSeatCount = 0;
                                            tongTien = 0;
                                            dsGheDaChon.clear();
                                            txtSLGheDat.setText("0");
                                            txtTongTien.setText("0 đ");
                                            Toast.makeText(context, "Đặt vé thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                    ImageButton btnClose = dialog.findViewById(R.id.btnClose_DialogThanhToan);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();

        progressDialog = new ProgressDialog(context);
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setPositiveButton("OK", null);

        svToaTau = findViewById(R.id.sv_ToaTau);
        layoutToaTau = findViewById(R.id.layout_ToaTau);
        btnBack = findViewById(R.id.btnBack_DatVeTau);
        btnDatVe = findViewById(R.id.btnDatVe);
        txtTenChuyenTau = findViewById(R.id.txtTenChuyenTau_DatVeTau);
        txtNgayGioXuatPhat = findViewById(R.id.txtNgayGioXuatPhat_DatVeTau);
        txtSLGheDat = findViewById(R.id.txtSLGheDat);
        txtTongTien = findViewById(R.id.txtTongTien);

        txtSLGheDat.setText("0");
        txtTongTien.setText("0 đ");

        Bundle bundle = getIntent().getExtras();
        chuyenTau = (ChuyenTau) bundle.get("chuyenTau");

        dsGheDaDat = new ArrayList<>();
        dsGheDaChon = new ArrayList<>();

    }

    private void setUpDSGhe() {
        int slToa = chuyenTau.getTuyenDuong().getSlGhe() / 10;
        for (int i = 1; i <= slToa; i++) {
            View view = getLayoutInflater().inflate(R.layout.layout_datvetau, null);
            TextView txtToa = view.findViewById(R.id.txtToa);
            txtToa.setText("Toa " + i);

            TextView itemGhe1 = view.findViewById(R.id.item_ghe1);
            TextView itemGhe2 = view.findViewById(R.id.item_ghe2);
            TextView itemGhe3 = view.findViewById(R.id.item_ghe3);
            TextView itemGhe4 = view.findViewById(R.id.item_ghe4);
            TextView itemGhe5 = view.findViewById(R.id.item_ghe5);
            TextView itemGhe6 = view.findViewById(R.id.item_ghe6);
            TextView itemGhe7 = view.findViewById(R.id.item_ghe7);
            TextView itemGhe8 = view.findViewById(R.id.item_ghe8);
            TextView itemGhe9 = view.findViewById(R.id.item_ghe9);
            TextView itemGhe10 = view.findViewById(R.id.item_ghe10);
            TextView itemGhe11 = view.findViewById(R.id.item_ghe11);
            TextView itemGhe12 = view.findViewById(R.id.item_ghe12);
            TextView itemGhe13 = view.findViewById(R.id.item_ghe13);
            TextView itemGhe14 = view.findViewById(R.id.item_ghe14);
            TextView itemGhe15 = view.findViewById(R.id.item_ghe15);
            TextView itemGhe16 = view.findViewById(R.id.item_ghe16);
            TextView itemGhe17 = view.findViewById(R.id.item_ghe17);
            TextView itemGhe18 = view.findViewById(R.id.item_ghe18);
            TextView itemGhe19 = view.findViewById(R.id.item_ghe19);
            TextView itemGhe20 = view.findViewById(R.id.item_ghe20);
            List<TextView> dsGhe = new ArrayList<>();
            dsGhe.add(itemGhe1);
            dsGhe.add(itemGhe2);
            dsGhe.add(itemGhe3);
            dsGhe.add(itemGhe4);
            dsGhe.add(itemGhe5);
            dsGhe.add(itemGhe6);
            dsGhe.add(itemGhe7);
            dsGhe.add(itemGhe8);
            dsGhe.add(itemGhe9);
            dsGhe.add(itemGhe10);
            dsGhe.add(itemGhe11);
            dsGhe.add(itemGhe12);
            dsGhe.add(itemGhe13);
            dsGhe.add(itemGhe14);
            dsGhe.add(itemGhe15);
            dsGhe.add(itemGhe16);
            dsGhe.add(itemGhe17);
            dsGhe.add(itemGhe18);
            dsGhe.add(itemGhe19);
            dsGhe.add(itemGhe20);

            int j = 1;
            for (TextView itemGhe : dsGhe) {
                itemGhe.setText("T" + i + "-" + j);

                DatabaseReference reference = database.getReference("VeTau");
                Query query = reference.orderByChild("chuyenTau/maChuyenTau").equalTo(chuyenTau.getMaChuyenTau());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (dsGheDaDat != null) {
                            dsGheDaDat.clear();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            VeTau veTau = dataSnapshot.getValue(VeTau.class);
                            if (veTau != null) {
                                dsGheDaDat.add(veTau.getSoGhe());
                            }
                        }
                        if (!dsGheDaDat.isEmpty()) {
                            if (dsGheDaDat.contains(itemGhe.getText().toString())) {
                                itemGhe.setBackgroundResource(R.drawable.bg_booked_seat);
                                itemGhe.setEnabled(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                j++;
                // Sử dụng biến isSelected để theo dõi trạng thái của EditText
                boolean[] isSelected = {false};
                itemGhe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Lấy trạng thái hiện tại của EditText
                        boolean currentlySelected = isSelected[0];

                        if (!itemGhe.isEnabled()) {
                            return;
                        }

                        if (currentlySelected) {
                            // Nếu đã được chọn, chuyển về background ban đầu
                            itemGhe.setBackgroundResource(R.drawable.bg_available_seat);
                            selectedSeatCount--;
                            dsGheDaChon.remove(itemGhe);
                        } else {
                            // Nếu chưa được chọn, chuyển đến background mới
                            itemGhe.setBackgroundResource(R.drawable.bg_selected_seat);
                            selectedSeatCount++;
                            dsGheDaChon.add(itemGhe);
                        }
                        // Cập nhật trạng thái
                        isSelected[0] = !currentlySelected;
                        txtSLGheDat.setText(String.valueOf(selectedSeatCount));
                        tongTien = chuyenTau.getTuyenDuong().getGiaVeGheNgoi() * selectedSeatCount;
                        NumberFormat format = NumberFormat.getCurrencyInstance(new
                                Locale("vi", "VN"));
                        String formattedTongTien = format.format(tongTien);
                        txtTongTien.setText(formattedTongTien);
                    }
                });
            }

            layoutToaTau.addView(view);
        }
    }

    private void datVeTau(String ngayDatVe, String gioDatVe,
                          String loaiGhe, String soGhe, String userID, ChuyenTau chuyenTau, float giaVe) {
        DatabaseReference reference = database.getReference("VeTau");
        VeTau veTau = new VeTau(ngayDatVe, gioDatVe, loaiGhe, soGhe, giaVe, userID, chuyenTau);

        DatabaseReference newRef = reference.push();
        String maVeTau = newRef.getKey();
        veTau.setMaVeTau(maVeTau);
        newRef.setValue(veTau).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

}