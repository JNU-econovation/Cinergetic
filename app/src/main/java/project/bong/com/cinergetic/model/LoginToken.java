package project.bong.com.cinergetic.model;

public class LoginToken {

    private String name;
    private String id;
    private boolean isAdmin;

    public LoginToken(String id, String name, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getAdmin(){
        return isAdmin;
    }

}
