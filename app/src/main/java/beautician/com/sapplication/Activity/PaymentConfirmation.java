package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;
import paytabs.project.PayTabActivity;

public class PaymentConfirmation extends AppCompatActivity {
    String page,lang,u_name,u_phone,u_mail,amout;
    public static EditText et_name,et_phone,et_email,et_amount,et_address,et_city,et_state,et_country,et_postal;
    Button bt_ok,bt_cancel;
    RelativeLayout con_rel;
    String MERCHANT_EMAIL ="mansour.nz@outlook.com";
    //Merchant Secret Key
    String MERCHANT_SECRET_KEY="yVvwlwTrcV8LEVrGSBTwU9X8X4e1JHMiCiOYOIq0EyMiudZmPTTJ3RS0V61FOOXr7LpWuPW8oJrSXqfMk9wn0pnWvpBkezLmlbiP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            page = extras.getString("PAGE");
            u_name = extras.getString("NAME");
            u_phone = extras.getString("PHONE");
            u_mail = extras.getString("MAIL");
            amout = extras.getString("AMOUNT");

            // and get whatever type user account id is
        }
        if(page.contentEquals("user_side")){
            super.setTheme(R.style.AppUserTheme);
        }
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);
        setContentView(R.layout.activity_payment_confirmation);

        con_rel=(RelativeLayout)findViewById(R.id.con_rel);
        et_name=(EditText)findViewById(R.id.et_user_name);
        et_phone=(EditText)findViewById(R.id.et_user_phone);
        et_email=(EditText)findViewById(R.id.et_user_email);
        et_amount=(EditText)findViewById(R.id.et_user_amount);
        et_address=(EditText)findViewById(R.id.et_user_address);
        et_city=(EditText)findViewById(R.id.et_user_city);
        et_state=(EditText)findViewById(R.id.et_user_state);
        et_country=(EditText)findViewById(R.id.et_user_country);
        et_postal=(EditText)findViewById(R.id.et_user_postal);
        bt_ok=(Button)findViewById(R.id.bt_ok);
        bt_cancel=(Button)findViewById(R.id.bt_cancel);
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
            showSnackBar("Enter Country");
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
                in.putExtra("pt_transaction_title", "Payment towards Beautician");
                in.putExtra("pt_amount", amount);
                in.putExtra("pt_currency_code", "USD"); //Use Standard 3 character ISO
                in.putExtra("pt_shared_prefs_name", name);
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
        if(lang.contentEquals("Arabic")){
            Toast.makeText(PaymentConfirmation.this, "كود الاستجابة PayTabs : " + pt_response_code,
                    Toast.LENGTH_LONG).show();
            Toast.makeText(PaymentConfirmation.this, ": Paytabs معرف المعاملة بعد الدفع  " +
                    pt_transaction_id, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(PaymentConfirmation.this, "PayTabs Response Code : " + pt_response_code,
                    Toast.LENGTH_LONG).show();
            Toast.makeText(PaymentConfirmation.this, "Paytabs transaction ID after payment : " +
                    pt_transaction_id, Toast.LENGTH_LONG).show();
        }

    }
}
