package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class SearchShop extends AppCompatActivity {
    EditText et_searchText;
    TextView note;
    Button bt_search,bt_all;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_search_shop);

        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        et_searchText=(EditText)findViewById(R.id.et_searchText);
        bt_search=(Button)findViewById(R.id.bt_search);
        bt_all=(Button)findViewById(R.id.bt_all);
        note=(TextView) findViewById(R.id.note);
        if(lang.contentEquals("Arabic")){
            et_searchText.setHint("البحث عن المحلات التجارية");
            bt_search.setText("بحث");
            bt_all.setText("الكل");
            note.setText("البحث عن مقدمي الخدمة سوف يكون على نطاق 100 كم من موقعك الحالي");
            setTitle("بحث");
        }
        else{
            et_searchText.setHint("Search for shops");
            bt_search.setText("Search");
            bt_all.setText("All");
            note.setText("Search can be possible for the shops who are within 100 KM radius");
            setTitle("Search");

        }
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lang.contentEquals("Arabic")){
                    if (et_searchText.getText().toString().trim().length()<=0){
                        Toast.makeText(SearchShop.this,"الرجاء إدخال النص للبحث ",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(CheckInternet.getNetworkConnectivityStatus(SearchShop.this)){
                            Intent intent=new Intent(SearchShop.this,SearchShopList.class);
                            intent.putExtra("SEARCH",et_searchText.getText().toString().trim());
                            startActivity(intent);
                        }
                        else{
                            Constants.noInternetDialouge(SearchShop.this,"لا انترنت");
                        }

                   /* Intent intent=new Intent(SearchShop.this,SearchShopList.class);
                    startActivity(intent);*/
                    }
                }
                else{
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


            }
        });
        bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.getNetworkConnectivityStatus(SearchShop.this)){
                    Intent intent=new Intent(SearchShop.this,SearchShopList.class);
                    intent.putExtra("SEARCH","");
                    startActivity(intent);
                }
                else{
                    if(lang.contentEquals("Arabic")) {
                        Constants.noInternetDialouge(SearchShop.this,"لا انترنت");
                    }
                    else{
                        Constants.noInternetDialouge(SearchShop.this, "No Internet");
                    }
                }
            }
        });
    }



}
