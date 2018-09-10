package beautician.com.sapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;
import paytabs.project.PayTabActivity;

public class PaymentConfirmation extends AppCompatActivity {
    String page,lang,u_name,u_phone,u_mail,amout;
    Double avail_amount;
    public static EditText et_name,et_phone,et_email,et_amount,et_address,et_city,et_state,et_country,et_postal;
    Button bt_ok,bt_cancel;
    RelativeLayout con_rel;
    TextView pay_name,pay_phone,pay_mail,pay_amount,pay_add,pay_city,pay_state,pay_country,pay_code;
    String MERCHANT_EMAIL ="mansour.nz@outlook.com";
    //Merchant Secret Key
    String own_id;
    String lang_to_send="en";
    int responsecode=0;
    String MERCHANT_SECRET_KEY="yVvwlwTrcV8LEVrGSBTwU9X8X4e1JHMiCiOYOIq0EyMiudZmPTTJ3RS0V61FOOXr7LpWuPW8oJrSXqfMk9wn0pnWvpBkezLmlbiP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_payment_confirmation);

        if (extras != null) {
            page = extras.getString("PAGE");
            u_name = extras.getString("NAME");
            u_phone = extras.getString("PHONE");
            u_mail = extras.getString("MAIL");
            amout = extras.getString("AMOUNT");
            avail_amount = extras.getDouble("AVAIL_AMOUNT");


            // and get whatever type user account id is
        }
        if(page.contentEquals("user_side")){
            super.setTheme(R.style.AppUserTheme);
        }

