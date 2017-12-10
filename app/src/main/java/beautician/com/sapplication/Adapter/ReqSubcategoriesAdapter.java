package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.RequestSubcategories;
import beautician.com.sapplication.Pojo.SubCategoryList;
import beautician.com.sapplication.R;

/**
 * Created by Amaresh on 11/19/17.
 */

public class ReqSubcategoriesAdapter extends BaseAdapter {
    Context _context;
    ArrayList<SubCategoryList> new_list;
    Holder holder,holder1;
    Dialog dialog;
    String user_id;
    public ReqSubcategoriesAdapter(RequestSubcategories requestSubcategories, ArrayList<SubCategoryList> scList) {
        this._context=requestSubcategories;
        this.new_list=scList;
    }

    @Override
    public int getCount() {
        return new_list.size();
    }

    @Override
    public Object getItem(int position) {
        return new_list.get(position);
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

        final SubCategoryList _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.catsub_list, parent, false);
            holder.cat_name=(TextView)convertView.findViewById(R.id.c_name);
            holder.iv_letterView=(ImageView)convertView.findViewById(R.id.iv_letterView);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder)convertView.getTag();
        }
        holder.cat_name.setTag(position);
        holder.iv_letterView.setTag(position);
        holder.cat_name.setText(_pos.getSubcategory());

        String firstLetter = _pos.getSubcategory().substring(0, 1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(_pos.getSubcategory());
        //int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
        holder.iv_letterView.setImageDrawable(drawable);




        return convertView;
    }
}
