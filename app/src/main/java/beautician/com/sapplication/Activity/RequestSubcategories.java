package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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

import beautician.com.sapplication.Adapter.ReqSubcategoriesAdapter;
import beautician.com.sapplication.Pojo.SubCategoryList;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class RequestSubcategories extends AppCompatActivity {
    private String category_id,categoryname;
    private ListView lv_subcategory;
    private SwipeRefreshLayout swipe_subcategory;
    ArrayList<SubCategoryList> scList;
    ProgressBar loader_sub_category;
    int server_status;
    String server_message;
    TextView blank_text_sc,cattext;
    ReqSubcategoriesAdapter scadapter;
    RelativeLayout rel_subcategory;
    SearchView searchView_sub_category;
    String shop_id;
    Button bt_ok,bt_cancel;
    public static String subcateryName,SubcateryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_request_subcategories);
        category_id=CategoriesRequest.catid;
        categoryname=CategoriesRequest.value;
        scList=new ArrayList<>();
        shop_id = RequestSubcategories.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
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
            new getsubcategoryList().execute();
        }
        else{
            Constants.noInternetDialouge(this,"Kindly Check Your Internet Connection");
        }

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestSubcategories.this.finish();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    StringBuffer sb = new StringBuffer();
                    StringBuffer sb_id = new StringBuffer();

                    for (SubCategoryList bean : scList) {
                        /*if (counter<5) {
                            counter++;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Only five please", Toast.LENGTH_SHORT).show();
                        }*/
                        if (bean.isSelected()) {
                            if (sb.toString().trim().contains(bean.getSubcategory())) {

                            } else {
                                sb.append(bean.getSubcategory());
                                sb.append(",");
                                sb_id.append(bean.getId());
                                sb_id.append(",");
                            }
                        }
                        if (sb.length() <= 0) {
                            //SPSignup.et_category.setText("");
                            RequestSubcategories.this.finish();
                        } else {
                            //SPSignup.et_category.setText(sb.toString().trim().substring(0, sb.length() - 1));
                            //SPSignup.selected_category_id=sb_id.toString().trim().substring(0,sb_id.length()-1);
                            subcateryName=sb.toString().trim().substring(0,sb.length()-1);
                            SubcateryId=sb_id.toString().trim().substring(0,sb_id.length()-1);


                        }

                    }




            }
        });

        swipe_subcategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_subcategory.setRefreshing(false);

                if(CheckInternet.getNetworkConnectivityStatus(RequestSubcategories.this)){
                    new getsubcategoryList().execute();
                }
                else{
                    Constants.noInternetDialouge(RequestSubcategories .this,"Kindly Check Your Internet Connection");
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
        lv_subcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategoryList users= (SubCategoryList) parent.getItemAtPosition(position);
                //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
                SubcateryId = users.getId();
                subcateryName = users.getSubcategory();
                callSub();
            }
        });


    }

    private void callSub() {
        Intent intent=new Intent(RequestSubcategories.this,PostActivity.class);
        startActivity(intent);
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
        scadapter = new ReqSubcategoriesAdapter(RequestSubcategories.this,flatlist_search );
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
                        .appendQueryParameter("category_id", CategoriesRequest.catid);
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
                scadapter = new ReqSubcategoriesAdapter(RequestSubcategories.this,scList );
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
    @Override
    public void onBackPressed()
    {
        finish();
    }
    }

