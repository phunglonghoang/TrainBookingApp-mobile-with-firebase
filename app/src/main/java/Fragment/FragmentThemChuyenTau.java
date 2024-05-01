package Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Object.*;

public class FragmentThemChuyenTau extends Fragment {
    private View rootView;
    private FirebaseDatabase database;
    private AutoCompleteTextView atTenTuyenDuong;
    private Button btnTraCuu;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rvTuyenDuong;
    private TuyenDuongAdapter2 tuyenDuongAdapter2;
    private List<TuyenDuong> tuyenDuongList;
    private List<String> dsTenTuyenDuong;
    private List<TuyenDuong> dsKQTraCuuTD;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_themchuyentau, container, false);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        rvTuyenDuong.setLayoutManager(linearLayoutManager);
        rvTuyenDuong.setAdapter(tuyenDuongAdapter2);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, dsTenTuyenDuong);
        atTenTuyenDuong.setAdapter(arrayAdapter);

        btnTraCuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenTuyenDuong = atTenTuyenDuong.getText().toString().trim();
                if(tenTuyenDuong.isEmpty()){
                    Toast.makeText(getContext(), "Hãy nhập tên tuyến đường cần tra cứu", Toast.LENGTH_SHORT).show();
                }
                else {
                    dsKQTraCuuTD = kqTraCuuTD(tenTuyenDuong);
                    if (dsKQTraCuuTD.size() == 0){
                        builder.setMessage("Không tìm thấy tuyến đường cần tra cứu");
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else {
                        tuyenDuongAdapter2 = new TuyenDuongAdapter2(getContext(), dsKQTraCuuTD);
                        rvTuyenDuong.setAdapter(tuyenDuongAdapter2);
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tuyenDuongAdapter2 = new TuyenDuongAdapter2(getContext(), tuyenDuongList);
                rvTuyenDuong.setAdapter(tuyenDuongAdapter2);
                atTenTuyenDuong.setText("");

                refreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }
    private void init(){
        database = FirebaseDatabase.getInstance();

        atTenTuyenDuong = rootView.findViewById(R.id.atTenTuyenDuong_ThemCT);
        btnTraCuu = rootView.findViewById(R.id.btnTraCuu_ThemCT);
        refreshLayout = rootView.findViewById(R.id.refreshLayout_ThemTD);
        rvTuyenDuong = rootView.findViewById(R.id.rvThemChuyenTau);
        tuyenDuongList = new ArrayList<>();
        layDSTuyenDuong();

        tuyenDuongAdapter2 = new TuyenDuongAdapter2(getContext(), tuyenDuongList);

        dsTenTuyenDuong = new ArrayList<>();
        layDSTenTuyenDuong();

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo");
        builder.setPositiveButton("OK", null);
    }
    private void layDSTuyenDuong(){
        DatabaseReference reference = database.getReference("TuyenDuong");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(tuyenDuongList != null){
                    tuyenDuongList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TuyenDuong tuyenDuong = dataSnapshot.getValue(TuyenDuong.class);
                    tuyenDuongList.add(tuyenDuong);
                }
                tuyenDuongAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void layDSTenTuyenDuong(){
        DatabaseReference reference = database.getReference("TuyenDuong");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(dsTenTuyenDuong != null){
                    dsTenTuyenDuong.clear();
                }
                HashSet<String> uniqueTenChuyenTau = new HashSet<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TuyenDuong tuyenDuong = dataSnapshot.getValue(TuyenDuong.class);
                    uniqueTenChuyenTau.add(tuyenDuong.getTenTuyenDuong());
                }
                dsTenTuyenDuong.addAll(uniqueTenChuyenTau);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private List<TuyenDuong> kqTraCuuTD(String tenTuyenDuong){
        List<TuyenDuong> dsKQTraCuu = new ArrayList<TuyenDuong>();
        for(TuyenDuong tuyenDuong : tuyenDuongList){
            if (tuyenDuong.getTenTuyenDuong().equals(tenTuyenDuong)){
                dsKQTraCuu.add(tuyenDuong);
            }
        }
        return dsKQTraCuu;
    }

}
