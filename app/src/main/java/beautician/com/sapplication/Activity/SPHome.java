package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import beautician.com.sapplication.R;

public class SPHome extends AppCompatActivity {
    CardView card_manageservice,card_profile,card_checkPost,card_proposal,card_mywallet,card_offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphome);
        card_manageservice=(CardView)findViewById(R.id.card_manageservice);
        card_profile=(CardView)findViewById(R.id.card_profile);
        card_checkPost=(CardView)findViewById(R.id.checkPost);
        card_proposal=(CardView)findViewById(R.id.card_proposal);
        card_mywallet=(CardView)findViewById(R.id.card_mywallet);
        card_offer=(CardView)findViewById(R.id.card_offer);


        card_manageservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(SPHome.this,Categories.class);
                Intent intent=new Intent(SPHome.this,Manageservice.class);
              //  intent.putExtra("PAGE","service_home");
                startActivity(intent);
            }
        });

        card_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,SPProfile.class);
               // intent.putExtra("PAGE","service_home");
                startActivity(intent);
            }
        });
        card_checkPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,SpRequestHome.class);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);

            }
        });
        card_proposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,SpProposal.class);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);
            }
        });
        card_mywallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,Wallet.class);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);
            }
        });
        card_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,OfferSet.class);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);
            }
        });
    }
}
