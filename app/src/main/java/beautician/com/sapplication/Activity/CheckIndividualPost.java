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

import beautician.com.sapplication.Adapter.IndServiceRequestAdapter;
import beautician.com.sapplication.Adapter.IndServiceRequestAdapterUser;
import beautician.com.sapplication.Adapter.ServiceReqAdapter;
import beautician.com.sapplication.Adapter.ServiceReqAdapterSP;
import beautician.com.sapplication.Pojo.IndServiceRequest;
import beautician.com.sapplication.Pojo.ServiceRequest;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class CheckIndividualPost extends AppCompatActivity {
    ProgressBar loader_chekInd_post;
    SwipeRefreshLayout swipe_Indcheckpost;
    ListView lv_Indcheckpost;
    String user_id,page,server_message;
    int server_status;
    ArrayList<IndServiceRequest> isrList;
    IndServiceRequestAdapter indAdapter;
    IndServiceRequestAdapterUser indUAdapter;

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
        else{
            super.setTheme(R.style.AppUserTheme);
        }
        setContentView(R.layout.activity_check_individual_post);
        user_id = CheckIndividualPost.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        swipe_Indcheckpost=(SwipeRefreshLayout) findViewById(R.id.swipe_Indcheckpost);
        loader_chekInd_post=(ProgressBar)findViewById(R.id.loader_chekInd_post);
        lv_Indcheckpost=(ListView)findViewById(R.id.lv_Indcheckpost);
        isrList=new ArrayList<>();


        getAllPost();

    }

    private void getAllPost() {
        if(CheckInternet.getNetworkConnectivityStatus(CheckIndividualPost.this)){
            GetIndividualPostList getPost=new GetIndividualPostList();
            getPost.execute(user_id);
        }
        else{
            Constants.noInternetDialouge(CheckIndividualPost.this,"No Internet");
        }
    }
    /*
* GET POST LIST ASYNTASK*/
    private class GetIndividualPostList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getPOSTList";

        @Override
        protected void onPreExecute() {
            loader_chekInd_post.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _user_id=params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.INDICISUAL_REQUEST_LIST;
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
     "serviceIndivisualRequests": [
        {
            "id": 44,
            "shop_id": 11,
            "user_id": 20,
            "remarks": "done",
            "no_of_user": 1,
            "status": false,
            "created": "2017-11-26T08:00:02+05:30",
            "modified": "2017-11-26T08:00:02+05:30",
            "user": {
                "id": 20,
                "name": "amresh",
                "email": "ama@gmail.com",
                "mobile": "9090403050",
                "photo": "file15101535291311553082.jpg",
                "created": "2017-11-08T15:05:30+05:30",
                "modified": "2017-11-08T15:05:30+05:30",
                "usertype": "user"
            },
            "shop": {
                "id": 11,
                "shopname": "MARS",
                "address": "bangalore",
                "latitudelongitude": "13.0356129,77.6332397",
                "photo1": "file15115175501043044287.jpg",
                "photo2": "file15115175501043044287.jpg",
                "photo3": "file15115175501043044287.jpg",
                "email": "amaresh.samantaray4@gmail.com",
                "mobile": "7026405551",
                "no_of_reviews": 12,
                "avg_rating": 25.29,
                "created": "2017-11-21T14:04:23+05:30",
                "modified": "2017-11-21T14:04:23+05:30"
            }
        }
    ]
}
}
}
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray serviceListArray = res.getJSONArray("serviceIndivisualRequests");
                    if(serviceListArray.length()<=0){
                        server_status=0;
                        server_message="No Category Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < serviceListArray.length(); i++) {
                            JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                            JSONObject new_obj=o_list_obj.getJSONObject("user");
                            JSONObject new_obj1=o_list_obj.getJSONObject("shop");
                            String id = o_list_obj.getString("id");
                            String remarks=o_list_obj.getString("remarks");
                            String personId=new_obj.getString("id");
                            String no_of_user=o_list_obj.getString("no_of_user");
                            String personName=new_obj.getString("name");
                            String personemail=new_obj.getString("email");
                            String personmobile=new_obj.getString("mobile");
                            String personphoto=new_obj.getString("photo");
                            String status=o_list_obj.getString("status");
                            String shopname=new_obj1.getString("shopname");
                            String expected_date=o_list_obj.getString("expected_date");
                            IndServiceRequest list1 = new IndServiceRequest(id,remarks,personId,personName,personemail,personmobile,
                                    personphoto,status,no_of_user,shopname,expected_date);
                            isrList.add(list1);
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
                    indAdapter = new IndServiceRequestAdapter(CheckIndividualPost.this, isrList);
                    lv_Indcheckpost.setAdapter(indAdapter);
                    indAdapter.notifyDataSetChanged();
                }
                else{
                    indUAdapter = new IndServiceRequestAdapterUser(CheckIndividualPost.this, isrList);
                    lv_Indcheckpost.setAdapter(indUAdapter);
                    indUAdapter.notifyDataSetChanged();
                }
            }
            else{
                loader_chekInd_post.setVisibility(View.GONE);

            }
            loader_chekInd_post.setVisibility(View.GONE);
        }
    }
}
