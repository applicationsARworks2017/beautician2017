package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.OfferSet;
import beautician.com.sapplication.Pojo.Offers;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/24/17.
 */

public class OffersAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Offers> new_list;
    Holder holder;
    String user_id;

    public OffersAdapter(OfferSet offerSet, ArrayList<Offers> oList) {
        this._context=offerSet;
        this.new_list=oList;
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
    private class Holder{
        TextView offerHeading,offer_details;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Offers _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.offer_list, parent, false);
            user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.offerHeading=(TextView)convertView.findViewById(R.id.of_heading);
            holder.offer_details=(TextView)convertView.findViewById(R.id.offeredetails);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.offerHeading.setTag(position);
        holder.offer_details.setTag(position);

        holder.offerHeading.setText(_pos.getTitle()+" at "+_pos.getShopname());
        holder.offer_details.setText(_pos.getOffer_detail());
        return convertView;
    }
}
