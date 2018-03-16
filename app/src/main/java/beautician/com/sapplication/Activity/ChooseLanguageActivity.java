package beautician.com.sapplication.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import beautician.com.sapplication.Pojo.SpList;
import beautician.com.sapplication.Pojo.User;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

import static beautician.com.sapplication.Utils.Constants.hasPermissions;

/**
 * Created by mobileapplication on 3/1/18.
 */

public class ChooseLanguageActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    RadioButton english, arabic, radio_consumer, radio_sp;
    TextView tv_english,tv_arabic;
    RadioGroup radio_user_type;
    RadioGroup user_type;
    Button btn_contn;
    ProgressBar login_loader;
    RadioButton radioButton;
    ArrayList<User> userlist;
    ArrayList<SpList> splist;
    String langg="";
    String lang;
    LinearLayout tv_english1,tv_arabic1;
    RelativeLayout login_rel;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang);

        tv_arabic=(TextView)findViewById(R.id.tv_arabic);
        tv_english=(TextView)findViewById(R.id.tv_english);
        tv_english1=(LinearLayout) findViewById(R.id.tv_english1);
        tv_arabic1=(LinearLayout) findViewById(R.id.tv_arabic1);
        btn_contn=(Button)findViewById(R.id.btn_contn);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // here it is checking whether the permission is granted previously or not
            if (!hasPermissions(this, PERMISSIONS)) {
                //Permission is granted
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

            }
        }
        tv_english1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                langg="english";
                tv_english1.setBackgroundResource(R.drawable.border1);
                tv_arabic1.setBackgroundResource(R.drawable.border);
                tv_arabic.setTextColor(Color.parseColor("#000000"));
                tv_english.setTextColor(Color.parseColor("#ffffff"));
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLanguageActivity.this);
                builder.setTitle("Choose Language");
                builder.setMessage("Do you want to change the Language");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //check own balance
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                lang = "English";
                SharedPreferences sharedPreferences = ChooseLanguageActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.LANG_TYPE, lang);
                editor.commit();

            }
        });

        tv_arabic1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                langg="arabic";
                tv_english1.setBackgroundResource(R.drawable.border);
                tv_arabic1.setBackgroundResource(R.drawable.border1);

                tv_english.setTextColor(Color.parseColor("#000000"));
                tv_arabic.setTextColor(Color.parseColor("#ffffff"));

                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLanguageActivity.this);
                builder.setTitle("Choose Language");
                builder.setMessage("Do you want to change the Language");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //check own balance
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                lang = "Arabic";
                SharedPreferences sharedPreferences = ChooseLanguageActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.LANG_TYPE, lang);
                editor.commit();


            }
        });
        btn_contn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(langg.contentEquals("")||langg==null){
                    Toast.makeText(getApplicationContext(),"Please Choose Your Language",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(ChooseLanguageActivity.this, Login_Activity.class);
                    startActivity(i);
                }
            }
        });

    }
}

