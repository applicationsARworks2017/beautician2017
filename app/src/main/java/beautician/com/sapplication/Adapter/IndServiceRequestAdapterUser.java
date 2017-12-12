package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.CheckIndividualPost;
import beautician.com.sapplication.Pojo.IndServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/26/17.
 */

public class IndServiceRequestAdapterUser extends BaseAdapter {
    Context _context;
    ArrayList<IndServiceRequest> new_list;
    Holder holder;
    String user_id;
    public IndServiceRequestAdapterUser(CheckIndividualPost checkIndividualPost, ArrayList<IndServiceRequest> isrList) {
        this._context=checkIndividualPost;
        this.new_list=isrList;
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
    public class Holder{
        TextView Name_service,remarks,expected_date,otp_service;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IndServiceRequest _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.serviceind_list_user, parent, false);
            user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.Name_service=(TextView)convertView.findViewById(R.id.name_service);
            holder.remarks=(TextView)convertView.findViewById(R.id.servicedetails);
            holder.otp_service=(TextView)convertView.findViewById(R.id.otp_service);
            holder.expected_date=(TextView)convertView.findViewById(R.id.expected_date);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.Name_service.setTag(position);
        holder.remarks.setTag(position);
        holder.otp_service.setTag(position);
        holder.Name_service.setText("You have requested to "+_pos.getShopname()+" for the service");
        holder.remarks.setText(_pos.getRemarks());
        final String status=_pos.getStatus();

        if(status.contentEquals("0")){
            Resources ress = _context.getResources();
            Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_white_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
        }
        else if(status.contentEquals("1")){
            holder.otp_service.setVisibility(View.VISIBLE);
            holder.otp_service.setText("Share OTP before service and get back your $ 5. OTP : "+_pos.getOtp());
            Resources ress = _context.getResources();
            Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
        }
        if(_pos.getExpected_date().contentEquals("")|| _pos.getExpected_date().contentEquals("null")){
            holder.expected_date.setText("No date defined");

        }
        else {
            holder.expected_date.setText("Expected Date: " + _pos.getExpected_date());
        }
        return convertView;
    }
}
