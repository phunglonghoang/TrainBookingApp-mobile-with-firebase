package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.EditPasswordActivity;
import com.tvc.datvetaumobileapp.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Object.User;
import Service.ShowDatePicker;

public class FragmentEditProfile extends Fragment {
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;
    private User user;
    private View rootView;
    private EditText etName, etNgaySinh, etEmail;
    private TextView txtChangePassword;
    private Button btnUpdateProfile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_editprofile, container, false);
        init();

        getProfileCurrentUser();
        ShowDatePicker showDatePicker = new ShowDatePicker(getContext(), Calendar.getInstance(), etNgaySinh);
        etNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker.showDatePickerDialog();
            }
        });
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String ngaySinh = etNgaySinh.getText().toString();
                if(name.isEmpty() || ngaySinh.isEmpty()){
                    Toast.makeText(getContext(), "Hãy nhập đủ thông tin cần cập nhật", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateProfileAdmin();
                    getProfileCurrentUser();
                }
            }
        });
        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
    private void init(){
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();

        etName = rootView.findViewById(R.id.etName_EditProfileAdmin);
        etEmail = rootView.findViewById(R.id.etEmail_EditProfileAdmin);
        etNgaySinh = rootView.findViewById(R.id.etNgaySinh_EditProfileAdmin);
        btnUpdateProfile = rootView.findViewById(R.id.btnUpdateProfileAdmin);
        txtChangePassword = rootView.findViewById(R.id.txtChangePasswordAdmin);
    }
    private void getProfileCurrentUser(){
        DatabaseReference databaseReference = database.getReference("Users").
                child(mUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if(user != null){
                    etName.setText(user.getName());
                    etNgaySinh.setText(user.getDateOfBirth());
                    etEmail.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateProfileAdmin(){
        DatabaseReference databaseReference = database.getReference("Users").
                child(mUser.getUid());
        Map<String, Object> map = new HashMap<>();
        map.put("name", etName.getText().toString());
        map.put("dateOfBirth", etNgaySinh.getText().toString());
        databaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error == null){
                    Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
