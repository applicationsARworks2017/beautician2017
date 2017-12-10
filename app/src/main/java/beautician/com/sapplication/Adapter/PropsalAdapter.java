package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import beautician.com.sapplication.Activity.GiveCommentActivity;
import beautician.com.sapplication.Activity.PropsalView;
import beautician.com.sapplication.Activity.ShopDetails;
import beautician.com.sapplication.Activity.SpProposal;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.Proposals;
import beautician.com.sapplication.Pojo.RatingsPoints;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/23/17.
 */

public class PropsalAdapter extends BaseAdapter {
    private Context _context;
    Holder holder,holder1;
    Dialog dialog;
    String from_page;
    String callPage="blanck";
    private ArrayList<Proposals> new_list;
    public PropsalAdapter(SpProposal spProposal, ArrayList<Proposals> pList,String page) {
        this._context=spProposal;
        this.new_list=pList;
        this.from_page=page;
    }

    public PropsalAdapter(FragmentActivity activity, ArrayList<Proposals> pList, String user_side) {
        this._context=activity;
        this.new_list=pList;
        this.from_page=user_side;
    }

    public PropsalAdapter() {

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
        TextView propsal_hd,vew_details,gv_feedback,actualtime,user_details;
        ImageView im_reply,im_agree;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Proposals _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.propsal_list, parent, false);
            holder.propsal_hd=(TextView)convertView.findViewById(R.id.propsal_hd);
            holder.vew_details=(TextView)convertView.findViewById(R.id.view_details);
            holder.gv_feedback=(TextView)convertView.findViewById(R.id.gv_feedback);
            holder.actualtime=(TextView)convertView.findViewById(R.id.actualtime);
            holder.im_reply=(ImageView)convertView.findViewById(R.id.im_reply);
            holder.im_agree=(ImageView)convertView.findViewById(R.id.im_agree);
            holder.user_details=(TextView) convertView.findViewById(R.id.user_details);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.propsal_hd.setTag(position);
        holder.vew_details.setTag(position);
        holder.actualtime.setTag(position);
        holder.im_reply.setTag(holder);
        holder.im_agree.setTag(holder);
        holder.gv_feedback.setTag(holder);
        holder.user_details.setTag(holder);
        holder.gv_feedback.setVisibility(View.GONE);
        holder.user_details.setVisibility(View.GONE);

        final String status=_pos.getStatus();

