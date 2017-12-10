package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import beautician.com.sapplication.Adapter.CategoryRequestAdapter;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class CategoriesRequest extends AppCompatActivity {
    ArrayList<CategoryList> cList;
    private ProgressBar loader_categoty;
    private int server_status;
    private String server_message;
    ListView lv_category;
    TextView blank_text,cattext;
    CategoryRequestAdapter cAdapter;
    SearchView searchView_category;
    public static String catid,value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_categories_request);
        cList=new ArrayList<>();
        loader_categoty=(ProgressBar)findViewById(R.id.loader_category);
        lv_category=(ListView)findViewById(R.id.lv_category);
        blank_text=(TextView) findViewById(R.id.blank_text);
        cattext=(TextView) findViewById(R.id.cattext);
        searchView_category=(SearchView) findViewById(R.id.searchView_category);
        if(CheckInternet.getNetworkConnectivityStatus(this)){
            new getcategoryList().execute();
        }
        else{
            Constants.noInternetDialouge(this,"Kindly Check Your Internet Connection");
        }

        searchView_category.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        //*** setOnQueryTextListener ***
        searchView_category.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setQuestionList(newText);
                return false;
            }
        });
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryList users= (CategoryList) parent.getItemAtPosition(position);
                //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
                 catid = users.getId();
                 value = users.getCategory();
                callSub();
            }
        });


    }

    private void callSub() {
        Intent st=new Intent(CategoriesRequest.this,RequestSubcategories.class);
        startActivity(st);
    }

    private void setQuestionList(String filterText) {

        final ArrayList<CategoryList> flatlist_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < cList .size(); i++) {
                String q_title = cList.get(i).getCategory();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    flatlist_search.add(cList.get(i));
                }
            }
        }else{
            flatlist_search.addAll(cList);
        }
        // create an Object for Adapter
        cAdapter = new CategoryRequestAdapter(CategoriesRequest.this, flatlist_search);
        lv_category.setAdapter(cAdapter);
        //  mAdapter.notifyDataSetChanged();


        if (cList.isEmpty()) {
            lv_category.setVisibility(View.GONE);
            blank_text.setVisibility(View.VISIBLE);
        } else {
            lv_category.setVisibility(View.VISIBLE);
            blank_text.setVisibility(View.GONE);
        }

        cAdapter.notifyDataSetChanged();
    }
    /*
* GET CATEGORY LIST ASYNTASK*/
    private class getcategoryList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getcategoryList";

        @Override
        protected void onPreExecute() {
            loader_categoty.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.CATEGORYLIST;
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
                        .appendQueryParameter("date", "");
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
    "categories": [
        {
            "id": 1,
            "title": "test",
            "is_enable": "Y",
            "created": "30-10-2017",
            "modified": "2017-10-30T18:10:37+00:00"
        },
        {
            "id": 2,
            "title": "other",
            "is_enable": "Y",
            "created": "30-10-2017",
            "modified": "2017-10-30T18:10:47+00:00"
        }
    ]
}
}
}
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray categoryListArray = res.getJSONArray("categories");
                    if(categoryListArray.length()<=0){
                        server_message="No Category Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < categoryListArray.length(); i++) {
                            JSONObject o_list_obj = categoryListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String category = o_list_obj.getString("title");
                            CategoryList list1 = new CategoryList(id,category);
                            cList.add(list1);
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
                cAdapter = new CategoryRequestAdapter(CategoriesRequest.this,cList );
                lv_category.setAdapter(cAdapter);
            }
            else{
                lv_category.setVisibility(View.GONE);
                blank_text.setVisibility(View.VISIBLE);

            }
            loader_categoty.setVisibility(View.GONE);
        }
    }
}
