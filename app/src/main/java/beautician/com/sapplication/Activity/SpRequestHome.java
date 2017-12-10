package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import beautician.com.sapplication.R;

public class SpRequestHome extends AppCompatActivity {
    LinearLayout lin_public,lin_individual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_proposal_home);
        lin_public=(LinearLayout)findViewById(R.id.lin_public);
        lin_individual=(LinearLayout)findViewById(R.id.lin_individual);
        lin_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SpRequestHome.this,CheckPost.class);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);
            }
        });
        lin_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SpRequestHome.this,CheckIndividualPost.class);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);
            }
        });

    }
}
