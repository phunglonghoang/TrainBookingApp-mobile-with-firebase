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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GaTauAdapter extends RecyclerView.Adapter<GaTauAdapter.UserViewHolder>{
    private Context context;
    private List<GaTau> gaTauList;
    public GaTauAdapter(Context context, List<GaTau> gaTauList){
        this.context = context;
        this.gaTauList = gaTauList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gatau, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final GaTau gaTau = gaTauList.get(position);
        if(gaTau == null){
            return;
        }
        holder.txtTenGa.setText(gaTau.getTenGaTau());
        holder.txtDiaChi.setText(gaTau.getDiaChi());
        holder.itemGaTau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInformationGaTau(Gravity.CENTER, gaTau);
            }
        });
    }
    private void showInformationGaTau(int gravity, GaTau gaTau){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_infor_gatau);

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
        Button btnSua = dialog.findViewById(R.id.btnThemGaTau);
        Button btnXoa = dialog.findViewById(R.id.btnXoa_InforGaTau);

        etTenGa.setText(gaTau.getTenGaTau());
        etDiaChi.setText(gaTau.getDiaChi());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(context);
                String tenGa = etTenGa.getText().toString();
                String diaChi = etDiaChi.getText().toString();
                if(tenGa.trim().isEmpty() || diaChi.trim().isEmpty()){
                    Toast.makeText(context, "Hãy nhập đầy đủ thông tin ga tàu", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference("GaTau").
                            child(gaTau.getMaGaTau());
                    Map<String, Object> map = new HashMap<>();
                    map.put("tenGaTau", tenGa);
                    map.put("diaChi", diaChi);
                    databaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            progressDialog.dismiss();
                            if(error == null){
                                Toast.makeText(context, "Sửa ga tàu thành công", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Xóa ga tàu này?");
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressDialog progressDialog = new ProgressDialog(context);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference("GaTau").
                                child(gaTau.getMaGaTau());
                        progressDialog.show();
                        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Xóa ga tàu thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(context, "Xóa ga tàu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        if (gaTauList != null){
            return gaTauList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTenGa, txtDiaChi;
        private CardView itemGaTau;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenGa = itemView.findViewById(R.id.txtTenGa_ItemGaTau);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi_ItemGaTau);
            itemGaTau = itemView.findViewById(R.id.item_gatau);
        }
    }
}
