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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import beautician.com.sapplication.Activity.HomeActivity;
import beautician.com.sapplication.Activity.PostActivity;
import beautician.com.sapplication.Activity.PropsalView;
import beautician.com.sapplication.Activity.ShopDetails;
import beautician.com.sapplication.Activity.SpProposal;
import beautician.com.sapplication.Activity.Wallet;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.Proposals;
import beautician.com.sapplication.Pojo.RatingsPoints;
import beautician.com.sapplication.R;
import beautician.com.sapplication.SplashScreen;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/23/17.
 */

public class PropsalAdapter extends BaseAdapter {
    private Context _context;
    Holder holder,holder1;
    Dialog dialog;
    int updated_status;
    String from_page,wpage="no page",shop_id,user_id,propsal_id;
    Double user_balance, shop_balance;
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
        TextView propsal_hd,vew_details,gv_feedback,actualtime,user_details,tv_otp;
        ImageView im_reply,im_agree;
        LinearLayout paidline;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Proposals _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.propsal_list, parent, false);
            shop_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.propsal_hd=(TextView)convertView.findViewById(R.id.propsal_hd);
            holder.vew_details=(TextView)convertView.findViewById(R.id.view_details);
            holder.gv_feedback=(TextView)convertView.findViewById(R.id.gv_feedback);
            holder.actualtime=(TextView)convertView.findViewById(R.id.actualtime);
            holder.tv_otp=(TextView)convertView.findViewById(R.id.tv_otp);
            holder.im_reply=(ImageView)convertView.findViewById(R.id.im_reply);
            holder.im_agree=(ImageView)convertView.findViewById(R.id.im_agree);
            holder.user_details=(TextView) convertView.findViewById(R.id.user_details);
            holder.paidline=(LinearLayout)convertView.findViewById(R.id.paidline);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.propsal_hd.setTag(position);
        holder.vew_details.setTag(position);
        holder.actualtime.setTag(position);
        holder.paidline.setTag(position);
        holder.tv_otp.setTag(position);
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
            holder.tv_otp.setVisibility(View.GONE);
            holder.im_agree.setVisibility(View.VISIBLE);
            if(status.contentEquals("1")) {
                holder.paidline.setVisibility(View.GONE);
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.black));
                holder.im_agree.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("2")){
                holder.paidline.setVisibility(View.GONE);
                holder.tv_otp.setVisibility(View.GONE);
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("3")){
                holder.paidline.setVisibility(View.VISIBLE);
                holder.tv_otp.setVisibility(View.VISIBLE);
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
                holder.im_agree.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("4")){ // otp given and 5 $ reversed
                holder.paidline.setVisibility(View.GONE);
                holder.tv_otp.setVisibility(View.GONE);
                holder.gv_feedback.setVisibility(View.VISIBLE);
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_power_input_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);
            }
            else if(status.contentEquals("5")){ // completed
                holder.paidline.setVisibility(View.GONE);
                holder.tv_otp.setVisibility(View.GONE);
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
            holder.tv_otp.setText("Share OTP before service and get back your $ 5. OTP : "+_pos.getOtp());

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
                intent.putExtra("PAGE","propsal");
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
                    user_id=_pos.getUser_id();
                    propsal_id=_pos.getId();
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setTitle("User is ready to take the service");
                    builder.setMessage("You have to pay $5 , Do you want to go ahead?");
                    final String finalCallTo = callTo;
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //check own balance
                            wpage="sp_home";
                            getWdetails getWdetails=new getWdetails();
                            getWdetails.execute(shop_id);

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
                    updated_status=4;
                    user_id=_pos.getUser_id();
                    final Dialog dialog = new Dialog(_context);
                    final String finalCallTo = callTo;
                    dialog.setContentView(R.layout.otpscreen);
                    ImageView imageView=(ImageView)dialog.findViewById(R.id.close);
                    Button submit=(Button)dialog.findViewById(R.id.add_money);
                    final EditText et_add_money=(EditText) dialog.findViewById(R.id.et_add_money);
                    dialog.show();
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ConfirmToProp confirmToProp = new ConfirmToProp();
                            confirmToProp.execute(_pos.getId(), finalCallTo,et_add_money.getText().toString().trim());
                            dialog.cancel();
                        }
                    });
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    builder.setTitle("Service");
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
                    dialog.show();*/
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
        private ProgressDialog progressDialog = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // onPreExecuteTask();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(_context, "Updating", "Please wait...");
            }
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
                if(_status.contentEquals("3") || _status.contentEquals("4")){
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
                server_message = "N Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.cancel();
            if(callPage.contentEquals("comment")){

            }
            else {
                if (server_status == 1) {
                    if(updated_status==4){

                        wpage = "user_side";
                        Log.i("userid", user_id);
                        getWdetails getUWdetails = new getWdetails();
                        getUWdetails.execute(user_id);
                        if (holder1.im_reply.getVisibility() == View.VISIBLE) {
                            holder1.im_reply.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Toast.makeText(_context, server_message, Toast.LENGTH_SHORT).show();
                        if (holder1.im_agree.getVisibility() == View.VISIBLE) {
                            holder1.im_agree.setVisibility(View.GONE);
                        } else if (holder1.im_reply.getVisibility() == View.VISIBLE) {
                            holder1.im_reply.setVisibility(View.GONE);
                        } else if (holder1.gv_feedback.getVisibility() == View.VISIBLE) {
                            holder1.gv_feedback.setVisibility(View.GONE);
                        }
                    }
                }
                else{
                    Constants.noInternetDialouge(_context, "Failed to update");
                }
            }

        }
    }

    public void calltoupdate(String id, String status,String call){
        callPage=call;
        ConfirmToProp confirmToProp = new ConfirmToProp();
        confirmToProp.execute(id, status);
    }
    /**
     * Async task to get wallet balance from  camp table from server
     * */
    private class getWdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get Wallet Balance";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(_context, "Checking wallet", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userid = params[0];
                InputStream in = null;
                int resCode = -1;
                String link=null;
                if(wpage.contentEquals("user_side")) {
                    link = Constants.ONLINEURL + Constants.USER_BALANCE;
                }
                else if(wpage.contentEquals("sp_home")){
                    link = Constants.ONLINEURL + Constants.SHOP_BALANCE;

                }
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
                if(wpage.contentEquals("user_side")) {
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", _userid);
                }
                else if(wpage.contentEquals("sp_home")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("shop_id", _userid);
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
                if(in == null){
                    return null;
                }
                BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "",data="";

                while ((data = reader.readLine()) != null){
                    response += data + "\n";
                }

                Log.i(TAG, "Response : "+response);

                /**
                 * {
                 "userWallets": [
                 {
                 "id": 7,
                 "user_id": 10,
                 "debit": 0,
                 "credit": 10,
                 "balance": 10,
                 "created": "2017-12-03T10:28:29+05:30",
                 "modified": "2017-12-03T10:28:29+05:30",
                 "user": {
                 "id": 10,
                 "name": "avinash pathak",
                 "email": "avinasha@yahoo.com",
                 "mobile": "7205674061",
                 "photo": null,
                 "created": "1988-01-23T00:00:00+05:30",
                 "modified": "1988-01-23T00:00:00+05:30",
                 "usertype": "test",
                 "fcm_id": null
                 }
                 }
                 ]
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONArray serviceListArray;
                    if(wpage.contentEquals("user_side")) {
                        serviceListArray = res.getJSONArray("userWallets");
                        if(serviceListArray.length()<=0) {
                            server_status = 0;
                        }
                        else{
                            server_status = 1;
                            for (int i = 0; i < serviceListArray.length(); i++) {
                                JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                                String id = o_list_obj.getString("id");
                                user_balance = o_list_obj.getDouble("balance");
                            }
                        }
                    }
                    else if(wpage.contentEquals("sp_home")){
                        serviceListArray = res.getJSONArray("wallets");
                        if(serviceListArray.length()<=0) {
                            server_status = 0;
                        }
                        else{
                            server_status = 1;
                            for (int i = 0; i < serviceListArray.length(); i++) {
                                JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                                String id = o_list_obj.getString("id");
                                shop_balance = o_list_obj.getDouble("balance");
                            }
                        }
                    }


                }

                return null;
            } catch(Exception exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if (server_status == 1) {
                if(updated_status==4){
                    Toast.makeText(_context, "Service started", Toast.LENGTH_SHORT).show();
                    wpage = "user_side";
                    Transactwallet transactwallet = new Transactwallet();
                    transactwallet.execute(user_id, "5", String.valueOf(user_balance+5), "0");
                }
                else {
                    if (wpage.contentEquals("sp_home")) {
                        if (shop_balance > 5) {
                            wpage = "user_side";
                            Log.i("userid", user_id);
                            getWdetails getUWdetails = new getWdetails();
                            getUWdetails.execute(user_id);
                        } else {
                            Constants.noInternetDialouge(_context, "You don't have sufficient amount in wallet");
                        }
                    } else if (wpage.contentEquals("user_side")) {
                        if (user_balance > 5) {
                            wpage = "sp_home";
                            Transactwallet transactwallet = new Transactwallet();
                            transactwallet.execute(shop_id, "0", String.valueOf(shop_balance - 5), "5");
                            //Toast.makeText(_context,"go aahead",Toast.LENGTH_LONG).show();
                        } else {
                            // this is for insufficient balance in the user side
                            Constants.noInternetDialouge(_context, "User is not ready to take the service");

                        }

                    }
                }


              //  Toast.makeText(_context,shop_balance+"/"+user_balance,Toast.LENGTH_LONG).show();
            }

        }
    }
        /**
         *
         * Async task to Update the wallet
         * */
        private class Transactwallet extends AsyncTask<String, Void, Void> {

            private static final String TAG = "update wallet";
            int wallet_status;
            String server_message;
            ProgressDialog progressDialog=null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(progressDialog == null) {
                    progressDialog = ProgressDialog.show(_context, "Updating wallet", "Please wait...");
                }
            }
            @Override
            protected Void doInBackground(String... params) {

                try {
                    String _userid = params[0];
                    String _recharge_amount = params[1];
                    String _debit_amount = params[3];
                    String _balance_amount = params[2];
                    InputStream in = null;
                    int resCode = -1;

                    String link = null;
                    if(wpage.contentEquals("sp_home")) {
                        link = Constants.ONLINEURL + Constants.SHOP_WALLLET_UPDATE;
                    }
                    else if(wpage.contentEquals("user_side")){
                        link = Constants.ONLINEURL + Constants.USER_WALLLET_UPDATE;

                    }
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
                    if(wpage.contentEquals("sp_home")) {
                        builder = new Uri.Builder()
                                .appendQueryParameter("shop_id", _userid)
                                .appendQueryParameter("debit", _debit_amount)
                                .appendQueryParameter("credit", _recharge_amount)
                                .appendQueryParameter("remarks", "Service started")
                                .appendQueryParameter("balance", _balance_amount);
                    }
                    else if(wpage.contentEquals("user_side")){
                        builder = new Uri.Builder()
                                .appendQueryParameter("user_id", _userid)
                                .appendQueryParameter("debit", _debit_amount)
                                .appendQueryParameter("credit", _recharge_amount)
                                .appendQueryParameter("remarks", "Service started")
                                .appendQueryParameter("balance", _balance_amount);
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
                    if(in == null){
                        return null;
                    }
                    BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String response = "",data="";

                    while ((data = reader.readLine()) != null){
                        response += data + "\n";
                    }

                    Log.i(TAG, "Response : "+response);

                    /**
                     * {
                     "status": 1,
                     "message": "Data inserted successfully"
                     }
                     * */

                    if(response != null && response.length() > 0) {
                        JSONObject res = new JSONObject(response.trim());
                        JSONObject j_obj=res.getJSONObject("res");
                        wallet_status = j_obj.optInt("status");
                        if(wallet_status==1) {
                            server_message="Wallet Updated";
                        }
                        else{
                            server_message="Wallet can't be Updated";

                        }

                    }

                    return null;
                } catch(Exception exception){
                    server_message="Wallet error";
                    Log.e(TAG, "SynchMobnum : doInBackground", exception);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void user) {
                super.onPostExecute(user);
                progressDialog.dismiss();
                if(wallet_status==1){
                    if(wpage.contentEquals("sp_home")) {
                        wpage = "user_side";
                        Transactwallet transactwallet = new Transactwallet();
                        transactwallet.execute(user_id, "0", String.valueOf(user_balance - 5), "5");
                    }
                    //Toast.makeText(PostActivity.this,"Hello",Toast.LENGTH_LONG).show();
                    if(updated_status==4){
                        Toast.makeText(_context,"User's wallet updated",Toast.LENGTH_LONG).show();

                    }
                    else {
                        String otp = String.valueOf(Constants.generatePIN());
                        ConfirmToProp confirmToProp = new ConfirmToProp();
                        confirmToProp.execute(propsal_id, "3", otp);
                    }
                }
                else{
                    Constants.noInternetDialouge(_context,"Action failed");
                }
            }
        }


}
