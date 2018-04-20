package beautician.com.sapplication.Adapter;

import android.app.Activity;
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

import beautician.com.sapplication.Activity.Categories;
import beautician.com.sapplication.Activity.CountryList;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.Countries;
import beautician.com.sapplication.R;

/**
 * Created by Amaresh on 11/3/17.
 */

public class CountriesAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Countries> categoryLists;
    Holder holder;
    String lang;


    public CountriesAdapter(CountryList countryList, ArrayList<Countries> cList) {
        this._context=countryList;
        this.categoryLists=cList;
        this.lang=lang;
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

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Countries _pos=categoryLists.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.coun_list, parent, false);
            holder.cat_name=(TextView)convertView.findViewById(R.id.c_name);

            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.cat_name.setTag(position);
        holder.cat_name.setText(_pos.getCountry()+"  ( "+_pos.getA3()+" )");
        return convertView;
    }
}
