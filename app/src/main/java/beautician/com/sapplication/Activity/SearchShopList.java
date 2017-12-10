package beautician.com.sapplication.Activity;

import android.net.Uri;
import android.os.AsyncTask;
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

import beautician.com.sapplication.Adapter.ShopListAdapter;
import beautician.com.sapplication.Pojo.Shops;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class SearchShopList extends AppCompatActivity {

    String search_text,page,server_message;
    int server_status;
    TextView blanck_search;
    SwipeRefreshLayout swipe_searchshop;
    ProgressBar loader_search;
    ArrayList<Shops> sList;
    ShopListAdapter sAdapter;
    ListView lv_searchshop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_search_shop_list);
        blanck_search=(TextView)findViewById(R.id.blanck_search);
        swipe_searchshop=(SwipeRefreshLayout) findViewById(R.id.swipe_searchshop);
        loader_search=(ProgressBar)findViewById(R.id.loader_search);
        lv_searchshop=(ListView)findViewById(R.id.lv_searchshop);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            search_text = extras.getString("SEARCH");
            // and get whatever type user account id is
        }
        sList=new ArrayList<>();
        getSearchShop();
        swipe_searchshop.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSearchShop();

            }
        });

    }

    private void getSearchShop() {
        sList.clear();
        swipe_searchshop.setRefreshing(false);
        if(CheckInternet.getNetworkConnectivityStatus(SearchShopList.this)){
            Searchshop seach=new Searchshop();
            seach.execute(search_text);
        }
        else{
            Constants.noInternetDialouge(SearchShopList.this,"No Internet");
        }

    }

    //* GET CATEGORY LIST ASYNTASK
    private class Searchshop extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getcategoryList";

        @Override
        protected void onPreExecute() {
            loader_search.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.NEW_SEARCH_SHOP;
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
                Uri.Builder builder=null;
                    builder = new Uri.Builder()
                            .appendQueryParameter("shopname", search_text)
                            .appendQueryParameter("page",page);
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
* "shops": [
        {
            "id": 7,
            "shopname": "an mart",
            "address": "Karnataka",
            "latitudelongitude": "13.042775499999998,77.6344609",
            "photo1": "http://applicationworld.net/beautician/webroot/files/profile/file15101537521523341327.jpg",
            "photo2": "http://applicationworld.net/beautician/webroot/files/profile/file1510153753279822175.jpg",
            "photo3": "http://applicationworld.net/beautician/webroot/files/profile/file1510153754144948710.jpg",
            "email": "mgt@gmail.com",
            "mobile": "9999999999",
            "no_of_reviews": null,
            "avg_rating": null,
            "created": "2017-11-08T15:09:14+05:30",
            "modified": "2017-11-08T15:09:14+05:30"
        },
* */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray servicePurposalArray = res.getJSONArray("shops");
                    if(servicePurposalArray.length()<=0){
                        server_message="No Category Found";
                        server_status=0;

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < servicePurposalArray.length(); i++) {
                            JSONObject o_list_obj = servicePurposalArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String latitudelongitude = o_list_obj.getString("latitudelongitude");
                            String photo1 = o_list_obj.getString("photo1");
                            String photo2 = o_list_obj.getString("photo2");
                            String photo3 = o_list_obj.getString("photo3");
                            String no_of_reviews = o_list_obj.getString("no_of_reviews");
                            String avg_rating = o_list_obj.getString("avg_rating");
                            String created = o_list_obj.getString("created");
                            String shopname = o_list_obj.getString("shopname");
                            String address = o_list_obj.getString("address");
                            Shops list1 = new Shops(id,latitudelongitude,photo1,photo2,photo3,no_of_reviews,avg_rating,created,shopname,address);
                            sList.add(list1);
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
                sAdapter = new ShopListAdapter (SearchShopList.this,sList);
                lv_searchshop.setAdapter(sAdapter);
            }
            else{
                lv_searchshop.setVisibility(View.GONE);
                blanck_search.setVisibility(View.VISIBLE);
            }
            loader_search.setVisibility(View.GONE);
        }
    }
}
