package beautician.com.sapplication.Pojo;

/**
 * Created by RN on 11/9/2017.
 */

public class User {
    String id,name,email,mobile,photo,created_dt,modified_dt,usertype;

    public User(String id, String name, String email, String mobile, String photo,
                String created_dt, String modified_dt, String usertype) {
        this.id=id;
        this.name=name;
        this.email=email;
        this.mobile=mobile;
        this.photo=photo;
        this.created_dt=created_dt;
        this.modified_dt=modified_dt;
        this.usertype=usertype;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreated_dt() {
        return created_dt;
    }

    public void setCreated_dt(String created_dt) {
        this.created_dt = created_dt;
    }

    public String getModified_dt() {
        return modified_dt;
    }

    public void setModified_dt(String modified_dt) {
        this.modified_dt = modified_dt;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
