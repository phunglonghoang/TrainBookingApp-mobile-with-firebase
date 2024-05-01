package Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.R;

import java.util.ArrayList;
import java.util.List;

import Object.*;

public class FragmentThemGaTau extends Fragment {
    private View rootView;
    private FirebaseDatabase database;
    private RecyclerView rvGaTau;
    private GaTauAdapter gaTauAdapter;
    private List<GaTau> gaTauList;
    private Button btnShowDialogThemGaTau;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_themgatau, container, false);
        init();

        btnShowDialogThemGaTau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogThemGaTau(Gravity.CENTER);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        rvGaTau.setLayoutManager(linearLayoutManager);
        rvGaTau.setAdapter(gaTauAdapter);

        return rootView;
    }
    private void init(){
        database = FirebaseDatabase.getInstance();
        gaTauList = new ArrayList<>();
        layDSGaTau();

        rvGaTau = rootView.findViewById(R.id.rvGaTau);
        gaTauAdapter = new GaTauAdapter(getContext(), gaTauList);

        btnShowDialogThemGaTau = rootView.findViewById(R.id.btnShowDialog_ThemGaTau);

        progressDialog = new ProgressDialog(getContext());

    }
    private void themGaTau(String tenGa, String diaChi){
        DatabaseReference reference = database.getReference("GaTau");
        GaTau gaTau = new GaTau(tenGa, diaChi);

        DatabaseReference newRef = reference.push();
        String maGaTau = newRef.getKey();
        gaTau.setMaGaTau(maGaTau);
        progressDialog.show();
        newRef.setValue(gaTau).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Thêm ga tàu thành công", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Thêm ga tàu thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showDialogThemGaTau(int gravity){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_gatau);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        EditText etTenGa = dialog.findViewById(R.id.etTenGa_ThemGaTau);
        EditText etDiaChi = dialog.findViewById(R.id.etDiaChi_ThemGaTau);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose_DialogThemGaTau);
        Button btnThem = dialog.findViewById(R.id.btnThemGaTau);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenGa = etTenGa.getText().toString().trim();
                String diaChi = etDiaChi.getText().toString().trim();
                if (tenGa.isEmpty() || diaChi.isEmpty()){
                    Toast.makeText(getContext(), "Hãy nhập đủ thông tin cho ga tàu", Toast.LENGTH_SHORT).show();
                } else if (checkGaTau(tenGa) == true) {
                    Toast.makeText(getContext(), "Ga tàu đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    themGaTau(tenGa, diaChi);
                    etTenGa.setText("");
                    etDiaChi.setText("");
                }
            }
        });

        dialog.show();
    }
    private void layDSGaTau(){
        DatabaseReference databaseReference = database.getReference("GaTau");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(gaTauList != null){
                    gaTauList.clear();
                }
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GaTau gaTau = dataSnapshot.getValue(GaTau.class);
                    gaTauList.add(gaTau);
                }
                gaTauAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean checkGaTau(String tenGa){
        for (GaTau gaTau : gaTauList){
            if(gaTau.getTenGaTau().equals(tenGa)){
                return true;
            }
        }
        return false;
    }

}
