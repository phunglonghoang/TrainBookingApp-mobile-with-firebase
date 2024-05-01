package Object;

import java.io.Serializable;
import java.util.List;

public class ChuyenTau implements Serializable {
    private String maChuyenTau, ngayDi;
    private TuyenDuong tuyenDuong;
    private List<VeTau> dsVe;

    public List<VeTau> getDsVe() {
        return dsVe;
    }

    public void setDsVe(List<VeTau> dsVe) {
        this.dsVe = dsVe;
    }

    public ChuyenTau() {
    }

    public ChuyenTau(String ngayDi, TuyenDuong tuyenDuong) {
        this.ngayDi = ngayDi;
        this.tuyenDuong = tuyenDuong;
    }

    public String getMaChuyenTau() {
        return maChuyenTau;
    }

    public void setMaChuyenTau(String maChuyenTau) {
        this.maChuyenTau = maChuyenTau;
    }

    public String getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(String ngayDi) {
        this.ngayDi = ngayDi;
    }

    public TuyenDuong getTuyenDuong() {
        return tuyenDuong;
    }

    public void setTuyenDuong(TuyenDuong tuyenDuong) {
        this.tuyenDuong = tuyenDuong;
    }
}
