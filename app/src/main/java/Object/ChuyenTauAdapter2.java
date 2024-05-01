package Object;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tvc.datvetaumobileapp.DatVeTauActivity;
import com.tvc.datvetaumobileapp.R;

import java.util.List;

public class ChuyenTauAdapter2 extends RecyclerView.Adapter<ChuyenTauAdapter2.UserViewHolder>{
    private Context context;
    private List<ChuyenTau> chuyenTauList;
    public ChuyenTauAdapter2(Context context, List<ChuyenTau> chuyenTauList){
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
                Intent intent = new Intent(context, DatVeTauActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chuyenTau", chuyenTau);
                intent.putExtras(bundle);
                context.startActivity(intent);
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

}