        own_id = PaymentConfirmation.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);


        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        et_name=(EditText)findViewById(R.id.et_user_name);
        et_phone=(EditText)findViewById(R.id.et_user_phone);
        et_email=(EditText)findViewById(R.id.et_user_email);
        et_amount=(EditText)findViewById(R.id.et_user_amount);
        et_address=(EditText)findViewById(R.id.et_user_address);
        et_city=(EditText)findViewById(R.id.et_user_city);
        et_state=(EditText)findViewById(R.id.et_user_state);
        et_country=(EditText)findViewById(R.id.et_user_country);


        pay_name=(TextView)findViewById(R.id.pay_name);
        pay_phone=(TextView)findViewById(R.id.pay_phone);
        pay_mail=(TextView)findViewById(R.id.pay_mail);
        pay_amount=(TextView)findViewById(R.id.pay_amount);
        pay_add=(TextView)findViewById(R.id.pay_add);
        pay_city=(TextView)findViewById(R.id.pay_city);
        pay_state=(TextView)findViewById(R.id.pay_state);
        pay_country=(TextView)findViewById(R.id.pay_country);
        pay_code=(TextView)findViewById(R.id.pay_code);

        bt_ok=(Button)findViewById(R.id.bt_ok);
        bt_cancel=(Button)findViewById(R.id.bt_cancel);



        if(lang.contentEquals("Arabic")){
            setTitle("تأكيد الدفع");
            lang_to_send="ar";
            pay_name.setText(R.string.name_ar);
            pay_phone.setText(R.string.mobile_ar);
            pay_mail.setText(R.string.mail_ar);
            pay_amount.setText(R.string.amount_ar);
            pay_add.setText(R.string.address_ar);
            pay_city.setText(R.string.city_ar);
            pay_state.setText(R.string.state_ar);
            pay_country.setText(R.string.country_ar);
            pay_code.setText("الرقم الدولي");
            bt_ok.setText("حسنا");
            bt_cancel.setText("إلغاء");
        }
        else{
            setTitle("Payment Confirmation");
            lang_to_send="en";
            pay_name.setText("Name");
            pay_phone.setText("Phone");
            pay_mail.setText("Mail");
            pay_amount.setText("Amount");
            pay_add.setText("Address");
            pay_city.setText("City");
            pay_state.setText("State");
            pay_country.setText("Country");
            pay_code.setText("Country Code");
            bt_ok.setText("OK");
            bt_cancel.setText("Cancel");
        }


        con_rel=(RelativeLayout)findViewById(R.id.con_rel);

        et_postal=(EditText)findViewById(R.id.et_user_postal);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentConfirmation.this.finish();
            }
        });
        et_name.setText(u_name);
        et_phone.setText(u_phone);
        et_email.setText(u_mail);
        et_amount.setText(amout);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
        et_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(PaymentConfirmation.this,CountryList.class);
                startActivity(intent);
            }
        });
    }

    private void checkValidation() {
        //    EditText et_name,et_phone,et_email,et_amount,et_address,et_city,et_state,et_country,et_postal;
        if(et_name.getText().toString().trim().length()<=0){
            showSnackBar("Enter Name");
        }
        else if(et_phone.getText().toString().trim().length()<=0){
            showSnackBar("Enter Phone");
        }else if(et_email.getText().toString().trim().length()<=0){
            showSnackBar("Enter Mail");
        }else if(et_amount.getText().toString().trim().length()<=0){
            showSnackBar("Enter Amount");
        }else if(et_address.getText().toString().trim().length()<=0){
            showSnackBar("Enter Address");
        }else if(et_city.getText().toString().trim().length()<=0){
            showSnackBar("Enter City");
        }else if(et_state.getText().toString().trim().length()<=0){
            showSnackBar("Enter State");
        }else if(et_country.getText().toString().trim().length()<=0){
            showSnackBar("Enter Country");
        }else if(et_postal.getText().toString().trim().length()<=0){
            showSnackBar("Enter Country Code");
        }
        else{

            String name=et_name.getText().toString().trim();
            String phone=et_phone.getText().toString().trim();
            String email=et_email.getText().toString().trim();
            String amount=et_amount.getText().toString().trim();
            String address=et_address.getText().toString().trim();
            String city=et_city.getText().toString().trim();
            String state=et_state.getText().toString().trim();
            String country=et_country.getText().toString().trim();
            String postal=et_postal.getText().toString().trim();

            if(CheckInternet.getNetworkConnectivityStatus(PaymentConfirmation.this)){
                Intent in=new Intent(PaymentConfirmation.this, PayTabActivity.class);
                in.putExtra("pt_merchant_email", MERCHANT_EMAIL);
                in.putExtra("pt_secret_key", MERCHANT_SECRET_KEY);//Add your Secret Key Here
                in.putExtra("pt_lang", lang_to_send);//For language change
                //in.putExtra("pt_lang", "ar");//For language change
                in.putExtra("pt_transaction_title", "Payment towards Beautician");
                in.putExtra("pt_amount", amount);
                in.putExtra("pt_currency_code", "SAR"); //Use Standard 3 character ISO
                in.putExtra("pt_shared_prefs_name", "myapp_shared");
                in.putExtra("pt_customer_email", email);
                in.putExtra("pt_customer_phone_number", phone);
                in.putExtra("pt_order_id", "1234567");
                in.putExtra("pt_product_name", "Membership");
                in.putExtra("pt_timeout_in_seconds", "300"); //Optional
//Billing Address
                in.putExtra("pt_address_billing", address);
                in.putExtra("pt_city_billing", city);
                in.putExtra("pt_state_billing", state);
                in.putExtra("pt_country_billing", country);
                in.putExtra("pt_postal_code_billing", postal); //Put Country Phone code if Postal
//Shipping Address
                in.putExtra("pt_address_shipping", address);
                in.putExtra("pt_city_shipping", city);
                in.putExtra("pt_state_shipping", state);
                in.putExtra("pt_country_shipping", country);
                in.putExtra("pt_postal_code_shipping", postal); //Put Country Phone code if Postal
                int requestCode = 0;
                startActivityForResult(in, requestCode);            }
            else{
                showSnackBar("Check Internet");
            }
        }
    }

    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(con_rel, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#7a2da6"));

        snackbar.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences shared_prefs = getSharedPreferences("myapp_shared", MODE_PRIVATE);
        String pt_response_code = shared_prefs.getString("pt_response_code", "");
        String pt_transaction_id = shared_prefs.getString("pt_transaction_id", "");
        if(pt_response_code!=null || !pt_response_code.contentEquals("")) {
            responsecode = Integer.valueOf(pt_response_code.trim());
        }
        else{
            responsecode=2;
        }
        /*if(lang.contentEquals("Arabic")){
            Toast.makeText(PaymentConfirmation.this, "رقم عملية الدفع : " + pt_response_code,
                    Toast.LENGTH_LONG).show();
            Toast.makeText(PaymentConfirmation.this, ": رقم عملية الدفع " +
                    pt_transaction_id, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(PaymentConfirmation.this, "PayTabs Response Code : " + pt_response_code,
                    Toast.LENGTH_LONG).show();
            Toast.makeText(PaymentConfirmation.this, "Paytabs transaction ID after payment : " +
                    pt_transaction_id, Toast.LENGTH_LONG).show();
        }*/
        if(responsecode==100){
            updateBalance();
        }
        else if(responsecode==2){
            Toast.makeText(PaymentConfirmation.this, "App Error : "+pt_response_code.trim(),
                    Toast.LENGTH_LONG).show();
        }
        else{
            if(lang.contentEquals("Arabic")){
                Toast.makeText(PaymentConfirmation.this, "كود الاستجابة PayTabs : " + pt_response_code,
                        Toast.LENGTH_LONG).show();
                Toast.makeText(PaymentConfirmation.this, ": Paytabs معرف المعاملة بعد الدفع  " +
                        pt_transaction_id, Toast.LENGTH_LONG).show();
            }
            else{https://www.myntra.com/tshirts/mufti/mufti-blue-slim-fit-henley-t-shirt/1444971/buy
                Toast.makeText(PaymentConfirmation.this, "PayTabs Response Code : " + pt_response_code,
                        Toast.LENGTH_LONG).show();
                Toast.makeText(PaymentConfirmation.this, "Paytabs transaction ID after payment : " +
                        pt_transaction_id, Toast.LENGTH_LONG).show();
            }
        }

    }
    private void updateBalance() {
        Double update_amount= Double.valueOf(et_amount.getText().toString().trim());
        if(page.contentEquals("user_side")) {
            Transactwallet transactwallet = new Transactwallet();
            transactwallet.execute(own_id, String.valueOf(update_amount), String.valueOf(avail_amount+update_amount), "0");
        }
        else if(page.contentEquals("sp_home")){
            Transactwallet transactwallet = new Transactwallet();
            transactwallet.execute(own_id,String.valueOf(update_amount), String.valueOf(avail_amount+update_amount), "0");
        }
    }

    private class Transactwallet extends AsyncTask<String, Void, Void> {

        private static final String TAG = "update wallet";
        int wallet_status;
        String server_message;
        ProgressDialog progressDialog=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(PaymentConfirmation.this, "Updating wallet", "Please wait...");
            }
        }
        @Override
        protected Void doInBackground(String... params) {

            try {
                String _userid = params[0];
                String _recharge_amount = params[1];
                String _balance_amount = params[2];
                String _debit_amount = params[3];
                InputStream in = null;
                int resCode = -1;

                String link = null;
                if(page.contentEquals("sp_home")) {
                    link = Constants.ONLINEURL + Constants.SHOP_WALLLET_UPDATE;
                }
                else if(page.contentEquals("user_side")){
                    link = Constants.ONLINEURL + Constants.USER_WALLLET_UPDATE;

                }
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
                            .appendQueryParameter("shop_id", _userid)
                            .appendQueryParameter("debit", _debit_amount)
                            .appendQueryParameter("credit", _recharge_amount)
                            .appendQueryParameter("remarks", "Recharge")
                            .appendQueryParameter("balance", _balance_amount);
                }
                else if(page.contentEquals("user_side")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", _userid)
                            .appendQueryParameter("debit", _debit_amount)
                            .appendQueryParameter("credit", _recharge_amount)
                            .appendQueryParameter("remarks", "Recharge")
                            .appendQueryParameter("balance", _balance_amount);
                }

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
                if(in == null){
                    return null;
                }
                BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "",data="";

                while ((data = reader.readLine()) != null){
                    response += data + "\n";
                }

                Log.i(TAG, "Response : "+response);

                /**
                 * {
                 "status": 1,
                 "message": "Data inserted successfully"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    wallet_status = j_obj.optInt("status");
                    if(wallet_status==1) {
                        if(lang.contentEquals("Arabic")){
                            server_message="تم تحديث المحفظة";

                        }
                        else{
                            server_message="Wallet Updated";

                        }
                    }
                    else{
                        if(lang.contentEquals("Arabic")){
                            server_message="لا يمكن تحديث المحفظة";

                        }
                        else{
                            server_message="Wallet can't be Updated";

                        }

                    }

                }

                return null;
            } catch(Exception exception){
                server_message="Wallet error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if(wallet_status==1){
                Toast.makeText(PaymentConfirmation.this,server_message,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PaymentConfirmation.this,Wallet.class);
                intent.putExtra("PAGE",page);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            else{
                Constants.noInternetDialouge(PaymentConfirmation.this,server_message);
            }
        }
    }
}
