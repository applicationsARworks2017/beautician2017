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
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.ArrayList;

import beautician.com.sapplication.Adapter.CategoryAdapter;
import beautician.com.sapplication.Adapter.CountriesAdapter;
import beautician.com.sapplication.Pojo.CategoryList;
import beautician.com.sapplication.Pojo.Countries;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

public class CountryList extends AppCompatActivity {
    ListView countryList;
    SearchView search_country;
    ArrayList<Countries> cList;
    CountriesAdapter cAdapter;
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);
        search_country=(SearchView)findViewById(R.id.search_country);
        countryList=(ListView)findViewById(R.id.countryList);
        cList=new ArrayList<>();
        getCountryList();
        lang =getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);
        if(lang.contentEquals("Arabic")){
            setTitle("قائمة الدول");
        }
        else{
            setTitle("Country List");
        }

        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Countries countries= (Countries) parent.getItemAtPosition(position);
                PaymentConfirmation.et_country.setText(countries.getA3());
                PaymentConfirmation.et_postal.setText(countries.getDialing_code());
                CountryList.this.finish();
            }
        });
        search_country.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        //*** setOnQueryTextListener ***
        search_country.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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

        final ArrayList<Countries> flatlist_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < cList .size(); i++) {
                String q_title = cList.get(i).getCountry();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    flatlist_search.add(cList.get(i));
                }
            }
        }else{
            flatlist_search.addAll(cList);
        }
        // create an Object for Adapter
        cAdapter = new CountriesAdapter(CountryList.this, flatlist_search);
        countryList.setAdapter(cAdapter);
        //  mAdapter.notifyDataSetChanged();
        cAdapter.notifyDataSetChanged();
    }

    private void getCountryList() {
        if(CheckInternet.getNetworkConnectivityStatus(CountryList.this)){
            Getcountries getcountries=new Getcountries();
            getcountries.execute();
        }
        else{
            Toast.makeText(CountryList.this,"No Internet",Toast.LENGTH_SHORT).show();
        }
    }
    /*
* GET COUNTRY LIST ASYNTASK*/
    private class Getcountries extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getcountryList";
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.COUNTRYLIST;
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
"countryCodes": [
        {
            "id": 1,
            "country": "Afghanistan",
            "a2": "AF",
            "a3": "AFG",
            "num": 4,
            "dialing_code": 93
        },}
}
}
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    cList=new ArrayList<>();
                    // server_status=res.getInt("status");
                    JSONArray categoryListArray = res.getJSONArray("countryCodes");
                    if(categoryListArray.length()<=0){
                        server_message="No Country Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < categoryListArray.length(); i++) {
                            JSONObject o_list_obj = categoryListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String country = o_list_obj.getString("country");
                            String a2 = o_list_obj.getString("a2");
                            String a3 = o_list_obj.getString("a3");
                            String dialing_code = o_list_obj.getString("dialing_code");
                            Countries list1 = new Countries(id,country,a2,a3,dialing_code);
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
                cAdapter = new CountriesAdapter(CountryList.this,cList );
                countryList.setAdapter(cAdapter);
            }

        }
    }
}
