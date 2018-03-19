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

import beautician.com.sapplication.Activity.CheckPost;
import beautician.com.sapplication.Pojo.ServiceRequest;
import beautician.com.sapplication.R;

/**
 * Created by Amaresh on 11/21/17.
 */

public class ServiceReqAdapter extends BaseAdapter{
    Context _context;
    ArrayList<ServiceRequest> new_list;
    Holder holder;
    String lang;
    public ServiceReqAdapter(CheckPost checkPost, ArrayList<ServiceRequest> srList,String lang) {
    this._context=checkPost;
        this.new_list=srList;
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

    private  class Holder{
        TextView Name_service,servide_details,remarks,tv_expected_date,actualtime;
        ImageView reply;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ServiceRequest _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.serviceuser_list, parent, false);
            holder.Name_service=(TextView)convertView.findViewById(R.id.name_service);
            holder.remarks=(TextView)convertView.findViewById(R.id.servicedetails);
            holder.tv_expected_date=(TextView)convertView.findViewById(R.id.tv_expected_date);
            holder.actualtime=(TextView)convertView.findViewById(R.id.actualtime);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.Name_service.setTag(position);
        holder.remarks.setTag(position);

        holder.actualtime.setText(_pos.getCreated());
        if(lang.contentEquals("Arabic")){
            holder.Name_service.setText("تم نشر الخدمه" + _pos.getSub_category());

        }
        else{
            holder.Name_service.setText("You have posted for " + _pos.getSub_category());

        }

        if(lang.contentEquals("Arabic")){
            if(_pos.getExpected_date().contentEquals("null")){
                holder.tv_expected_date.setText(("لم يتم تحديد عدد الأشخاص"));

            }
            else{
                holder.tv_expected_date.setText((_pos.getExpected_date()+":التاريخ المتوقع"));
            }

        }
        else{
            if(_pos.getExpected_date().contentEquals("null")){
                holder.tv_expected_date.setText(("No Expected defined"));

            }
            else{
                holder.tv_expected_date.setText(("Expected Date: "+_pos.getExpected_date()));
            }
        }





        holder.remarks.setText(_pos.getRemarks());
        return convertView;
    }
}
