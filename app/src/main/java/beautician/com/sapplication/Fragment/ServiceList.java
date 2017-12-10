package beautician.com.sapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import beautician.com.sapplication.Activity.CheckIndividualPost;
import beautician.com.sapplication.Activity.CheckPost;
import beautician.com.sapplication.Activity.SpProposal;
import beautician.com.sapplication.Adapter.PropsalAdapter;
import beautician.com.sapplication.Pojo.Proposals;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;


public class ServiceList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FloatingActionButton postList,reqList;
    ProgressBar loader_propsals;
    SwipeRefreshLayout swipe_propsal;
    TextView no_propsal_txt;
    String user_id,server_message;
    int server_status;
    ListView lv_propsals;
    ArrayList<Proposals> pList;
    PropsalAdapter propsalAdapter;

    private OnFragmentInteractionListener mListener;

    public ServiceList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceList.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceList newInstance(String param1, String param2) {
        ServiceList fragment = new ServiceList();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_service_list, container, false);
        postList=(FloatingActionButton)v.findViewById(R.id.postList);
        reqList=(FloatingActionButton)v.findViewById(R.id.reqList);
        postList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CheckPost.class);
                intent.putExtra("PAGE","user_home");
                startActivity(intent);
            }
        });
        reqList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CheckIndividualPost.class);
                intent.putExtra("PAGE","user_home");
                startActivity(intent);
            }
        });
        pList=new ArrayList<>();
        loader_propsals=(ProgressBar)v.findViewById(R.id.loader_propsals);
        swipe_propsal=(SwipeRefreshLayout)v.findViewById(R.id.swip_propsal);
        lv_propsals=(ListView)v.findViewById(R.id.proposal_list);
        no_propsal_txt=(TextView)v.findViewById(R.id.no_propsal_txt);
        user_id = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        getPropsalList();
        swipe_propsal.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pList.clear();
                swipe_propsal.setRefreshing(false);
                getPropsalList();

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
    private void getPropsalList() {
        if(CheckInternet.getNetworkConnectivityStatus(getActivity())){
            CheckPropsal checkpropsals=new CheckPropsal();
            checkpropsals.execute(user_id);
        }
        else{
            Constants.noInternetDialouge(getActivity(),"No Internet");
        }
    }
    //* GET PROPSAL LIST ASYNTASK
    private class CheckPropsal extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getpropsal";

        @Override
        protected void onPreExecute() {
            loader_propsals.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINEURL + Constants.PROPOSALS;
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
                Uri.Builder builder=null;
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", user_id);
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
*
* "servicePurposal": [
        {
            "id": 20,
            "service_request_id": 36,
            "shop_id": 11,
            "remarks": "hello",
            "status": 1,
            "created": "2017-11-23T07:24:50+05:30",
            "modified": "2017-11-23T07:24:50+05:30",
            "shop": {
                "id": 11,
                "shopname": "MARS",
                "address": "bangalore",
                "latitudelongitude": "13.0356129,77.6332397",
                "photo1": "file1511273052578734998.jpg",
                "photo2": "file15112730571702631970.jpg",
                "photo3": "file15112730631927402507.jpg",
                "email": "amaresh.samantaray4@gmail.com",
                "mobile": "7026405551",
                "created": "2017-11-21T14:04:23+05:30",
                "modified": "2017-11-21T14:04:23+05:30"
            }
        },
* */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray servicePurposalArray = res.getJSONArray("servicePurposal");
                    if(servicePurposalArray.length()<=0){
                        server_message="No Category Found";
                        server_status=0;

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < servicePurposalArray.length(); i++) {
                            JSONObject o_list_obj = servicePurposalArray.getJSONObject(i);
                            JSONObject new_obj=o_list_obj.getJSONObject("shop");
                            String id = o_list_obj.getString("id");
                            String service_request_id = o_list_obj.getString("service_request_id");
                            String remarks = o_list_obj.getString("remarks");
                            String status = o_list_obj.getString("status");
                            String created = o_list_obj.getString("created");
                            String shop_id=new_obj.getString("id");
                            String shop_name=new_obj.getString("shopname");
                            if(status.contentEquals("4") || status.contentEquals("5")){
                                Proposals list1 = new Proposals(id, service_request_id, remarks, status, created, shop_id, shop_name);
                                pList.add(list1);
                            }
                            else {

                            }
                        }
                    }
                }
                return null;
            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void data) {
            super.onPostExecute(data);
            if(server_status==1) {
                propsalAdapter = new  PropsalAdapter (getActivity(),pList ,"user_side");
                lv_propsals.setAdapter(propsalAdapter);
                propsalAdapter.notifyDataSetChanged();
            }
            else{
                lv_propsals.setVisibility(View.GONE);
                no_propsal_txt.setVisibility(View.VISIBLE);
            }
            loader_propsals.setVisibility(View.GONE);
        }
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
