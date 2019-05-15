package beautician.com.sapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class SupportActivity extends AppCompatActivity {
    TextView tv_email,message_head,cont_head,name_head;
    EditText fullname,contact,message;
    Button send_feed;
    String user_id,user_name,user_phone,user_email,page,lang;
    LinearLayout new_;
    String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        tv_email = (TextView) findViewById(R.id.tv_email);
        name_head = (TextView) findViewById(R.id.name_head);
        cont_head = (TextView) findViewById(R.id.cont_head);
        message_head = (TextView) findViewById(R.id.message_head);
        fullname = (EditText) findViewById(R.id.fullname);
        contact = (EditText) findViewById(R.id.contact);
        message = (EditText) findViewById(R.id.message);
        send_feed = (Button) findViewById(R.id.send_feed);
        tv_email.setText("support@beautician.life");
        new_ = (LinearLayout)findViewById(R.id.new_);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            page = extras.getString("PAGE");
            lang = extras.getString("LANG");

        }

        if(lang.contentEquals("Arabic")){
            name_head.setText(R.string.name_ar);
            cont_head.setText(R.string.mobile_ar);
            message_head.setText(R.string.enter_details_ar);
            send_feed.setText(R.string.submit_ar);
        }
        else{
            name_head.setText(R.string.name_en);
            cont_head.setText(R.string.mobile_en);
            message_head.setText(R.string.enter_details_en);
            send_feed.setText(R.string.submit_en);

        }

        fullname.setEnabled(false);
        contact.setEnabled(false);


        if(page.contentEquals("user_side")) {

            user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            user_name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_NAME, null);
            user_phone = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_MOBILE, null);
            user_email = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_EMAIL, null);
            user_type = "User";
            fullname.setText(user_name);
            contact.setText(user_phone);
        }
        else {
            user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            user_name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_NAME, null);
            user_phone = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_MOBILE, null);
            user_email = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_EMAIL, null);
            user_type = "Shop";
            fullname.setText(user_name);
            contact.setText(user_phone);
        }



        new_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: support@beautican.life"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });

        send_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(message.getText().toString().trim().length()<=0){
                    Toast.makeText(SupportActivity.this,"Message is Mandatory",Toast.LENGTH_LONG).show();
                }
                else {


                    if (CheckInternet.getNetworkConnectivityStatus(SupportActivity.this)) {
                        String s_name = fullname.getText().toString().trim();
                        String s_phone = contact.getText().toString().trim();
                        String s_message = message.getText().toString().trim();
                        String s_title = "Feedback From" + s_name;
                        SubmitFeedBack submitFeedBack = new SubmitFeedBack();
                        submitFeedBack.execute(user_id, s_name, user_type, s_phone, user_email, s_message, s_title);
                    } else {
                        Toast.makeText(SupportActivity.this, "No Internet", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }


    private class SubmitFeedBack extends AsyncTask<String, Void, Void> {

        private static final String TAG = "User Support";
        ProgressDialog progressDialog;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(SupportActivity.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(SupportActivity.this, "Loading", "Please wait...");

                }            }

        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                String user_id= params[0];
                String user_name= params[1];
                String type= params[2];
                String user_phone= params[3];
                String user_email= params[4];
                String user_message= params[5];
                String user_title= params[6];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL+Constants.SEND_FEEDBACK;
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
                        .appendQueryParameter("reference_id", user_id)
                        .appendQueryParameter("name", user_name)
                        .appendQueryParameter("type", type)
                        .appendQueryParameter("mobile", user_phone)
                        .appendQueryParameter("email", user_email)
                        .appendQueryParameter("description", user_message)
                        .appendQueryParameter("title", user_title);
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
                    JSONObject res = new JSONObject(response);
                    JSONObject nreres = res.getJSONObject("res");
                    server_status = nreres.optInt("status");
                    if (server_status == 1) {
                        if(lang.contentEquals("Arabic")){
                            server_message="ناجح";

                        }
                        else{
                            server_message="Successful";

                        }                    } else {
                        if(lang.contentEquals("Arabic")){
                            server_message = "آسف!! فشل الدخول";

                        }else{
                            server_message = "Sorry !! Entry failed";

                        }                    }

                }
                return null;

            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.cancel();
            Toast.makeText(SupportActivity.this,server_message,Toast.LENGTH_SHORT).show();
            if(server_status == 1){
                SupportActivity.this.finish();
            }

        }
    }
}
