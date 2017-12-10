package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/26/17.
 */

public class IndServiceRequest {
    String id,remarks,personId,personName,personemail,personmobile,
            personphoto,status, no_of_user,shopname,expected_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonemail() {
        return personemail;
    }

    public void setPersonemail(String personemail) {
        this.personemail = personemail;
    }

    public String getPersonmobile() {
        return personmobile;
    }

    public void setPersonmobile(String personmobile) {
        this.personmobile = personmobile;
    }

    public String getPersonphoto() {
        return personphoto;
    }

    public void setPersonphoto(String personphoto) {
        this.personphoto = personphoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNo_of_user() {
        return no_of_user;
    }

    public void setNo_of_user(String no_of_user) {
        this.no_of_user = no_of_user;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public IndServiceRequest(String id, String remarks, String personId, String personName, String personemail, String personmobile, String personphoto,
                             String status, String no_of_user, String shopname, String ecpected_date) {

        this.id=id;
        this.remarks=remarks;
        this.personId=personId;
        this.personName=personName;
        this.personemail=personemail;
        this.personmobile=personmobile;
        this.personphoto=personphoto;
        this.status=status;
        this.no_of_user=no_of_user;
        this.shopname=shopname;
        this.expected_date=ecpected_date;




    }
}
