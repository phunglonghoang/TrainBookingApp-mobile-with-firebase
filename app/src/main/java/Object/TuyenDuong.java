package Object;

import java.io.Serializable;

public class TuyenDuong implements Serializable {
    private String maTuyenDuong, tenTuyenDuong, gaDi, gaDen, gioKhoiHanh, thoiGianDiChuyen;
    private int slGhe;
    private float giaVeGiuongNam, giaVeGheNgoi;

    public TuyenDuong() {
    }

    public TuyenDuong(String tenTuyenDuong, String gaDi, String gaDen, String gioKhoiHanh,
                      String thoiGianDiChuyen, int slGhe, float giaVeGiuongNam, float giaVeGheNgoi) {
        this.tenTuyenDuong = tenTuyenDuong;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
        this.gioKhoiHanh = gioKhoiHanh;
        this.thoiGianDiChuyen = thoiGianDiChuyen;
        this.slGhe = slGhe;
        this.giaVeGiuongNam = giaVeGiuongNam;
        this.giaVeGheNgoi = giaVeGheNgoi;
    }

    public String getMaTuyenDuong() {
        return maTuyenDuong;
    }

    public void setMaTuyenDuong(String maTuyenDuong) {
        this.maTuyenDuong = maTuyenDuong;
    }

    public String getTenTuyenDuong() {
        return tenTuyenDuong;
    }

    public void setTenTuyenDuong(String tenTuyenDuong) {
        this.tenTuyenDuong = tenTuyenDuong;
    }

    public String getGaDi() {
        return gaDi;
    }

    public void setGaDi(String gaDi) {
        this.gaDi = gaDi;
    }

    public String getGaDen() {
        return gaDen;
    }

    public void setGaDen(String gaDen) {
        this.gaDen = gaDen;
    }

    public String getGioKhoiHanh() {
        return gioKhoiHanh;
    }

    public void setGioKhoiHanh(String gioKhoiHanh) {
        this.gioKhoiHanh = gioKhoiHanh;
    }

    public String getThoiGianDiChuyen() {
        return thoiGianDiChuyen;
    }

    public void setThoiGianDiChuyen(String thoiGianDiChuyen) {
        this.thoiGianDiChuyen = thoiGianDiChuyen;
    }

    public int getSlGhe() {
        return slGhe;
    }

    public void setSlGhe(int slGhe) {
        this.slGhe = slGhe;
    }

    public float getGiaVeGiuongNam() {
        return giaVeGiuongNam;
    }

    public void setGiaVeGiuongNam(float giaVeGiuongNam) {
        this.giaVeGiuongNam = giaVeGiuongNam;
    }

    public float getGiaVeGheNgoi() {
        return giaVeGheNgoi;
    }

    public void setGiaVeGheNgoi(float giaVeGheNgoi) {
        this.giaVeGheNgoi = giaVeGheNgoi;
    }
}
