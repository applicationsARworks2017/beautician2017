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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;

import beautician.com.sapplication.Adapter.PropsalAdapter;
import beautician.com.sapplication.Adapter.RatingspointsAdapter;
import beautician.com.sapplication.Pojo.RatingsPoints;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class GiveCommentActivity extends AppCompatActivity {
    RatingBar customer_ratings;
    String ratings_value;
    RelativeLayout rel_ratingpoints;
    ArrayList<RatingsPoints> ratingspoints;
    RatingspointsAdapter radapter;
    ListView lv_ratings_value;
    Button bt_subratings;
    EditText et_comments_feedback;
    String improve;
    String shop_id,shop_name,user_id,propsal_id;
    PropsalAdapter objadapter=new PropsalAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_give_comment);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shop_id = extras.getString("SHOP_ID");
            shop_name = extras.getString("SHOP_NAME");
            propsal_id = extras.getString("PROPSAL_ID");
            // and get whatever type user account id is
        }
        user_id = GiveCommentActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        customer_ratings=(RatingBar)findViewById(R.id.customer_ratings);
        rel_ratingpoints=(RelativeLayout)findViewById(R.id.rel_ratingpoints);
        lv_ratings_value=(ListView)findViewById(R.id.ratings_value);
        et_comments_feedback=(EditText)findViewById(R.id.et_comments_feedback);
        bt_subratings=(Button)findViewById(R.id.bt_subratings);
        bt_subratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllvalue();
            }
        });
        customer_ratings.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //ratings.setText(String.valueOf(rating));
                ratings_value=String.valueOf(rating);
                if(rating<4.0){
                        rel_ratingpoints.setVisibility(View.VISIBLE);
                    }
                    else{
                        rel_ratingpoints.setVisibility(View.GONE);
                }
            }


        });

        ratingspoints = new ArrayList();
        ratingspoints.add(new RatingsPoints("Shop Interier", false));
        ratingspoints.add(new RatingsPoints("Service Quality", false));
        ratingspoints.add(new RatingsPoints("Staff Behaviuor", false));
        ratingspoints.add(new RatingsPoints("Others", false));
        radapter = new RatingspointsAdapter(ratingspoints, getApplicationContext());
        lv_ratings_value.setAdapter(radapter);


    }

    private void getAllvalue() {
        StringBuffer sb = new StringBuffer();

        if(Double.valueOf(ratings_value)<4.0) {

            for (RatingsPoints bean : ratingspoints) {
                        /*if (counter<5) {
                            counter++;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Only five please", Toast.LENGTH_SHORT).show();
                        }*/
                if (bean.getIschecked()) {
                    if (sb.toString().trim().contains(bean.getPoints())) {

                    } else {
                        sb.append(bean.getPoints());
                        sb.append(",");
                    }
                }
            }
            if (sb.length() <= 0) {
                Toast.makeText(GiveCommentActivity.this, "Please check atleast one point", Toast.LENGTH_SHORT).show();

            } else {
                improve = sb.toString().trim().substring(0, sb.length() - 1);
                submit();
            }
        }

        else{
            submit();
        }

    }

    private void submit() {
        String comments=et_comments_feedback.getText().toString().trim();
        if(ratings_value.contentEquals("")||ratings_value.contentEquals("null")){
            Toast.makeText(GiveCommentActivity.this,"Enter Rating Please",Toast.LENGTH_SHORT).show();
        }
        else if(et_comments_feedback.getText().toString().trim().length()<=0){
            Toast.makeText(GiveCommentActivity.this,"Enter Comments Please",Toast.LENGTH_SHORT).show();
        }
        else{
            if(CheckInternet.getNetworkConnectivityStatus(GiveCommentActivity.this)){
                SubmitRatings submitRatings=new SubmitRatings();
                submitRatings.execute(ratings_value,comments,shop_id,improve,user_id);
            }
            else{
                Constants.noInternetDialouge(GiveCommentActivity.this,"No Internet");
            }
        }
    }


    /*
    *
    * rating:5
review:20
shop_id:11
improve:2
user_id:12
    * */


    public void onItemClickOfListView(int getPosition, boolean checked) {
        CheckBox chk = (CheckBox)findViewById(R.id.checkbox);
        RatingsPoints bean =ratingspoints.get(getPosition);
        if (bean.getIschecked()) {
            bean.setIschecked(false);
            chk.setChecked(false);
        } else {
            bean.setIschecked(true);
            chk.setChecked(true);
        }
    }
    private class SubmitRatings extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        String server_message;
        private ProgressDialog progressDialog = null;

        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(GiveCommentActivity.this, "Comment", "Please wait...");
            }
            super.onPreExecute();

            // ratings_value,comments,shop_id,ratings_point_value,user_id
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _ratings_value = params[0];
                String _comments = params[1];
                String _shop_id = params[2];
                String _ratings_point_value = params[3];
                String _user_id = params[4];

                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.COMMENTS ;
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
                        .appendQueryParameter("rating", _ratings_value)
                        .appendQueryParameter("review", _comments)
                        .appendQueryParameter("shop_id", _shop_id)
                        .appendQueryParameter("improve", _ratings_point_value)
                        .appendQueryParameter("user_id", _user_id);

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
                        server_message = "Done";
                    }
                    else{
                        server_message = "failed";

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
                finish();
                objadapter.calltoupdate(propsal_id,"5","comment");
            }
            progressDialog.dismiss();
            Toast.makeText(GiveCommentActivity.this,server_message,Toast.LENGTH_SHORT).show();
        }
    }
}
