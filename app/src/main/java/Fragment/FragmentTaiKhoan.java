package Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.EditPasswordActivity;
import com.tvc.datvetaumobileapp.EditProfileUserActivity;
import com.tvc.datvetaumobileapp.LoginActivity;
import com.tvc.datvetaumobileapp.R;

import Object.User;

public class FragmentTaiKhoan extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private View rootView;
    private TextView txtName, txtEmail;
    private ConstraintLayout layoutEditProfile, layoutEditPassword, layoutLogout;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private FirebaseUser mUser;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_taikhoan, container, false);
        init();
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
        getCurrentUser();

        layoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileUserActivity.class);
                startActivity(intent);

            }
        });
        layoutEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();

        txtName = rootView.findViewById(R.id.txtName_ProfileUser);
        txtEmail = rootView.findViewById(R.id.txtEmail_ProfileUser);
        layoutEditProfile = rootView.findViewById(R.id.layout_EditProfile);
        layoutEditPassword = rootView.findViewById(R.id.layout_EditPassword);
        layoutLogout = rootView.findViewById(R.id.layout_LogoutUser);

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo");
        builder.setPositiveButton("Cancel", null);
    }

    private void logoutUser() {
        builder.setMessage("Bạn có muốn đăng xuất?");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
    private void getCurrentUser(){
        DatabaseReference databaseReference = database.getReference("Users").
                child(mUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                txtName.setText(user.getName());
                txtEmail.setText(mUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
