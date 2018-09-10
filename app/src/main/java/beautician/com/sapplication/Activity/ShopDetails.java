package beautician.com.sapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
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
    ImageView img1,img2,img3,img4;
    TextView shop_name,shop_email,shop_address,shop_reviws,allservice;
    RatingBar rating;
    String lang,id,shopname,address,latlong,photo1,photo2,photo3,photo4,email,mobile,reviews,ratings;
    RelativeLayout rel_back;
    Toolbar sp_toolbar;
    String MAP;
    LinearLayout mapview;
    String[] latlonglist ;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        Bundle extras = getIntent().getExtras();
        lang =getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);
        if(lang.contentEquals("Arabic")){
            setTitle("تفاصيل المتجر");
        }
        else{
            setTitle("Shop Details");
        }
        if (extras != null) {
            shop_id = extras.getString("SHOP_ID");
            MAP = extras.getString("MAP");
            // and get whatever type user account id is
        }
        sp_toolbar=(Toolbar)findViewById(R.id.sp_toolbar);
        rel_back=(RelativeLayout)sp_toolbar.findViewById(R.id.rel_back);
        img1=(ImageView)findViewById(R.id.img1);
        img2=(ImageView)findViewById(R.id.img2);
        img3=(ImageView)findViewById(R.id.img3);
        img4=(ImageView)findViewById(R.id.img4);

        shop_name=(TextView)findViewById(R.id.shop_name);
        shop_email=(TextView)findViewById(R.id.shop_email);
        shop_address=(TextView)findViewById(R.id.shop_address);
        shop_reviws=(TextView)findViewById(R.id.shopreviews);
        allservice=(TextView)findViewById(R.id.allservice);
        rating=(RatingBar)findViewById(R.id.rating);
        mapview=(LinearLayout)findViewById(R.id.mapview);
        getShopDetails();
        if(MAP.contentEquals("true")){
            mapview.setVisibility(View.VISIBLE);
        }
        else{
            mapview.setVisibility(View.GONE);
        }
        rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(lang.contentEquals("Arabic")){
            allservice.setText("تحقق من تفاصيل الخدمة هنا");
        }
        else{
            allservice.setText("Check service details here");

        }
        allservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShopDetails.this,MyserviceList.class);
                intent.putExtra("PAGE","service_home");
                intent.putExtra("USERID",shop_id);
                startActivity(intent);
            }
        });
        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShopDetails.this,MapLocation.class);
                intent.putExtra("LATITUDE",latlonglist[0]);
                intent.putExtra("LONGITUDE",latlonglist[1]);
                startActivity(intent);
            }
        });
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
                    photo4=j_obj.getString("photo4");
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
        latlonglist=latlong.split(",");
        shop_name.setText(shopname);
        shop_email.setText(email);
        shop_address.setText(address);
        if(reviews=="" || reviews==null ||reviews.contentEquals("null")){
            shop_reviws.setText("No "+ "Reviews");
        }
        else {
            shop_reviws.setText(reviews+" Reviews");

        }
        if(ratings==null || ratings.contentEquals("") || ratings.contentEquals("null")){

        }
        else {
            rating.setRating(Float.parseFloat(ratings));
        }

        if(!photo1.isEmpty()) {
            Glide.with(ShopDetails.this).load(Constants.SHOP_PICURL+photo1)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img1);
           // imageLoader.displayImage(Constants.SHOP_PICURL+photo1,img1,options);
            /*Picasso.with(ShopDetails.this).load(Constants.SHOP_PICURL+photo1)
                    //.resize(300,300)
                    .into(img1);*/
        }
        if(!photo2.isEmpty()) {
            Glide.with(ShopDetails.this).load(Constants.SHOP_PICURL+photo2)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img2);
            /*Picasso.with(ShopDetails.this).load(Constants.SHOP_PICURL+photo2)
                    //.resize(300,300)
                    .into(img2);*/
            //imageLoader.displayImage(Constants.SHOP_PICURL+photo2,img2,options);

        }
        if(!photo3.isEmpty()) {
            Glide.with(ShopDetails.this).load(Constants.SHOP_PICURL+photo3)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img3);
            //Picasso.with(ShopDetails.this).load(Constants.SHOP_PICURL+photo3);
                   // .resize(300,300).into(img3);
            //imageLoader.displayImage(Constants.SHOP_PICURL+photo3,img3,options);

        }if(!photo4.isEmpty()) {
            Glide.with(ShopDetails.this).load(Constants.SHOP_PICURL+photo4)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img4);
          //  Picasso.with(ShopDetails.this).load(Constants.SHOP_PICURL+photo4);
                    //.resize(300,300).into(img4);
           // imageLoader.displayImage(Constants.SHOP_PICURL+photo4,img4,options);

        }

    }
}
