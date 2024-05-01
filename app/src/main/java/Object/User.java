package Object;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class User implements Serializable {
    private String userUID, name, dateOfBirth, email;
    private int userRole;
    private float accountBalance;
    private List<VeTau> dsVe;

    public List<VeTau> getDsVe() {
        return dsVe;
    }

    public void setDsVe(List<VeTau> dsVe) {
        this.dsVe = dsVe;
    }

    public User() {
    }

    public User(String userUID, String name, int userRole, String dateOfBirth, String email) {
        this.userUID = userUID;
        this.name = name;
        this.userRole = userRole;
        this.dateOfBirth = dateOfBirth;
        this.email = email;

    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


}
