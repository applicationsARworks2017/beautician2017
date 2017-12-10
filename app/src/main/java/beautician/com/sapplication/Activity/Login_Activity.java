package beautician.com.sapplication.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import beautician.com.sapplication.Pojo.SpList;
import beautician.com.sapplication.Pojo.User;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

import static beautician.com.sapplication.Utils.Constants.hasPermissions;

public class Login_Activity extends AppCompatActivity {
    TextView tv_signup,tv_forgotpassword;
    RadioGroup radio_user_type;
    RadioGroup user_type;
    Button lin_signin;
    EditText et_phone,et_password;
    ProgressBar login_loader;
    RadioButton radioButton;
    ArrayList<User> userlist;
    ArrayList<SpList> splist;
    String fcm_id;
    RelativeLayout login_rel;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_phone=(EditText)findViewById(R.id.et_phn);
        et_password=(EditText)findViewById(R.id.et_password);
        tv_signup=(TextView)findViewById(R.id.tv_signup);
        tv_forgotpassword=(TextView)findViewById(R.id.tv_forgotpassword);
        radio_user_type=(RadioGroup)findViewById(R.id.radio_user_type);
        lin_signin=(Button) findViewById(R.id.lin_signin);
        login_rel=(RelativeLayout)findViewById(R.id.login_rel);
        userlist=new ArrayList<>();
        splist=new ArrayList<>();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // here it is checking whether the permission is granted previously or not
            if (!hasPermissions(this, PERMISSIONS)) {
                //Permission is granted
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

            }
        }
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_Activity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        lin_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checklogin();
                // get selected radio button from radioGroup
                int selectedId = radio_user_type.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                if(radioButton.getText().toString().trim().contains("Costumer")){
                    Checklogin("user");
                }
                else{
                    Checklogin("sp");

                }
            }
        });
        /*tv_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_Activity.this,SPHome.class);
                startActivity(intent);
            }
        });*/
    }

    private void Checklogin(String type) {
        if(CheckInternet.getNetworkConnectivityStatus(Login_Activity.this)) {
            if (type.contentEquals("user")) {
                String login_user_phn_num = et_phone.getText().toString().trim();
                String login_user_phn_pass = et_password.getText().toString().trim();
                new LoginUserAsyntask().execute(login_user_phn_num, login_user_phn_pass);
            } else {
                String login_sp_phn_num = et_phone.getText().toString().trim();
                String login_sp_phn_pass = et_password.getText().toString().trim();
                new LoginSPAsyntask().execute(login_sp_phn_num, login_sp_phn_pass);

            }
        }
        else{
            Constants.noInternetDialouge(Login_Activity.this,"No Intrnet Connection");
        }
    }

    private class LoginUserAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        String server_message;
        String id,name,email,mobile,photo,created_dt,modified_dt,usertype;
        private ProgressDialog progressDialog = null;

        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Login_Activity.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String phone_number = params[0];
                String password = params[1];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.LOGIN_USER ;
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
                        .appendQueryParameter("mobile", phone_number)
                        .appendQueryParameter("password", password);

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
                 "users": {
                 "id": 20,
                 "name": "amresh",
                 "email": "ama@gmail.com",
                 "mobile": "9090403050",
                 "photo": "file15101535291311553082.jpg",
                 "created": "2017-11-08T15:05:30+00:00",
                 "modified": "2017-11-08T15:05:30+00:00",
                 "usertype": "user",
                 "message": "user available.",
                 "status": 1
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("users");
                        id=j_obj.getString("id");
                        name=j_obj.getString("name");
                        email=j_obj.getString("email");
                        mobile=j_obj.getString("mobile");
                        photo=j_obj.getString("photo");
                        created_dt=j_obj.getString("created");
                        modified_dt=j_obj.getString("modified");
                        usertype=j_obj.getString("usertype");
                    server_status = j_obj.optInt("status");

                    User user_list=new User(id,name,email,mobile,photo,created_dt,modified_dt,usertype);
                    userlist.add(user_list);
                    if (server_status == 1) {
                        server_message = j_obj.optString("message");
                    } else {
                        server_message = "Invalid Credentials";
                    }
                }
                return null;
            } catch (Exception exception) {
                server_message = "Invalid Credentials";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if (server_status == 1) {
                SharedPreferences sharedPreferences = Login_Activity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USER_ID, id);
                editor.putString(Constants.USER_TYPE, "custumer");
                editor.commit();
                Intent i=new Intent(Login_Activity.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
            else{
                showSnackBar(server_message);

            }
            progressDialog.dismiss();
        }
    }

    private class LoginSPAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        String server_message;
        int server_status;
        String id,shop_name,address,latitudelongitude,photo1,photo2,photo3,email,mobile,created_dt,modified_dt;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String phone_number = params[0];
                String password = params[1];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.LOGIN_SHOP;
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
                        .appendQueryParameter("mobile", phone_number)
                        .appendQueryParameter("password", password);

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
                        JSONObject j_obj=res.getJSONObject("shops");
                        id=j_obj.getString("id");
                        shop_name=j_obj.getString("shopname");
                        address=j_obj.getString("address");
                        latitudelongitude=j_obj.getString("latitudelongitude");
                        photo1=j_obj.getString("photo1");
                        photo2=j_obj.getString("photo2");
                        photo3=j_obj.getString("photo3");
                        email=j_obj.getString("email");
                        mobile=j_obj.getString("mobile");
                        created_dt=j_obj.getString("created");
                        modified_dt=j_obj.getString("modified");
                    server_status = j_obj.optInt("status");
                    SpList sp_list=new SpList(id,shop_name,address,latitudelongitude,photo1,photo2,photo3,
                            email,mobile,created_dt,modified_dt);
                    splist.add(sp_list);
                    if (server_status == 1) {
                        server_message = "Successful";

                    } else {
                        server_message = "Invalid Credentials";
                    }
                }
                return null;

            } catch (Exception exception) {
                server_message = "Invalid Credentials";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if (server_status == 1) {

                SharedPreferences sharedPreferences = Login_Activity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USER_ID, id);
                editor.putString(Constants.USER_TYPE, "service_provider");

                editor.commit();

                Intent i=new Intent(Login_Activity.this,SPHome.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
            else{
                showSnackBar(server_message);
            }
        }
    }
    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(login_rel, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#7a2da6"));

        snackbar.show();
    }
}
