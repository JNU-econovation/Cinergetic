package project.bong.com.cinergetic.model;

import java.io.Serializable;

public class User {

    String id, pw, name, email;
    boolean isAdmin;

    public User(){
        this.id = this.pw = this.name = this.email = "";
        this.isAdmin = false;
    }

    public User(String id, String pw, String name, String email) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.isAdmin = (id.equals("admin")) ? true : false;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }
}
