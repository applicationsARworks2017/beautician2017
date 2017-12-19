package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import beautician.com.sapplication.R;

public class ForgotPassword extends AppCompatActivity {
    Button resetpassword;
    String page;

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
        resetpassword=(Button)findViewById(R.id.resetpassword);
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgotPassword.this,"Mail will be sent to mail id",Toast.LENGTH_SHORT);
                finish();
            }
        });
    }
}
