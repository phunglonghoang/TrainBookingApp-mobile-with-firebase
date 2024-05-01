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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.AdminActivity;
import com.tvc.datvetaumobileapp.R;

import java.util.ArrayList;
import java.util.List;

import Object.*;

public class FragmentQuanLyTaiKhoan extends Fragment {
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;
    private View rootView;
    private RecyclerView rvDanhSachTaiKhoan;
    private UserAdapter userAdapter;
    private List<User> userList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quanlytaikhoan, container, false);
        init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        rvDanhSachTaiKhoan.setLayoutManager(linearLayoutManager);
        rvDanhSachTaiKhoan.setAdapter(userAdapter);
        return rootView;
    }
    private void init(){
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();

        userList = new ArrayList<>();
        getDanhSachUser();

        rvDanhSachTaiKhoan = rootView.findViewById(R.id.rvDanhSachTaiKhoan);
        userAdapter = new UserAdapter(getContext(), userList);
    }
    private void getDanhSachUser(){
        DatabaseReference databaseReference = database.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(userList != null){
                    userList.clear();
                }
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getUserRole() == 1){
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