        holder.actualtime.setText(_pos.getCreated());
        if(from_page.contentEquals("user_side")){
            holder.im_reply.setVisibility(View.GONE);
            holder.im_agree.setVisibility(View.VISIBLE);
            if(status.contentEquals("1")) {
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.black));
                holder.im_agree.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("2")){
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("3")){

                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
                holder.im_agree.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("4")){ // otp given and 5 $ reversed
                holder.gv_feedback.setVisibility(View.VISIBLE);
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_power_input_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("5")){ // completed
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);
                holder.gv_feedback.setVisibility(View.GONE);
            }
            else{
                holder.im_agree.setEnabled(false);
            }
        }
        else{
            holder.im_agree.setVisibility(View.GONE);
            holder.im_reply.setVisibility(View.VISIBLE);
            if(status.contentEquals("1")){
                Resources ress = _context.getResources(); // no response
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_error_black_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
                holder.im_reply.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("2")){  // got response and wave
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_assignment_turned_in_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.black));
                holder.im_reply.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("3")){ // service is giving after getting user details
                holder.user_details.setVisibility(View.VISIBLE);
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_assignment_turned_in_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_reply.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("4")){ // otp matched and service continuing
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_power_input_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_reply.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("5")){ // completed
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_reply.setImageDrawable(drawable1);

            }
            else {
                holder.im_reply.setEnabled(false);
            }
        }
        if(from_page.contentEquals("user_side")){
            holder.propsal_hd.setText(_pos.getShop_name().toUpperCase()+" has replied : "+_pos.getRemarks());

        }
        else {
            holder.propsal_hd.setText(_pos.getRemarks());
        }
        holder.propsal_hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from_page.contentEquals("user_side")) {
                    Intent intent = new Intent(_context, ShopDetails.class);
                    intent.putExtra("SHOP_ID", _pos.getShop_id());
                    _context.startActivity(intent);
                }
            }
        });
        holder.gv_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder)v.getTag();
                holder1.gv_feedback.setVisibility(View.GONE);
                Intent intent=new Intent(_context,GiveCommentActivity.class);
                intent.putExtra("SHOP_NAME",_pos.getShop_name());
                intent.putExtra("SHOP_ID",_pos.getShop_id());
                intent.putExtra("PROPSAL_ID",_pos.getId());
                _context.startActivity(intent);
            }
        });
        holder.vew_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_context,PropsalView.class);
                intent.putExtra("SID",_pos.getService_request_id());
                intent.putExtra("PID",_pos.getId());
                intent.putExtra("PSTATUS",_pos.getStatus());
                intent.putExtra("PROPSAL",_pos.getRemarks());
                _context.startActivity(intent);

            }
        });
        holder.im_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder) v.getTag();
                String callTo = null;
                if(status.contentEquals("1")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setTitle("Sorry");
                    builder.setMessage("You can't do anything till user respond");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            //   dialog.dismiss();


                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(status.contentEquals("2")){
                    callTo = "3";
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setTitle("User is ready to take the service");
                    builder.setMessage("You have to pay $5 , Do you want to go ahead?");
                    final String finalCallTo = callTo;
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            //   dialog.dismiss();
                          //  Toast.makeText(_context,"Insufficient wallet balance",Toast.LENGTH_SHORT).show();
                            String otp=String.valueOf(Constants.generatePIN());
                            ConfirmToProp confirmToProp = new ConfirmToProp();
                            confirmToProp.execute(_pos.getId(), finalCallTo,otp);

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(status.contentEquals("3")){
                    callTo = "4";
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setTitle("Service Complete");
                    builder.setMessage("Do you want to confirm that your service has completed ?");
                    final String finalCallTo = callTo;
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            //   dialog.dismiss();
                            ConfirmToProp confirmToProp = new ConfirmToProp();
                            confirmToProp.execute(_pos.getId(), finalCallTo);

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        holder.im_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder) v.getTag();
                String callTo = null;
                if(status.contentEquals("1")) {
                    callTo = "2";
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setTitle("Are you Comfortable with this proposal");
                    builder.setMessage("Do you want to go ahead ?");
                    final String finalCallTo = callTo;
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            //   dialog.dismiss();
                            ConfirmToProp confirmToProp = new ConfirmToProp();
                            confirmToProp.execute(_pos.getId(), finalCallTo);

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(status.contentEquals("2")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setTitle("Allready Allowed");
                    builder.setMessage("You have sent notification for service");
                    final String finalCallTo = callTo;
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            //   dialog.dismiss();


                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        holder.user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    public class ConfirmToProp extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Share Sync";
        String server_message;
        String id,username,email_address,contact_no;
        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _id = params[0];
                String _status = params[1];
                String _otp = params[2];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.EDIT_PROPSAL ;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = null;
                if(_status.contentEquals("3")){
                    builder=new Uri.Builder()
                            .appendQueryParameter("id", _id)
                            .appendQueryParameter("status", _status)
                            .appendQueryParameter("otp", _otp);
                }
                else {
                    builder = new Uri.Builder()
                            .appendQueryParameter("id", _id)
                            .appendQueryParameter("status", _status);
                }


                //.appendQueryParameter("deviceid", deviceid);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1 ) {
                        server_message="Successful";
                    }
                    else  {
                        server_message = "Error";
                    }

                }
                return null;
            } catch (Exception exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(callPage.contentEquals("comment")){

            }
            else {
                Toast.makeText(_context, server_message, Toast.LENGTH_SHORT).show();
                if (server_status == 1) {
                    if (holder1.im_agree.getVisibility() == View.VISIBLE) {
                        holder1.im_agree.setVisibility(View.GONE);
                    } else if (holder1.im_reply.getVisibility() == View.VISIBLE) {
                        holder1.im_reply.setVisibility(View.GONE);
                    } else if (holder1.gv_feedback.getVisibility() == View.VISIBLE) {
                        holder1.gv_feedback.setVisibility(View.GONE);
                    }
                }
            }

        }
    }

    public void calltoupdate(String id, String status,String call){
        callPage=call;
        ConfirmToProp confirmToProp = new ConfirmToProp();
        confirmToProp.execute(id, status);
    }

}
