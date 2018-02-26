package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.CheckIndividualPost;
import beautician.com.sapplication.Activity.GiveCommentActivity;
import beautician.com.sapplication.Pojo.IndServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/26/17.
 */

public class IndServiceRequestAdapterUser extends BaseAdapter {
    Context _context;
    ArrayList<IndServiceRequest> new_list;
    Holder holder,holder1;
    String user_id,lang;
    public IndServiceRequestAdapterUser(CheckIndividualPost checkIndividualPost, ArrayList<IndServiceRequest> isrList,String lang) {
        this._context=checkIndividualPost;
        this.new_list=isrList;
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
    public class Holder{
        TextView Name_service,remarks,expected_date,otp_service,gv_feedback;
        ImageView im_reply;

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
            holder.im_reply=(ImageView)convertView.findViewById(R.id.im_reply);
            holder.gv_feedback=(TextView) convertView.findViewById(R.id.gv_feedback);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.Name_service.setTag(position);
        holder.remarks.setTag(position);
        holder.otp_service.setTag(position);
        holder.im_reply.setTag(holder);
        holder.gv_feedback.setTag(holder);
        holder.Name_service.setText("You have requested to "+_pos.getShopname()+" for the service");
        holder.remarks.setText(_pos.getRemarks());
        final String status=_pos.getStatus();

        if(status.contentEquals("0")){
            holder.gv_feedback.setTag(holder);
            holder.gv_feedback.setVisibility(View.GONE);
            Resources ress = _context.getResources();
            Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_white_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
            holder.im_reply.setImageDrawable(drawable1);

        }
        else if(status.contentEquals("1")){
            holder.gv_feedback.setVisibility(View.GONE);
            holder.otp_service.setVisibility(View.VISIBLE);
            holder.otp_service.setText("Share OTP before service and get back your $ 5. OTP : "+_pos.getOtp());
            Resources ress = _context.getResources();
            Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
            holder.im_reply.setImageDrawable(drawable1);

        }
        else if(status.contentEquals("2")){
            holder.gv_feedback.setVisibility(View.GONE);
            holder.otp_service.setVisibility(View.GONE);
            Resources ress = _context.getResources();
            Drawable drawable1 = ress.getDrawable(R.mipmap.ic_power_input_white_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
            holder.im_reply.setImageDrawable(drawable1);

        }
        else if(status.contentEquals("3")){
            holder.gv_feedback.setVisibility(View.VISIBLE);
            holder.otp_service.setVisibility(View.GONE);
            Resources ress = _context.getResources();
            Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
            holder.im_reply.setImageDrawable(drawable1);

        }
        else{
            holder.im_reply.setEnabled(false);
            holder.gv_feedback.setVisibility(View.GONE);

        }
        if(_pos.getExpected_date().contentEquals("")|| _pos.getExpected_date().contentEquals("null")){
            holder.expected_date.setText("No date defined");

        }
        else {
            holder.expected_date.setText("Expected Date: " + _pos.getExpected_date());
        }
        holder.gv_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder)v.getTag();
                holder1.gv_feedback.setVisibility(View.GONE);
                Intent intent=new Intent(_context,GiveCommentActivity.class);
                intent.putExtra("PAGE","indrequest");
                intent.putExtra("SHOP_NAME",_pos.getShopname());
                intent.putExtra("SHOP_ID",_pos.getShopid());
                intent.putExtra("PROPSAL_ID",_pos.getId());
                _context.startActivity(intent);
            }
        });
        return convertView;
    }
}
