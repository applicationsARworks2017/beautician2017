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
import beautician.com.sapplication.Adapter.ServiceReqAdapter;
import beautician.com.sapplication.Adapter.ServiceReqAdapterSP;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.ServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class CheckPost extends AppCompatActivity {
    String page;
    String user_id,server_message;
    ProgressBar loader_chek_post;
    int server_status;
    ArrayList<ServiceRequest> srList;
    ServiceReqAdapter serviceReqAdapter;
    ServiceReqAdapterSP serviceReqAdapterSP;
    SwipeRefreshLayout swipe_checkpost;
    ListView lv_checkpost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            page = extras.getString("PAGE");
            // and get whatever type user account id is
        }
        if(page.contentEquals("sp_home")){

        }
        else {
            super.setTheme(R.style.AppUserTheme);
        }
        setContentView(R.layout.activity_check_post);
        srList=new ArrayList<>();
        user_id = CheckPost.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        loader_chek_post=(ProgressBar)findViewById(R.id.loader_chek_post);
        swipe_checkpost=(SwipeRefreshLayout)findViewById(R.id.swipe_checkpost);
        lv_checkpost=(ListView)findViewById(R.id.lv_checkpost);

        loader_chek_post.setVisibility(View.GONE);


        getAllpost();
        swipe_checkpost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srList.clear();
                swipe_checkpost.setRefreshing(false);
                getAllpost();

            }
        });
    }

    private void getAllpost() {
        if(CheckInternet.getNetworkConnectivityStatus(CheckPost.this)){
            GetPostList getPost=new GetPostList();
            getPost.execute(user_id);
        }
        else{
            Constants.noInternetDialouge(CheckPost.this,"No Internet");
        }
    }

    /*
* GET POST LIST ASYNTASK*/
    private class GetPostList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getPOSTList";

        @Override
        protected void onPreExecute() {
            loader_chek_post.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _user_id=params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.SERVICE_REQUEST_LIST;
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
    {
    "serviceRequests": [
        {
            "id": 2,
            "name": "avinash pathak",
            "email": "avinash@yahoo.com",
            "mobile": "7205674061",
            "photo": null,
            "created": "1988-01-23T00:00:00+00:00",
            "modified": "1988-01-23T00:00:00+00:00",
            "usertype": "test",
            "remarks": "jhgjhghjgjg",
            "category": "Massage",
            "sub_category": "Body Massage"
        },
    ]
}
}
}
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray serviceListArray = res.getJSONArray("serviceRequests");
                    if(serviceListArray.length()<=0){
                        server_status=0;
                        server_message="No Category Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < serviceListArray.length(); i++) {
                            JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String name = o_list_obj.getString("name");
                            String mobile = o_list_obj.getString("mobile");
                            String email = o_list_obj.getString("email");
                            String photo = o_list_obj.getString("photo");
                            String remarks = o_list_obj.getString("remarks");
                            String category = o_list_obj.getString("category");
                            String sub_category = o_list_obj.getString("sub_category");
                            String created = o_list_obj.getString("created");
                            String status = o_list_obj.getString("status");
                            String expected_date = o_list_obj.getString("expected_date");
                            ServiceRequest list1 = new ServiceRequest(id,name,mobile,email,photo,remarks,category,sub_category,status,created,expected_date);
                            srList.add(list1);
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
                if(page.contentEquals("sp_home")) {
                    serviceReqAdapterSP = new ServiceReqAdapterSP(CheckPost.this, srList);
                    lv_checkpost.setAdapter(serviceReqAdapterSP);
                    serviceReqAdapterSP.notifyDataSetChanged();
                }
                else{
                    serviceReqAdapter = new ServiceReqAdapter(CheckPost.this, srList);
                    lv_checkpost.setAdapter(serviceReqAdapter);
                    serviceReqAdapter.notifyDataSetChanged();
                }
            }
            else{
                swipe_checkpost.setVisibility(View.GONE);

            }
            loader_chek_post.setVisibility(View.GONE);
        }
    }

}
