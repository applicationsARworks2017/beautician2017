package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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

import beautician.com.sapplication.Activity.CheckIndividualPost;
import beautician.com.sapplication.Activity.HomeActivity;
import beautician.com.sapplication.Activity.PaymentConfirmation;
import beautician.com.sapplication.Activity.RejectOption;
import beautician.com.sapplication.Activity.SPHome;
import beautician.com.sapplication.Activity.Wallet;
import beautician.com.sapplication.Pojo.IndServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.APIManager;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/26/17.
 */

public class IndServiceRequestAdapter extends BaseAdapter {
    Context _context;
    ArrayList<IndServiceRequest> new_list;
    Holder holder,holder1,rej_holder;
    String user_id, shop_id, lang, wpage="no page";
    Double shop_balance,user_balance;
    int updated_status;
    String credit_id,credit_request_id;
    String id;
    public IndServiceRequestAdapter(CheckIndividualPost checkIndividualPost, ArrayList<IndServiceRequest> isrList,String lang) {
        this._context=checkIndividualPost;
        this.new_list=isrList;
        this.lang=lang;
    }

    public IndServiceRequestAdapter() {

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
        TextView Name_service,remarks,actualtime,tv_expected_date;
        TextView im_reply, imreject;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IndServiceRequest _pos = new_list.get(position);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.serviceind_list, parent, false);
            shop_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.Name_service = (TextView) convertView.findViewById(R.id.name_service);
            holder.remarks = (TextView) convertView.findViewById(R.id.servicedetails);
            holder.actualtime = (TextView) convertView.findViewById(R.id.actualtime);
            holder.tv_expected_date = (TextView) convertView.findViewById(R.id.tv_expected_date);
            holder.im_reply = (TextView) convertView.findViewById(R.id.im_reply);
            holder.imreject = (TextView) convertView.findViewById(R.id.imreject);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.Name_service.setTag(position);
        holder.actualtime.setTag(position);
        holder.remarks.setTag(position);
        holder.tv_expected_date.setTag(position);
        holder.im_reply.setTag(holder);
        holder.imreject.setTag(holder);
        holder.remarks.setText(_pos.getRemarks());
        holder.actualtime.setText(Constants.getOurDate(_pos.getCreated()));
       //  user_id=_pos.getPersonId();

        if(_pos.getExpected_date().contentEquals("null")){
            if(lang.contentEquals("Arabic")){
                holder.tv_expected_date.setText(("لم يتم تحديد عدد الأشخاص"));

            }
            else{
                holder.tv_expected_date.setText(("No Expected defined"));

            }

        }
        else{
            if(lang.contentEquals("Arabic")){
                holder.tv_expected_date.setText((_pos.getExpected_date()+":التاريخ المتوقع "));

            }
            else {
                holder.tv_expected_date.setText(("Expected Date: " + _pos.getExpected_date()));
            }
        }


         if(lang.contentEquals("Arabic")){
             holder.Name_service.setText(_pos.getPersonName() + "لقد طلب الخدمات التاليه "+ _pos.getNo_of_user()+"اشخاص");

         }
         else{
             holder.Name_service.setText(_pos.getPersonName() + " has requseted you for the service for "+ _pos.getNo_of_user()+"people");

         }

