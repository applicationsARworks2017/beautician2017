package beautician.com.sapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import beautician.com.sapplication.Activity.CategoriesRequest;
import beautician.com.sapplication.Activity.OfferSet;
import beautician.com.sapplication.Activity.SPHome;
import beautician.com.sapplication.Activity.SearchShop;
import beautician.com.sapplication.Activity.SpProposal;
import beautician.com.sapplication.Activity.Wallet;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RelativeLayout rel_requsetservice,rel_searchfrmhome,amazing_offers,user_propsal,user_wallet;
    TextView tv_serviceheading,tv_choose_category,tv_SPheading,tv_serch_byname,tv_propsalheading,chk_response,
            tv_offerheading,offer_check,tv_walletheading,ad_mony;
    RelativeLayout proposal_notification,wallet_notification;
    TextView wallettext,propsaltext;
    String user_id;
    private int propsal_req,wallet_req;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String lang;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);

        lang = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);
        user_id = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        rel_requsetservice=(RelativeLayout)v.findViewById(R.id.rel_requsetservice);
        rel_searchfrmhome=(RelativeLayout)v.findViewById(R.id.rel_searchfrmhome);
        amazing_offers=(RelativeLayout)v.findViewById(R.id.amazing_offers);
        user_propsal=(RelativeLayout)v.findViewById(R.id.user_propsal);
        user_wallet=(RelativeLayout)v.findViewById(R.id.user_wallet);
        tv_serviceheading=(TextView)v.findViewById(R.id.tv_serviceheading);
        tv_choose_category=(TextView)v.findViewById(R.id.tv_choose_category);
        tv_SPheading=(TextView)v.findViewById(R.id.tv_SPheading);
        tv_serch_byname=(TextView)v.findViewById(R.id.tv_serch_byname);
        tv_propsalheading=(TextView)v.findViewById(R.id.tv_propsalheading);
        chk_response=(TextView)v.findViewById(R.id.chk_response);
        tv_offerheading=(TextView)v.findViewById(R.id.tv_offerheading);
        offer_check=(TextView)v.findViewById(R.id.offer_check);
        tv_walletheading=(TextView)v.findViewById(R.id.tv_walletheading);
        ad_mony=(TextView)v.findViewById(R.id.ad_mony);
        user_wallet=(RelativeLayout)v.findViewById(R.id.user_wallet);
        user_wallet=(RelativeLayout)v.findViewById(R.id.user_wallet);

        proposal_notification=(RelativeLayout)v.findViewById(R.id.proposal_notification);
        wallet_notification=(RelativeLayout)v.findViewById(R.id.wallet_notification);

        proposal_notification.setVisibility(View.GONE);
        wallet_notification.setVisibility(View.GONE);
        propsaltext=(TextView)v.findViewById(R.id.propsaltext);
        wallettext=(TextView)v.findViewById(R.id.wallettext);
        getAllNotification();



        if(lang.contentEquals("Arabic")){
            tv_serviceheading.setText("طلب الخدمة");
            tv_choose_category.setText("اختر الفئة، أدخل التفاصيل، والحصول على الخدمة وأكثر من ذلك");
            tv_SPheading.setText("البحث عن مقدمي الخدمات");
            tv_serch_byname.setText("البحث عن طريق الاسم، وبعد المسافة، وطلب الخدمة وأكثر من ذلك");
            tv_propsalheading.setText(" الاطلاع على المقترحات");
            chk_response.setText("تحقق من الردود واقبل احد العروض");
            tv_offerheading.setText("عروض مذهلة");
            offer_check.setText("تحقق من العرض، والحصول على خدمات مخفضة");
            tv_walletheading.setText("إدارة المحفظة");
            ad_mony.setText(" إضافة رصيد، والتحقق من المعاملات.");
        }
        else{
            tv_serviceheading.setText("Request for Service");
            tv_choose_category.setText("Choose Category, Enter the details, Get the service and more");
            tv_SPheading.setText("Find Service Providers");
            tv_serch_byname.setText("Search by name,distance,ask for service and more");
            tv_propsalheading.setText("See the Proposals");
            chk_response.setText("Check the responses, accept  one and enjoy");
            tv_offerheading.setText("Amazing Offers");
            offer_check.setText("Check the offer, Grab one and get discounted services");
            tv_walletheading.setText("Manage Wallet");
            ad_mony.setText("Add money, check the transactions.");

        }

        rel_requsetservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),CategoriesRequest.class);
                intent.putExtra("PAGE","service_request");
                startActivity(intent);
            }
        });
        rel_searchfrmhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchShop.class);
                startActivity(intent);
            }
        });
        amazing_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),OfferSet.class);
                intent.putExtra("PAGE","user_side");
                startActivity(intent);
            }
        });
        user_propsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SpProposal.class);
                intent.putExtra("PAGE","user_side");
                startActivity(intent);
            }
        });
        user_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update
                Intent intent=new Intent(getActivity(),Wallet.class);
                intent.putExtra("PAGE","user_side");
                startActivity(intent);
            }
        });
        return v;
    }
    private void getAllNotification() {
        String reference_id= user_id;
        String reference_type= "User";
        String is_read= "0";

        new ShowSPNotification().execute(reference_id,reference_type,is_read);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class ShowSPNotification extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;

        int server_status;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _reference_id = params[0];
                String _reference_type = params[1];
                String _is_read = params[2];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.ONLINEURL + Constants.SHOP_NOTIFICATION_COUNT;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("reference_id", _reference_id)
                        .appendQueryParameter("reference_type", _reference_type)
                        .appendQueryParameter("is_read", _is_read);

                //.appendQueryParameter("deviceid", deviceid);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

                /*
                * "notificationType": {
        "Wallet": 0,
        "ServiceIndivisualRequest": 0,
        "ServicePurposal": "1",
        "ServiceRequest": 0,
        "UserWallet": "4"
    },
    "res": {
        "message": "available",
        "status": 1
    }
                * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj = res.getJSONObject("res");
                    server_status=j_obj.getInt("status");
                    if(server_status==1) {
                        JSONObject j_obj1 = res.getJSONObject("notificationType");
                        wallet_req=j_obj1.getInt("UserWallet");
                        propsal_req=j_obj1.getInt("ServicePurposal");

                    }

                }
                return null;
            } catch (Exception exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(wallet_req>0){
                wallet_notification.setVisibility(View.VISIBLE);
                wallettext.setText(String.valueOf(wallet_req));
            }

        }
    }
}
