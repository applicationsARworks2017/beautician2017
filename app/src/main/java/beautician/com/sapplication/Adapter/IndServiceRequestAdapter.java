package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

import beautician.com.sapplication.Activity.CheckIndividualPost;
import beautician.com.sapplication.Pojo.IndServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/26/17.
 */

public class IndServiceRequestAdapter extends BaseAdapter {
    Context _context;
    ArrayList<IndServiceRequest> new_list;
    Holder holder,holder1;
    String user_id;
    Double shop_balance;
    String id;
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
        final IndServiceRequest _pos = new_list.get(position);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.serviceind_list, parent, false);
            user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.Name_service = (TextView) convertView.findViewById(R.id.name_service);
            holder.remarks = (TextView) convertView.findViewById(R.id.servicedetails);
            holder.im_reply = (ImageView) convertView.findViewById(R.id.im_reply);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.Name_service.setTag(position);
        holder.remarks.setTag(position);
        holder.im_reply.setTag(holder);
        holder.Name_service.setText(_pos.getPersonName() + " has requseted you for the service");
        holder.remarks.setText(_pos.getRemarks());
        final String status = _pos.getStatus();
         id = _pos.getId();
        if (status.contentEquals("0")) {  // got the individual request and want to go ahead
            Resources ress = _context.getResources();
            Drawable drawable1 = ress.getDrawable(R.mipmap.ic_assignment_turned_in_white_24dp);
            drawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.black));
            holder.im_reply.setImageDrawable(drawable1);
        }
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
                        //finish()   ;
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        //dialog.dismiss();
                        //finish();
                        if (CheckInternet.getNetworkConnectivityStatus(_context)) {
                            getWdetails getUWdetails = new getWdetails();
                            getUWdetails.execute(user_id);
                        } else {
                            Constants.noInternetDialouge(_context, "No Internet");
                        }

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return convertView;
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
                link = Constants.ONLINEURL + Constants.SHOP_BALANCE;
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

                Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("shop_id", _userid);

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
                    serviceListArray = res.getJSONArray("wallets");
                    if (serviceListArray.length() <= 0) {
                        server_status = 0;
                    } else {
                        server_status = 1;
                        for (int i = 0; i < serviceListArray.length(); i++) {
                            JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            shop_balance = o_list_obj.getDouble("balance");
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

                if (shop_balance > 5) {
                    String status = "1";
                    String otp = String.valueOf(Constants.generatePIN());
                    ConfirmToReq creq = new ConfirmToReq();
                    creq.execute(id, status, otp);
                } else {
                    Constants.noInternetDialouge(_context, "You don't have sufficient amount in wallet");
                }
            }


            //  Toast.makeText(_context,shop_balance+"/"+user_balance,Toast.LENGTH_LONG).show();
        }

        private class ConfirmToReq extends AsyncTask<String, Void, Void> {

            private static final String TAG = "Share Sync";
            String server_message;
            String id, username, email_address, contact_no;
            int server_status;
            private ProgressDialog progressDialog = null;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // onPreExecuteTask();
                if (progressDialog == null) {
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

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", _id)
                            .appendQueryParameter("status", _status)
                            .appendQueryParameter("otp", _otp);

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
                    server_message = "N Error";
                    Log.e(TAG, "SynchMobnum : doInBackground", exception);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void user) {
                super.onPostExecute(user);
                if(server_status==1){
                    Transactwallet transactwallet = new Transactwallet();
                    transactwallet.execute(user_id, "0", String.valueOf(shop_balance - 5), "5");
                }
                else {

                }
                progressDialog.cancel();
            /*if (callPage.contentEquals("comment")) {

            }*/
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
                link = Constants.ONLINEURL + Constants.SHOP_WALLLET_UPDATE;
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
                Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("shop_id", _userid)
                            .appendQueryParameter("debit", _debit_amount)
                            .appendQueryParameter("credit", _recharge_amount)
                            .appendQueryParameter("remarks", "Service started")
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
            if(wallet_status==1){
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_assignment_turned_in_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_reply.setImageDrawable(drawable1);
            }
            else{
                Constants.noInternetDialouge(_context,"Action failed");
            }
            progressDialog.dismiss();

        }
    }
}
