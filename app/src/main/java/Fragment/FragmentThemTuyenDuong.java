package Fragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.R;

import java.util.ArrayList;
import java.util.List;

import Service.*;
import Object.*;

public class FragmentThemTuyenDuong extends Fragment {
    private View rootView;
    private AutoCompleteTextView atGaDi, atGaDen;
    private EditText etSLGhe, etGiaVeGiuongNam, etGiaVeGheNgoi, etGioXuatPhat, etTGDiChuyen;
    private Button btnThemTuyenDuong;
    private FirebaseDatabase database;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private TimePickerDialog timePickerDialog;
    private ShowTimePicker showTimePicker;
    private List<String> dsGaTau;
    private List<TuyenDuong> dsTuyenDuong;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_themtuyenduong, container, false);
        init();
        etGioXuatPhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker.showTimePickerDialog();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, dsGaTau);
        atGaDi.setAdapter(arrayAdapter);
        atGaDen.setAdapter(arrayAdapter);
        btnThemTuyenDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themTuyenDuong();
            }
        });
        return rootView;
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        atGaDi = rootView.findViewById(R.id.atGaDi_DialogInforTD);
        atGaDen = rootView.findViewById(R.id.atGaDen_DialogInforTD);
        etSLGhe = rootView.findViewById(R.id.etSLGhe_DialogInforTD);
        etGiaVeGiuongNam = rootView.findViewById(R.id.etGiaVeGiuongNam_DialogInforTD);
        etGiaVeGheNgoi = rootView.findViewById(R.id.etGiaVeGheNgoi_DialogInforTD);
        etGioXuatPhat = rootView.findViewById(R.id.etGioXuatPhat_DialogInforTD);
        etTGDiChuyen = rootView.findViewById(R.id.etTGDiChuyen_DialogInforTD);
        btnThemTuyenDuong = rootView.findViewById(R.id.btnThemTuyenDuong);
        builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Thông báo");
        builder.setPositiveButton("OK", null);
        showTimePicker = new ShowTimePicker(getContext(), etGioXuatPhat, timePickerDialog);

        dsGaTau = new ArrayList<>();
        layDSGaTau();
        progressDialog = new ProgressDialog(getContext());

        dsTuyenDuong = new ArrayList<>();
        layDSTuyenDuong();
    }

    private void layDSGaTau() {
        DatabaseReference databaseReference = database.getReference("GaTau");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (dsGaTau != null) {
                    dsGaTau.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GaTau gaTau = dataSnapshot.getValue(GaTau.class);
                    dsGaTau.add(gaTau.getTenGaTau());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void themTuyenDuong() {
        String gaDi = atGaDi.getText().toString();
        String gaDen = atGaDen.getText().toString();
        String slGhe = etSLGhe.getText().toString();
        String giaVeGiuongNam = etGiaVeGiuongNam.getText().toString();
        String giaVeGheNgoi = etGiaVeGheNgoi.getText().toString();
        String gioXuatPhat = etGioXuatPhat.getText().toString();
        String tgDiChuyen = etTGDiChuyen.getText().toString();
        if (gaDi.trim().isEmpty() || gaDen.trim().isEmpty() || slGhe.trim().isEmpty()
                || giaVeGiuongNam.trim().isEmpty() || giaVeGheNgoi.trim().isEmpty()
                || gioXuatPhat.isEmpty() || tgDiChuyen.trim().isEmpty()) {
            Toast.makeText(getContext(), "Hãy nhập đủ thông tin tuyến đường", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isDigitsOnly(slGhe)) {
            etSLGhe.setError("Hãy nhập đúng định dạng số ghê");
            etSLGhe.requestFocus();
        } else {
            try {
                String tenTuyenDuong = gaDi + " - " + gaDen;
                int slGheInt = Integer.parseInt(slGhe);
                float giaVeGiuongNamFloat = Float.parseFloat(giaVeGiuongNam);
                float giaVeGheNgoiFloat = Float.parseFloat(giaVeGheNgoi);
                if (slGheInt <= 0) {
                    etSLGhe.setError("Số lượng ghế phải lớn hơn 0");
                    etSLGhe.requestFocus();
                } else if (giaVeGiuongNamFloat <= 0) {
                    etGiaVeGiuongNam.setError("Giá vé phải có giá trị lớn hơn 0");
                    etGiaVeGiuongNam.requestFocus();
                } else if (giaVeGheNgoiFloat <= 0) {
                    etGiaVeGheNgoi.setError("Giá vé phải có giá trị lớn hơn 0");
                    etGiaVeGheNgoi.requestFocus();
                }
                else if (gaDi.trim().equals(gaDen.trim())){
                    atGaDen.setError("Ga đến không thể trùng với ga đi");
                    atGaDen.requestFocus();
                }else {
                    if(checkTuyenDuong(tenTuyenDuong, gioXuatPhat) == true){
                        builder.setMessage("Đã tồn tại tuyến đường này trên CSDL");
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else {
                        progressDialog.show();
                        DatabaseReference reference = database.getReference("TuyenDuong");
                        TuyenDuong tuyenDuong = new TuyenDuong(tenTuyenDuong, gaDi, gaDen, gioXuatPhat,
                                tgDiChuyen, slGheInt, giaVeGiuongNamFloat, giaVeGheNgoiFloat);
                        DatabaseReference newRef = reference.push();
                        String maTuyenDuong = newRef.getKey();
                        tuyenDuong.setMaTuyenDuong(maTuyenDuong);
                        newRef.setValue(tuyenDuong).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    builder.setMessage("Thêm tuyến đường thành công");
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                    clearInfor();
                                } else {
                                    builder.setMessage(task.getException().getMessage().toString());
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });
                    }
                }
            } catch (NumberFormatException e) {
                etGiaVeGiuongNam.setError("Hãy nhập đúng định dạng giá vé");
                etGiaVeGiuongNam.requestFocus();

                etGiaVeGheNgoi.setError("Hãy nhập đúng định dạng giá vé");
                etGiaVeGheNgoi.requestFocus();
            }
        }
    }
    private void clearInfor() {
        atGaDi.setText("");
        atGaDen.setText("");
        etGiaVeGiuongNam.setText("");
        etGiaVeGheNgoi.setText("");
        etSLGhe.setText("");
        etTGDiChuyen.setText("");
        etGioXuatPhat.setText("");
    }
    private void layDSTuyenDuong(){
        DatabaseReference reference = database.getReference("TuyenDuong");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(dsTuyenDuong != null){
                    dsTuyenDuong.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TuyenDuong tuyenDuong = dataSnapshot.getValue(TuyenDuong.class);
                    dsTuyenDuong.add(tuyenDuong);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean checkTuyenDuong(String tenTuyenDuong, String gioXuatPhat){
        for (TuyenDuong tuyenDuong : dsTuyenDuong){
            if(tuyenDuong.getTenTuyenDuong().equals(tenTuyenDuong) &&
                    tuyenDuong.getGioKhoiHanh().equals(gioXuatPhat)){
                return true;
            }
        }
        return false;
    }
}
