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

import com.tvc.datvetaumobileapp.LapLichChuyenTauActivity;
import com.tvc.datvetaumobileapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TuyenDuongAdapter2 extends RecyclerView.Adapter<TuyenDuongAdapter2.UserViewHolder>{
    private Context context;
    private List<TuyenDuong> tuyenDuongList;
    public TuyenDuongAdapter2(Context context, List<TuyenDuong> tuyenDuongList){
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
                Intent intent = new Intent(context, LapLichChuyenTauActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tuyenDuong", tuyenDuong);
                intent.putExtras(bundle);
                context.startActivity(intent);
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

}
