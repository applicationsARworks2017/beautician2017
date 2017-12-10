package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import beautician.com.sapplication.Adapter.CategoryAdapter;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class SearchShop extends AppCompatActivity {
    EditText et_searchText;
    Button bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_search_shop);
        et_searchText=(EditText)findViewById(R.id.et_searchText);
        bt_search=(Button)findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_searchText.getText().toString().trim().length()<=0){
                    Toast.makeText(SearchShop.this,"Please enter text for search",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(CheckInternet.getNetworkConnectivityStatus(SearchShop.this)){
                    Intent intent=new Intent(SearchShop.this,SearchShopList.class);
                        intent.putExtra("SEARCH",et_searchText.getText().toString().trim());
                    startActivity(intent);
                    }
                    else{
                        Constants.noInternetDialouge(SearchShop.this,"No Internet");
                    }

                   /* Intent intent=new Intent(SearchShop.this,SearchShopList.class);
                    startActivity(intent);*/
                }
            }
        });
    }



}
