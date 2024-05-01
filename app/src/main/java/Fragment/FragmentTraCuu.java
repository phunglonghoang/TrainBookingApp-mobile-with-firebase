package Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.ChuyenTauDetailActivity;
import com.tvc.datvetaumobileapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Object.*;
import Service.*;
import kotlin.collections.ArrayDeque;

public class FragmentTraCuu extends Fragment {
    private FirebaseDatabase database;
    private View rootView;
    private AutoCompleteTextView atGaDi, atGaDen;
    private EditText etNgayKhoiHanh;
    private List<ChuyenTau> dsKQTraCuu;
    private List<String> dsGaTau;
    private Button btnTraCuu;
    private ShowDatePicker showDatePicker;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tracuu, container, false);
        init();
        etNgayKhoiHanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker.showDatePickerDialogForFuture(2);
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dsGaTau);
        atGaDi.setAdapter(arrayAdapter);
        atGaDen.setAdapter(arrayAdapter);
        btnTraCuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String gaDi = atGaDi.getText().toString().trim();
                String gaDen = atGaDen.getText().toString().trim();
                String ngayKhoiHanh = etNgayKhoiHanh.getText().toString();
                if (gaDi.isEmpty() || gaDen.isEmpty() || ngayKhoiHanh.isEmpty()) {
                    Toast.makeText(getContext(), "Hãy nhập đủ thông tin chuyến tàu cần tra cứu", Toast.LENGTH_SHORT).show();
                } else {
                    String tenChuyenTau = gaDi + " - " + gaDen;
                    progressDialog.show(); // Hiển thị ProgressDialog

                    traCuuChuyenTau(ngayKhoiHanh, tenChuyenTau);
                }
            }
        });
        return rootView;
    }

    private void init() {
        database = FirebaseDatabase.getInstance();

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo");
        builder.setPositiveButton("OK", null);
        progressDialog = new ProgressDialog(getContext());

        atGaDi = rootView.findViewById(R.id.atGaDi_TraCuuCT);
        atGaDen = rootView.findViewById(R.id.atGaDen_TraCuuCT);
        etNgayKhoiHanh = rootView.findViewById(R.id.etNgayKhoiHanh_TraCuuCT);
        btnTraCuu = rootView.findViewById(R.id.btnTraCuuCT);
        showDatePicker = new ShowDatePicker(getContext(), Calendar.getInstance(), etNgayKhoiHanh);

        dsKQTraCuu = new ArrayList<>();

        dsGaTau = new ArrayList<>();
        layDSGaTau();
    }

    private void layDSGaTau() {
        DatabaseReference reference = database.getReference("GaTau");
        reference.addValueEventListener(new ValueEventListener() {
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

    private void traCuuChuyenTau(String ngayKhoiHanh, String tenChuyenTau) {
        DatabaseReference reference = database.getReference("ChuyenTau");
        Query query = reference.orderByChild("ngayDi").equalTo(ngayKhoiHanh);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss(); // Ẩn ProgressDialog sau khi đã nhận được dữ liệu

                if (dsKQTraCuu != null) {
                    dsKQTraCuu.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChuyenTau chuyenTau = dataSnapshot.getValue(ChuyenTau.class);

                    if (chuyenTau.getTuyenDuong().getTenTuyenDuong().equals(tenChuyenTau)) {
                        dsKQTraCuu.add(chuyenTau);
                    }
                }

                // Kiểm tra dsKQTraCuu và hiển thị thông báo
                if (dsKQTraCuu.isEmpty()) {
                    builder.setMessage("Không tìm thấy chuyến tàu");
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Intent intent = new Intent(getContext(), ChuyenTauDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dsChuyenTau", (Serializable) dsKQTraCuu);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss(); // Ẩn ProgressDialog nếu có lỗi
            }
        });
    }
}
