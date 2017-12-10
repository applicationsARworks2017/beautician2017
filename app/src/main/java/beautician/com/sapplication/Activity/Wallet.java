package beautician.com.sapplication.Activity;

import android.app.Dialog;
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

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class Wallet extends AppCompatActivity {
    TextView tv_addMoney,tv_balance,tv_refresh;
    String page,user_id;
    Double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            page = extras.getString("PAGE");
            // and get whatever type user account id is
        }
        if(page.contentEquals("user_side")){
            super.setTheme(R.style.AppUserTheme);
        }
        setContentView(R.layout.activity_wallet);
        tv_addMoney=(TextView)findViewById(R.id.tv_addMoney);
        tv_balance=(TextView)findViewById(R.id.tv_balance);
        tv_refresh=(TextView)findViewById(R.id.tv_refresh);
        tv_addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Wallet.this);
                dialog.setContentView(R.layout.dialog_add_money);
                ImageView imageView=(ImageView)dialog.findViewById(R.id.close);
                Button add_money=(Button)dialog.findViewById(R.id.add_money);
                final EditText et_add_money=(EditText) dialog.findViewById(R.id.et_add_money);
                dialog.show();
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWalletBalance();
            }
        });
        user_id = Wallet.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        getWalletBalance();
    }

    private void getWalletBalance() {
        if(CheckInternet.getNetworkConnectivityStatus(Wallet.this)){
            getWdetails getWdetails=new getWdetails();
            getWdetails.execute(user_id);
        }
        else{
            Constants.noInternetDialouge(Wallet.this, "No Internet");

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
                progressDialog = ProgressDialog.show(Wallet.this, "Loading", "Please wait...");
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
                if(page.contentEquals("user_side")) {
                     link = Constants.ONLINEURL + Constants.USER_BALANCE;
                }
                else{
                    link = Constants.ONLINEURL + Constants.SHOP_BALANCE;

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

                Uri.Builder builder = null;
                if(page.contentEquals("user_side")) {
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", _userid);
                }
                else{
                    builder = new Uri.Builder()
                            .appendQueryParameter("shop_id", _userid);
                }

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
                    if(page.contentEquals("user_side")) {
                         serviceListArray = res.getJSONArray("userWallets");
                    }
                    else{
                         serviceListArray = res.getJSONArray("wallets");

                    }
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
            if (server_status == 1) {
                tv_balance.setText(String.valueOf("$ "+balance));
            }
            else{
                tv_balance.setText("$ 0");
            }
        }
    }
}
