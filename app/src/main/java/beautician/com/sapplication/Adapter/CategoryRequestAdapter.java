package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.CategoriesRequest;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.R;

/**
 * Created by Amaresh on 11/21/17.
 */

public class CategoryRequestAdapter extends BaseAdapter {
    Context _context;
    ArrayList<CategoryList> categoryLists;
    Holder holder;
    public CategoryRequestAdapter(CategoriesRequest categoriesRequest, ArrayList<CategoryList> cList) {
        this._context=categoriesRequest;
        this.categoryLists=cList;
    }

    @Override
    public int getCount() {
        return categoryLists.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView cat_name;
        ImageView iv_letterView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoryList _pos=categoryLists.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.catrequest_list, parent, false);
            holder.cat_name=(TextView)convertView.findViewById(R.id.c_name);
            holder.iv_letterView=(ImageView)convertView.findViewById(R.id.iv_letterView);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.iv_letterView.setTag(position);
        holder.cat_name.setTag(position);
        holder.cat_name.setText(_pos.getCategory());
        String firstLetter = _pos.getCategory().substring(0, 1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(_pos.getCategory());
        //int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
        holder.iv_letterView.setImageDrawable(drawable);
        return convertView;
    }
}
