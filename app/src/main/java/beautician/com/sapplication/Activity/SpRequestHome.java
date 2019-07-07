package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.APIManager;
import beautician.com.sapplication.Utils.Constants;

import static beautician.com.sapplication.Activity.SPHome.ServiceIndivisualRequest;
import static beautician.com.sapplication.Activity.SPHome.service_public;

public class SpRequestHome extends AppCompatActivity {
    LinearLayout lin_public,lin_individual;
    String lang,user_id;
    RelativeLayout post_not_sp,ind_not_sp;
    TextView public_post,invalid_requset,indpost_txt_sp,publicpost_txt_sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_proposal_home);
        lin_public=(LinearLayout)findViewById(R.id.lin_public);
        post_not_sp=(RelativeLayout) findViewById(R.id.post_not_sp);
        ind_not_sp=(RelativeLayout) findViewById(R.id.ind_not_sp);
        lin_individual=(LinearLayout)findViewById(R.id.lin_individual);
        public_post=(TextView)findViewById(R.id.public_post);
        invalid_requset=(TextView)findViewById(R.id.invalid_requset);
        indpost_txt_sp=(TextView)findViewById(R.id.indpost_txt_sp);
        publicpost_txt_sp=(TextView)findViewById(R.id.publicpost_txt_sp);
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);
        user_id = SpRequestHome.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        if(ServiceIndivisualRequest >0){
            indpost_txt_sp.setVisibility(View.VISIBLE);
            indpost_txt_sp.setText(String.valueOf(ServiceIndivisualRequest));
            ind_not_sp.setVisibility(View.VISIBLE);

        }
        else{
            indpost_txt_sp.setVisibility(View.GONE);
            ind_not_sp.setVisibility(View.GONE);

        }
        if(service_public >0){
            publicpost_txt_sp.setVisibility(View.VISIBLE);
            post_not_sp.setVisibility(View.VISIBLE);
            publicpost_txt_sp.setText(String.valueOf(service_public));
        }
        else{
            publicpost_txt_sp.setVisibility(View.GONE);
            post_not_sp.setVisibility(View.GONE);

        }


        if(lang.contentEquals("Arabic")){
            setTitle("اختر صنف");
            public_post.setText("المشاركة العامة");
            invalid_requset.setText("طلب فردي");
        }
        else{
            setTitle("Select Type");
            public_post.setText("Public Post");
            invalid_requset.setText("Individual Request");
        }
        lin_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToAPI(user_id,"ServiceRequest","Shop");
                publicpost_txt_sp.setVisibility(View.GONE);
                post_not_sp.setVisibility(View.GONE);
                Intent intent=new Intent(SpRequestHome.this,CheckPost.class);
                intent.putExtra("PAGE","sp_home");
                intent.putExtra("LANG",lang);
                startActivity(intent);
            }
        });
        lin_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToAPI(user_id,"ServiceIndivisualRequest","Shop");
                indpost_txt_sp.setVisibility(View.GONE);
                ind_not_sp.setVisibility(View.GONE);
                Intent intent=new Intent(SpRequestHome.this,CheckIndividualPost.class);
                intent.putExtra("PAGE","sp_home");
                intent.putExtra("LANG",lang);
                startActivity(intent);

            }
        });

    }

    private void CallToAPI(String user_id, String serviceRequest, String shop) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("reference_id", user_id);
            jsonObject.put("type", serviceRequest);
            jsonObject.put("reference_type", shop);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIManager().ModifyAPI(Constants.ONLINEURL + Constants.UPDATE_COUNT, "res", jsonObject, SpRequestHome.this, new APIManager.APIManagerInterface() {
            @Override
            public void onSuccess(Object resultObj) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
