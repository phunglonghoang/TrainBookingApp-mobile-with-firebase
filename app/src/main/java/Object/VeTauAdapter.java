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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tvc.datvetaumobileapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VeTauAdapter extends RecyclerView.Adapter<VeTauAdapter.UserViewHolder>{
    private Context context;
    private List<VeTau> veTauList;
    public VeTauAdapter(Context context, List<VeTau> veTauList){
        this.context = context;
        this.veTauList = veTauList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vetau, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final VeTau veTau = veTauList.get(position);
        if(veTau == null){
            return;
        }
        holder.txtTenChuyenTau.setText(veTau.getChuyenTau().getTuyenDuong().getTenTuyenDuong());
        holder.txtMaVeTau.setText(veTau.getMaVeTau());
        holder.txtNgayGioKhoiHanh.setText(veTau.getChuyenTau().getNgayDi() + " " + veTau.getChuyenTau().getTuyenDuong().getGioKhoiHanh());
        holder.txtSoGhe.setText(veTau.getSoGhe());
        holder.txtSoToa.setText(veTau.getSoGhe().substring(1, 2));
        holder.txtLoaiGhe.setText(veTau.getLoaiGhe());
        NumberFormat format = NumberFormat.getCurrencyInstance(new
                Locale("vi", "VN"));
        String formattedGiaVe = format.format(veTau.getGiaVe());
        holder.txtGiaVe.setText(formattedGiaVe);
        holder.itemVeTau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInformationChuyenTau(Gravity.CENTER, veTau);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (veTauList != null){
            return veTauList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTenChuyenTau, txtMaVeTau, txtNgayGioKhoiHanh,
                txtSoGhe, txtSoToa, txtLoaiGhe, txtGiaVe;
        private CardView itemVeTau;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenChuyenTau = itemView.findViewById(R.id.txtTenChuyenTau_ItemVeTau);
            txtMaVeTau = itemView.findViewById(R.id.txtMaVe_ItemVeTau);
            txtNgayGioKhoiHanh = itemView.findViewById(R.id.txtNgayGioXuatPhat_ItemVeTau);
            txtSoGhe = itemView.findViewById(R.id.txtSoGhe_ItemVeTau);
            txtSoToa = itemView.findViewById(R.id.txtSoToaTau_ItemVeTau);
            txtLoaiGhe = itemView.findViewById(R.id.txtLoaiGhe_ItemVeTau);
            txtGiaVe = itemView.findViewById(R.id.txtGiaVe_ItemVeTau);

            itemVeTau = itemView.findViewById(R.id.item_vetau);
        }
    }
    private void showInformationChuyenTau(int gravity, VeTau veTau){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_infor_vetau);

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

        TextView txtTenChuyenTau = dialog.findViewById(R.id.txtTenChuyenTau_DialogLichSuDatVe);
        TextView txtMaVeTau = dialog.findViewById(R.id.txtMaVe_DialogLichSuDatVeTau);
        TextView txtNgayGioKhoiHanh = dialog.findViewById(R.id.txtNgayGioKhoiHanh_DialogLichSuDatVe);
        TextView txtSoGhe = dialog.findViewById(R.id.txtSoGhe_DialogLichSuDatVe);
        TextView txtSoToa = dialog.findViewById(R.id.txtSoToa_DialogLichSuDatVe);
        TextView txtLoaiGhe = dialog.findViewById(R.id.txtLoaiGhe_DialogLichSuDatVe);
        TextView txtGiaVe = dialog.findViewById(R.id.txtGiaVe_DiaLogLichSuDatVe);
        TextView txtEmail = dialog.findViewById(R.id.txtEmail_DialogLichSuDatVe);
        TextView txtHoTen = dialog.findViewById(R.id.txtHoTen_DialogLichSuDatVe);
        TextView txtNgayGioDatVe = dialog.findViewById(R.id.txtNgayGioDatVe_LichSuDatVe);

        txtTenChuyenTau.setText(veTau.getChuyenTau().getTuyenDuong().getTenTuyenDuong());
        txtMaVeTau.setText(veTau.getMaVeTau());
        txtNgayGioKhoiHanh.setText(veTau.getChuyenTau().getNgayDi() + " " + veTau.getChuyenTau().getTuyenDuong().getGioKhoiHanh());
        txtSoGhe.setText(veTau.getSoGhe());
        txtSoToa.setText(veTau.getSoGhe().substring(1, 2));
        txtLoaiGhe.setText(veTau.getLoaiGhe());
        NumberFormat format = NumberFormat.getCurrencyInstance(new
                Locale("vi", "VN"));
        String formattedGiaVe = format.format(veTau.getGiaVe());
        txtGiaVe.setText(formattedGiaVe);
        txtNgayGioDatVe.setText(veTau.getNgayDatVe() + " " + veTau.getGioDatVe());

        DatabaseReference reference = FirebaseDatabase.getInstance().
                getReference("Users").child(veTau.getUserID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    txtHoTen.setText(user.getName());
                    txtEmail.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ImageButton btnClose = dialog.findViewById(R.id.btnClose_DialogThanhToan);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnHuyVe = dialog.findViewById(R.id.btnInVe);
        btnHuyVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog alertDialog;
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận hủy vé?");
                builder.setPositiveButton("Cancel", null);
                ProgressDialog progressDialog = new ProgressDialog(context);
                builder.setNegativeButton("Hủy vé", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                                getReference("VeTau").child(veTau.getMaVeTau());
                        progressDialog.show();
                        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Hủy vé tàu thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(context, "Hủy vé tàu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        dialog.show();
    }

}
