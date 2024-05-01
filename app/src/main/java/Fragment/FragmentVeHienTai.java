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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import Object.*;

public class FragmentVeHienTai extends Fragment {
    private View rootView;
    private FirebaseDatabase database;
    private FirebaseUser mUser;
    private RecyclerView rvVeTau;
    private VeTauAdapter veTauAdapter;
    private List<VeTau> veTauList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vehientai, container, false);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        rvVeTau.setLayoutManager(linearLayoutManager);
        rvVeTau.setAdapter(veTauAdapter);

        return rootView;
    }
    private void init(){
        database = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        rvVeTau = rootView.findViewById(R.id.rvVeTau_VeHienTai);
        veTauList = new ArrayList<>();
        layDSVeHienTai(mUser.getUid());
        veTauAdapter = new VeTauAdapter(getContext(), veTauList);
    }
    private void layDSVeHienTai(String userID){
        LocalDate currentDate = LocalDate.now();
        DatabaseReference reference = database.getReference("VeTau");
        Query query = reference.orderByChild("userID").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(veTauList != null){
                    veTauList.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    VeTau veTau = dataSnapshot.getValue(VeTau.class);
                    if(veTau != null){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        LocalDate ngayKhoiHanh = LocalDate.parse(veTau.getChuyenTau().getNgayDi(), formatter);
                        long daysBetwen = ChronoUnit.DAYS.between(currentDate, ngayKhoiHanh);
                        if(daysBetwen >= 2){
                            veTauList.add(veTau);
                        }

                    }
                }
                veTauAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
