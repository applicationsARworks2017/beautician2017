package beautician.com.sapplication.Activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import beautician.com.sapplication.Pojo.User;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class PropsalView extends AppCompatActivity {
    TextView prop_head,_remarks;
    EditText _comments;
    Button _ok;
    ProgressBar loader_prop_details;
    String sid,pid,propsal,p_status;
    String ser_req_id,post_details,sub_categorytitle,cat_title,userid,name,email,mobile,photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propsal_view);
        prop_head=(TextView) findViewById(R.id.prop_head);
        _remarks=(TextView) findViewById(R.id._remarks);
        _comments=(EditText)findViewById(R.id._comments);
        loader_prop_details=(ProgressBar)findViewById(R.id.loader_prop_details);
        _comments.setEnabled(false);
        _ok=(Button)findViewById(R.id._ok);
        _ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sid = extras.getString("SID");
            pid = extras.getString("PID");
            propsal = extras.getString("PROPSAL");
            p_status = extras.getString("PSTATUS");
            // and get whatever type user account id is
        }

        if(CheckInternet.getNetworkConnectivityStatus(this)){
            ViewPropsal viewPropsal=new ViewPropsal();
            viewPropsal.execute(sid);
        }
        else {
            Constants.noInternetDialouge(this,"No Internet");
        }
    }
    private class ViewPropsal extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;

        int server_status;

        @Override
        protected void onPreExecute() {
            loader_prop_details.setVisibility(View.VISIBLE);
            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _SID = params[0];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.VIEW_PROPSAL ;
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
                        .appendQueryParameter("service_request_id", _SID);

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
                 {
                 "serviceRequest": {
                 "id": 1,
                 "shop_id": null,
                 "user_id": 11,
                 "category_id": 1,
                 "sub_category_id": 2,
                 "remarks": "ghg",
                 "no_of_user": null,
                 "status": false,
                 "created": "2017-11-11T17:38:59+05:30",
                 "modified": "2017-11-11T17:38:59+05:30",
                 "sub_category": {
                 "id": 2,
                 "category_id": 1,
                 "title": "Head Massage",
                 "is_enable": "Y",
                 "created": "2017-10-30T18:17:12+05:30",
                 "modified": "2017-10-30T18:17:12+05:30"
                 },
                 "category": {
                 "id": 1,
                 "title": "Massage",
                 "is_enable": "Y",
                 "created": "2017-10-30T18:10:37+05:30",
                 "modified": "2017-10-30T18:10:37+05:30"
                 },
                 "user": {
                 "id": 11,
                 "name": "avinash pathak",
                 "email": "",
                 "mobile": "",
                 "photo": null,
                 "created": "1988-01-23T00:00:00+05:30",
                 "modified": "1988-01-23T00:00:00+05:30",
                 "usertype": null
                 }
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("serviceRequest");
                    JSONObject j_s_cat=j_obj.getJSONObject("sub_category");
                    JSONObject j_cat=j_obj.getJSONObject("category");
                    JSONObject j_user=j_obj.getJSONObject("user");
                    ser_req_id=j_obj.getString("id");
                    post_details=j_obj.getString("remarks");
                    sub_categorytitle=j_s_cat.getString("title");
                    cat_title=j_cat.getString("title");
                    userid=j_user.getString("id");
                    name=j_user.getString("name");
                    email=j_user.getString("email");
                    mobile=j_user.getString("mobile");
                    photo=j_user.getString("photo");
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
        }
    }

    private void setValues() {
        _remarks.setText(post_details);
        prop_head.setText(cat_title+":"+sub_categorytitle);
        _comments.setText(propsal);
        loader_prop_details.setVisibility(View.GONE);
    }
}
