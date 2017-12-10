package beautician.com.sapplication.Pojo;

/**
 * Created by RN on 11/9/2017.
 */

public class SpList {
    String id,shop_name,address,latitudelongitude,photo1,photo2,photo3,email,mobile,created_dt,modified_dt;

    public SpList(String id, String shop_name, String address, String latitudelongitude
            , String photo1, String photo2, String photo3, String email, String mobile,
                  String created_dt, String modified_dt) {
        this.id=id;
        this.shop_name=shop_name;
        this.address=address;
        this.latitudelongitude=latitudelongitude;
        this.photo1=photo1;
        this.photo2=photo2;
        this.photo3=photo3;
        this.email=email;
        this.mobile=mobile;
        this.created_dt=created_dt;
        this.modified_dt=modified_dt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitudelongitude() {
        return latitudelongitude;
    }

    public void setLatitudelongitude(String latitudelongitude) {
        this.latitudelongitude = latitudelongitude;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
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
}
