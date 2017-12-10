package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import beautician.com.sapplication.Activity.Subcategories;
import beautician.com.sapplication.Pojo.SubCategoryList;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/11/17.
 */

public class SubcategoriesAdapter extends BaseAdapter {
    Context _context;
    ArrayList<SubCategoryList> new_list;
    Holder holder,holder1;
    Dialog dialog;
    String user_id;
    public SubcategoriesAdapter(Subcategories subcategories, ArrayList<SubCategoryList> scList) {
        this._context=subcategories;
        this.new_list=scList;
    }



    @Override
    public int getCount() {
        return new_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        TextView tv_category,tv_subcategory,tv_price;
        ImageView im_edit;
        Button bt_add;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SubCategoryList _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.sub_cat_list, parent, false);
            user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

            holder.tv_category=(TextView)convertView.findViewById(R.id.tv_catname);
            holder.tv_subcategory=(TextView)convertView.findViewById(R.id.tv_subcatname);
            holder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
            holder.im_edit=(ImageView) convertView.findViewById(R.id.iv_edit);
            holder.bt_add=(Button) convertView.findViewById(R.id.bt_add);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.tv_category.setTag(position);
        holder.tv_subcategory.setTag(position);
        holder.tv_price.setTag(position);
        holder.im_edit.setTag(holder);
        holder.bt_add.setTag(holder);

        holder.tv_subcategory.setText(_pos.getSubcategory());
        holder.bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder)v.getTag();
                dialog=new Dialog((_context));
                dialog.setContentView(R.layout.dialog_price);
                ImageView im_close=(ImageView)dialog.findViewById(R.id.im_close);
                TextView tv_subcat=(TextView)dialog.findViewById(R.id.subcategoryname);
                tv_subcat.setText(_pos.getSubcategory());
                im_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                final EditText et_cost=(EditText)dialog.findViewById(R.id.et_price);
                Button ok=(Button)dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String price=et_cost.getText().toString().trim();
                        if(price.length()<=0){
                            Toast.makeText(_context,"Please enter the cost",Toast.LENGTH_SHORT).show();
                        }
                        else{
                           // Toast.makeText(_context,"Done",Toast.LENGTH_SHORT).show();
                            if(CheckInternet.getNetworkConnectivityStatus(_context)){
                                SetPrice setPrice=new SetPrice();
                                setPrice.execute(user_id,_pos.getCategory_id(),_pos.getId(),price);
                            }
                            else{
                                Constants.noInternetDialouge(_context,"No Internet");
                            }

                        }
                    }
                });
                dialog.show();

            }
        });
        return convertView;


    }
    private class SetPrice extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Share Sync";
        String server_message;
        String id,username,email_address,contact_no;
        int server_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // onPreExecuteTask();
        }

        //                                setPrice.execute(user_id,_pos.getCategory_id(),_pos.getId(),price);

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _user_id = params[0];
                String _cat_id = params[1];
                String _sub_cat_id = params[2];
                String _price = params[3];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.PRICE_SET ;
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
                        .appendQueryParameter("shop_id", _user_id)
                        .appendQueryParameter("category_id", _cat_id)
                        .appendQueryParameter("sub_category_id", _sub_cat_id)
                        .appendQueryParameter("price", _price);

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

                /**
                 {
                 "res": {
                 "message": "The shop has been saved.",
                 "status": 1
                 }
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1) {
                        server_message="Added";
                    } else {
                        server_message = "Error";
                    }
                }
                return null;
            } catch (Exception exception) {
                server_message = "Server Connection Failed";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            Toast.makeText(_context,server_message,Toast.LENGTH_SHORT).show();
            if(server_status==1){
                dialog.dismiss();
            }

        }
    }
}