        if (_pos.getStatus().contentEquals("0")) {  // got the individual request and want to go ahead

            if(lang.contentEquals("Arabic")) {
                holder.im_reply.setText(R.string.accept_ar);
                holder.imreject.setText(R.string.reject_ar);
            }
            else{
                holder.im_reply.setText("Accept");
                holder.imreject.setText("Reject");
            }
            holder.imreject.setEnabled(true);
            holder.imreject.setVisibility(View.VISIBLE);

        }
        else if (_pos.getStatus().contentEquals("1")) {  // paid and go
            if(lang.contentEquals("Arabic")){
                holder.im_reply.setText(R.string.enter_otp_ar);

            }
            else {
                holder.im_reply.setText(R.string.enter_otp_en);
            }
            holder.imreject.setVisibility(View.GONE);
            /*Resources ress = _context.getResources();
            Drawable drawable1 = ress.getDrawable(R.mipmap.ic_assignment_turned_in_white_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
            holder.im_reply.setImageDrawable(drawable1);*/
        }
        else if (_pos.getStatus().contentEquals("2")) {  // service going on
            if(lang.contentEquals("Arabic")){
                holder.im_reply.setText(R.string.finsih_en);

            }
            else {
                holder.im_reply.setText(R.string.finsih_ar);
            }
            holder.imreject.setVisibility(View.GONE);
        }
        else if (_pos.getStatus().contentEquals("3")) {  // completed
            if(lang.contentEquals("Arabic")){
                holder.im_reply.setText(R.string.completed_ar);

            }
            else {
                holder.im_reply.setText(R.string.completed_en);
            }
            holder.imreject.setVisibility(View.GONE);
        }
        else if (_pos.getStatus().contentEquals("4")) {  // completed and feed back submitted
            if(lang.contentEquals("Arabic")){
                holder.im_reply.setText(R.string.completed_ar);

            }
            else {
                holder.im_reply.setText(R.string.completed_en);
            }
            holder.imreject.setVisibility(View.GONE);
           // holder.im_reply.setVisibility(View.GONE);
        }
        else if (_pos.getStatus().contentEquals("8")) {  // completed and feed back submitted
            holder.im_reply.setVisibility(View.GONE);
            holder.imreject.setVisibility(View.VISIBLE);
            if(lang.contentEquals("Arabic")){
                holder.imreject.setText(R.string.rejected_ar);

            }
            else {
                holder.imreject.setText(R.string.rejected_en);
            }
            holder.imreject.setTextColor(Color.parseColor("#d00000"));
            holder.imreject.setEnabled(false);
            // holder.im_reply.setVisibility(View.GONE);
        }
        holder.imreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rej_holder=(Holder)v.getTag();
                credit_id= _pos.getPersonId();
                credit_request_id = _pos.getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                if(lang.contentEquals("Arabic")){
                    builder.setMessage(R.string.want_reject_ar);

                }
                else {
                    builder.setMessage(R.string.want_reject_en);
                }
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                        //finish()   ;
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CheckInternet.getNetworkConnectivityStatus(_context)) {
                           // wpage = "sp_home";

                            GetBalance getWalletBalance = new GetBalance();
                            getWalletBalance.execute(credit_id);

                        } else {
                            //Constants.noInternetDialouge(_context, "No Internet");
                        }

                    }
                });
                builder.show();
            }
        });
        holder.im_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = _pos.getStatus();
                holder1=(Holder)v.getTag();
                String tv_text = holder1.im_reply.getText().toString().trim();
                id=_pos.getId();
                user_id=_pos.getPersonId();
               // if(status.contentEquals("0") || tv_text.contentEquals("Accept")) {
                if(tv_text.contentEquals("Accept") || tv_text.contentEquals("قبول")) {
                    updated_status = 1;
                    if (lang.contentEquals("Arabic")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setMessage(" يرجى دفع 5 ريالات مقابل الحصول على الخدمة.");
                        builder.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                                //finish()   ;
                            }
                        });
                        builder.setPositiveButton("نعم فعلا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (CheckInternet.getNetworkConnectivityStatus(_context)) {
                                    wpage = "sp_home";
                                    getWdetails getUWdetails = new getWdetails();
                                    getUWdetails.execute(shop_id);
                                } else {
                                    Constants.noInternetDialouge(_context, "No Internet");
                                }

                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setMessage("Please pay SAR"+ SPHome.min_service_charge+"for getting the service.");
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                                //finish()   ;
                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (CheckInternet.getNetworkConnectivityStatus(_context)) {
                                    wpage = "sp_home";
                                    getWdetails getUWdetails = new getWdetails();
                                    getUWdetails.execute(shop_id);
                                } else {
                                    Constants.noInternetDialouge(_context, "No Internet");
                                }

                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
               // else if(status.contentEquals("1") || tv_text.contentEquals("Enter OTP")){
                else if(tv_text.contentEquals("Enter OTP") || tv_text.contentEquals("أدخل رقم إنهاء الخدمة")){
                    updated_status=2;
                    final Dialog dialog = new Dialog(_context);
                    dialog.setContentView(R.layout.otpscreen);
                    ImageView imageView=(ImageView)dialog.findViewById(R.id.close);
                    Button submit=(Button)dialog.findViewById(R.id.add_money);
                    TextView otp_head=(TextView) dialog.findViewById(R.id.otp_head);
                    if(lang.contentEquals("Arabic")){
                        otp_head.setText("أدخل OTP لمتابعة الخدمة");
                        submit.setText("موافق");
                    }
                    else{
                        otp_head.setText("Enter OTP to continue the service");
                        submit.setText("Submit");
                    }
                    final EditText et_otp=(EditText) dialog.findViewById(R.id.et_add_money);
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
                            dialog.dismiss();
                            ConfirmToReq creq = new ConfirmToReq();
                            creq.execute(id, "2", et_otp.getText().toString().trim());
                        }
                    });
                }
               // else if(status.contentEquals("2") || tv_text.contentEquals("Service On")) {
                else if(tv_text.contentEquals("Finsih") || tv_text.contentEquals("إنهاء")) {
                    updated_status = 3;
                    if (lang.contentEquals("Arabic")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setMessage("؟ هل أنت متأكد ، لقد انتهت الخدمة");
                        builder.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                                //finish()   ;
                            }
                        });
                        builder.setPositiveButton("نعم فعلا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ConfirmToReq creq = new ConfirmToReq();
                                creq.execute(id, "3", "1234");

                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setMessage("Are you sure, Service has finished ?");
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                                //finish()   ;
                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ConfirmToReq creq = new ConfirmToReq();
                                creq.execute(id, "3", "1234");

                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });

        return convertView;
    }

    public void statuschange(String id,String status,String otp){
        ConfirmToReq creq = new ConfirmToReq();
        creq.execute(id,status,otp);
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

                }                URL url = new URL(link);
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

                if(updated_status ==1) {
                    if (wpage.contentEquals("sp_home")) {
                        if (shop_balance >= SPHome.min_sp_balance) {
                            wpage = "user_side";
                            Log.i("userid", user_id);
                            getWdetails getUWdetails = new getWdetails();
                            getUWdetails.execute(user_id);
                        } else {
                            if (lang.contentEquals("Arabic")) {
                                Constants.noInternetDialouge(_context, "ليس لديك مبلغ كافي في المحفظة");
                            } else {
                                Constants.noInternetDialouge(_context, "You don't have sufficient amount in wallet");

                            }
                        }
                    } else if (wpage.contentEquals("user_side")) {
                        if (user_balance >= HomeActivity.min_user_balance) {
                            wpage = "sp_home";
                            Transactwallet transactwallet = new Transactwallet();
                            transactwallet.execute(shop_id, "0", String.valueOf(shop_balance - SPHome.min_service_charge), String.valueOf(SPHome.min_service_charge));
                            //Toast.makeText(_context,"go aahead",Toast.LENGTH_LONG).show();
                        } else {
                            if (lang.contentEquals("Arabic")) {
                                Constants.noInternetDialouge(_context, "العميل لايرغب بالحصول على الخدمة");
                            } else {
                                Constants.noInternetDialouge(_context, "User is not ready to take the service");

                            }
                            // this is for insufficient balance in the user side

                        }

                    } else {
                        Constants.noInternetDialouge(_context, "Undefined");

                    }
                }
                else if(updated_status == 2){
                    wpage = "user_side";
                    Transactwallet transactwallet = new Transactwallet();
                    transactwallet.execute(user_id, String.valueOf(HomeActivity.min_post_charge),String.valueOf(user_balance + HomeActivity.min_post_charge),"0");

                }
            }
            else{
                Constants.noInternetDialouge(_context,server_message);

            }


            //  Toast.makeText(_context,shop_balance+"/"+user_balance,Toast.LENGTH_LONG).show();
        }
    }
        public class ConfirmToReq extends AsyncTask<String, Void, Void> {

            private static final String TAG = "Share Sync";
            String server_message;
            String id, username, email_address, contact_no;
            int server_status;
            private ProgressDialog progressDialog = null;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // onPreExecuteTask();
                /*if (progressDialog == null) {
                    progressDialog = ProgressDialog.show(_context, "Updating", "Please wait...");
                }*/
            }

            @Override
            protected Void doInBackground(String... params) {

                try {
                    String _id = params[0];
                    String _status = params[1];
                    String _otp = params[2];
                    InputStream in = null;
                    int resCode = -1;

                    String link = Constants.ONLINEURL + Constants.EDIT_IND_REQUEST;
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

                    Uri.Builder builder =null;
                    if(_status.contentEquals("1") || _status.contentEquals("2")) {
                        builder = new Uri.Builder()
                                .appendQueryParameter("id", _id)
                                .appendQueryParameter("status", _status)
                                .appendQueryParameter("otp", _otp);
                    }
                    else if(_status.contentEquals("3") || _status.contentEquals("4")){
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
                        JSONObject j_obj = res.getJSONObject("res");
                        server_status = j_obj.optInt("status");
                        if (server_status == 1) {
                            server_message = "Successful";
                        } else {
                            server_message = "Error";
                        }

                    }
                    return null;
                } catch (Exception exception) {
                    server_message = "Error";
                    Log.e(TAG, "SynchMobnum : doInBackground", exception);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void user) {
                super.onPostExecute(user);
                if(server_status==1){
                    if(updated_status==1) {
                        if(lang.contentEquals("Arabic")){
                            holder1.im_reply.setText(R.string.enter_otp_ar);

                        }
                        else{
                            holder1.im_reply.setText("Enter OTP");
                        }
                        holder1.imreject.setVisibility(View.GONE);
                        /*Resources ress = _context.getResources();
                        Drawable drawable1 = ress.getDrawable(R.mipmap.ic_assignment_turned_in_white_24dp);
                        drawable1 = DrawableCompat.wrap(drawable1);
                        DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                        holder1.im_reply.setImageDrawable(drawable1);*/
                    }
                    else if(updated_status==2){
                      //
                        if(lang.contentEquals("Arabic")){
                            holder1.im_reply.setText(R.string.on_going_ar);

                        }
                        else{
                            holder1.im_reply.setText("Ongoing");

                        }
                        holder1.imreject.setVisibility(View.GONE);
                        /*Resources ress = _context.getResources();
                        Drawable drawable1 = ress.getDrawable(R.mipmap.ic_power_input_white_24dp);
                        drawable1 = DrawableCompat.wrap(drawable1);
                        DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                        holder1.im_reply.setImageDrawable(drawable1);*/
                        wpage = "user_side";
                        getWdetails getUWdetails = new getWdetails();
                        getUWdetails.execute(user_id);

                    }
                    else if(updated_status==3){
                        if(lang.contentEquals("Arabic")){
                            holder1.im_reply.setText(R.string.completed_ar);

                        }
                        else{
                            holder1.im_reply.setText("Completed");

                        }
                       // holder1.im_reply.setVisibility(View.GONE);
                        holder1.imreject.setVisibility(View.GONE);

                    }
                }
                else {
                    Constants.noInternetDialouge(_context,server_message);
                }
             //   progressDialog.cancel();
            /*if (callPage.contentEquals("comment")) {

            }*/
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

                }                URL url = new URL(link);
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
                        if(lang.contentEquals("Arabic")){
                            server_message="تم تحديث المحفظة";

                        }
                        else{
                            server_message="Wallet Updated";

                        }
                    }
                    else{
                        if(lang.contentEquals("Arabic")){
                            server_message="لا يمكن تحديث المحفظة";

                        }
                        else{
                            server_message="Wallet can't be Updated";

                        }

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
            if(wallet_status==1){
                if(updated_status==2){
                  wpage ="sp_home";
                }
                else {
                    if (wpage.contentEquals("sp_home")) {
                        wpage = "user_side";
                        Transactwallet transactwallet = new Transactwallet();
                        transactwallet.execute(user_id, "0", String.valueOf(user_balance - HomeActivity.min_post_charge), String.valueOf(HomeActivity.min_post_charge));
                    }
                    //Toast.makeText(PostActivity.this,"Hello",Toast.LENGTH_LONG).show();
                    else if (wpage.contentEquals("user_side")) {
                        if (lang.contentEquals("Arabic")) {
                            Toast.makeText(_context, "تم تحديث المحفظه", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(_context, "User's wallet updated", Toast.LENGTH_LONG).show();

                        }
                        String otp = String.valueOf(Constants.generatePIN());
                        ConfirmToReq creq = new ConfirmToReq();
                        creq.execute(id, "1", otp);
                    }

                }


            }
            else{
                Constants.noInternetDialouge(_context,"Action failed");
            }
            progressDialog.dismiss();

        }

    }


    private class GetBalance extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get Wallet Balance";
        private ProgressDialog progressDialog = null;
        int server_status;
        Double myBalance;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(_context, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(_context, "Loading", "Please wait...");

                }
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

                    link = Constants.ONLINEURL + Constants.USER_BALANCE;
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

                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", _userid);
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
                        serviceListArray = res.getJSONArray("userWallets");

                    if(serviceListArray.length()<=0) {
                        server_status = 0;
                    }
                    else{
                        server_status = 1;
                        for (int i = 0; i < serviceListArray.length(); i++) {
                            JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            myBalance = o_list_obj.getDouble("balance");
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
               Addbalance addbalance = new Addbalance();
               addbalance.execute(credit_id,"5",String.valueOf(myBalance + 5),"0");

            }
        }
    }

    private class Addbalance extends AsyncTask<String, Void, Void> {

        private static final String TAG = "update wallet";
        int wallet_status;
        String server_message;
        ProgressDialog progressDialog=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... params) {

            try {
                String _userid = params[0];
                String _recharge_amount = params[1];
                String _balance_amount = params[2];
                String _debit_amount = params[3];
                InputStream in = null;
                int resCode = -1;

                String link = null;
                    link = Constants.ONLINEURL + Constants.USER_WALLLET_UPDATE;


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

                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", _userid)
                            .appendQueryParameter("debit", _debit_amount)
                            .appendQueryParameter("credit", _recharge_amount)
                            .appendQueryParameter("remarks", "Rejected Refund")
                            .appendQueryParameter("balance", _balance_amount);


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
                        if(lang.contentEquals("Arabic")){
                            server_message="تم تحديث المحفظة";

                        }
                        else{
                            server_message="Wallet Updated";

                        }
                    }
                    else{
                        if(lang.contentEquals("Arabic")){
                            server_message="لا يمكن تحديث المحفظة";

                        }
                        else{
                            server_message="Wallet can't be Updated";

                        }

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
            if(wallet_status==1){
            updatestatus();
            }

        }
    }

    private void updatestatus() {

        final ProgressDialog pd = new ProgressDialog(_context);
        pd.setTitle("Updating Request");
        pd.setMessage("Please wait...");
        pd.show();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", credit_request_id);
            jsonObject.put("status", "8");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIManager().ModifyAPI(Constants.ONLINEURL + Constants.EDIT_IND_REQUEST, "res", jsonObject, _context, new APIManager.APIManagerInterface() {
            @Override
            public void onSuccess(Object resultObj) {
                pd.dismiss();
                if(lang.contentEquals("Arabic")) {
                    Toast.makeText(_context, R.string.rejected_ar, Toast.LENGTH_SHORT).show();
                    rej_holder.imreject.setText(R.string.rejected_ar);
                }
                else{
                    Toast.makeText(_context, "Rejected", Toast.LENGTH_SHORT).show();
                    rej_holder.imreject.setText("Rejected");
                }
                rej_holder.im_reply.setVisibility(View.GONE);
                rej_holder.imreject.setTextColor(Color.parseColor("#d00000"));
                rej_holder.imreject.setEnabled(false);
                Intent intent = new Intent(_context,RejectOption.class);
                intent.putExtra("REQ_ID",credit_request_id);
                _context.startActivity(intent);
                // holder1.im_remove.setVisibility(View.GONE);



            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                Toast.makeText(_context,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });


    }
}
