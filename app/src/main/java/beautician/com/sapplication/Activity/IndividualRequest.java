package beautician.com.sapplication.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import beautician.com.sapplication.Pojo.IndServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class IndividualRequest extends AppCompatActivity {
    TextView shopName;
    EditText et_details;
    Spinner sp_num;
    Button post;
    String user_id,shop_id,shop_name,exp_date;
    ImageView btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_individual_request);
        user_id = IndividualRequest.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shop_id = extras.getString("SHOP_ID");
            shop_name = extras.getString("SHOP_NAME");
            // and get whatever type user account id is
        }
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        shopName=(TextView)findViewById(R.id.postHeading);
        shopName.setText("You are Interested for :"+ shop_name);
        et_details=(EditText)findViewById(R.id.et_contentheading);
        sp_num=(Spinner)findViewById(R.id.adult_spin);
        post=(Button)findViewById(R.id.submit_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String postDetails=et_details.getText().toString().trim();
                    String numof=sp_num.getSelectedItem().toString();
                    exp_date = txtDate.getText().toString().trim() + " " + txtTime.getText().toString().trim();

                if(txtDate.getText().toString().trim().length()<=0){
                    Toast.makeText(IndividualRequest.this,"Please give Expected Date",Toast.LENGTH_LONG).show();
                }
                else {
                    if (CheckInternet.getNetworkConnectivityStatus(IndividualRequest.this)) {
                        Postservice postservice = new Postservice();
                        postservice.execute(user_id, numof, postDetails, shop_id,exp_date);
                    } else {
                        Constants.noInternetDialouge(IndividualRequest.this, "No Internet");

                    }
                }


            }
        });
        btnDatePicker=(ImageView)findViewById(R.id.btn_date);
        btnTimePicker=(ImageView)findViewById(R.id.btn_time);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    private class Postservice extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        String server_message;
        String id,name,email,mobile,photo,created_dt,modified_dt,usertype;
        private ProgressDialog progressDialog = null;


        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(IndividualRequest.this, "Posting", "Please wait...");
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
                Intent intent = new Intent(IndividualRequest.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            Toast.makeText(IndividualRequest.this,server_message,Toast.LENGTH_SHORT).show();
        }
    }
}
