package beautician.com.sapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.APIManager;
import beautician.com.sapplication.Utils.Constants;

public class SPHome extends AppCompatActivity {
    CardView card_manageservice, card_profile, card_checkPost, card_proposal, card_mywallet, card_offer;
    String lang,user_id;
    TextView check_post, profilee, manage_offers, my_wallet, manage_service, my_proposal;
    public static String user_type;
    public static double min_sp_balance = 0.0;
    public static double min_service_charge = 5.0;
    RelativeLayout prop_not_sp, post_not_sp, ser_not_sp, wal_not_sp;
    TextView prop_txt_sp, post_txt_sp, ser_txt_sp, wal_txt_sp;
    private int propsal_req,wallet_req;
    public static  int ServiceIndivisualRequest,service_public;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphome);
        lang = SPHome.this.getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);
        user_id = SPHome.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        if (lang.contentEquals("Arabic")) {
            setTitle("لوحة القيادة");
        } else {
            setTitle("Dashboard");
        }
        getAllNotification();
        card_manageservice = (CardView) findViewById(R.id.card_manageservice);
        card_profile = (CardView) findViewById(R.id.card_profile);
        card_checkPost = (CardView) findViewById(R.id.checkPost);
        card_proposal = (CardView) findViewById(R.id.card_proposal);
        card_mywallet = (CardView) findViewById(R.id.card_mywallet);
        card_offer = (CardView) findViewById(R.id.card_offer);

        check_post = (TextView) findViewById(R.id.check_post);
        my_proposal = (TextView) findViewById(R.id.my_proposal);
        manage_service = (TextView) findViewById(R.id.manage_service);
        my_wallet = (TextView) findViewById(R.id.my_wallet);
        manage_offers = (TextView) findViewById(R.id.manage_offers);
        profilee = (TextView) findViewById(R.id.profilee);

        prop_txt_sp = (TextView) findViewById(R.id.prop_txt_sp);
        post_txt_sp = (TextView) findViewById(R.id.post_txt_sp);
        ser_txt_sp = (TextView) findViewById(R.id.ser_txt_sp);
        wal_txt_sp = (TextView) findViewById(R.id.wal_txt_sp);


        prop_not_sp = (RelativeLayout) findViewById(R.id.prop_not_sp);
        post_not_sp = (RelativeLayout) findViewById(R.id.post_not_sp);
        ser_not_sp = (RelativeLayout) findViewById(R.id.ser_not_sp);
        wal_not_sp = (RelativeLayout) findViewById(R.id.wal_not_sp);

        prop_not_sp.setVisibility(View.GONE);
        post_not_sp.setVisibility(View.GONE);
        ser_not_sp.setVisibility(View.GONE);
        wal_not_sp.setVisibility(View.GONE);

        if (lang.contentEquals("Arabic")) {
            check_post.setText(("تحقق من المشاركات"));
            my_proposal.setText(("مقترحاتي"));
            manage_service.setText(("إدارة الخدمات"));
            my_wallet.setText(("محفظتى"));
            manage_offers.setText(("إدارة العروض"));
            profilee.setText(("الملف الشخصي"));
        } else {
            check_post.setText(("Check Posts"));
            my_proposal.setText(("My Proposals"));
            manage_service.setText(("Manage Services"));
            my_wallet.setText(("My Wallet"));
            manage_offers.setText(("Manage Offers"));
            profilee.setText(("Profile"));

        }
        card_manageservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(SPHome.this,Categories.class);
                Intent intent = new Intent(SPHome.this, Manageservice.class);
                intent.putExtra("LANG", lang);
                startActivity(intent);
            }
        });

        card_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SPHome.this, SPProfile.class);
                intent.putExtra("LANG", lang);
                startActivity(intent);
            }
        });
        card_checkPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SPHome.this, SpRequestHome.class);
                intent.putExtra("LANG", lang);
                intent.putExtra("PAGE", "sp_home");
                startActivity(intent);

            }
        });
        card_proposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToAPI(user_id,"ServicePurposal","Shop");
                Intent intent = new Intent(SPHome.this, SpProposal.class);
                intent.putExtra("LANG", lang);
                intent.putExtra("PAGE", "sp_home");
                startActivity(intent);
            }
        });
        card_mywallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToAPI(user_id,"Wallet","Shop");
                Intent intent = new Intent(SPHome.this, Wallet.class);
                intent.putExtra("LANG", lang);
                intent.putExtra("PAGE", "sp_home");
                startActivity(intent);
            }
        });
        card_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SPHome.this, OfferSet.class);
                intent.putExtra("LANG", lang);
                intent.putExtra("PAGE", "sp_home");
                startActivity(intent);
            }
        });
    }

    private void CallToAPI(String user_id, String wallet, String shop) {


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("reference_id", user_id);
            jsonObject.put("type", wallet);
            jsonObject.put("reference_type", shop);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIManager().ModifyAPI(Constants.ONLINEURL + Constants.UPDATE_COUNT, "res", jsonObject, SPHome.this, new APIManager.APIManagerInterface() {
            @Override
            public void onSuccess(Object resultObj) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getAllNotification() {
        String reference_id= user_id;
        String reference_type= "Shop";
        String is_read= "0";

        new ShowSPNotification().execute(reference_id,reference_type,is_read);
    }

    private class ShowSPNotification extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;

        int server_status;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _reference_id = params[0];
                String _reference_type = params[1];
                String _is_read = params[2];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.ONLINEURL + Constants.SHOP_NOTIFICATION_COUNT;
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
                        .appendQueryParameter("reference_id", _reference_id)
                        .appendQueryParameter("reference_type", _reference_type)
                        .appendQueryParameter("is_read", _is_read);

                //.appendQueryParameter("deviceid", deviceid);
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
                * "notificationType": {
        "Wallet": 0,
        "ServiceIndivisualRequest": "1",
        "ServicePurposal": 0,
        "ServiceRequest": "5",
        "UserWallet": 0
    },
    "res": {
        "message": "available",
        "status": 1
    }
                * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj = res.getJSONObject("res");
                    server_status=j_obj.getInt("status");
                    if(server_status==1) {
                        JSONObject j_obj1 = res.getJSONObject("notificationType");
                        wallet_req=j_obj1.getInt("Wallet");
                        ServiceIndivisualRequest=j_obj1.getInt("ServiceIndivisualRequest");
                        propsal_req=j_obj1.getInt("ServicePurposal");
                        service_public=j_obj1.getInt("ServiceRequest");

                    }

                }
                return null;
            } catch (Exception exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(wallet_req>0){
                wal_not_sp.setVisibility(View.VISIBLE);
                wal_txt_sp.setText(String.valueOf(wallet_req));
            }
            if(ServiceIndivisualRequest >0 || service_public > 0){
                post_not_sp.setVisibility(View.VISIBLE);
                post_txt_sp.setText(String.valueOf(ServiceIndivisualRequest+service_public));
            }
            if(propsal_req > 0){
                prop_not_sp.setVisibility(View.VISIBLE);
                prop_txt_sp.setText(String.valueOf(propsal_req));
            }
        }
    }
}
