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

import beautician.com.sapplication.Adapter.SubcategoriesAdapter;
import beautician.com.sapplication.Pojo.SubCategoryList;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class Subcategories extends AppCompatActivity {
    private String category_id;
    private ListView lv_subcategory;
    private SwipeRefreshLayout swipe_subcategory;
    ArrayList<SubCategoryList> scList;
    ProgressBar loader_sub_category;
    int server_status;
    String server_message;
    TextView blank_text_sc;
    SubcategoriesAdapter scadapter;
    RelativeLayout rel_subcategory;
    SearchView searchView_sub_category;
    String shop_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategories);
        category_id=Categories.category_id;
        scList=new ArrayList<>();
        shop_id = Subcategories.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        swipe_subcategory=(SwipeRefreshLayout)findViewById(R.id.swipe_subcategory);
        blank_text_sc=(TextView)findViewById(R.id.blank_text_sc);
        lv_subcategory=(ListView)findViewById(R.id.lv_sub_category);
        searchView_sub_category=(SearchView)findViewById(R.id.searchView_sub_category);
        loader_sub_category=(ProgressBar)findViewById(R.id.loader_subcategory);
        rel_subcategory=(RelativeLayout)findViewById(R.id.rel_subcategory);
        if(CheckInternet.getNetworkConnectivityStatus(this)){
            new getsubcategoryList().execute();
        }
        else{
            Constants.noInternetDialouge(this,"Kindly Check Your Internet Connection");
        }

        swipe_subcategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_subcategory.setRefreshing(false);

                if(CheckInternet.getNetworkConnectivityStatus(Subcategories.this)){
                    new getsubcategoryList().execute();
                }
                else{
                    Constants.noInternetDialouge(Subcategories .this,"Kindly Check Your Internet Connection");
                }
            }
        });
        //*** setOnQueryTextListener ***
        searchView_sub_category.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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


    }

    private void setQuestionList(String filterText) {

        final ArrayList<SubCategoryList> flatlist_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < scList .size(); i++) {
                String q_title = scList.get(i).getSubcategory();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    flatlist_search.add(scList.get(i));
                }
            }
        }else{
            flatlist_search.addAll(scList);
        }
        // create an Object for Adapter
        scadapter = new SubcategoriesAdapter(Subcategories.this,flatlist_search );
        lv_subcategory.setAdapter(scadapter);
        //  mAdapter.notifyDataSetChanged();


        if (scList.isEmpty()) {
            swipe_subcategory.setVisibility(View.GONE);
            blank_text_sc.setVisibility(View.VISIBLE);
        } else {
            swipe_subcategory.setVisibility(View.VISIBLE);
            blank_text_sc.setVisibility(View.GONE);
        }

        scadapter.notifyDataSetChanged();
    }
    /*
* GET SUBCATEGORY LIST ASYNTASK*/
    private class getsubcategoryList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "GET SUB CATEGORY ";

        @Override
        protected void onPreExecute() {
            swipe_subcategory.setVisibility(View.GONE);
            loader_sub_category.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.SUB_CATEGORYLIST;
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
                        .appendQueryParameter("category_id", Categories.category_id)
                        .appendQueryParameter("shop_id", shop_id);
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
    "subCategories": [
        {
            "id": 1,
            "category_id": 1,
            "title": "first sub category",
            "is_enable": "Y",
            "created": "30-10-2017",
            "modified": null
        },
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    scList.clear();
                    JSONArray categoryListArray = res.getJSONArray("subCategories");
                    if(categoryListArray.length()<=0){
                        server_message="No Data Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < categoryListArray.length(); i++) {
                            JSONObject o_list_obj = categoryListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String subcategory = o_list_obj.getString("title");
                            String category_id = o_list_obj.getString("category_id");
                            SubCategoryList list1 = new SubCategoryList(id,subcategory,category_id);
                            scList.add(list1);
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
                scadapter = new SubcategoriesAdapter(Subcategories.this,scList );
                lv_subcategory.setAdapter(scadapter);
            }
            else{
                lv_subcategory.setVisibility(View.GONE);
                blank_text_sc.setVisibility(View.VISIBLE);
                Snackbar snackbar = Snackbar
                        .make(rel_subcategory, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            loader_sub_category.setVisibility(View.GONE);
            swipe_subcategory.setVisibility(View.VISIBLE);
        }
    }
    public void onBackPressed() {
        finish();
    }

}
