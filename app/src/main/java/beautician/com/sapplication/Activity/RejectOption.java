package beautician.com.sapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import beautician.com.sapplication.Adapter.RatingspointsAdapter;
import beautician.com.sapplication.Pojo.RatingsPoints;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.APIManager;
import beautician.com.sapplication.Utils.Constants;

public class RejectOption extends AppCompatActivity {
    String rquest_id;
    ArrayList<RatingsPoints> ratingspoints;
    ListView lv_ratings_value;
    RatingspointsAdapter radapter;
    Button cancel_reject,sub_reject;
    String improve,lang;
    TextView selectreason;
    EditText txtreason;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_option);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rquest_id = extras.getString("REQ_ID");

        }
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);


        lv_ratings_value=(ListView)findViewById(R.id.ratings_value);
        selectreason=(TextView) findViewById(R.id.selectreason);
        txtreason=(EditText) findViewById(R.id.txtreason);
        ratingspoints = new ArrayList();
        cancel_reject=(Button)findViewById(R.id.cancel_reject);
        sub_reject=(Button)findViewById(R.id.sub_reject);
        if(lang.contentEquals("Arabic")){
            ratingspoints.add(new RatingsPoints("لايويجد وقت شاغر", false));
            ratingspoints.add(new RatingsPoints("الوقت غير مناسب", false));
            ratingspoints.add(new RatingsPoints("أسباب اخرى", false));
            selectreason.setText(R.string.reasons_booking_ar);
            cancel_reject.setText("إلغاء");
            sub_reject.setText(R.string.ok_ar);
            txtreason.setHint(R.string.reason_ar);

        }
        else {
            ratingspoints.add(new RatingsPoints("Fully booked", false));
            ratingspoints.add(new RatingsPoints("Time isn't suitable", false));
            ratingspoints.add(new RatingsPoints("Other Reasons", false));
            selectreason.setText(R.string.reasons_booking_en);
            cancel_reject.setText("Cancel");
            sub_reject.setText("OK");
            txtreason.setHint(R.string.reason_en);

        }
        radapter = new RatingspointsAdapter(ratingspoints, getApplicationContext());
        lv_ratings_value.setAdapter(radapter);

        cancel_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RejectOption.this.finish();
            }
        });
        sub_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllvalue();

            }
        });
    }

    private void getAllvalue() {

            StringBuffer sb = new StringBuffer();
            for (RatingsPoints bean : ratingspoints) {
                        /*if (counter<5) {
                            counter++;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Only five please", Toast.LENGTH_SHORT).show();
                        }*/
                if (bean.getIschecked()) {
                    if (sb.toString().trim().contains(bean.getPoints())) {

                    } else {
                        sb.append(bean.getPoints());
                        sb.append(",");
                    }
                }
            }
            if (sb.length() <= 0) {
                Toast.makeText(RejectOption.this, "Please check atleast one point", Toast.LENGTH_SHORT).show();

            } else {
                improve = sb.toString().trim().substring(0, sb.length() - 1);
                submit();
            }


    }

    private void submit() {
        if(txtreason.getText().toString().trim().length()>0){
            improve = improve + " : " + txtreason.getText().toString().trim();
        }
        final ProgressDialog pd = new ProgressDialog(RejectOption.this);
        if(lang.contentEquals("Arabic")){
            pd.setTitle("Updating Request");
            pd.setMessage("Please wait...");
        }
        else {
            pd.setTitle("Updating Request");
            pd.setMessage("Please wait...");
        }
        pd.show();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", rquest_id);
            jsonObject.put("reason", improve);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIManager().ModifyAPI(Constants.ONLINEURL + Constants.EDIT_IND_REQUEST, "res", jsonObject, RejectOption.this, new APIManager.APIManagerInterface() {
            @Override
            public void onSuccess(Object resultObj) {
                pd.dismiss();
                Toast.makeText(RejectOption.this,"Reason Added",Toast.LENGTH_SHORT).show();

                // holder1.im_remove.setVisibility(View.GONE);
                RejectOption.this.finish();


            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                Toast.makeText(RejectOption.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
