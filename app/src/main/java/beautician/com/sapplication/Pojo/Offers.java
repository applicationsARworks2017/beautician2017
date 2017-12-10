package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/24/17.
 */

public class Offers {
    String id,title,offer_detail,shop_id,shopname;

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

    public Offers(String id, String title, String offer_detail, String shop_id, String shopname) {
        this.id=id;
        this.title=title;
        this.offer_detail=offer_detail;
        this.shop_id=shop_id;
        this.shopname=shopname;

    }
}
