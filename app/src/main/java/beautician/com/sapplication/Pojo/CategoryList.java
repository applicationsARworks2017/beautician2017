package beautician.com.sapplication.Pojo;

/**
 * Created by Amaresh on 11/2/17.
 */

public class CategoryList {
    String id,category;
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

    public CategoryList(String id, String category) {
        this.id=id;
        this.category=category;
    }
}
