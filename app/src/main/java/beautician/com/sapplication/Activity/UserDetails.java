package beautician.com.sapplication.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class UserDetails extends AppCompatActivity {
    ImageView user_pic;
    String user_id;
    TextView username,userphone,usermail;
    String Uname,id,photo,email,mobile;
    RelativeLayout rel_back;
    Toolbar user_toolbar;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        user_pic=(ImageView)findViewById(R.id.profile_user);
        username=(TextView)findViewById(R.id.user_name);
        userphone=(TextView)findViewById(R.id.user_phone);
        usermail=(TextView)findViewById(R.id.user_email);
        user_toolbar=(Toolbar)findViewById(R.id.user_toolbar);
        rel_back=(RelativeLayout)user_toolbar.findViewById(R.id.rel_back);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("USER_ID");
            // and get whatever type user account id is
        }
        rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        getUserDetails();

    }

    private void getUserDetails() {
        if(CheckInternet.getNetworkConnectivityStatus(UserDetails.this)){
            userView();
        }
        else{
            Constants.noInternetDialouge(UserDetails.this,"No Internet");
        }
    }

    private void userView() {
        ViewUser viewUser=new ViewUser();
        viewUser.execute(user_id);
    }
    private class ViewUser extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;
        ProgressDialog progressDialog=null;

        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(UserDetails.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(UserDetails.this, "Loading", "Please wait...");

                }
            }            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _SID = params[0];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.USER_DETAILS ;
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
                        .appendQueryParameter("user_id", _SID);

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

               /*
               * {
    "user": {
        "id": 10,
        "name": "avinash pathak",
        "email": "avinasha@yahoo.com",
        "mobile": "7205674061",
        "photo": null,
        "created": "1988-01-23T00:00:00+05:30",
        "modified": "1988-01-23T00:00:00+05:30",
        "usertype": "test"
    }
}
               * */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("user");
                     Uname=j_obj.getString("name");
                     id=j_obj.getString("id");
                     photo=j_obj.getString("photo");
                     email=j_obj.getString("email");
                     mobile=j_obj.getString("mobile");

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
            setValues();
            progressDialog.dismiss();
        }
    }

    private void setValues() {
        //TextView shop_name,shop_email,shop_address,shop_reviws;

        username.setText(Uname);
        usermail.setText(email);
        userphone.setText(mobile);
        if(!photo.isEmpty()) {
            Picasso.with(UserDetails.this).load(Constants.PICURL+photo).into(user_pic);
        }

    }

}
