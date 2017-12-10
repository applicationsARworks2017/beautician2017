package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/21/17.
 */

public class ServiceRequest {
    String id,name,mobile,email,photo,remarks,category,sub_category,status,created, expected_date;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
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

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public ServiceRequest(String id, String name, String mobile, String email, String photo, String remarks,
                          String category, String sub_category, String status, String created, String expected_date) {
        this.id=id;
        this.name=name;
        this.mobile=mobile;
        this.email=email;
        this.photo=photo;
        this.remarks=remarks;
        this.category=category;
        this.sub_category=sub_category;
        this.status=status;
        this.created=created;
        this.expected_date=expected_date;

    }
}
