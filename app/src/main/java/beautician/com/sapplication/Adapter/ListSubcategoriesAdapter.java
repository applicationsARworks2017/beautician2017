package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.MySubcategories;
import beautician.com.sapplication.Activity.SubCategoryEditActivity;
import beautician.com.sapplication.Pojo.SubCategoryList;
import beautician.com.sapplication.R;

/**
 * Created by Amaresh on 12/4/17.
 */

public class ListSubcategoriesAdapter extends BaseAdapter {
    Context _context;
    ArrayList<SubCategoryList> new_list;
    Holder holder,holder1;
    Dialog dialog;
    String sub_cat_name,lang,user_type;
    public ListSubcategoriesAdapter(MySubcategories mySubcategories, ArrayList<SubCategoryList> scList,String lang,String user_type) {
        this._context=mySubcategories;
        this.new_list=scList;
        this.lang=lang;
        this.user_type=user_type;

    }

    @Override
    public int getCount() {
        return new_list.size();
    }

    @Override
    public Object getItem(int position) {
        return  new_list.get(position);    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView cat_name,c_price;
        ImageView iv_letterView,subc_edit;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SubCategoryList _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.mycatsub_list, parent, false);
            holder.cat_name=(TextView)convertView.findViewById(R.id.c_name);
            holder.c_price=(TextView)convertView.findViewById(R.id.c_price);
            holder.iv_letterView=(ImageView)convertView.findViewById(R.id.iv_letterView);
            holder.subc_edit=(ImageView)convertView.findViewById(R.id.subc_edit);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder)convertView.getTag();
        }
        holder.cat_name.setTag(position);
        holder.c_price.setTag(position);
        holder.iv_letterView.setTag(position);
        holder.subc_edit.setTag(holder);
        holder.c_price.setText("SAR "+_pos.getPrice());
       if(lang.contentEquals("Arabic")){
           holder.cat_name.setText(_pos.getArabic_title());

       }
       else{
           holder.cat_name.setText(_pos.getSubcategory());


       }
       if(user_type.contentEquals("SP")){
           holder.subc_edit.setVisibility(View.VISIBLE);
       }
        String firstLetter = _pos.getSubcategory().substring(0, 1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(_pos.getSubcategory());
        //int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
        holder.iv_letterView.setImageDrawable(drawable);

        holder.subc_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder)v.getTag();
                Intent intent=new Intent(_context,SubCategoryEditActivity.class);
                intent.putExtra("SUB_CATG_ENGLISH",_pos.getSubcategory());
                intent.putExtra("SUB_CATG_ARABIC",_pos.getArabic_title());
                intent.putExtra("SUB_PRICE",_pos.getPrice());
                intent.putExtra("SUB_ID",_pos.getId());
                intent.putExtra("PRICE_ID",_pos.getPrice_id());
                _context.startActivity(intent);
            }
        });



        return convertView;
    }
}
