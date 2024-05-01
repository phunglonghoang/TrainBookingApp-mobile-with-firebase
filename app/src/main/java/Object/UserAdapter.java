package Object;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tvc.datvetaumobileapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private Context context;
    private List<User> userList;
    public UserAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = userList.get(position);
        if(user == null){
            return;
        }
        holder.txtName.setText(user.getName());
        holder.txtEmail.setText(user.getEmail());
        holder.itemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInformationUser(Gravity.CENTER, user);
            }
        });
    }
    private void showInformationUser(int gravity, User user){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_infor_user);

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

        TextView txtName = dialog.findViewById(R.id.txtName_Dialog_Infor_User);
        TextView txtEmail = dialog.findViewById(R.id.txtEmail_Dialog_Infor_user);
        TextView txtNgaySinh = dialog.findViewById(R.id.txtNgaySinh_Dialog_Infor_user);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose_DialogThemGaTau);

        txtName.setText(user.getName());
        txtNgaySinh.setText(user.getDateOfBirth());
        txtEmail.setText(user.getEmail());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    @Override
    public int getItemCount() {
        if (userList != null){
            return userList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtEmail;
        private CardView itemUser;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtTenGa_ItemGaTau);
            txtEmail = itemView.findViewById(R.id.txtDiaChi_ItemGaTau);
            itemUser = itemView.findViewById(R.id.item_account);
        }
    }
}
