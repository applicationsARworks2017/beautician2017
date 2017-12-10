package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/24/17.
 */

public class Shops {
    String id,latitudelongitude,photo1,photo2,photo3,no_of_reviews,avg_rating,price,created,shopname,address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNo_of_reviews() {
        return no_of_reviews;
    }

    public void setNo_of_reviews(String no_of_reviews) {
        this.no_of_reviews = no_of_reviews;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Shops(String id, String latitudelongitude, String photo1, String photo2, String photo3, String no_of_reviews, String avg_rating, String created,
                 String shopname, String address) {
        this.id=id;
        this.latitudelongitude=latitudelongitude;
        this.photo1=photo1;
        this.photo2=photo2;
        this.photo3=photo3;
        this.no_of_reviews=no_of_reviews;
        this.avg_rating=avg_rating;
        this.created=created;
        this.shopname=shopname;
        this.address=address;


    }
}
