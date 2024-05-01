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

import java.util.ArrayList;
import java.util.List;

import Object.*;

public class FragmentQLTuyenDuong extends Fragment {
    private FirebaseDatabase database;
    private View rootView;
    private RecyclerView rvTuyenDuong;
    private TuyenDuongAdapter tuyenDuongAdapter;
    private List<TuyenDuong> tuyenDuongList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_qltuyenduong, container, false);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        rvTuyenDuong.setLayoutManager(linearLayoutManager);
        rvTuyenDuong.setAdapter(tuyenDuongAdapter);

        return rootView;
    }
    private void init(){
        database = FirebaseDatabase.getInstance();

        rvTuyenDuong = rootView.findViewById(R.id.rvTuyenDuong_QLTuyenDuong);
        tuyenDuongList = new ArrayList<>();
        layDSTuyenDuong();

        tuyenDuongAdapter = new TuyenDuongAdapter(getContext(), tuyenDuongList);

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
                tuyenDuongAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
