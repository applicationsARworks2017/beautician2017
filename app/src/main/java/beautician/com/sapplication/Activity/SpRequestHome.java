package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

public class SpRequestHome extends AppCompatActivity {
    LinearLayout lin_public,lin_individual;
    String lang;
    TextView public_post,invalid_requset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_proposal_home);
        lin_public=(LinearLayout)findViewById(R.id.lin_public);
        lin_individual=(LinearLayout)findViewById(R.id.lin_individual);
        public_post=(TextView)findViewById(R.id.public_post);
        invalid_requset=(TextView)findViewById(R.id.invalid_requset);
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        if(lang.contentEquals("Arabic")){
            public_post.setText("المشاركة العامة");
            invalid_requset.setText("طلب فردي");
        }
        else{
            public_post.setText("Public Post");
            invalid_requset.setText("Individual Request");
        }
        lin_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SpRequestHome.this,CheckPost.class);
                intent.putExtra("PAGE","sp_home");
                intent.putExtra("LANG",lang);
                startActivity(intent);
            }
        });
        lin_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SpRequestHome.this,CheckIndividualPost.class);
                intent.putExtra("PAGE","sp_home");
                intent.putExtra("LANG",lang);
                startActivity(intent);
            }
        });

    }
}
