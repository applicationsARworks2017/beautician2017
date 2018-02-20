package beautician.com.sapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import beautician.com.sapplication.Activity.CategoriesRequest;
import beautician.com.sapplication.Activity.OfferSet;
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




        if(lang.contentEquals("Arabic")){
            tv_serviceheading.setText("طلب الخدمة");
            tv_choose_category.setText("اختر الفئة، أدخل التفاصيل، والحصول على الخدمة وأكثر من ذلك");
            tv_SPheading.setText("البحث عن مقدمي الخدمات");
            tv_serch_byname.setText("البحث عن طريق الاسم، وبعد المسافة، وطلب الخدمة وأكثر من ذلك");
            tv_propsalheading.setText("انظر المقترحات");
            chk_response.setText("تحقق من الردود، وقبول واحد والتمتع بها");
            tv_offerheading.setText("عروض مذهلة");
            offer_check.setText("تحقق من العرض، والاستيلاء على واحد والحصول على خدمات مخفضة");
            tv_walletheading.setText("إدارة المحفظة");
            ad_mony.setText("إضافة المال، والتحقق من المعاملات.");
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
                Intent intent=new Intent(getActivity(),Wallet.class);
                intent.putExtra("PAGE","user_side");
                startActivity(intent);
            }
        });
        return v;
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
}
