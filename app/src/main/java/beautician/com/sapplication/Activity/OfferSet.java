package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import beautician.com.sapplication.Adapter.OffersAdapter;
import beautician.com.sapplication.Adapter.ServiceReqAdapter;
import beautician.com.sapplication.Adapter.ServiceReqAdapterSP;
import beautician.com.sapplication.Pojo.Offers;
import beautician.com.sapplication.Pojo.ServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class OfferSet extends AppCompatActivity {
    FloatingActionButton comp_gift;
    ArrayList<Offers> oList;
    ListView lv_setoffers;
    SwipeRefreshLayout swipe_set_offers;
    TextView off_blank_;
    ProgressBar loader_of_set;
    String user_id,page,server_message;
    int server_status;
    OffersAdapter oAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_set);
        comp_gift=(FloatingActionButton)findViewById(R.id.comp_gift);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            page = extras.getString("PAGE");
            // and get whatever type user account id is
        }
        user_id = OfferSet.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        oList=new ArrayList<>();
        if(page.contentEquals("user_side")){
            comp_gift.setVisibility(View.GONE);
        }
        lv_setoffers=(ListView)findViewById(R.id.lv_setoffers);
        swipe_set_offers=(SwipeRefreshLayout)findViewById(R.id.swipe_set_offers);
        off_blank_=(TextView)findViewById(R.id.off_blank_);
        loader_of_set=(ProgressBar)findViewById(R.id.loader_of_set);
        comp_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OfferSet.this,Offer_creation.class);
                startActivity(intent);
            }
        });
        checkOfferList();
    }

    private void checkOfferList() {
        if(CheckInternet.getNetworkConnectivityStatus(OfferSet.this)){
            GetOfferList getPost=new GetOfferList();
            getPost.execute(user_id);
        }
        else{
            Constants.noInternetDialouge(OfferSet.this,"No Internet");
        }
    }
    /*
* GET OFFER LIST ASYNTASK*/
    private class GetOfferList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getOfferList";

        @Override
        protected void onPreExecute() {
            loader_of_set.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _user_id=params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.LIST_OFFER;
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
                if(page.contentEquals("sp_home")) {
                    builder = new Uri.Builder()
                            .appendQueryParameter("shop_id", _user_id);
                }
                else{
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", _user_id);
                }
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
                *
                * {
    "offers": [
        {
            "id": 43,
            "title": "5 $ discount",
            "shop_id": 11,
            "remarks": "It'll be not free",
            "created": "2017-11-23T23:21:10+05:30",
            "modified": "2017-11-23T23:21:10+05:30",
            "shop": {
                "id": 11,
                "shopname": "MARS",
                "address": "bangalore",
                "latitudelongitude": "13.0356129,77.6332397",
                "photo1": "file1511273052578734998.jpg",
                "photo2": "file15112730571702631970.jpg",
                "photo3": "file15112730631927402507.jpg",
                "email": "amaresh.samantaray4@gmail.com",
                "mobile": "7026405551",
                "created": "2017-11-21T14:04:23+05:30",
                "modified": "2017-11-21T14:04:23+05:30"
            }
        },
}
}
}
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray serviceListArray = res.getJSONArray("offers");
                    if(serviceListArray.length()<=0){
                        server_status=0;
                        server_message="No Category Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < serviceListArray.length(); i++) {
                            JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                            JSONObject jobj=o_list_obj.getJSONObject("shop");
                            String id = o_list_obj.getString("id");
                            String title = o_list_obj.getString("title");
                            String offer_detail = o_list_obj.getString("remarks");
                            String shop_id=jobj.getString("id");
                            String shopname=jobj.getString("shopname");

                            Offers list1 = new Offers(id,title,offer_detail,shop_id,shopname);
                            oList.add(list1);
                        }
                    }
                }
                return null;
            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void data) {
            super.onPostExecute(data);
            if(server_status==1) {
                oAdapter=new OffersAdapter(OfferSet.this,oList);
                lv_setoffers.setAdapter(oAdapter);
            }
            else{
                swipe_set_offers.setVisibility(View.GONE);
                off_blank_.setVisibility(View.VISIBLE);

            }
            loader_of_set.setVisibility(View.GONE);
        }
    }

}
