package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/23/17.
 */

public class Proposals {
    String id,service_request_id,remarks,category,status,created,shop_id, shop_name, user_id, otp, photo1,photo2,photo3,photo4;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_request_id() {
        return service_request_id;
    }

    public void setService_request_id(String service_request_id) {
        this.service_request_id = service_request_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public Proposals(String id, String service_request_id, String remarks, String status, String created,
                     String shop_id, String shop_name, String user_id, String otp, String photo1, String photo2, String photo3, String photo4) {
        this.id=id;
        this.service_request_id=service_request_id;
        this.remarks=remarks;
        this.status=status;
        this.created=created;
        this.shop_id=shop_id;
        this.shop_name=shop_name;
        this.user_id=user_id;
        this.otp=otp;
        this.photo1=photo1;
        this.photo2=photo2;
        this.photo3=photo3;
        this.photo4=photo4;

    }
}
