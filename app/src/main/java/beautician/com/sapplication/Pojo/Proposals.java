package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/23/17.
 */

public class Proposals {
    String id,service_request_id,remarks,category,status,created,shop_id, shop_name, user_id, otp;

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

    public Proposals(String id, String service_request_id, String remarks, String status, String created,
                     String shop_id, String shop_name, String user_id, String otp) {
        this.id=id;
        this.service_request_id=service_request_id;
        this.remarks=remarks;
        this.status=status;
        this.created=created;
        this.shop_id=shop_id;
        this.shop_name=shop_name;
        this.user_id=user_id;
        this.otp=otp;

    }
}
