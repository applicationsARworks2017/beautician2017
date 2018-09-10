package beautician.com.sapplication.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;

import beautician.com.sapplication.Adapter.TransactionAdapter;
import beautician.com.sapplication.Pojo.Transactions;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class Wallet extends AppCompatActivity {
    TextView tv_addMoney,tv_balance,tv_refresh;
    String page,user_id,lang,user_name,user_phone,user_mail;
    Double balance;
    SwipeRefreshLayout thistory_rel;
    TextView no_transactions;
    ListView trans_listview;
    ArrayList<Transactions> transactionsList;
    TransactionAdapter tadapter;

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
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);
        setContentView(R.layout.activity_wallet);
        tv_addMoney=(TextView)findViewById(R.id.tv_addMoney);
        tv_balance=(TextView)findViewById(R.id.tv_balance);
        tv_refresh=(TextView)findViewById(R.id.tv_refresh);
        trans_listview=(ListView) findViewById(R.id.trans_listview);
        no_transactions=(TextView)findViewById(R.id.no_transactions);
        thistory_rel=(SwipeRefreshLayout) findViewById(R.id.thistory_rel);


        if(lang.contentEquals("Arabic")){
            tv_refresh.setText("تحديث");
            tv_balance.setText("إجمالي الرصيد");
            tv_addMoney.setText("إضافة مبلغ الشحن");
            setTitle("محفظة نقود");
        }
        else{
            tv_refresh.setText("Refresh");
            tv_balance.setText("Total Balance");
            tv_addMoney.setText("Add Money");
            setTitle("Wallet");
        }


        tv_addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Wallet.this);
                dialog.setContentView(R.layout.dialog_add_money);
                TextView tv_wallet=(TextView)dialog.findViewById(R.id.tv_wallet);
                ImageView imageView=(ImageView)dialog.findViewById(R.id.close);
                Button add_money=(Button)dialog.findViewById(R.id.add_money);
                final EditText et_add_money=(EditText) dialog.findViewById(R.id.et_add_money);

                if(lang.contentEquals("Arabic")){
                    tv_wallet.setText("مبلغ الشحن (SAR)");
                    add_money.setText("موافق");

                }
                else{
                    tv_wallet.setText("Recharge Amount  ( In SAR )");
                    add_money.setText("Add Money");

                }
                dialog.show();
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                add_money.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent=new Intent(Wallet.this,PaymentConfirmation.class);
                        intent.putExtra("NAME",user_name);
                        intent.putExtra("PHONE",user_phone);
                        intent.putExtra("MAIL",user_mail);
                        intent.putExtra("AMOUNT",et_add_money.getText().toString().trim());
                        intent.putExtra("PAGE",page);
                        intent.putExtra("AVAIL_AMOUNT",balance);
                        startActivity(intent);
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
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWalletBalance();
            }
        });
        user_id = Wallet.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        thistory_rel.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTansactions();
            }
        });
        getWalletBalance();
        getTansactions();
        getUserDetails();
    }

    private void getUserDetails() {
        ViewUser viewUser=new ViewUser();
        viewUser.execute(user_id);
    }

    private void getTansactions() {
        thistory_rel.setRefreshing(false);
        if(CheckInternet.getNetworkConnectivityStatus(Wallet.this)){
            GetTrnsactions getTrnsactions=new GetTrnsactions();
            getTrnsactions.execute(user_id);
        }
        else{
            Constants.noInternetDialouge(Wallet.this, "No Internet");

        }
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
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(Wallet.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(Wallet.this, "Loading", "Please wait...");

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
                tv_balance.setText(String.valueOf("SAR "+balance));
            }
            else{
                tv_balance.setText("SAR 0");
            }
        }
    }
    /**
     * Async task to get transactionhistory
     * */
    private class GetTrnsactions extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get History";
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                    link = Constants.ONLINEURL + Constants.USER_TRASACTIONS;
                }
                else{
                    link = Constants.ONLINEURL + Constants.SHOP_TRASACTIONS;

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
                 wallets": [
                 {
                 "id": 28,
                 "shop_id": 1,
                 "debit": 100,
                 "credit": 0,
                 "balance": 899.99,
                 "remarks": "dfgdfg",
                 "created": "24-12-2017 03:29 AM",
                 "modified": "2017-12-24T03:29:50+05:30",
                 * */
                transactionsList=new ArrayList<>();

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
                            String debit = o_list_obj.getString("debit");
                            String credit = o_list_obj.getString("credit");
                            String balance = o_list_obj.getString("balance");
                            String remarks = o_list_obj.getString("remarks");
                            String created = o_list_obj.getString("created");
                            Transactions transactions=new Transactions(id,debit,credit,balance,remarks,created);
                            transactionsList.add(transactions);
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
            if (server_status == 1) {
               thistory_rel.setVisibility(View.VISIBLE);
               no_transactions.setVisibility(View.GONE);
                tadapter = new TransactionAdapter (Wallet.this,transactionsList);
                trans_listview.setAdapter(tadapter);
            }
            else{
                thistory_rel.setVisibility(View.GONE);
                no_transactions.setVisibility(View.VISIBLE);            }
        }
    }


    /*
    *
    * GET THE DETAILS
    * */
    private class ViewUser extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;
        ProgressDialog progressDialog=null;

        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(Wallet.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(Wallet.this, "Loading", "Please wait...");

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
                String link=null;
                if(page.contentEquals("user_side")) {
                     link = Constants.ONLINEURL + Constants.USER_DETAILS;
                }
                else{
                     link =Constants.ONLINEURL+ Constants.SHOP_DETAILS ;

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

                Uri.Builder builder =null;
                if(page.contentEquals("user_side")) {
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", _SID);
                }
                else{
                    builder = new Uri.Builder()
                            .appendQueryParameter("shop_id", _SID);
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
                    if(page.contentEquals("user_side")) {
                        JSONObject res = new JSONObject(response.trim());
                        JSONObject j_obj = res.getJSONObject("user");
                        user_name = j_obj.getString("name");
                        user_mail = j_obj.getString("email");
                        user_phone = j_obj.getString("mobile");
                    }
                    else{
                        JSONObject res = new JSONObject(response.trim());
                        JSONObject j_obj=res.getJSONObject("shop");
                        user_name=j_obj.getString("shopname");
                        user_mail=j_obj.getString("email");
                        user_phone=j_obj.getString("mobile");
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
            progressDialog.dismiss();
           // Toast.makeText(Wallet.this,user_name+user_phone,Toast.LENGTH_SHORT).show();
        }
    }
}
