package beautician.com.sapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

public class SPHome extends AppCompatActivity {
    CardView card_manageservice,card_profile,card_checkPost,card_proposal,card_mywallet,card_offer;
    String lang;
    TextView check_post,profilee,manage_offers,my_wallet,manage_service,my_proposal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphome);
        lang = SPHome.this.getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        card_manageservice=(CardView)findViewById(R.id.card_manageservice);
        card_profile=(CardView)findViewById(R.id.card_profile);
        card_checkPost=(CardView)findViewById(R.id.checkPost);
        card_proposal=(CardView)findViewById(R.id.card_proposal);
        card_mywallet=(CardView)findViewById(R.id.card_mywallet);
        card_offer=(CardView)findViewById(R.id.card_offer);

        check_post=(TextView)findViewById(R.id.check_post);
        my_proposal=(TextView)findViewById(R.id.my_proposal);
        manage_service=(TextView)findViewById(R.id.manage_service);
        my_wallet=(TextView)findViewById(R.id.my_wallet);
        manage_offers=(TextView)findViewById(R.id.manage_offers);
        profilee=(TextView)findViewById(R.id.profilee);

        if(lang.contentEquals("Arabic")){
         check_post.setText(("تحقق من المشاركات"));
         my_proposal.setText(("مقترحاتي"));
         manage_service.setText(("إدارة الخدمات"));
            my_wallet.setText(("محفظتى"));
            manage_offers.setText(("إدارة العروض"));
            profilee.setText(("الملف الشخصي"));
        }
        else{
            check_post.setText(("Check Posts"));
            my_proposal.setText(("My Proposals"));
            manage_service.setText(("Manage Services"));
            my_wallet.setText(("My Wallet"));
            manage_offers.setText(("Manage Offers"));
            profilee.setText(("Profile"));

        }
        card_manageservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(SPHome.this,Categories.class);
                Intent intent=new Intent(SPHome.this,Manageservice.class);
                intent.putExtra("LANG",lang);
                startActivity(intent);
            }
        });

        card_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,SPProfile.class);
                intent.putExtra("LANG",lang);
                startActivity(intent);
            }
        });
        card_checkPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,SpRequestHome.class);
                intent.putExtra("LANG",lang);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);

            }
        });
        card_proposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,SpProposal.class);
                intent.putExtra("LANG",lang);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);
            }
        });
        card_mywallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                Intent intent=new Intent(SPHome.this,Wallet.class);
                intent.putExtra("LANG",lang);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);
            }
        });
        card_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPHome.this,OfferSet.class);
                intent.putExtra("LANG",lang);
                intent.putExtra("PAGE","sp_home");
                startActivity(intent);
            }
        });
    }
}
