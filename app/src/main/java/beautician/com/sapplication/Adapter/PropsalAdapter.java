package beautician.com.sapplication.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import beautician.com.sapplication.Activity.GiveCommentActivity;
import beautician.com.sapplication.Activity.HomeActivity;
import beautician.com.sapplication.Activity.PropsalView;
import beautician.com.sapplication.Activity.SPHome;
import beautician.com.sapplication.Activity.ShopDetails;
import beautician.com.sapplication.Activity.SpProposal;
import beautician.com.sapplication.Activity.UserDetails;
import beautician.com.sapplication.Pojo.Proposals;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;

/**
 * Created by Amaresh on 11/23/17.
 */

public class PropsalAdapter extends BaseAdapter {
    private Context _context;
    Holder holder,holder1;
    Dialog dialog;
    String OTP;
    int updated_status;
    String from_page,wpage="no page",shop_id,user_id,propsal_id;
    Double user_balance, shop_balance;
    String callPage="blanck";
    String lang;
    private ArrayList<Proposals> new_list;
    public PropsalAdapter(SpProposal spProposal, ArrayList<Proposals> pList,String page,String lang) {
        this._context=spProposal;
        this.new_list=pList;
        this.from_page=page;
        this.lang=lang;
    }

    public PropsalAdapter(FragmentActivity activity, ArrayList<Proposals> pList, String user_side,String lang) {
        this._context=activity;
        this.new_list=pList;
        this.from_page=user_side;
        this.lang=lang;

    }

    public PropsalAdapter() {

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
        TextView propsal_hd,vew_details,gv_feedback,actualtime,user_details,tv_otp,im_reply;
        TextView im_agree;
        LinearLayout paidline;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Proposals _pos=new_list.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.propsal_list, parent, false);
            shop_id = _context.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
            holder.propsal_hd=(TextView)convertView.findViewById(R.id.propsal_hd);
            holder.vew_details=(TextView)convertView.findViewById(R.id.view_details);
            holder.gv_feedback=(TextView)convertView.findViewById(R.id.gv_feedback);
            holder.actualtime=(TextView)convertView.findViewById(R.id.actualtime);
            holder.tv_otp=(TextView)convertView.findViewById(R.id.tv_otp);
            holder.im_reply=(TextView)convertView.findViewById(R.id.im_reply);
            holder.im_agree=(TextView) convertView.findViewById(R.id.im_agree);
            holder.user_details=(TextView) convertView.findViewById(R.id.user_details);
            holder.paidline=(LinearLayout)convertView.findViewById(R.id.paidline);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.propsal_hd.setTag(position);
        holder.vew_details.setTag(position);
        holder.actualtime.setTag(position);
        holder.paidline.setTag(position);
        holder.tv_otp.setTag(holder);
        holder.im_reply.setTag(holder);
        holder.im_agree.setTag(holder);
        holder.gv_feedback.setTag(holder);
        holder.user_details.setTag(position);
        holder.gv_feedback.setVisibility(View.GONE);
        holder.user_details.setVisibility(View.GONE);

        final String status=_pos.getStatus();

             if (lang.contentEquals("Arabic")) {
            holder.vew_details.setText("تفاصيل الخدمة");
            holder.gv_feedback.setText("ردود الفعل");

        } else {
            holder.vew_details.setText("Service Details");
            holder.gv_feedback.setText("Give Feedback");

        }

