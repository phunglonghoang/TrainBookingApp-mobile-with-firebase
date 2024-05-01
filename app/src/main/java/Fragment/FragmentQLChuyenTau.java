package Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import Object.*;


public class FragmentQLChuyenTau extends Fragment {
    private FirebaseDatabase database;
    private View rootView;
    private RecyclerView rvChuyenTau;
    private List<ChuyenTau> chuyenTauList;
    private ChuyenTauAdapter chuyenTauAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_qlchuyentau, container, false);
        init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        rvChuyenTau.setLayoutManager(linearLayoutManager);
        rvChuyenTau.setAdapter(chuyenTauAdapter);
        return rootView;
    }
    private void init(){
        database = FirebaseDatabase.getInstance();
        rvChuyenTau = rootView.findViewById(R.id.rvChuyenTau_QLChuyenTau);
        chuyenTauList = new ArrayList<>();
        layDSChuyenTau();

        chuyenTauAdapter = new ChuyenTauAdapter(getContext(), chuyenTauList);
    }
    private void layDSChuyenTau(){
        LocalDate currentDate = LocalDate.now();
        DatabaseReference reference = database.getReference("ChuyenTau");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(chuyenTauList != null){
                    chuyenTauList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChuyenTau chuyenTau = dataSnapshot.getValue(ChuyenTau.class);
                    //Tạo định dạng giống với định dạng lúc lưu trên CSDL trước khi parse
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate ngayKhoiHanh = LocalDate.parse(chuyenTau.getNgayDi(), formatter);

                    long daysBetwen = ChronoUnit.DAYS.between(currentDate, ngayKhoiHanh);
                    if(daysBetwen >= 2){
                        chuyenTauList.add(chuyenTau);
                    }
                }
                chuyenTauAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
