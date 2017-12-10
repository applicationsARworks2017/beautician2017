package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class Offer_creation extends AppCompatActivity {
    EditText title,et_details;
    Button submit;
    String user_id;
    ProgressBar offer_loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_creation);
        user_id = Offer_creation.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        title=(EditText)findViewById(R.id.title);
        et_details=(EditText)findViewById(R.id.et_content);
        submit=(Button)findViewById(R.id.submit);
        offer_loader=(ProgressBar)findViewById(R.id.offer_loader);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String offer_title=title.getText().toString().trim();
                String offer_body=et_details.getText().toString().trim();
                if(offer_title.length()<=0){

                }
                else if(offer_body.length()<=0){

                }
                else{
                    // Toast.makeText(_context,"Done",Toast.LENGTH_SHORT).show();
                    if(CheckInternet.getNetworkConnectivityStatus(Offer_creation.this)){
                        SetOffer setOffer=new SetOffer();
                        setOffer.execute(user_id,offer_title,offer_body);
                    }
                    else{
                        Constants.noInternetDialouge(Offer_creation.this,"No Internet");
                    }
                }
            }
        });

    }
    private class SetOffer extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Offer add Sync";
        String server_message;
        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            offer_loader.setVisibility(View.VISIBLE);
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _shop_id = params[0];
                String _title = params[1];
                String _body = params[2];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.CREATE_OFFER ;
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
                        .appendQueryParameter("shop_id", _shop_id)
                        .appendQueryParameter("title", _title)
                        .appendQueryParameter("remarks", _body);

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
                 "message": "The service offer has been saved.",
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
                        server_message="Offer Created";
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

            if (server_status == 1) {
                Intent intent = new Intent(Offer_creation.this, SPHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            offer_loader.setVisibility(View.GONE);
            Toast.makeText(Offer_creation.this,server_message,Toast.LENGTH_SHORT).show();
        }
    }
}
