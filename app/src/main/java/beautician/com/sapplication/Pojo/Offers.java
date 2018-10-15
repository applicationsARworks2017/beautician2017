package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/24/17.
 */

public class Offers {
    String id,title,offer_detail,shop_id,shopname,photo1,photo2,photo3,photo4;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOffer_detail() {
        return offer_detail;
    }

    public void setOffer_detail(String offer_detail) {
        this.offer_detail = offer_detail;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
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

    public Offers(String id, String title, String offer_detail, String shop_id, String shopname, String photo1,
                  String photo2, String photo3, String photo4) {
        this.id=id;
        this.title=title;
        this.offer_detail=offer_detail;
        this.shop_id=shop_id;
        this.shopname=shopname;
        this.photo1=photo1;
        this.photo2=photo2;
        this.photo3=photo3;
        this.photo4=photo4;


    }
}
