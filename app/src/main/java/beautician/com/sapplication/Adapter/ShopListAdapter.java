package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.IndividualRequest;
import beautician.com.sapplication.Activity.MyserviceList;
import beautician.com.sapplication.Activity.SearchShopList;
import beautician.com.sapplication.Activity.ShopDetails;
import beautician.com.sapplication.Pojo.Shops;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/24/17.
 */

public class ShopListAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Shops> new_list;
    Holder holder;
    String lang;
    public static String selected_shopid,selected_shop_name;
    public ShopListAdapter(SearchShopList searchShopList, ArrayList<Shops> sList,String lang) {
        this._context=searchShopList;
        this.new_list=sList;
        this.lang=lang;
    }

    @Override
    public int getCount() {
        return new_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class Holder {
        TextView Shopname,check_details,shop_add,price_min,rev_shop,dis_shop;
        ImageView pic1,pic2,pic3,pic4;
        RatingBar ratingBar;
        Button reqButton;
        LinearLayout img_lin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Shops _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.shop_list, parent, false);
            holder.Shopname=(TextView)convertView.findViewById(R.id.Shopname);
            holder.check_details=(TextView)convertView.findViewById(R.id.check_details);
            holder.shop_add=(TextView)convertView.findViewById(R.id.shop_add);
            holder.price_min=(TextView)convertView.findViewById(R.id.price_min);
            holder.rev_shop=(TextView)convertView.findViewById(R.id.rev_shop);
            holder.dis_shop=(TextView)convertView.findViewById(R.id.dis_shop);
            holder.pic1=(ImageView)convertView.findViewById(R.id.pic1);
            holder.pic2=(ImageView)convertView.findViewById(R.id.pic2);
            holder.pic3=(ImageView)convertView.findViewById(R.id.pic3);
            holder.pic4=(ImageView)convertView.findViewById(R.id.pic4);
            holder.ratingBar=(RatingBar)convertView.findViewById(R.id.rating);
            holder.reqButton=(Button)convertView.findViewById(R.id.reqButton);
            holder.img_lin=(LinearLayout)convertView.findViewById(R.id.img_lin);

            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.Shopname.setTag(position);
        holder.check_details.setTag(position);
        holder.shop_add.setTag(position);
        holder.price_min.setTag(position);
        holder.rev_shop.setTag(position);
        holder.dis_shop.setTag(position);
        holder.pic1.setTag(position);
        holder.pic2.setTag(position);
        holder.pic3.setTag(position);
        holder.pic4.setTag(position);
        holder.ratingBar.setTag(position);
        holder.reqButton.setTag(position);
        holder.img_lin.setTag(position);

        if(lang.contentEquals("Arabic")){
            holder.check_details.setText("تحقق من تفاصيل الخدمة هنا");
            holder.reqButton.setText("طلب");
        }
        else{
            holder.check_details.setText("Check service details here");
            holder.reqButton.setText("Request");

        }

        holder.Shopname.setText(_pos.getShopname());
        holder.shop_add.setText(_pos.getAddress());
        holder.price_min.setText("$ "+_pos.getPrice());
        holder.rev_shop.setText(" . "+_pos.getNo_of_reviews()+" reviews");
        holder.dis_shop.setText(". ~ "+_pos.getDistance()+" KM");
        String location=_pos.getLatitudelongitude();
        /*if(location==null || location.contentEquals("")){

        }
        else {
            String locList[] = location.split(",");
            Double latitude = Double.valueOf(locList[0]);
            Double longitude = Double.valueOf(locList[1]);
            Double distance = Constants.CalculationByDistance(latitude, longitude);

        //    holder.dis_shop.setText(String.valueOf(" . ~ " + distance + " KM"));
        }
*/
        String ratings=_pos.getAvg_rating();
        if((ratings==null) || ratings.contentEquals("")){

        }
        else{
            if(ratings==null || ratings=="" || ratings.contentEquals("null")){

            }
            else {
                holder.ratingBar.setRating(Float.parseFloat(ratings));
            }

        }

         if(!_pos.getPhoto1().isEmpty()) {
            Picasso.with(_context).load(_pos.getPhoto1()).resize(300,300).into(holder.pic1);
        }
        if(!_pos.getPhoto2().isEmpty()) {
            Picasso.with(_context).load(_pos.getPhoto2())
                    .resize(300,300).into(holder.pic2);
        }
        if(!_pos.getPhoto3().isEmpty()) {
            Picasso.with(_context).load(_pos.getPhoto3()).resize(300,300).into(holder.pic3);
        }if(!_pos.getPhoto4().isEmpty()) {
            Picasso.with(_context).load(_pos.getPhoto3()).resize(300,300).into(holder.pic4);
        }
        holder.reqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_context,IndividualRequest.class);
                intent.putExtra("SHOP_ID",_pos.getId());
                intent.putExtra("SHOP_NAME",_pos.getShopname());
                intent.putExtra("DETAILS","");
                _context.startActivity(intent);
            }
        });
        holder.Shopname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_context,ShopDetails.class);
                intent.putExtra("SHOP_ID",_pos.getId());
                intent.putExtra("MAP","false");
                _context.startActivity(intent);
            }
        });
        holder.img_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_context,ShopDetails.class);
                intent.putExtra("SHOP_ID",_pos.getId());
                intent.putExtra("MAP","false");
                _context.startActivity(intent);
            }
        });


        holder.check_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_shop_name=_pos.getShopname();
                selected_shopid=_pos.getId();
            Intent intent=new Intent(_context, MyserviceList.class);
                intent.putExtra("USERID",_pos.getId());
                intent.putExtra("PAGE","user");
                _context.startActivity(intent);
            }
        });

        return convertView;

    }
}
