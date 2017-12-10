package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import beautician.com.sapplication.Activity.CheckIndividualPost;
import beautician.com.sapplication.Pojo.IndServiceRequest;
import beautician.com.sapplication.Pojo.ServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/26/17.
 */

public class IndServiceRequestAdapter extends BaseAdapter {
    Context _context;
    ArrayList<IndServiceRequest> new_list;
    Holder holder,holder1;
    String user_id;
    public IndServiceRequestAdapter(CheckIndividualPost checkIndividualPost, ArrayList<IndServiceRequest> isrList) {
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
    private class Holder{
        TextView Name_service,remarks;
        ImageView im_reply;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IndServiceRequest _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.serviceind_list, parent, false);
            user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.Name_service=(TextView)convertView.findViewById(R.id.name_service);
            holder.remarks=(TextView)convertView.findViewById(R.id.servicedetails);
            holder.im_reply=(ImageView)convertView.findViewById(R.id.im_reply);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.Name_service.setTag(position);
        holder.remarks.setTag(position);
        holder.im_reply.setTag(holder);
        holder.Name_service.setText(_pos.getPersonName()+" has requseted you for the service");
        holder.remarks.setText(_pos.getRemarks());
        holder.im_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Please pay $5 for getting the service.");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                        //finish();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                        //finish();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        return convertView;
    }
}
