package Object;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.tvc.datvetaumobileapp.R;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;


public class VeTauAdapter2 extends RecyclerView.Adapter<VeTauAdapter2.UserViewHolder>{
    private Context context;
    private List<VeTau> veTauList;
    public VeTauAdapter2(Context context, List<VeTau> veTauList){
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
        dialog.setContentView(R.layout.dialog_infor_lichsudatve);

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
        Button btnInVeTau = dialog.findViewById(R.id.btnInVe);
        btnInVeTau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int REQUEST_CODE = 1102;
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                boolean success = createVeTauPDF(veTau, txtHoTen.getText().toString(), txtEmail.getText().toString());
                if (success){
                    Toast.makeText(context, "In vé tàu thành công(PDF)", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Không thể in vé tàu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    private boolean createVeTauPDF(VeTau veTau, String hoTen, String email){
        LocalDateTime localDateTime = LocalDateTime.now();
        String tenChuyenTau = veTau.getChuyenTau().getTuyenDuong().getTenTuyenDuong();
        String maVeTau = veTau.getMaVeTau();
        String ngayGioKhoiHanh = veTau.getChuyenTau().getNgayDi() + " " + veTau.getChuyenTau().getTuyenDuong().getGioKhoiHanh();
        String soGhe = veTau.getSoGhe();
        String soToa = veTau.getSoGhe().substring(1, 2);
        String loaiGhe = veTau.getLoaiGhe();
        NumberFormat format = NumberFormat.getCurrencyInstance(new
                Locale("vi", "VN"));
        String formattedGiaVe = format.format(veTau.getGiaVe());
        String ngayGioDatVe = veTau.getNgayDatVe() + " " + veTau.getGioDatVe();
        try {
            // Đường dẫn đến thư mục lưu trữ của máy ảo (hoặc thiết bị thực)
            String pdfPath = context.getExternalFilesDir(null).getAbsolutePath() + "/my_ticket" + localDateTime.toString()+".pdf";

            // Tạo một tệp PDF mới
            PdfWriter pdfWriter = new PdfWriter(pdfPath);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            BarcodeQRCode qrCode = new BarcodeQRCode(tenChuyenTau + maVeTau + ngayGioDatVe + soGhe + soToa +
                    loaiGhe + formattedGiaVe + hoTen + email + ngayGioDatVe);
            PdfFormXObject formXObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
            Image image = new Image(formXObject).setWidth(125).setHorizontalAlignment(HorizontalAlignment.CENTER);

            Table table = new Table(UnitValue.createPercentArray(new float[]{1}));
            table.setWidth(300);
            table.setHeight(370);

            Cell cell = createCell(String.format("TRAIN TICKET\nTrip Name: %s\nTicket ID : %s\nDeparture Date/Time: %s\nSeat Number: %s\nNumber of Train Carriage: %s\n" +
                    "Seat Type: %s\nTicket Price: %s\nCustomer Information:\nFull Name: %s\nEmail: %s\nDate/Time of Booking: %s\nQR Code:\n",
                    tenChuyenTau, maVeTau, ngayGioKhoiHanh, soGhe, soToa, loaiGhe, formattedGiaVe, hoTen, email, ngayGioDatVe));
            cell.add(image);
            table.addCell(cell);
            // Căn giữa bảng trong trang PDF
            table.setTextAlignment(TextAlignment.CENTER);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Thêm bảng vào tài liệu
            document.add(table);

            // Đóng tệp PDF
            document.close();

            // Cập nhật Device File Explorer
            refreshDeviceFileExplorer(pdfPath);

            // Trả về true để biểu thị thành công
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            // Trả về false để biểu thị lỗi
            return false;
        }
    }
    private void refreshDeviceFileExplorer(String filePath) {
        // Broadcast để thông báo cập nhật Device File Explorer
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
    }
    private Cell createCell(String content) {
        Cell cell = new Cell();
        cell.add(new Paragraph(content));
        cell.setBorder(new SolidBorder(1)); // Thiết lập viền với độ rộng 1
        return cell;
    }

}
