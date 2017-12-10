package beautician.com.sapplication.Activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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

import beautician.com.sapplication.Adapter.CategoryAdapter;
import beautician.com.sapplication.Adapter.PropsalAdapter;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.Proposals;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class SpProposal extends AppCompatActivity {
    ProgressBar loader_propsals;
    SwipeRefreshLayout swipe_propsal;
    TextView no_propsal_txt;
    String user_id,server_message;
    int server_status;
    ListView lv_propsals;
    ArrayList<Proposals> pList;
    PropsalAdapter propsalAdapter;
    String page;


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
        setContentView(R.layout.activity_sp_proposal);
        pList=new ArrayList<>();
        loader_propsals=(ProgressBar)findViewById(R.id.loader_propsals);
        swipe_propsal=(SwipeRefreshLayout)findViewById(R.id.swip_propsal);
        lv_propsals=(ListView)findViewById(R.id.proposal_list);
        no_propsal_txt=(TextView)findViewById(R.id.no_propsal_txt);
        user_id = SpProposal.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        getPropsalList();
        swipe_propsal.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pList.clear();
                swipe_propsal.setRefreshing(false);
                getPropsalList();

            }
        });

    }

    private void getPropsalList() {
        if(CheckInternet.getNetworkConnectivityStatus(SpProposal.this)){
            CheckPropsal checkpropsals=new CheckPropsal();
            checkpropsals.execute(user_id);
        }
        else{
            Constants.noInternetDialouge(SpProposal.this,"No Internet");
        }
    }
//* GET PROPSAL LIST ASYNTASK
    private class CheckPropsal extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getpropsal";

        @Override
        protected void onPreExecute() {
            loader_propsals.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.PROPOSALS;
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
                if(page.contentEquals("user_side")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", user_id);
                }
                else {
                     builder = new Uri.Builder()
                            .appendQueryParameter("shop_id", user_id);
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
* "servicePurposal": [
        {
            "id": 20,
            "service_request_id": 36,
            "shop_id": 11,
            "remarks": "hello",
            "status": 1,
            "created": "2017-11-23T07:24:50+05:30",
            "modified": "2017-11-23T07:24:50+05:30",
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
* */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray servicePurposalArray = res.getJSONArray("servicePurposal");
                    if(servicePurposalArray.length()<=0){
                        server_message="No Category Found";
                        server_status=0;

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < servicePurposalArray.length(); i++) {
                            JSONObject o_list_obj = servicePurposalArray.getJSONObject(i);
                            JSONObject new_obj=o_list_obj.getJSONObject("shop");
                            String id = o_list_obj.getString("id");
                            String service_request_id = o_list_obj.getString("service_request_id");
                            String remarks = o_list_obj.getString("remarks");
                            String status = o_list_obj.getString("status");
                            String created = o_list_obj.getString("created");
                            String shop_id=new_obj.getString("id");
                            String shop_name=new_obj.getString("shopname");
                            if(page.contentEquals("user_side") && status.contentEquals("4")){

                            }
                            else {
                                Proposals list1 = new Proposals(id, service_request_id, remarks, status, created, shop_id, shop_name);
                                pList.add(list1);
                            }
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
                propsalAdapter = new  PropsalAdapter (SpProposal.this,pList ,page);
                lv_propsals.setAdapter(propsalAdapter);
                propsalAdapter.notifyDataSetChanged();
            }
            else{
                lv_propsals.setVisibility(View.GONE);
                no_propsal_txt.setVisibility(View.VISIBLE);
            }
            loader_propsals.setVisibility(View.GONE);
        }
    }
}
