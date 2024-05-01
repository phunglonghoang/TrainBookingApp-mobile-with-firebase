package Object;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TuyenDuongAdapter extends RecyclerView.Adapter<TuyenDuongAdapter.UserViewHolder>{
    private Context context;
    private List<TuyenDuong> tuyenDuongList;
    public TuyenDuongAdapter(Context context, List<TuyenDuong> tuyenDuongList){
        this.context = context;
        this.tuyenDuongList = tuyenDuongList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tuyenduong, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final TuyenDuong tuyenDuong = tuyenDuongList.get(position);
        if(tuyenDuong == null){
            return;
        }
        holder.txtGaDi.setText(tuyenDuong.getGaDi());
        holder.txtGaDen.setText(tuyenDuong.getGaDen());
        holder.txtGioKhoiHanh.setText(tuyenDuong.getGioKhoiHanh());
        holder.txtSLGhe.setText("SLGhế: " + tuyenDuong.getSlGhe());
        NumberFormat format = NumberFormat.getCurrencyInstance(new
                Locale("vi", "VN"));
        holder.txtGiaVe.setText("Giá vé: " + format.format(tuyenDuong.getGiaVeGheNgoi()));
        holder.itemTuyenDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInformationTD(Gravity.CENTER, tuyenDuong);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (tuyenDuongList != null){
            return tuyenDuongList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView txtGaDi, txtGaDen, txtGioKhoiHanh, txtSLGhe, txtGiaVe;
        private CardView itemTuyenDuong;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGaDi = itemView.findViewById(R.id.txtGaDi_ItemTD);
            txtGaDen = itemView.findViewById(R.id.txtGaDen_ItemTD);
            txtGioKhoiHanh = itemView.findViewById(R.id.txtGioKhoiHanh_ItemTD);
            txtSLGhe = itemView.findViewById(R.id.txtSLGhe_ItemTD);
            txtGiaVe = itemView.findViewById(R.id.txtGiaVe_ItemTD);
            itemTuyenDuong = itemView.findViewById(R.id.item_tuyenduong);
        }
    }
    private void showInformationTD(int gravity, TuyenDuong tuyenDuong){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_infor_tuyenduong);

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
        AutoCompleteTextView atGaDi = dialog.findViewById(R.id.atGaDi_DialogInforTD);
        AutoCompleteTextView atGaDen = dialog.findViewById(R.id.atGaDen_DialogInforTD);
        EditText etSLGhe = dialog.findViewById(R.id.etSLGhe_DialogInforTD);
        EditText etGiaVeGiuongNam = dialog.findViewById(R.id.etGiaVeGiuongNam_DialogInforTD);
        EditText etGiaVeGheNgoi = dialog.findViewById(R.id.etGiaVeGheNgoi_DialogInforTD);
        EditText etGioXuatPhat = dialog.findViewById(R.id.etGioXuatPhat_DialogInforTD);
        EditText etTGDiChuyen = dialog.findViewById(R.id.etTGDiChuyen_DialogInforTD);

        atGaDi.setText(tuyenDuong.getGaDi());
        atGaDen.setText(tuyenDuong.getGaDen());
        etSLGhe.setText(String.valueOf(tuyenDuong.getSlGhe()));
        etGiaVeGiuongNam.setText(String.valueOf(tuyenDuong.getGiaVeGiuongNam()));
        etGiaVeGheNgoi.setText(String.valueOf(tuyenDuong.getGiaVeGheNgoi()));
        etGioXuatPhat.setText(tuyenDuong.getGioKhoiHanh());
        etTGDiChuyen.setText(tuyenDuong.getThoiGianDiChuyen());

        ImageButton btnClose = dialog.findViewById(R.id.btnClose_DialogThanhToan);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnXoa = dialog.findViewById(R.id.btnXoa_DialogInforTD);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Xóa tuyến đường này?");
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.show();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TuyenDuong").
                                child(tuyenDuong.getMaTuyenDuong());
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Xóa tuyến đường thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(context, "Xóa tuyến đường thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Button btnSua = dialog.findViewById(R.id.btnSua_DialogInforTD);
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slGhe = etSLGhe.getText().toString();
                String giaVeGiuongNam = etGiaVeGiuongNam.getText().toString();
                String giaVeGheNgoi = etGiaVeGheNgoi.getText().toString();
                String tgDiChuyen = etTGDiChuyen.getText().toString();
                if(slGhe.isEmpty() || giaVeGiuongNam.isEmpty() ||
                        giaVeGheNgoi.isEmpty() || tgDiChuyen.trim().isEmpty()){
                    Toast.makeText(context, "Hãy nhập đủ thông tin tuyến đường cần sửa", Toast.LENGTH_SHORT).show();
                }
                else if(!TextUtils.isDigitsOnly(slGhe)){
                    etSLGhe.setError("Hãy nhập đúng định dạng số ghê");
                    etSLGhe.requestFocus();
                }
                else {
                    try {
                        int slGheInt = Integer.parseInt(slGhe);
                        float giaVeGiuongNamFloat = Float.parseFloat(giaVeGiuongNam);
                        float giaVeGheNgoiFloat = Float.parseFloat(giaVeGheNgoi);
                        if (slGheInt <= 0) {
                            etSLGhe.setError("Số lượng ghế phải lớn hơn 0");
                            etSLGhe.requestFocus();
                        } else if (giaVeGiuongNamFloat <= 0) {
                            etGiaVeGiuongNam.setError("Giá vé phải có giá trị lớn hơn 0");
                            etGiaVeGiuongNam.requestFocus();
                        } else if (giaVeGheNgoiFloat <= 0) {
                            etGiaVeGheNgoi.setError("Giá vé phải có giá trị lớn hơn 0");
                            etGiaVeGheNgoi.requestFocus();
                        }
                        else {
                            ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.show();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TuyenDuong").
                                    child(tuyenDuong.getMaTuyenDuong());
                            Map<String, Object> map = new HashMap<>();
                            map.put("slGhe", slGheInt);
                            map.put("giaVeGiuongNam", giaVeGiuongNamFloat);
                            map.put("giaVeGheNgoi", giaVeGheNgoiFloat);
                            map.put("thoiGianDiChuyen", tgDiChuyen);
                            reference.updateChildren(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    progressDialog.dismiss();
                                    if(error == null){
                                        Toast.makeText(context, "Sửa tuyến đường thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    } catch (NumberFormatException e){
                        etGiaVeGiuongNam.setError("Hãy nhập đúng định dạng giá vé");
                        etGiaVeGiuongNam.requestFocus();

                        etGiaVeGheNgoi.setError("Hãy nhập đúng định dạng giá vé");
                        etGiaVeGheNgoi.requestFocus();
                    }

                }
            }
        });

        dialog.show();
    }
}
