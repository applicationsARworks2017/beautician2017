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

public class SubCategoryEditActivity extends AppCompatActivity{
    String SUB_CATG_NAME,SUB_PRICE,SUB_ID,SUB_CATG_arabic,lang,PRICE_ID;
    EditText sub_name,sub_price;
    Button save,delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SUB_CATG_NAME = extras.getString("SUB_CATG_ENGLISH");
            SUB_CATG_arabic = extras.getString("SUB_CATG_ARABIC");
            SUB_PRICE = extras.getString("SUB_PRICE");
            SUB_ID = extras.getString("SUB_ID");
            PRICE_ID = extras.getString("PRICE_ID");

            // and get whatever type user account id is
        }
        lang =getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        sub_name=(EditText)findViewById(R.id.sub_name_edit);
        sub_price=(EditText)findViewById(R.id.sub_pri_edit);
        save=(Button)findViewById(R.id.save);
        delete=(Button)findViewById(R.id.delete);
        if(lang.contentEquals("Arabic")) {
            sub_name.setText(SUB_CATG_arabic);

        }else {
            sub_name.setText(SUB_CATG_NAME);
            sub_price.setText(SUB_PRICE);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.getNetworkConnectivityStatus(SubCategoryEditActivity.this)){
                        new SubCEDIT().execute(PRICE_ID,sub_price.getText().toString().trim());

                    }
                }
    });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.getNetworkConnectivityStatus(SubCategoryEditActivity.this)){
                    String price=sub_price.getText().toString();
                        //new SubCEDIT().execute(PRICE_ID, price);

                    }
                }
    });

    }
    private class SubCEDIT extends AsyncTask<String, Void, Void> {

        String TAG = "Edit SP";
        private boolean is_success = false;
        private ProgressDialog progressDialog = null;
       int server_status;
       String server_message;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(SubCategoryEditActivity.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(SubCategoryEditActivity.this, "Loading", "Please wait...");

                }
            }

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.SUBCATEGORY_EDIT ;
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
                        .appendQueryParameter("id", params[0])
                        .appendQueryParameter("price", params[1]);

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


                if (response != null && response.length() > 0) {
                    JSONObject res_server = new JSONObject(response.trim());
                    JSONObject newObj = new JSONObject(String.valueOf(res_server.getJSONObject("res")));
                    server_status = newObj.optInt("status");
                    if (server_status == 1) {
                        server_message = "Edit Successful";

                    } else {
                        server_message = "Sorry !! Edit failed";
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

            progressDialog.dismiss();
            if (server_status == 1) {
                server_message = "Edit Successful";
                finish();
            } else {
                server_message = "Sorry !! Edit failed";
            }
        }
    }

    private class SubCDelete extends AsyncTask<String, Void, Void> {

        String TAG = "Edit SP";
        private boolean is_success = false;
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(SubCategoryEditActivity.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(SubCategoryEditActivity.this, "Loading", "Please wait...");

                }
            }

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.SUBCATEGORY_EDIT ;
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
                        .appendQueryParameter("id", params[0])
                        .appendQueryParameter("price", params[1]);

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


                if (response != null && response.length() > 0) {
                    JSONObject res_server = new JSONObject(response.trim());
                    JSONObject newObj = new JSONObject(String.valueOf(res_server.getJSONObject("res")));
                    server_status = newObj.optInt("status");
                    if (server_status == 1) {
                        server_message = "Delete Successful";

                    } else {
                        server_message = "Sorry !! Delete failed";
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

            progressDialog.dismiss();
            if (server_status == 1) {
                server_message = "Delete Successful";
                finish();
            } else {
                server_message = "Sorry !! Delete failed";
            }
        }
    }
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        finish();
    }
}
