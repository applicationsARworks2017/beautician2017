package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import beautician.com.sapplication.Activity.CheckPost;
import beautician.com.sapplication.Activity.Login_Activity;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.ServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.SplashScreen;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/21/17.
 */

public class ServiceReqAdapterSP extends BaseAdapter{
    Context _context;
    ArrayList<ServiceRequest> new_list;
    Holder holder,holder1;
    Dialog dialog;
    String user_id;
    private ProgressDialog progressDialog = null;

    String serviceRequestid;
    public ServiceReqAdapterSP(CheckPost checkPost, ArrayList<ServiceRequest> srList) {
        this._context=checkPost;
        this.new_list=srList;

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
            convertView = mInflater.inflate(R.layout.service_list, parent, false);
            user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.Name_service=(TextView)convertView.findViewById(R.id.name_service);
            holder.tv_expected_date=(TextView)convertView.findViewById(R.id.tv_expected_date);
            holder.actualtime=(TextView)convertView.findViewById(R.id.actualtime);
            holder.remarks=(TextView)convertView.findViewById(R.id.servicedetails);
            holder.reply=(ImageView) convertView.findViewById(R.id.im_reply);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        if(_pos.getStatus().contentEquals("false") || _pos.getStatus().contentEquals("0")){
            holder.reply.setVisibility(View.VISIBLE);
        }
        else{
            holder.reply.setVisibility(View.GONE);
        }
        holder.Name_service.setTag(position);
        holder.tv_expected_date.setTag(position);
        holder.remarks.setTag(position);
        holder.reply.setTag(position);
        holder.actualtime.setTag(position);

        holder.Name_service.setText(_pos.getName()+" has posted for " + _pos.getSub_category());
        holder.remarks.setText(_pos.getRemarks());
        holder.actualtime.setText(_pos.getCreated());
        if(_pos.getExpected_date().contentEquals("null")){
            holder.tv_expected_date.setText(("No Expected defined"));

        }
        else{
            holder.tv_expected_date.setText(("Expected Date: "+_pos.getExpected_date()));
        }

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // holder1=(Holder)v.getTag();
                dialog=new Dialog((_context));
                dialog.setContentView(R.layout.dialog_reply);
                ImageView im_close=(ImageView)dialog.findViewById(R.id.im_close);
                final EditText et_comments=(EditText) dialog.findViewById(R.id.et_comments);
                im_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button ok=(Button)dialog.findViewById(R.id.ok);
                Button cancel=(Button)dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(progressDialog == null) {
                            progressDialog = ProgressDialog.show(_context, "Loading", "Please wait...");
                        }
                        String details=et_comments.getText().toString().trim();
                        serviceRequestid=_pos.getId();
                        if(details.length()<=0){
                            Toast.makeText(_context,"Please enter in detail",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // Toast.makeText(_context,"Done",Toast.LENGTH_SHORT).show();
                            if(CheckInternet.getNetworkConnectivityStatus(_context)){
                                SetProposal setpropsal=new SetProposal();
                                setpropsal.execute(_pos.getId(),user_id,"1",details);
                            }
                            else{
                                Constants.noInternetDialouge(_context,"No Internet");
                            }

                        }
                    }
                });
                dialog.show();

            }
        });


        return convertView;
    }
    private class SetProposal extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Proposal add Sync";
        String server_message;
        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _req_id = params[0];
                String _shop_id = params[1];
                String _status = params[2];
                String _remarks = params[3];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.ADD_PROPOSAL ;
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
                        .appendQueryParameter("service_request_id", _req_id)
                        .appendQueryParameter("shop_id", _shop_id)
                        .appendQueryParameter("status", _status)
                        .appendQueryParameter("remarks", _remarks);

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

                /**
                 * {
                 {
                 "res": {
                 "message": "The service purposal has been saved.",
                 "status": 1
                 }
                 }
                 }
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1 ) {
                        server_message="Proposal Sobmitted";
                    }
                    else if(server_status==2) {
                        server_message = "Already submitted";
                    }
                    else {
                        server_message="Failed ";
                    }

                }
                return null;
            } catch (Exception exception) {
                server_message = "Connectivity Issue";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(server_status==1) {
                dialog.dismiss();
                UpdateService updateService=new UpdateService();
                updateService.execute(serviceRequestid,"1");
              //  Toast.makeText(_context,server_message,Toast.LENGTH_SHORT).show();

            }
            else if(server_status==2){
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Already Proposal submitted.");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                        //finish();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                progressDialog.dismiss();
            }
            else{
                Toast.makeText(_context,server_message,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        }
    }
    private class UpdateService extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Update  Request Sync";
        String server_message;
        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _req_id = params[0];
                String _status = params[1];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.UPDATE_SERVICE_REQUEST ;
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
                        .appendQueryParameter("id", _req_id)
                        .appendQueryParameter("status", _status);

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

                /**
                 * {
                 {
                 "res": {
                 "message": "The service purposal has been saved.",
                 "status": 1
                 }
                 }
                 }
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1 ) {
                        server_message="Proposal Sobmitted";
                    }
                    else  {
                        server_message = "Error";
                    }

                }
                return null;
            } catch (Exception exception) {
                server_message = "Connectivity Issue";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            Toast.makeText(_context, server_message, Toast.LENGTH_SHORT).show();
        }
    }
}
