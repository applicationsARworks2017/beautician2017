package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/11/17.
 */

public class SubCategoryList {
    String id,category,subcategory,category_id,price;
    private boolean selected;
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public SubCategoryList(String id, String subcategory, String category_id, String price) {
        this.id=id;
        this.subcategory=subcategory;
        this.category_id=category_id;
        this.price=price;




    }
}
