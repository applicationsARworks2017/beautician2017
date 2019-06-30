package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import beautician.com.sapplication.Adapter.ListSubcategoriesAdapter;
import beautician.com.sapplication.Adapter.ShopListAdapter;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.SubCategoryList;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class MySubcategories extends AppCompatActivity {
    String user_id,page;
    private String category_id,categoryname;
    private ListView lv_subcategory;
    private SwipeRefreshLayout swipe_subcategory;
    ArrayList<SubCategoryList> scList;
    ProgressBar loader_sub_category;
    int server_status;
    String server_message;
    TextView blank_text_sc,cattext;
    ListSubcategoriesAdapter scadapter;
    RelativeLayout rel_subcategory;
    SearchView searchView_sub_category;
    String shop_id,lang,user_type,user_typee;
    Button bt_ok,bt_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subcategories);
        setTitle("");
        lang =getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("USERID");
            page = extras.getString("PAGE");
        }
        category_id=MyserviceList.catid;
        user_type=getSharedPreferences(Constants.SHAREDPREFERENCE_BEAUTICIAN, 0).getString(Constants.BEAUTICIAN_TYPE, null);

        categoryname=MyserviceList.value;
        scList=new ArrayList<>();
        cattext=(TextView)findViewById(R.id.cattext);
        cattext.setText(categoryname);
        swipe_subcategory=(SwipeRefreshLayout)findViewById(R.id.swipe_subcategory);
        blank_text_sc=(TextView)findViewById(R.id.blank_text_sc);
        lv_subcategory=(ListView)findViewById(R.id.lv_sub_category);
        searchView_sub_category=(SearchView)findViewById(R.id.searchView_sub_category);
        loader_sub_category=(ProgressBar)findViewById(R.id.loader_subcategory);
        rel_subcategory=(RelativeLayout)findViewById(R.id.rel_subcategory);
        bt_ok=(Button)findViewById(R.id.bt_ok);
        bt_cancel=(Button)findViewById(R.id.bt_cancel);
        if(CheckInternet.getNetworkConnectivityStatus(this)){
            scList = new ArrayList<>();
            new getsubcategoryList().execute();
        }
        else{
            Constants.noInternetDialouge(this,"Kindly Check Your Internet Connection");
        }

        lv_subcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(page.contentEquals("user")) {
                    String detail;
                    SubCategoryList users = (SubCategoryList) parent.getItemAtPosition(position);
                    //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MySubcategories.this,IndividualRequest.class);
                    intent.putExtra("SHOP_ID", ShopListAdapter.selected_shopid);
                    intent.putExtra("SHOP_NAME",ShopListAdapter.selected_shop_name);
                    if(lang.contentEquals("Arabic")){
                        intent.putExtra("DETAILS",users.getArabic_title());

                    }
                    else{
                        intent.putExtra("DETAILS",users.getSubcategory());
                    }
                    startActivity(intent);
                }
            }
        });

        swipe_subcategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_subcategory.setRefreshing(false);

                if(CheckInternet.getNetworkConnectivityStatus(MySubcategories.this)){
                    new getsubcategoryList().execute();
                }
                else{
                    Constants.noInternetDialouge(MySubcategories .this,"Kindly Check Your Internet Connection");
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
        scadapter = new ListSubcategoriesAdapter(MySubcategories.this,flatlist_search,lang,user_type );
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
                String link = Constants.ONLINEURL + Constants.SUBCATEGORYLIST_SHOPWISE;
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
                        .appendQueryParameter("category_id",MyserviceList.catid)
                        .appendQueryParameter("shop_id",user_id);
                        /*.appendQueryParameter("shop_id", shop_id)
                        .appendQueryParameter("page", "1");*/
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
           "id": 3,
            "title": "Hair Cut",
            "price_id": 76,
            "arabic_title": null,
            "price": 50"id": 3,
            "title": "Hair Cut",
            "price_id": 76,
            "arabic_title": null,
            "price": 50
        },
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    scList.clear();
                    JSONArray categoryListArray = res.getJSONArray("SubCategories");
                    if(categoryListArray.length()<=0){
                        server_message="No Data Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < categoryListArray.length(); i++) {
                            JSONObject o_list_obj = categoryListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String subcategory = o_list_obj.getString("title");
                            String price = o_list_obj.getString("price");
                            String arabic_title = o_list_obj.getString("arabic_title");
                            String price_id = o_list_obj.getString("price_id");
                            SubCategoryList list1 = new SubCategoryList(id,subcategory,category_id,price,arabic_title,price_id);
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
                scadapter = new ListSubcategoriesAdapter(MySubcategories.this,scList,lang,user_type );
                lv_subcategory.setAdapter(scadapter);
            }
            else{
                lv_subcategory.setVisibility(View.GONE);
                blank_text_sc.setVisibility(View.VISIBLE);
                /*Snackbar snackbar = Snackbar
                        .make(rel_subcategory, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();*/
            }
            loader_sub_category.setVisibility(View.GONE);
            swipe_subcategory.setVisibility(View.VISIBLE);
        }
    }
   /* @Override
    public void onBackPressed()
    {
        finish();
    }*/
}