        holder.actualtime.setText(Constants.getOurDate(_pos.getCreated()));
        if(from_page.contentEquals("user_side")){
            holder.im_reply.setVisibility(View.GONE);
            holder.tv_otp.setVisibility(View.GONE);
            holder.im_agree.setVisibility(View.VISIBLE);
            if(status.contentEquals("1")) {
                holder.paidline.setVisibility(View.GONE);
                if(lang.contentEquals("Arabic")){
                    holder.im_agree.setText(R.string.confirm_ar);
                }
                else {
                    holder.im_agree.setText("Confirm");
                }

                /*Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.black));
                holder.im_agree.setImageDrawable(drawable1);*/
            }
            else if(status.contentEquals("2")){

                holder.paidline.setVisibility(View.GONE);
                holder.tv_otp.setVisibility(View.GONE);
                holder.im_agree.setText("Skip");
               /* Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);*/
            }
            else if(status.contentEquals("3")){
                holder.paidline.setVisibility(View.VISIBLE);
                holder.tv_otp.setVisibility(View.VISIBLE);
                holder.im_agree.setText(_pos.getOtp());
                /*Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
                holder.im_agree.setImageDrawable(drawable1);*/
            }
            else if(status.contentEquals("4")){ // otp given and 5 $ reversed
                holder.paidline.setVisibility(View.GONE);
                holder.tv_otp.setVisibility(View.GONE);
                holder.gv_feedback.setVisibility(View.GONE);
                if(lang.contentEquals("Arabic")){
                    holder.im_agree.setText(R.string.on_going_ar);
                }
                else {
                    holder.im_agree.setText("Ongoing");
                }
               /* Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_power_input_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);*/
            }
            else if(status.contentEquals("5")){ // completed waiting for the feed back
                holder.paidline.setVisibility(View.GONE);
                holder.tv_otp.setVisibility(View.GONE);
                if(lang.contentEquals("Arabic")){
                    holder.im_agree.setText(R.string.completed_ar);
                }
                else {
                    holder.im_agree.setText("Completed");
                }
               /* Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);*/
                holder.gv_feedback.setVisibility(View.VISIBLE);
            }
            else if(status.contentEquals("6")){ // completed feedback submitted
                holder.paidline.setVisibility(View.GONE);
                holder.tv_otp.setVisibility(View.GONE);
                if(lang.contentEquals("Arabic")){
                    holder.im_agree.setText(R.string.completed_ar);
                }
                else {
                    holder.im_agree.setText("Completed");
                }               /* Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_agree.setImageDrawable(drawable1);*/
                holder.gv_feedback.setVisibility(View.GONE);
            }
            else{
                holder.im_agree.setEnabled(false);
            }
        }
        else{
            holder.im_agree.setVisibility(View.GONE);
            holder.im_reply.setVisibility(View.VISIBLE);
            if(status.contentEquals("1")){
                if(lang.contentEquals("Arabic")){
                    holder.im_reply.setText(R.string.waiting_ar);
                }
                else {
                    holder.im_reply.setText("Waiting");
                }
                /*Resources ress = _context.getResources(); // no response
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_error_black_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.deep_background));
                holder.im_reply.setImageDrawable(drawable1);*/
            }
            else if(status.contentEquals("3")){  // got response and wave
                if(lang.contentEquals("Arabic")){
                    holder.im_reply.setText(R.string.enter_otp_ar);
                }
                else {
                    holder.im_reply.setText("Enter OTP");
                }
               /* Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_assignment_turned_in_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.black));
                holder.im_reply.setImageDrawable(drawable1);*/
            }
            else if(status.contentEquals("2")){ // service is giving after getting user details
                holder.im_reply.setText("Skip");
                /*holder.user_details.setVisibility(View.VISIBLE);
                Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_assignment_turned_in_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_reply.setImageDrawable(drawable1);*/
            }
            else if(status.contentEquals("4")){ // otp matched and service continuing
                if(lang.contentEquals("Arabic")){
                    holder.im_reply.setText(R.string.finsih_ar);

                }
                else {
                    holder.im_reply.setText(R.string.finsih_en);

                }
                /*Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_power_input_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_reply.setImageDrawable(drawable1);*/
            }
            else if(status.contentEquals("5")){ // completed
                if(lang.contentEquals("Arabic")){
                    holder.im_reply.setText(R.string.completed_ar);
                }
                else {
                    holder.im_reply.setText("Completed");
                }
                /*Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_reply.setImageDrawable(drawable1);*/

            }
            else if(status.contentEquals("6")){ // completed and got the feedbacks
                if(lang.contentEquals("Arabic")){
                    holder.im_reply.setText(R.string.completed_ar);
                }
                else {
                    holder.im_reply.setText("Completed");
                }
               /* Resources ress = _context.getResources();
                Drawable drawable1 = ress.getDrawable(R.mipmap.ic_done_all_white_24dp);
                drawable1 = DrawableCompat.wrap(drawable1);
                DrawableCompat.setTint(drawable1, _context.getResources().getColor(R.color.colorPrimary));
                holder.im_reply.setImageDrawable(drawable1);*/

            }
            else {
                holder.im_reply.setEnabled(false);
            }
        }
        if(from_page.contentEquals("user_side")) {
            if (lang.contentEquals("Arabic")) {
                holder.propsal_hd.setText(_pos.getShop_name().toUpperCase() + "تم الرد" + _pos.getRemarks());
                holder.tv_otp.setText(" شارك الكود عند الوصول  للمقدم الخدمة لسترجاع مبلغ تأكييد الحجز إلى المحفظة:" + _pos.getOtp());
            } else {
                holder.propsal_hd.setText(_pos.getShop_name().toUpperCase() + " has replied : " + _pos.getRemarks());
                holder.tv_otp.setText("Share OTP before service and get back your SAR"+HomeActivity.min_post_charge+". OTP : " + _pos.getOtp());

            }
        }
        else {
            holder.propsal_hd.setText(_pos.getRemarks());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from_page.contentEquals("user_side")) {
                    String viewmap="false";
                    if(Integer.valueOf(_pos.getStatus())>=3){
                        viewmap="true";
                    }
                    Intent intent = new Intent(_context, ShopDetails.class);
                    intent.putExtra("SHOP_ID", _pos.getShop_id());
                    intent.putExtra("MAP", viewmap);
                    _context.startActivity(intent);
                }
            }
        });


        holder.gv_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder)v.getTag();
                holder1.gv_feedback.setVisibility(View.GONE);
                Intent intent=new Intent(_context,GiveCommentActivity.class);
                intent.putExtra("PAGE","propsal");
                intent.putExtra("SHOP_NAME",_pos.getShop_name());
                intent.putExtra("SHOP_ID",_pos.getShop_id());
                intent.putExtra("PROPSAL_ID",_pos.getId());
                _context.startActivity(intent);
            }
        });
        holder.vew_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_context,PropsalView.class);
                intent.putExtra("SID",_pos.getService_request_id());
                intent.putExtra("PID",_pos.getId());
                intent.putExtra("PSTATUS",_pos.getStatus());
                intent.putExtra("PROPSAL",_pos.getRemarks());
                intent.putExtra("LANG",lang);
                _context.startActivity(intent);

            }
        });
        holder.im_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder) v.getTag();
                String imreply_text=holder1.im_reply.getText().toString().trim();
                String callTo = null;
                if(imreply_text.contentEquals("Waiting") || imreply_text.contentEquals("انتظار")){
                    if(lang.contentEquals("Arabic")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("آسف");
                        builder.setMessage("لا يمكنك فعل أي شيء حتى يرد المستخدم");
                        builder.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();


                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("Sorry");
                        builder.setMessage("You can't do anything till user respond");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();


                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }
                /*else if(imreply_text.contentEquals("Enter OTP")){
                    //callTo = "3";
                    user_id=_pos.getUser_id();
                    propsal_id=_pos.getId();
                    if(lang.contentEquals("Arabic")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("المستخدم جاهز لأخذ الخدمة");
                        builder.setMessage("عليك أن تدفع 5 ريالات ، هل انت متاكد؟");
                        final String finalCallTo = callTo;
                        builder.setPositiveButton("نعم فعلا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //check own balance
                                wpage="sp_home";
                                getWdetails getWdetails=new getWdetails();
                                getWdetails.execute(shop_id);

                            }
                        });
                        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("User is ready to take the service");
                        builder.setMessage("You have to pay SAR " + SPHome.min_service_charge+", Do you want to go ahead?");
                        final String finalCallTo = callTo;
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //check own balance
                                wpage="sp_home";
                                getWdetails getWdetails=new getWdetails();
                                getWdetails.execute(shop_id);

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }*/
                else if(imreply_text.contentEquals("Enter OTP") || imreply_text.contentEquals("أدخل رقم إنهاء الخدمة")){
                    callTo = "4";
                    updated_status=4;
                    user_id=_pos.getUser_id();
                    final Dialog dialog = new Dialog(_context);
                    final String finalCallTo = callTo;
                    dialog.setContentView(R.layout.otpscreen);
                    ImageView imageView=(ImageView)dialog.findViewById(R.id.close);
                    Button submit=(Button)dialog.findViewById(R.id.add_money);
                    TextView otp_head=(TextView) dialog.findViewById(R.id.otp_head);
                    if(lang.contentEquals("Arabic")){
                        otp_head.setText("أدخل OTP لمتابعة الخدمة");
                        submit.setText("موافق");
                    }
                    else{
                        otp_head.setText("Enter OTP to continue the service");
                        submit.setText("Submit");
                    }
                    final EditText et_add_money=(EditText) dialog.findViewById(R.id.et_add_money);
                    dialog.show();
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OTP = et_add_money.getText().toString().trim();

                            ConfirmToProp confirmToProp = new ConfirmToProp();
                            confirmToProp.execute(_pos.getId(), finalCallTo);
                            dialog.cancel();
                        }
                    });

                }
                else if(imreply_text.contentEquals("Finish") || imreply_text.contentEquals("إنهاء")){
                    callTo = "4";
                    updated_status=5;
                    user_id=_pos.getUser_id();
                    if(lang.contentEquals("Arabic")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        // builder.setTitle("Are you Comfortable with this proposal");
                        builder.setMessage("تم اكتمال الخدمه ؟");
                        final String finalCallTo = callTo;
                        builder.setPositiveButton("نعم فعلا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();
                                ConfirmToProp confirmToProp = new ConfirmToProp();
                                confirmToProp.execute(_pos.getId(), "5");
                            }
                        });
                        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        // builder.setTitle("Are you Comfortable with this proposal");
                        builder.setMessage("Service Completed ?");
                        final String finalCallTo = callTo;
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();
                                ConfirmToProp confirmToProp = new ConfirmToProp();
                                confirmToProp.execute(_pos.getId(), "5");
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }


                }
            }
        });
        holder.im_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1=(Holder) v.getTag();
                String imagree_tvtext=holder1.im_agree.getText().toString().trim();
                String callTo = null;
                user_id=_pos.getUser_id();
                if(imagree_tvtext.contentEquals("Confirm") || imagree_tvtext.contentEquals("تأكيد")) {
                    callTo = "2";
                    if(lang.contentEquals("Arabic")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("هل أنت راضا مع هذا الاقتراح");
                        builder.setMessage("هل تود الاستمرار ؟ ");
                        final String finalCallTo = callTo;
                        builder.setPositiveButton("نعم فعلا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();
                                /*ConfirmToProp confirmToProp = new ConfirmToProp();
                                confirmToProp.execute(_pos.getId(), finalCallTo);*/
                                updated_status=3;
                                OTP = String.valueOf(Constants.generatePIN());
                                ConfirmToProp confirmToProp = new ConfirmToProp();
                                confirmToProp.execute(propsal_id, "3");
                                holder1.im_agree.setText(OTP);
                                holder1.tv_otp.setVisibility(View.VISIBLE);
                                holder1.tv_otp.setText(" شارك الكود عند الوصول  للمقدم الخدمة لسترجاع مبلغ تأكييد الحجز إلى المحفظة:" + OTP);


                            }
                        });
                        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("Are you Comfortable with this proposal");
                        builder.setMessage("Do you want to go ahead ?");
                        final String finalCallTo = callTo;
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();
                                /*ConfirmToProp confirmToProp = new ConfirmToProp();
                                confirmToProp.execute(_pos.getId(), finalCallTo);*/
                                updated_status=3;

                                OTP = String.valueOf(Constants.generatePIN());
                                ConfirmToProp confirmToProp = new ConfirmToProp();
                                confirmToProp.execute(_pos.getId(), "3");
                                holder1.im_agree.setText(OTP);
                                holder1.tv_otp.setVisibility(View.VISIBLE);
                                holder1.tv_otp.setText("Share OTP before service and get back your SAR"+HomeActivity.min_post_charge+". OTP : " + OTP);


                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }
                else if(imagree_tvtext.contentEquals("Waiting") || imagree_tvtext.contentEquals("انتظار")){
                    if(lang.contentEquals("Arabic")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("تم الموافقه مسبقا ");
                        builder.setMessage("لقد أرسلت اشعارا للخدمة");
                        final String finalCallTo = callTo;
                        builder.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();


                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                        builder.setTitle("Already Allowed");
                        builder.setMessage("You have sent notification for service");
                        final String finalCallTo = callTo;
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                //   dialog.dismiss();


                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                }
            }
        });

        holder.user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_context,UserDetails.class);
                intent.putExtra("USER_ID",_pos.getUser_id());
                _context.startActivity(intent);
            }
        });
        return convertView;
    }


    public class ConfirmToProp extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Share Sync";
        String server_message;
        String id,username,email_address,contact_no;
        int server_status;
        private ProgressDialog progressDialog = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // onPreExecuteTask();
           /* if(progressDialog == null) {
                progressDialog = ProgressDialog.show(_context, "Updating", "Please wait...");
            }*/
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _id = params[0];
                String _status = params[1];
               // String _otp = params[2];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.EDIT_PROPSAL ;
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
                if(_status.contentEquals("3")){
                    builder=new Uri.Builder()
                            .appendQueryParameter("id", _id)
                            .appendQueryParameter("status", _status)
                            .appendQueryParameter("otp", OTP);
                }
                else if(_status.contentEquals("4")){
                    builder=new Uri.Builder()
                            .appendQueryParameter("id", _id)
                            .appendQueryParameter("status", _status)
                            .appendQueryParameter("otp", OTP);
                }
                else {
                    builder = new Uri.Builder()
                            .appendQueryParameter("id", _id)
                            .appendQueryParameter("status", _status);
                }


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

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("res");
                    server_status = j_obj.optInt("status");
                    if (server_status == 1 ) {
                        if(lang.contentEquals("Arabic")){
                            server_message="ناجح";

                        }
                        else{
                            server_message="Successful";

                        }
                    }
                    else  {
                        if(lang.contentEquals("Arabic")){
                            server_message = "خطأ";

                        }
                        else{
                            server_message = "Error";

                        }
                    }

                }
                return null;
            } catch (Exception exception) {
                server_message = "N Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
           // progressDialog.cancel();
            if(callPage.contentEquals("comment")){

            }
            else {
                if (server_status == 1) {
                    if(updated_status==3){

                        wpage = "user_side";
                        Log.i("userid", user_id);
                        getWdetails getUWdetails = new getWdetails();
                        getUWdetails.execute(user_id);
                        /*if (holder1.im_reply.getVisibility() == View.VISIBLE) {
                            holder1.im_reply.setVisibility(View.GONE);
                        }*/
                    }
                    else if(updated_status == 4) {
                        Toast.makeText(_context, server_message, Toast.LENGTH_SHORT).show();
                        if(lang.contentEquals("Arabic")){
                            holder1.im_reply.setText(R.string.on_going_ar);

                        }
                        else{
                            holder1.im_reply.setText("Ongoing");

                        }
                        wpage = "user_side";
                        Log.i("userid", user_id);
                        getWdetails getUWdetails = new getWdetails();
                        getUWdetails.execute(user_id);
                        /*if (holder1.im_agree.getVisibility() == View.VISIBLE) {
                            holder1.im_agree.setVisibility(View.GONE);
                        } else if (holder1.im_reply.getVisibility() == View.VISIBLE) {
                            holder1.im_reply.setVisibility(View.GONE);
                        } else if (holder1.gv_feedback.getVisibility() == View.VISIBLE) {
                            holder1.gv_feedback.setVisibility(View.GONE);
                        }*/
                    }
                    else if(updated_status == 5) {
                        Toast.makeText(_context, server_message, Toast.LENGTH_SHORT).show();
                        if(lang.contentEquals("Arabic")){
                            holder1.im_reply.setText(R.string.completed_ar);

                        }
                        else{
                            holder1.im_reply.setText("Completed");

                        }
                        /*if (holder1.im_agree.getVisibility() == View.VISIBLE) {
                            holder1.im_agree.setVisibility(View.GONE);
                        } else if (holder1.im_reply.getVisibility() == View.VISIBLE) {
                            holder1.im_reply.setVisibility(View.GONE);
                        } else if (holder1.gv_feedback.getVisibility() == View.VISIBLE) {
                            holder1.gv_feedback.setVisibility(View.GONE);
                        }*/
                    }
                }
                else{
                    Constants.noInternetDialouge(_context, "Failed to update");
                }
            }

        }
    }

    public void calltoupdate(String id, String status,String call){
        callPage=call;
        ConfirmToProp confirmToProp = new ConfirmToProp();
        confirmToProp.execute(id, status);
    }
    /**
     * Async task to get wallet balance from  camp table from server
     * */
    private class getWdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get Wallet Balance";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(_context, "Checking wallet", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userid = params[0];
                InputStream in = null;
                int resCode = -1;
                String link=null;
                if(wpage.contentEquals("user_side")) {
                    link = Constants.ONLINEURL + Constants.USER_BALANCE;
                }
                else if(wpage.contentEquals("sp_home")){
                    link = Constants.ONLINEURL + Constants.SHOP_BALANCE;

                }
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
                if(wpage.contentEquals("user_side")) {
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_id", _userid);
                }
                else if(wpage.contentEquals("sp_home")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("shop_id", _userid);
                }

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
                 "userWallets": [
                 {
                 "id": 7,
                 "user_id": 10,
                 "debit": 0,
                 "credit": 10,
                 "balance": 10,
                 "created": "2017-12-03T10:28:29+05:30",
                 "modified": "2017-12-03T10:28:29+05:30",
                 "user": {
                 "id": 10,
                 "name": "avinash pathak",
                 "email": "avinasha@yahoo.com",
                 "mobile": "7205674061",
                 "photo": null,
                 "created": "1988-01-23T00:00:00+05:30",
                 "modified": "1988-01-23T00:00:00+05:30",
                 "usertype": "test",
                 "fcm_id": null
                 }
                 }
                 ]
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONArray serviceListArray;
                    if(wpage.contentEquals("user_side")) {
                        serviceListArray = res.getJSONArray("userWallets");
                        if(serviceListArray.length()<=0) {
                            server_status = 0;
                        }
                        else{
                            server_status = 1;
                            for (int i = 0; i < serviceListArray.length(); i++) {
                                JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                                String id = o_list_obj.getString("id");
                                user_balance = o_list_obj.getDouble("balance");
                            }
                        }
                    }
                    else if(wpage.contentEquals("sp_home")){
                        serviceListArray = res.getJSONArray("wallets");
                        if(serviceListArray.length()<=0) {
                            server_status = 0;
                        }
                        else{
                            server_status = 1;
                            for (int i = 0; i < serviceListArray.length(); i++) {
                                JSONObject o_list_obj = serviceListArray.getJSONObject(i);
                                String id = o_list_obj.getString("id");
                                shop_balance = o_list_obj.getDouble("balance");
                            }
                        }
                    }


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
                if(updated_status==4 && wpage.contentEquals("user_side")){
                    Toast.makeText(_context, "Service started", Toast.LENGTH_SHORT).show();
                    wpage = "user_side";
                    Transactwallet transactwallet = new Transactwallet();
                    transactwallet.execute(user_id, String.valueOf(HomeActivity.min_post_charge), String.valueOf(user_balance+HomeActivity.min_post_charge), "0");
                }
                if (updated_status==4 && wpage.contentEquals("sp_home")) {
                    if (shop_balance >= SPHome.min_sp_balance) {
                        wpage = "sp_home";
                        Transactwallet transactwallet = new Transactwallet();
                        transactwallet.execute(shop_id, "0", String.valueOf(shop_balance -SPHome.min_service_charge ), String.valueOf(SPHome.min_service_charge));
                        //Toast.makeText(_context,"go aahead",Toast.LENGTH_LONG).show();
                            /*wpage = "user_side";
                            Log.i("userid", user_id);
                            getWdetails getUWdetails = new getWdetails();
                            getUWdetails.execute(user_id);*/
                    } else {
                        Constants.noInternetDialouge(_context, "You don't have sufficient amount in wallet");
                    }
                }
                if(updated_status==3){
                    Toast.makeText(_context, "OTP Generated", Toast.LENGTH_SHORT).show();
                    if (user_balance >= HomeActivity.min_user_balance) {
                        wpage = "user_side";
                        Transactwallet transactwallet = new Transactwallet();
                        transactwallet.execute(user_id, "0", String.valueOf(user_balance - HomeActivity.min_post_charge), String.valueOf(HomeActivity.min_post_charge));
                    }
                    else{
                        Constants.noInternetDialouge(_context, "You don't have sufficient amount in wallet");

                    }

                }
                else {
                     /*else if (wpage.contentEquals("user_side")) {
                        if (user_balance >= HomeActivity.min_user_balance) {
                            wpage = "sp_home";
                            Transactwallet transactwallet = new Transactwallet();
                            transactwallet.execute(shop_id, "0", String.valueOf(shop_balance -SPHome.min_service_charge ), String.valueOf(SPHome.min_service_charge));
                            //Toast.makeText(_context,"go aahead",Toast.LENGTH_LONG).show();
                        } else {
                            // this is for insufficient balance in the user side
                            Constants.noInternetDialouge(_context, "User is not ready to take the service");

                        }

                    }*/
                }


              //  Toast.makeText(_context,shop_balance+"/"+user_balance,Toast.LENGTH_LONG).show();
            }
            else {
                if(lang.contentEquals("Arabic")){
                    Toast.makeText(_context, "يرجى إعادة شحن محفظتك", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(_context, "Please recharge your wallet", Toast.LENGTH_SHORT).show();

                }


            }

        }
    }
        /**
         *
         * Async task to Update the wallet
         * */
        private class Transactwallet extends AsyncTask<String, Void, Void> {

            private static final String TAG = "update wallet";
            int wallet_status;
            String server_message;
            ProgressDialog progressDialog=null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(progressDialog == null) {
                    progressDialog = ProgressDialog.show(_context, "Updating wallet", "Please wait...");
                }
            }
            @Override
            protected Void doInBackground(String... params) {

                try {
                    String _userid = params[0];
                    String _recharge_amount = params[1];
                    String _debit_amount = params[3];
                    String _balance_amount = params[2];
                    InputStream in = null;
                    int resCode = -1;

                    String link = null;
                    if(wpage.contentEquals("sp_home")) {
                        link = Constants.ONLINEURL + Constants.SHOP_WALLLET_UPDATE;
                    }
                    else if(wpage.contentEquals("user_side")){
                        link = Constants.ONLINEURL + Constants.USER_WALLLET_UPDATE;

                    }
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
                    if(wpage.contentEquals("sp_home")) {
                        builder = new Uri.Builder()
                                .appendQueryParameter("shop_id", _userid)
                                .appendQueryParameter("debit", _debit_amount)
                                .appendQueryParameter("credit", _recharge_amount)
                                .appendQueryParameter("remarks", "Service Insurance")
                                .appendQueryParameter("balance", _balance_amount);
                    }
                    else if(wpage.contentEquals("user_side")){
                        builder = new Uri.Builder()
                                .appendQueryParameter("user_id", _userid)
                                .appendQueryParameter("debit", _debit_amount)
                                .appendQueryParameter("credit", _recharge_amount)
                                .appendQueryParameter("remarks", "Service Insurance")
                                .appendQueryParameter("balance", _balance_amount);
                    }

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
                     "status": 1,
                     "message": "Data inserted successfully"
                     }
                     * */

                    if(response != null && response.length() > 0) {
                        JSONObject res = new JSONObject(response.trim());
                        JSONObject j_obj=res.getJSONObject("res");
                        wallet_status = j_obj.optInt("status");
                        if(wallet_status==1) {
                            if(lang.contentEquals("Arabic")){
                                server_message="تم تحديث المحفظة";

                            }
                            else{
                                server_message="Wallet Updated";

                            }
                        }
                        else{
                            if(lang.contentEquals("Arabic")){
                                server_message="لا يمكن تحديث المحفظة";

                            }
                            else{
                                server_message="Wallet can't be Updated";

                            }

                        }

                    }

                    return null;
                } catch(Exception exception){
                    server_message="Wallet error";
                    Log.e(TAG, "SynchMobnum : doInBackground", exception);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void user) {
                super.onPostExecute(user);
                progressDialog.dismiss();
                if(wallet_status==1){
                    if(updated_status==4 && wpage.contentEquals("user_side")) {
                        wpage="sp_home";
                        getWdetails getWdetails=new getWdetails();
                        getWdetails.execute(shop_id);

                        if (lang.contentEquals("Arabic")) {
                            Toast.makeText(_context, "محفظة المستخدم المحدثة", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(_context, "User's wallet updated", Toast.LENGTH_LONG).show();

                        }
                    }

                   /* if(wpage.contentEquals("sp_home")) {
                        wpage = "user_side";
                        Transactwallet transactwallet = new Transactwallet();
                        transactwallet.execute(user_id, "0", String.valueOf(user_balance - HomeActivity.min_post_charge), String.valueOf(HomeActivity.min_post_charge));
                    }
                    //Toast.makeText(PostActivity.this,"Hello",Toast.LENGTH_LONG).show();
                    if(updated_status==4){
                        if(lang.contentEquals("Arabic")){
                            Toast.makeText(_context,"محفظة المستخدم المحدثة",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(_context,"User's wallet updated",Toast.LENGTH_LONG).show();

                        }

                    }
                    else {
                         OTP = String.valueOf(Constants.generatePIN());
                        ConfirmToProp confirmToProp = new ConfirmToProp();
                        confirmToProp.execute(propsal_id, "3");
                    }*/
                }
                else{
                    Constants.noInternetDialouge(_context,"Action failed");
                }
            }
        }


}
