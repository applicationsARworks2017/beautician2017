package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/27/17.
 */

public class RatingsPoints { String points;
    Boolean ischecked;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Boolean getIschecked() {
        return ischecked;
    }

    public void setIschecked(Boolean ischecked) {
        this.ischecked = ischecked;
    }

    public RatingsPoints(String s, boolean b) {
        this.points=s;
        this.ischecked=b;

    }
}
