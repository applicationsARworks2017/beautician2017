package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 3/26/18.
 */

public class Countries {
    String id,country,a2,a3,dialing_code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getA3() {
        return a3;
    }

    public void setA3(String a3) {
        this.a3 = a3;
    }

    public String getDialing_code() {
        return dialing_code;
    }

    public void setDialing_code(String dialing_code) {
        this.dialing_code = dialing_code;
    }

    public Countries(String id, String country, String a2, String a3, String dialing_code) {
        this.id=id;
        this.country=country;
        this.a2=a2;
        this.a3=a3;
        this.dialing_code=dialing_code;

    }
}
