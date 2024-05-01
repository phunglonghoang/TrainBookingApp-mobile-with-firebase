package Object;

public class VeTau {
    private String maVeTau, ngayDatVe, gioDatVe, loaiGhe, userID;
    private String soGhe;
    private float giaVe;
    private ChuyenTau chuyenTau;

    public String getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }

    public VeTau() {
    }

    public VeTau(String ngayDatVe, String gioDatVe, String loaiGhe, String soGhe, float giaVe, String userID, ChuyenTau chuyenTau) {
        this.ngayDatVe = ngayDatVe;
        this.gioDatVe = gioDatVe;
        this.loaiGhe = loaiGhe;
        this.soGhe = soGhe;
        this.giaVe = giaVe;
        this.userID = userID;
        this.chuyenTau = chuyenTau;
    }

    public String getMaVeTau() {
        return maVeTau;
    }

    public void setMaVeTau(String maVeTau) {
        this.maVeTau = maVeTau;
    }

    public String getNgayDatVe() {
        return ngayDatVe;
    }

    public void setNgayDatVe(String ngayDatVe) {
        this.ngayDatVe = ngayDatVe;
    }

    public String getGioDatVe() {
        return gioDatVe;
    }

    public void setGioDatVe(String gioDatVe) {
        this.gioDatVe = gioDatVe;
    }

    public String getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }


    public float getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(float giaVe) {
        this.giaVe = giaVe;
    }



    public ChuyenTau getChuyenTau() {
        return chuyenTau;
    }

    public void setChuyenTau(ChuyenTau chuyenTau) {
        this.chuyenTau = chuyenTau;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
