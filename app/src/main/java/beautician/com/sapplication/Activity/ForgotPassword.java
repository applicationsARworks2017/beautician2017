package beautician.com.sapplication.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import beautician.com.sapplication.Pojo.SpList;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class ForgotPassword extends AppCompatActivity {
    Button resetpassword;
    EditText email_id,phone_nmbr;
    String page,lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            page = extras.getString("PAGE");

        }
        if(page.contentEquals("sp")){
            super.setTheme(R.style.AppTheme);
        }
        else{
            super.setTheme(R.style.AppUserTheme);

        }
        setContentView(R.layout.activity_forgot_password);
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        email_id=(EditText)findViewById(R.id.email_id);
        phone_nmbr=(EditText)findViewById(R.id.phn_nmbr);
        resetpassword=(Button)findViewById(R.id.resetpassword);
        if(lang.contentEquals("Arabic")){
           email_id.setHint("البريد الإلكتروني");
           phone_nmbr.setHint("رقم الجوال");
        }
        else{
            email_id.setHint("Email id");
            phone_nmbr.setHint("Phone Number");
        }
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.getNetworkConnectivityStatus(ForgotPassword.this)){
                    if (page.contentEquals("sp")) {
                        String email = email_id.getText().toString().trim();
                        String phone = phone_nmbr.getText().toString().trim();
                        new Resetpassword().execute(email, phone);
                    } else {
                        String email = email_id.getText().toString().trim();
                        String phone = phone_nmbr.getText().toString().trim();
                        new Resetpassword().execute(email, phone);

                    }
                }
                else{
                    if(lang.contentEquals("Arabic")){
                        Toast.makeText(ForgotPassword.this,"تحقق الإنترنت",Toast.LENGTH_SHORT);

                    }
                    else{
                        Toast.makeText(ForgotPassword.this,"Check Internet",Toast.LENGTH_SHORT);

                    }
                }

            }
        });
    }

    private class Resetpassword extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        String server_message;
        int server_status;
        String id,shop_name,address,latitudelongitude,photo1,photo2,photo3,email,mobile,created_dt,modified_dt;
        ProgressDialog progressDialog=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                if (lang .contentEquals("Arabic")) {
                    progressDialog = ProgressDialog.show(ForgotPassword.this, "جار التحميل", "يرجى الإنتظار");

                } else {
                    progressDialog = ProgressDialog.show(ForgotPassword.this, "Loading", "Please wait...");
                }
            }

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String email = params[0];
                String mobile = params[1];
                InputStream in = null;
                int resCode = -1;
                String link=null;
                if(page.contentEquals("sp")) {
                     link = Constants.ONLINEURL + Constants.SHOP_FORGOTPASS;
                }
                else{
                    link = Constants.ONLINEURL + Constants.USER_FORGOTPASS;

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

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("mobile", mobile);

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
                 * "shops": {
                 "id": 7,
                 "shopname": "an mart",
                 "address": "Karnataka",
                 "latitudelongitude": "13.042775499999998,77.6344609",
                 "photo1": "file15101537521523341327.jpg",
                 "photo2": "file1510153753279822175.jpg",
                 "photo3": "file1510153754144948710.jpg",
                 "email": "mgt@gmail.com",
                 "mobile": "9999999999",
                 "password": "amu",
                 "created": "2017-11-08T15:09:14+00:00",
                 "modified": "2017-11-08T15:09:14+00:00",
                 "message": "user available.",
                 "status": 1
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject res1=res.getJSONObject("res");
                   server_status=res1.getInt("status");
                   if(server_status==1){
                       if(lang.contentEquals("Arabic")){
                           server_message = "البريد المرسلة";

                       }
                       else{
                           server_message = "Mail Sent";
                       }
                   }
                   else{
                       if(lang.contentEquals("Arabic")){
                           server_message = "لم يتم العثور على بيانات";

                       }
                       else{
                           server_message = "Data Not Found";
                       }
                   }
                }
                return null;

            } catch (Exception exception) {
                if(lang.contentEquals("Arabic")){
                    server_message = "خطأ في الخادم";

                }
                else{
                    server_message = "Server Error";
                }
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if (server_status == 1) {
                Toast.makeText(ForgotPassword.this,server_message,Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ForgotPassword.this,server_message,Toast.LENGTH_SHORT).show();

            }
        }
    }
}
