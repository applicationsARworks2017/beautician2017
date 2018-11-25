package beautician.com.sapplication.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class IndividualRequest extends AppCompatActivity {
    TextView shopName,adult;
    EditText et_details;
    Spinner sp_num;
    Button post;
    Double balance;
    String user_id,shop_id,shop_name,exp_date;
    ImageView btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DatePickerDialog datePickerDialog;
    Toolbar toolreq;
    LinearLayout reqback;
    String lang,details;
    TextView pageheading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_individual_request);
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        user_id = IndividualRequest.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shop_id = extras.getString("SHOP_ID");
            shop_name = extras.getString("SHOP_NAME");
            details = extras.getString("DETAILS");
            // and get whatever type user account id is
        }
        toolreq=(Toolbar)findViewById(R.id.toolreq);
        reqback=(LinearLayout)toolreq.findViewById(R.id.reqback);
        pageheading=(TextView) toolreq.findViewById(R.id.pageheading);
        reqback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        shopName=(TextView)findViewById(R.id.postHeading);
        adult=(TextView)findViewById(R.id.adult);
        shopName.setText("You are Interested for :"+ shop_name);
        et_details=(EditText)findViewById(R.id.et_contentheading);
        et_details.setText(details);
        sp_num=(Spinner)findViewById(R.id.adult_spin);
        post=(Button)findViewById(R.id.submit_post);
        if(lang.contentEquals("Arabic")){
            shopName.setText("انت مهتم ب:");
            adult.setText("عدد الاشخاص");
            et_details.setHint("أضف التفاصيل هنا");
            post.setText("ارسال");
            setTitle("طلب للحصول على الخدمة");


        }
        else{
            shopName.setText("You are Interested for :");
            adult.setText("No of People");
            et_details.setHint("Add Detail here");
            post.setText("Post");
            setTitle("Request For Service");
        }
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nodate,notime,nocontent;

                if(lang.contentEquals("Arabic")){
                    nodate="حدد تاريخ" ;
                    notime= "اختر الوقت";
                    nocontent= "أدخل التفاصيل";

                }
                else{
                    nodate= "Select Date";
                    notime= "Select Time";
                    nocontent= "Enter Details";
                }

                if(txtDate.getText().toString().trim().length()<=0){
                    Toast.makeText(IndividualRequest.this,nodate,Toast.LENGTH_LONG).show();
                }
                else if(txtTime.getText().toString().trim().length()<=0){
                    Toast.makeText(IndividualRequest.this,notime,Toast.LENGTH_LONG).show();

                }
                else if(et_details.getText().toString().trim().length()<=0){
                    Toast.makeText(IndividualRequest.this,nocontent,Toast.LENGTH_LONG).show();

                }
                else {
                    if(lang.contentEquals("Arabic")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(IndividualRequest.this);
                        builder.setTitle("");
                        builder.setMessage("سيتم خصم 5 يال من المحفظة لهذا الطلب ");
                        builder.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (CheckInternet.getNetworkConnectivityStatus(IndividualRequest.this)) {
                                    getWdetails getWdetails=new getWdetails();
                                    getWdetails.execute(user_id);

                                    /*Postservice postservice = new Postservice();
                                    postservice.execute(user_id, numof, postDetails, shop_id,exp_date);*/
                                } else {
                                    Constants.noInternetDialouge(IndividualRequest.this, "لا انترنت");

                                }
                            }
                        });
                        builder.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(IndividualRequest.this);
                        builder.setTitle("");
                        builder.setMessage("Your wallet will be deducted with SAR "+HomeActivity.min_post_charge+" for this request");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (CheckInternet.getNetworkConnectivityStatus(IndividualRequest.this)) {
                                    getWdetails getWdetails=new getWdetails();
                                    getWdetails.execute(user_id);

                                    /*Postservice postservice = new Postservice();
                                    postservice.execute(user_id, numof, postDetails, shop_id,exp_date);*/
                                } else {
                                    Constants.noInternetDialouge(IndividualRequest.this, "No Internet");

                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }


                }


            }
        });
        btnDatePicker=(ImageView)findViewById(R.id.btn_date);
        btnTimePicker=(ImageView)findViewById(R.id.btn_time);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectdate();
            }


        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selecttime();

            }
        });
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectdate();
            }
        });
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecttime();
            }
        });
    }

    private void selecttime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(IndividualRequest.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    private void selectdate() {
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        datePickerDialog = new DatePickerDialog(IndividualRequest.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
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
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(IndividualRequest.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(IndividualRequest.this, "Loading", "Please wait...");

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

                String link = Constants.ONLINEURL+Constants.USER_BALANCE;
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
                    JSONArray serviceListArray = res.getJSONArray("userWallets");
                    if(serviceListArray.length()<=0) {
                        server_status = 0;
                    }
                    else{
                        server_status = 1;
                        for (int i = 0; i < serviceListArray.length(); i++) {
                            JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            balance = o_list_obj.getDouble("balance");
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
            if(server_status==1){
                if(balance>=HomeActivity.first_post_charge){
                    final String postDetails=et_details.getText().toString().trim();
                    final String numof=sp_num.getSelectedItem().toString();
                    exp_date = txtDate.getText().toString().trim() + " " + txtTime.getText().toString().trim();
                    if (CheckInternet.getNetworkConnectivityStatus(IndividualRequest.this)) {
                        Postservice postservice = new Postservice();
                        postservice.execute(user_id, numof, postDetails, shop_id,exp_date);
                    } else {
                        Constants.noInternetDialouge(IndividualRequest.this, "No Internet");

                    }
                }
                else{
                    if(lang.contentEquals("Arabic")){
                        Constants.noInternetDialouge(IndividualRequest.this,"مطلوب 10  ريالات على الأقل في محفظتك لنشر الخدمة");

                    }
                    else{
                        Constants.noInternetDialouge(IndividualRequest.this,"Atleast SAR "+HomeActivity.first_post_charge+" is required in your wallet for posting a service");

                    }
                }

            }
            else{
                if(lang.contentEquals("Arabic")){
                    Constants.noInternetDialouge(IndividualRequest.this,server_message);

                }
                else{
                    Constants.noInternetDialouge(IndividualRequest.this,server_message);

                }            }

        }
    }


    /*
    * Posting service
    * */


    private class Postservice extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        String server_message;
        String id,name,email,mobile,photo,created_dt,modified_dt,usertype;
        private ProgressDialog progressDialog = null;


        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(IndividualRequest.this, "نشر", "أرجو الإنتظار...");

                }else{
                    progressDialog = ProgressDialog.show(IndividualRequest.this, "Posting", "Please wait...");

                }
            }
            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _user_id = params[0];
                String _no_of_user = params[1];
                String _remarks = params[2];
                String _shop_id = params[3];
                String _exp_date = params[4];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.INDICISUAL_REQUEST ;
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
                        .appendQueryParameter("user_id", _user_id)
                        .appendQueryParameter("no_of_user", _no_of_user)
                        .appendQueryParameter("remarks", _remarks)
                        .appendQueryParameter("shop_id", _shop_id)
                        .appendQueryParameter("status", "0")
                        .appendQueryParameter("expected_date", exp_date);

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
                 "message": "The service request has been saved.",
                 "status": 1
                 }
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1) {
                        if(lang.contentEquals("Arabic")){
                            server_message = "نشر";
                        }
                        else{
                            server_message = "Posted";
                        }

                    }
                    else{
                        if(lang.contentEquals("Arabic")){
                            server_message = "فشل النشر";

                        }
                        else{
                            server_message = "Posting failed";

                        }

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
            if (server_status == 1) {
                Transactwallet transactwallet=new Transactwallet();
                transactwallet.execute(user_id,"0",String.valueOf(balance-HomeActivity.min_post_charge),String.valueOf(HomeActivity.min_post_charge));

            }
            Toast.makeText(IndividualRequest.this,server_message,Toast.LENGTH_SHORT).show();
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(IndividualRequest.this, "Deducting", "Please wait...");
            }
            super.onPreExecute();
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

                String link = Constants.ONLINEURL + Constants.USER_WALLLET_UPDATE;
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
                        .appendQueryParameter("user_id", _userid)
                        .appendQueryParameter("debit", _debit_amount)
                        .appendQueryParameter("credit", _recharge_amount)
                        .appendQueryParameter("remarks", "Individual Request")
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
                 "status": 1,
                 "message": "Data inserted successfully"
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj = res.getJSONObject("res");
                    wallet_status = j_obj.optInt("status");
                    if (wallet_status == 1) {
                        if(lang.contentEquals("Arabic")){
                            server_message = "تم تحديث المحفظة";

                        }
                        else{
                            server_message = "Wallet Updated";

                        }
                    } else {
                        if(lang.contentEquals("Arabic")){
                            server_message = "لا يمكن تحديث المحفظة";

                        }
                        else{
                            server_message = "Wallet can't be Updated";

                        }

                    }

                }

                return null;
            } catch (Exception exception) {
                server_message = "Wallet error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if (wallet_status == 1) {
                Intent intent = new Intent(IndividualRequest.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

            else{
                Toast.makeText(IndividualRequest.this,server_message,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
