package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/2/17.
 */

public class CategoryList {
    String id,category,arabic_title;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArabic_title() {
        return arabic_title;
    }

    public void setArabic_title(String arabic_title) {
        this.arabic_title = arabic_title;
    }

    public CategoryList(String id, String category, String arabic_title) {
        this.id=id;
        this.category=category;
        this.arabic_title=arabic_title;
    }
}
