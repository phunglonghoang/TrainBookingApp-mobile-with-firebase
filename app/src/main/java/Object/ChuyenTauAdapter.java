package Object;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tvc.datvetaumobileapp.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Service.*;

public class ChuyenTauAdapter extends RecyclerView.Adapter<ChuyenTauAdapter.UserViewHolder>{
    private Context context;
    private List<ChuyenTau> chuyenTauList;
    public ChuyenTauAdapter(Context context, List<ChuyenTau> chuyenTauList){
        this.context = context;
        this.chuyenTauList = chuyenTauList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chuyentau, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final ChuyenTau chuyenTau = chuyenTauList.get(position);
        if(chuyenTau == null){
            return;
        }
        holder.txtGaDi.setText(chuyenTau.getTuyenDuong().getGaDi());
        holder.txtGaDen.setText(chuyenTau.getTuyenDuong().getGaDen());
        holder.txtNgayKhoiHanh.setText(chuyenTau.getNgayDi());
        holder.txtSLGhe.setText("SL Ghế: " + String.valueOf(chuyenTau.getTuyenDuong().getSlGhe()));
        holder.txtGioXuatPhat.setText(chuyenTau.getTuyenDuong().getGioKhoiHanh());
        holder.itemChuyenTau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInformationChuyenTau(Gravity.CENTER, chuyenTau);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (chuyenTauList != null){
            return chuyenTauList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView txtGaDi, txtGaDen, txtNgayKhoiHanh, txtGioXuatPhat, txtSLGhe;
        private CardView itemChuyenTau;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGaDi = itemView.findViewById(R.id.txtGaDi_ItemTD);
            txtGaDen = itemView.findViewById(R.id.txtGaDen_ItemTD);
            txtNgayKhoiHanh = itemView.findViewById(R.id.txtNgayKhoiHanh_ItemCT);
            txtGioXuatPhat = itemView.findViewById(R.id.txtGioXuatPhat_ItemCT);
            txtSLGhe = itemView.findViewById(R.id.txtSLGhe_ItemCT);
            itemChuyenTau = itemView.findViewById(R.id.item_chuyentau);
        }
    }
    private void showInformationChuyenTau(int gravity, ChuyenTau chuyenTau){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_infor_chuyentau);

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
        EditText etTenChuyenTau = dialog.findViewById(R.id.etTienThanhToan_DialogThanhToan);
        EditText etGioXuatPhat = dialog.findViewById(R.id.etGioXuatPhat_DialogInforCT);
        EditText etNgayKhoiHanh = dialog.findViewById(R.id.etNgayKhoiHanh_DialogInforCT);

        etTenChuyenTau.setText("Chuyến tàu: " + chuyenTau.getTuyenDuong().getTenTuyenDuong());
        etGioXuatPhat.setText("Giờ xuất phát: " + chuyenTau.getTuyenDuong().getGioKhoiHanh());
        etNgayKhoiHanh.setText(chuyenTau.getNgayDi());
        etNgayKhoiHanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePicker showDatePicker = new ShowDatePicker(context, Calendar.getInstance(), etNgayKhoiHanh);
                showDatePicker.showDatePickerDialogForFuture(2);
            }
        });

        ImageButton btnClose = dialog.findViewById(R.id.btnClose_DialogThanhToan);
        Button btnSua = dialog.findViewById(R.id.btnSua_DialogInforCT);
        Button btnXoa = dialog.findViewById(R.id.btnXoa_DialogInforCT);

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog alertDialog;
                builder.setTitle("Thông báo");
                builder.setMessage("Hủy chuyến tàu này?");
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressDialog progressDialog = new ProgressDialog(context);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChuyenTau").
                                child(chuyenTau.getMaChuyenTau());
                        progressDialog.show();
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Hủy chuyến tàu thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(context, "Hủy chuyến tàu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(context);
                String ngayKhoiHanh = etNgayKhoiHanh.getText().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChuyenTau").
                        child(chuyenTau.getMaChuyenTau());
                progressDialog.show();
                Map<String, Object> map = new HashMap<>();
                map.put("ngayDi", ngayKhoiHanh);
                reference.updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        progressDialog.dismiss();
                        if(error == null){
                            Toast.makeText(context, "Đổi ngày khởi hành thành công", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Đổi ngày khởi hành thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
