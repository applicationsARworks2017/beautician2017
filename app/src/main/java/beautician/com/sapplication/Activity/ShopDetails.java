package beautician.com.sapplication.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class ShopDetails extends AppCompatActivity {
    String shop_id;
    ImageView img1,img2,img3;
    TextView shop_name,shop_email,shop_address,shop_reviws;
    RatingBar rating;
    String id,shopname,address,latlong,photo1,photo2,photo3,email,mobile,reviews,ratings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_shop_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shop_id = extras.getString("SHOP_ID");
            // and get whatever type user account id is
        }
        img1=(ImageView)findViewById(R.id.img1);
        img2=(ImageView)findViewById(R.id.img2);
        img3=(ImageView)findViewById(R.id.img3);

        shop_name=(TextView)findViewById(R.id.shop_name);
        shop_email=(TextView)findViewById(R.id.shop_email);
        shop_address=(TextView)findViewById(R.id.shop_address);
        shop_reviws=(TextView)findViewById(R.id.shopreviews);
        rating=(RatingBar)findViewById(R.id.rating);
        getShopDetails();
    }

    private void getShopDetails() {

        if(CheckInternet.getNetworkConnectivityStatus(ShopDetails.this)){
            shopView();
        }
        else{
            Constants.noInternetDialouge(ShopDetails.this,"No Internet");
        }
    }

    private void shopView() {
        ViewShops viewShops=new ViewShops();
        viewShops.execute(shop_id);
    }
    private class ViewShops extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;
        ProgressDialog progressDialog=null;

        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(ShopDetails.this, "Loading", "Please wait...");
            }            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _SID = params[0];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.SHOP_DETAILS ;
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
                        .appendQueryParameter("shop_id", _SID);

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
               *  "shop": {
        "id": 11,
        "shopname": "MARS",
        "address": "bangalore",
        "latitudelongitude": "13.0356129,77.6332397",
        "photo1": "file15115175501043044287.jpg",
        "photo2": "file15115175501043044287.jpg",
        "photo3": "file15115175501043044287.jpg",
        "email": "amaresh.samantaray4@gmail.com",
        "mobile": "7026405551",
        "no_of_reviews": 14,
        "avg_rating": 3.39,
        "created": "2017-11-21T14:04:23+05:30",
        "modified": "2017-11-21T14:04:23+05:30",
               * */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("shop");
                    shopname=j_obj.getString("shopname");
                    id=j_obj.getString("id");
                    address=j_obj.getString("address");
                    latlong=j_obj.getString("latitudelongitude");
                    photo1=j_obj.getString("photo1");
                    photo2=j_obj.getString("photo2");
                    photo3=j_obj.getString("photo3");
                    email=j_obj.getString("email");
                    mobile=j_obj.getString("mobile");
                    reviews=j_obj.getString("no_of_reviews");
                    ratings=j_obj.getString("avg_rating");

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

        shop_name.setText(shopname);
        shop_email.setText(email);
        shop_address.setText(address);
        shop_reviws.setText(reviews+" Reviews");
        rating.setRating(Float.parseFloat(ratings));

        if(!photo1.isEmpty()) {
            Picasso.with(ShopDetails.this).load(Constants.PICURL+photo1).into(img1);
        }
        if(!photo2.isEmpty()) {
            Picasso.with(ShopDetails.this).load(Constants.PICURL+photo2).into(img2);
        }
        if(!photo3.isEmpty()) {
            Picasso.with(ShopDetails.this).load(Constants.PICURL+photo3).into(img3);
        }

    }
}
