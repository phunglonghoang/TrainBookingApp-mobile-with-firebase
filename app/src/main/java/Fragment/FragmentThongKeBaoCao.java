package Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.R;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Object.*;

public class FragmentThongKeBaoCao extends Fragment {
    private View rootView;
    private FirebaseDatabase database;
    private Spinner spinnerMonth, spinnerYear;
    private String monthThongKe, yearThongKe;
    private AutoCompleteTextView atTenChuyenTau;
    private EditText etSLVeDat, etTyLe, etTongDoanhThu;
    private Button btnThongKe;

    private List<String> dsChuyenTau;
    private List<String> dsThangThongKe;
    private List<String> dsNamThongKe;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_thongkebaocao, container, false);
        init();
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dsThangThongKe);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dsNamThongKe);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonth.setAdapter(adapterMonth);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthThongKe = spinnerMonth.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerYear.setAdapter(adapterYear);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearThongKe = spinnerYear.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> adapterChuyenTau = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, dsChuyenTau);
        atTenChuyenTau.setAdapter(adapterChuyenTau);

        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenChuyenTau = atTenChuyenTau.getText().toString().trim();
                if (tenChuyenTau.isEmpty()) {
                    clearInformation();
                    Toast.makeText(getContext(), "Hãy nhập tên chuyến tàu cần thống kê", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    DatabaseReference databaseReference = database.getReference("VeTau");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<VeTau> veTauList = new ArrayList<>();
                            List<VeTau> dsVeTauThongKe = new ArrayList<>();
                            progressDialog.dismiss();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                VeTau veTau = dataSnapshot.getValue(VeTau.class);
                                if (veTau != null) {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                    LocalDate ngayDatVe = LocalDate.parse(veTau.getNgayDatVe(), formatter);
                                    if (String.valueOf(ngayDatVe.getMonthValue()).equals(monthThongKe) && String.valueOf(ngayDatVe.getYear()).equals(yearThongKe)) {
                                        veTauList.add(veTau);
                                    }
                                }
                            }
                            if (veTauList.isEmpty()) {
                                clearInformation();
                                builder.setMessage("Tháng này không có lượt đặt vé nào");
                                alertDialog = builder.create();
                                alertDialog.show();
                            } else {
                                for (VeTau veTau : veTauList) {
                                    if (veTau.getChuyenTau().getTuyenDuong().getTenTuyenDuong().equals(tenChuyenTau)) {
                                        dsVeTauThongKe.add(veTau);
                                    }
                                }
                                if (dsVeTauThongKe.isEmpty()) {
                                    clearInformation();
                                    builder.setMessage("Không tìm thấy chuyến tàu cần thống kê");
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    etSLVeDat.setText(String.valueOf(dsVeTauThongKe.size()));
                                    double tyLeVe = (double) dsVeTauThongKe.size() / veTauList.size();
                                    double tyLeVeTrenThang = tyLeVe * 100;
                                    etTyLe.setText(String.valueOf(tyLeVeTrenThang) + "%");
                                    float tongDoanhThu = 0;
                                    for (VeTau veTau : dsVeTauThongKe) {
                                        tongDoanhThu += veTau.getGiaVe();
                                    }
                                    NumberFormat format = NumberFormat.getCurrencyInstance(new
                                            Locale("vi", "VN"));
                                    String formattedDoanhThu = format.format(tongDoanhThu);
                                    etTongDoanhThu.setText(formattedDoanhThu);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        return rootView;
    }

    private void init() {
        database = FirebaseDatabase.getInstance();

        dsChuyenTau = new ArrayList<>();
        layDSChuyenTau();

        dsThangThongKe = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            dsThangThongKe.add(String.valueOf(i));
        }
        dsNamThongKe = new ArrayList<>();
        layDSNamThongKe();


        spinnerMonth = rootView.findViewById(R.id.spinner_month_ThongKe);
        spinnerYear = rootView.findViewById(R.id.spinner_year_ThongKe);
        atTenChuyenTau = rootView.findViewById(R.id.atTenChuyenTau_ThongKe);
        etSLVeDat = rootView.findViewById(R.id.etSoLuongVe_ThongKe);
        etTyLe = rootView.findViewById(R.id.etTyLe_ThongKe);
        etTongDoanhThu = rootView.findViewById(R.id.etTongDoanhThu_ThongKe);

        btnThongKe = rootView.findViewById(R.id.btnThongKe);

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo");
        builder.setPositiveButton("OK", null);

        progressDialog = new ProgressDialog(getContext());
    }

    private void clearInformation() {
        etTongDoanhThu.setText("");
        etTyLe.setText("");
        etSLVeDat.setText("");
    }

    private void layDSChuyenTau() {
        DatabaseReference reference = database.getReference("TuyenDuong");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (dsChuyenTau != null) {
                    dsChuyenTau.clear();
                }
                HashSet<String> uniqueTenTau = new HashSet<>(); // Sử dụng HashSet để loại bỏ trùng lặp
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TuyenDuong tuyenDuong = dataSnapshot.getValue(TuyenDuong.class);
                    if (tuyenDuong != null) {
                        uniqueTenTau.add(tuyenDuong.getTenTuyenDuong());
                    }
                }

                // Bây giờ danh sách tên chuyến tàu không trùng lặp trong uniqueTenTau
                dsChuyenTau.addAll(uniqueTenTau);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void layDSNamThongKe() {
        int numberOfYears = 5;
        List<Integer> yearList = new ArrayList<>();

        // Lấy năm hiện tại
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Thêm các năm vào danh sách
        for (int i = 0; i < numberOfYears; i++) {
            yearList.add(currentYear - i);
        }
        for (int i : yearList) {
            dsNamThongKe.add(String.valueOf(i));
        }
    }
}
