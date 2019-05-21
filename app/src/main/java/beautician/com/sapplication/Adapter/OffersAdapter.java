package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

import beautician.com.sapplication.Activity.IndividualRequest;
import beautician.com.sapplication.Activity.OfferSet;
import beautician.com.sapplication.Activity.ShopDetails;
import beautician.com.sapplication.Pojo.Offers;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/24/17.
 */

public class OffersAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Offers> new_list;
    Holder holder,vholder;
    String user_id,lang,page;

    public OffersAdapter(OfferSet offerSet, ArrayList<Offers> oList,String lang, String page) {
        this._context=offerSet;
        this.new_list=oList;
        this.lang=lang;
        this.page=page;
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
    private class Holder{
        TextView offerHeading,offer_details,shopname;
        ImageView im_reply,pic1,pic2,pic3,pic4;
        LinearLayout img_lin;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Offers _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.offer_list, parent, false);
            user_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.offerHeading=(TextView)convertView.findViewById(R.id.of_heading);
            holder.offer_details=(TextView)convertView.findViewById(R.id.offeredetails);
            holder.shopname=(TextView)convertView.findViewById(R.id.shopname);
            holder.im_reply=(ImageView) convertView.findViewById(R.id.im_reply);
            holder.pic1=(ImageView) convertView.findViewById(R.id.pic1);
            holder.pic2=(ImageView) convertView.findViewById(R.id.pic2);
            holder.pic3=(ImageView) convertView.findViewById(R.id.pic3);
            holder.pic4=(ImageView) convertView.findViewById(R.id.pic4);
            holder.img_lin=(LinearLayout)convertView.findViewById(R.id.img_lin);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.offerHeading.setTag(position);
        holder.im_reply.setTag(holder);
        holder.offer_details.setTag(position);
        holder.shopname.setTag(position);
        holder.pic1.setTag(position);
        holder.pic2.setTag(position);
        holder.pic3.setTag(position);
        holder.pic4.setTag(position);
        holder.img_lin.setTag(position);
        holder.offer_details.setText(_pos.getOffer_detail());
        holder.shopname.setText(_pos.getShopname());
        holder.shopname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, ShopDetails.class);
                intent.putExtra("SHOP_ID",_pos.getShop_id());
                intent.putExtra("MAP","false");
                _context.startActivity(intent);
            }
        });
        holder.img_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, ShopDetails.class);
                intent.putExtra("SHOP_ID",_pos.getShop_id());
                intent.putExtra("MAP","false");
                _context.startActivity(intent);
            }
        });
        if(lang.contentEquals("Arabic")){
            holder.offerHeading.setText(_pos.getTitle());

        }
        else{
            holder.offerHeading.setText(_pos.getTitle());

        }



        if(!_pos.getPhoto1().isEmpty()) {
             /*Glide.with(_context).load(_pos.getPhoto1())
                     .thumbnail(0.5f)
                     .crossFade()
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .into(holder.pic1);*/
             String uu=Constants.PICURL+_pos.getPhoto1();
            Picasso.with(_context).load(Constants.SHOP_PICURL+_pos.getPhoto1()).resize(100,100).into(holder.pic1);
            // imageLoader.displayImage(_pos.getPhoto1(),holder.pic1,options);
        }
        if(!_pos.getPhoto2().isEmpty()) {
            Picasso.with(_context).load(Constants.SHOP_PICURL+_pos.getPhoto2())
                    .resize(100,100).into(holder.pic2);
            /*Glide.with(_context).load(_pos.getPhoto2())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.pic2);*/
            //imageLoader.displayImage(_pos.getPhoto2(),holder.pic2,options);

        }
        if(!_pos.getPhoto3().isEmpty()) {
            Picasso.with(_context).load(Constants.SHOP_PICURL+_pos.getPhoto3()).resize(100,100).into(holder.pic3);
            //imageLoader.displayImage(_pos.getPhoto3(),holder.pic3,options);
            /*Glide.with(_context).load(_pos.getPhoto3())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.pic3);*/

        }if(!_pos.getPhoto4().isEmpty()) {
           /* Glide.with(_context).load(_pos.getPhoto4())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.pic4);*/
            Picasso.with(_context).load(Constants.SHOP_PICURL+_pos.getPhoto4()).resize(100,100).into(holder.pic4);
            //imageLoader.displayImage(_pos.getPhoto4(),holder.pic4,options);

        }

        if(page.contentEquals("user_side")){
            holder.im_reply.setVisibility(View.GONE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(_context,IndividualRequest.class);
                    intent.putExtra("SHOP_ID",_pos.getShop_id());
                    intent.putExtra("SHOP_NAME",_pos.getShopname());
                    intent.putExtra("DETAILS","");
                    _context.startActivity(intent);
                }
            });
        }
        else{
            holder.im_reply.setVisibility(View.VISIBLE);

        }
        holder.im_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vholder=(Holder)v.getTag();
                String message,yes,no;
                if(lang.contentEquals("Arabic")) {
                     message="هل تريد حذف هذا العرض ؟";
                     yes="نعم فعلا";
                     no="لا";
                }
                else{
                     message="Do you want to delete the offer ?";
                     yes="Yes";
                     no="No";

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage(message);
                builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        //   dialog.dismiss();
                        if(CheckInternet.getNetworkConnectivityStatus(_context)){
                            DeleteOffers deleteOffers=new DeleteOffers();
                            deleteOffers.execute(_pos.getId());
                        }
                        else{
                            Toast.makeText(_context,"No Internet",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }



        });


        return convertView;
    }


    /**
     * Async task to get wallet balance from  camp table from server
     * */
    private class DeleteOffers extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Delete Offer";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {

                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(_context, "جاري الحذف", "يرجى الانتظار...");

                }
                else {
                    progressDialog = ProgressDialog.show(_context, "Deleteing", "Please wait...");
                }
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _id = params[0];
                InputStream in = null;
                int resCode = -1;
                String link=null;
                    link = Constants.ONLINEURL + Constants.OFFER_DELETE;


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

                Uri.Builder builder = null;

                    builder = new Uri.Builder()
                            .appendQueryParameter("id", _id);


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
                if(in == null){
                    return null;
                }
                BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "",data="";

                while ((data = reader.readLine()) != null){
                    response += data + "\n";
                }

                Log.i(TAG, "Response : "+response);

                /**
                 * {
                 "res": {
                 "message": "The offers has been deleted.",
                 "status": 1
                 }
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject Obj=res.getJSONObject("res");
                    server_status=Obj.getInt("status");

                }

                return null;
            } catch(Exception exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.dismiss();
            if (server_status == 1) {
                vholder.im_reply.setVisibility(View.GONE);
            }
        }
    }
}
