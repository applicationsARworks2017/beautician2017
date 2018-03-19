package beautician.com.sapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

public class ForgotPassword extends AppCompatActivity {
    Button resetpassword;
    EditText email_id,phone_nmbr;
    String page,lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            page = extras.getString("PAGE");

        }
        if(page.contentEquals("sp")){
            super.setTheme(R.style.AppTheme);
        }
        else{
            super.setTheme(R.style.AppUserTheme);

        }
        setContentView(R.layout.activity_forgot_password);
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        email_id=(EditText)findViewById(R.id.email_id);
        phone_nmbr=(EditText)findViewById(R.id.phn_nmbr);
        resetpassword=(Button)findViewById(R.id.resetpassword);
        if(lang.contentEquals("Arabic")){
           email_id.setHint("البريد الإلكتروني");
           phone_nmbr.setHint("رقم الجوال");
        }
        else{
            email_id.setHint("Email id");
            phone_nmbr.setHint("Phone Number");
        }
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgotPassword.this,"Mail will be sent to mail id",Toast.LENGTH_SHORT);
                finish();
            }
        });
    }
}
