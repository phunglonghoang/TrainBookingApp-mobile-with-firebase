package Object;

public class GaTau {
    private String maGaTau, tenGaTau, diaChi;

    public GaTau() {
    }

    public GaTau(String tenGaTau, String diaChi) {
        this.tenGaTau = tenGaTau;
        this.diaChi = diaChi;
    }

    public String getMaGaTau() {
        return maGaTau;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setMaGaTau(String maGaTau) {
        this.maGaTau = maGaTau;
    }

    public String getTenGaTau() {
        return tenGaTau;
    }

    public void setTenGaTau(String tenGaTau) {
        this.tenGaTau = tenGaTau;
    }
}
