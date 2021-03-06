package beautician.com.sapplication.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

import static beautician.com.sapplication.Utils.Constants.hasPermissions;

public class PostActivity extends AppCompatActivity {
    TextView tv_services,postHeading,adultt;
    Spinner adult;
    String user_id,exp_date,lang,dateto_send,curr_date;
    EditText et_contentheading;
    Button submit_post;
    ImageView btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DatePickerDialog datePickerDialog;
    Double balance=0.0;
    private ProgressDialog progressDialog = null;
    private static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppUserTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        user_id = PostActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        if(lang.contentEquals("Arabic")){
            setTitle("تقديم العرض ");
        }
        else{
            setTitle("Post for service");
        }

        postHeading=(TextView)findViewById(R.id.postHeading);
        adultt=(TextView)findViewById(R.id.adult);
        tv_services=(TextView)findViewById(R.id.tv_services);
        adult=(Spinner)findViewById(R.id.adult_spin);
        et_contentheading=(EditText)findViewById(R.id.et_contentheading);
        submit_post=(Button)findViewById(R.id.submit_post);
        btnDatePicker=(ImageView) findViewById(R.id.btn_date);
        btnTimePicker=(ImageView) findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        curr_date = dateFormatter.format(today);
        dateto_send=curr_date.substring(0, Math.min(curr_date.length(), 10));

        if(lang.contentEquals("Arabic")){
            postHeading.setText(":انت مهتم ب");
            adultt.setText("عدد الاشخاص");
            et_contentheading.setHint("أضف التفاصيل هنا");
            tv_services.setText(RequestSubcategories.subcatogoryarabic);
            submit_post.setText("نشر");

        }
        else{
            postHeading.setText("You are Interested for:");
            adultt.setText("No of People");
            et_contentheading.setHint("Add Detail here");
            tv_services.setText(RequestSubcategories.subcateryName);
            submit_post.setText("Post");

        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // here it is checking whether the permission is granted previously or not
            if (!hasPermissions(PostActivity.this, PERMISSIONS)) {
                //Permission is granted
                ActivityCompat.requestPermissions(PostActivity.this, PERMISSIONS, 1);

            }
        }

        submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(PostActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_DENIED){
                    if(lang.contentEquals("Arabic")){
                        alert("الرجاء السماح بتحديد موقعك");

                    }
                    else {

                        alert("Please Allow Beautician to Access Location!");
                    }

                }
                if(ContextCompat.checkSelfPermission(PostActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_DENIED){
                    if(lang.contentEquals("Arabic")){
                        alert("الرجاء السماح بتحديد موقعك");
                    }
                    else {

                        alert("Please Allow Beautician to Access Location!");
                    }
                }



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
                    Toast.makeText(PostActivity.this,nodate,Toast.LENGTH_LONG).show();
                }
                else if(txtTime.getText().toString().trim().length()<=0){
                    Toast.makeText(PostActivity.this,notime,Toast.LENGTH_LONG).show();

                }
                else if(et_contentheading.getText().toString().trim().length()<=0){
                    Toast.makeText(PostActivity.this,nocontent,Toast.LENGTH_LONG).show();

                }
                else {

                    if (lang.contentEquals("Arabic")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                        builder.setTitle("");
                        builder.setMessage("سيتم خصم 5  ريال من المحفظة لهذا المنشور");
                        builder.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();
                                if (txtDate.getText().toString().trim().length() <= 0) {
                                    Toast.makeText(PostActivity.this, "يرجى إعطاء التاريخ المتوقع ", Toast.LENGTH_LONG).show();
                                } else {
                                    if(HomeActivity.latitude.isEmpty()){
                                        Toast.makeText(PostActivity.this, "لم نستطع تحديد موقعك. ربما تحصل على نتائج خاطئة", Toast.LENGTH_LONG).show();
                                    }
                                    if (CheckInternet.getNetworkConnectivityStatus(PostActivity.this)) {
                                        getWdetails getWdetails = new getWdetails();
                                        getWdetails.execute(user_id);
                                    } else {
                                        Constants.noInternetDialouge(PostActivity.this, "لا انترنت");

                                    }
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
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                        builder.setTitle("");
                        builder.setMessage("Your wallet will be deducted with SAR " + HomeActivity.min_post_charge + " for this post");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();
                                if (txtDate.getText().toString().trim().length() <= 0) {
                                    Toast.makeText(PostActivity.this, "Please give Expected Date", Toast.LENGTH_LONG).show();
                                } else {
                                    if(HomeActivity.latitude.isEmpty()){
                                        Toast.makeText(PostActivity.this, "Location not found for your device. You may get unwanted results.", Toast.LENGTH_LONG).show();
                                    }
                                    if (CheckInternet.getNetworkConnectivityStatus(PostActivity.this)) {
                                        getWdetails getWdetails = new getWdetails();
                                        getWdetails.execute(user_id);
                                    } else {
                                        Constants.noInternetDialouge(PostActivity.this, "No Internet");

                                    }
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

    private void alert(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("");
        builder.setMessage(s);
        if(lang.contentEquals("Arabic")) {
            builder.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialog.dismiss();

                }
            });
            builder.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        else{
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialog.dismiss();


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void selecttime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(PostActivity.this,
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


        datePickerDialog = new DatePickerDialog(PostActivity.this,
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


    /*
    *
    * "user_id:9
category_id:1
sub_category_id:1
no_of_user:5
remarks:jhgjhghjgjg"
    * */
    private class Postservice extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        String server_message;

        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(PostActivity.this, "نشر", "أرجو الإنتظار...");

                }else{
                    progressDialog = ProgressDialog.show(PostActivity.this, "Posting", "Please wait...");

                }
            }
            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _user_id = params[0];
                String _cat_id = params[1];
                String _scat_id = params[2];
                String _no_of_user = params[3];
                String _remarks = params[4];
                String _exp_date = params[5];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.SERVICE_REQUEST ;
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
                        .appendQueryParameter("category_id", _cat_id)
                        .appendQueryParameter("sub_category_id", _scat_id)
                        .appendQueryParameter("no_of_user", _no_of_user)
                        .appendQueryParameter("remarks", _remarks)
                        .appendQueryParameter("expected_date", _exp_date)
                        .appendQueryParameter("latitude", HomeActivity.latitude)
                        .appendQueryParameter("longitude", HomeActivity.longitude)
                        .appendQueryParameter("current_date", params[6]);

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
                        server_message = "Posted";
                    }
                    else{
                        server_message = "Posting failed";

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
              //  Toast.makeText(PostActivity.this,"Hello",Toast.LENGTH_LONG).show();

                Transactwallet transactwallet=new Transactwallet();
                transactwallet.execute(user_id,"0",String.valueOf(balance-HomeActivity.min_post_charge),String.valueOf(HomeActivity.min_post_charge));
            }
        }
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
                    progressDialog = ProgressDialog.show(PostActivity.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(PostActivity.this, "Loading", "Please wait...");

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
                server_status = 0;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if(server_status==1){
                if(balance>=HomeActivity.first_post_charge){
                    String postDetails = et_contentheading.getText().toString().trim();
                    String numof = adult.getSelectedItem().toString();
                    exp_date = txtDate.getText().toString().trim() + " " + txtTime.getText().toString().trim();
                    if (CheckInternet.getNetworkConnectivityStatus(PostActivity.this)) {
                        Postservice postservice = new Postservice();
                        postservice.execute(user_id, CategoriesRequest.catid, RequestSubcategories.SubcateryId, numof, postDetails, exp_date,curr_date);

                    } else {
                        Constants.noInternetDialouge(PostActivity.this, "No Internet");

                    }
                }
                else {
                    if(lang.contentEquals("Arabic")){
                        Constants.noInternetDialouge(PostActivity.this,"مطلوب 10  ريالات على الأقل في محفظتك لنشر الخدمة");

                    }
                    else{
                        Constants.noInternetDialouge(PostActivity.this,"Atleast SAR "+HomeActivity.first_post_charge+" is required in your wallet for posting a service");

                    }

                }

            }
            else{
                if(lang.contentEquals("Arabic")){
                    Constants.noInternetDialouge(PostActivity.this,"مطلوب 10  ريالات على الأقل في محفظتك لنشر الخدمة");

                }
                else{
                    Constants.noInternetDialouge(PostActivity.this,"Atleast SAR "+HomeActivity.first_post_charge+" is required in your wallet for posting a service");

                }
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
        @Override
        protected void onPreExecute() {
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

                String link = Constants.ONLINEURL+Constants.USER_WALLLET_UPDATE;
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
                        .appendQueryParameter("remarks", "Public Post")
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
            progressDialog.dismiss();
            if(wallet_status==1){
                if(lang.contentEquals("Arabic")){
                    Toast.makeText(PostActivity.this,"سيتم خصم 5 ريال من المحفظة لهذا المنشور ",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(PostActivity.this,"Request Posted and Wallet debited with SAR "+HomeActivity.min_post_charge,Toast.LENGTH_LONG).show();

                }
                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }
    }
}
