package is.hi.hopur16.nyttapp;

import java.io.Serializable;

public class User implements Serializable {

    public String username;
    public String name;
    public String phone;
    public String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String username, String name, String phone, String email) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
