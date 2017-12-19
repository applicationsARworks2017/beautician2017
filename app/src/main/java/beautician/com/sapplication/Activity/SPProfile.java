package beautician.com.sapplication.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;
import beautician.com.sapplication.Utils.MultipartUtility;

import static beautician.com.sapplication.Utils.Constants.modifyOrientation;

public class SPProfile extends AppCompatActivity {
    Button button,editsave;
    String shop_id;
    String id,shopname,address,latlong,photo1,photo2,photo3,email,mobile,reviews,ratings;
    ImageView pic1,pic2,pic3;
    EditText name_value,tv_phone_value,tv_email_value,tv_add_value;
    Boolean editable=false;
    RelativeLayout sp_profile_rel;
    int imageclick;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    File imageFile,imgfile1,imgfile2,imgfile3;
    Uri picUri=null;
    Boolean picAvailable=false;
    private static final int CAMERA_REQUEST = 1888;
    String imPath,server_message;
    int server_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spprofile);
        button=(Button)findViewById(R.id.logout_sp);
        editsave=(Button)findViewById(R.id.editsave);
        name_value=(EditText) findViewById(R.id.name_value);
        tv_phone_value=(EditText) findViewById(R.id.tv_phone_value);
        tv_email_value=(EditText) findViewById(R.id.tv_email_value);
        tv_add_value=(EditText) findViewById(R.id.tv_add_value);
        sp_profile_rel=(RelativeLayout) findViewById(R.id.sp_profile_rel);
        pic1=(ImageView)findViewById(R.id.photo1);
        pic2=(ImageView)findViewById(R.id.photo2);
        pic3=(ImageView)findViewById(R.id.photo3);
        name_value.setEnabled(false);
        tv_phone_value.setEnabled(false);
        tv_email_value.setEnabled(false);
        tv_add_value.setEnabled(false);


        shop_id = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        getThedetails();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent intent=new Intent(SPProfile.this,Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        editsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editsave.getText().toString().toLowerCase().contentEquals("edit")) {
                    editable = true;
                    editsave.setText("Save");
                    name_value.setEnabled(true);
                    tv_phone_value.setEnabled(true);
                    tv_email_value.setEnabled(true);
                    tv_add_value.setEnabled(true);
                }
                else{

                    savedata();
                    //Toast.makeText(getActivity(),"Saved",Toast.LENGTH_LONG).show();
                }
            }
        });
        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable==true) {
                    imageclick = 1;
                    captureImage(1);
                }
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable==true) {
                    imageclick = 2;
                    captureImage(2);
                }
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable==true) {
                    imageclick = 3;
                    captureImage(3);
                }
            }
        });
    }
    private void captureImage(int i) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (cameraIntent.resolveActivity(SPProfile.this.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile(i);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(SPProfile.this,
                            "beautician.com.sapplication",
                            photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        }
        else{
            imPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
            imageFile = new File(imPath);
            picUri = Uri.fromFile(imageFile); // convert path to Uri
            cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, picUri );
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

    }
    private File createImageFile(int i) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =SPProfile.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imPath = image.getAbsolutePath();
        imageFile = new File(imPath);
        if(i==1){
            imgfile1=imageFile;
        }
        else if(i==2) {
            imgfile2=imageFile;
        }
        else{
            imgfile3=imageFile;
        }
        picUri = Uri.fromFile(image); // convert path to Uri
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // imPath=picUri.getPath();
            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(SPProfile.this.getContentResolver(),picUri);
                Bitmap c_photo= Bitmap.createScaledBitmap(photo,300,300,true);
                Bitmap perfectImage=modifyOrientation(c_photo,imPath);

                picAvailable=true;
                if(imageclick==1) {
                    pic1.setImageBitmap(perfectImage);
                }
                else if(imageclick==2){
                    pic2.setImageBitmap(perfectImage);
                }
                else{
                    pic3.setImageBitmap(perfectImage);
                }
                //    profileImage=img1.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void savedata() {
        String shop_name=name_value.getText().toString().trim();
        String shop_number=tv_phone_value.getText().toString().trim();
        String shop_mail=tv_email_value.getText().toString().trim();
        String shop_address=tv_add_value.getText().toString().trim();
        if(shop_name.length()<=0){
            showSnackBar("Enter Name");
        }
        else if(shop_number.length()<0){
            showSnackBar("Enter Phone");
        }
        else if(shop_mail.length()<=0){
            showSnackBar("Enter Mail");
        }
        else if(!shop_mail.matches(emailPattern)){
            showSnackBar("Enter Valid Email");
        }
        else if(shop_address.length()<=0){
            showSnackBar("Enter Address");
        }
        else{
            EditSP asyntask=new EditSP();
            asyntask.execute(shop_name,shop_number,shop_mail,shop_address,shop_id);
        }
    }

    private void getThedetails() {
        ViewShops viewShops=new ViewShops();
        viewShops.execute(shop_id);
    }
    private class ViewShops extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;
        ProgressDialog progressDialog=null;

        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(SPProfile.this, "Loading", "Please wait...");
            }            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _SID = params[0];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.SHOP_DETAILS ;
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
                        .appendQueryParameter("shop_id", _SID);

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
               *  "shop": {
        "id": 11,
        "shopname": "MARS",
        "address": "bangalore",
        "latitudelongitude": "13.0356129,77.6332397",
        "photo1": "file15115175501043044287.jpg",
        "photo2": "file15115175501043044287.jpg",
        "photo3": "file15115175501043044287.jpg",
        "email": "amaresh.samantaray4@gmail.com",
        "mobile": "7026405551",
        "no_of_reviews": 14,
        "avg_rating": 3.39,
        "created": "2017-11-21T14:04:23+05:30",
        "modified": "2017-11-21T14:04:23+05:30",
               * */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("shop");
                    shopname=j_obj.getString("shopname");
                    id=j_obj.getString("id");
                    address=j_obj.getString("address");
                    latlong=j_obj.getString("latitudelongitude");
                    photo1=j_obj.getString("photo1");
                    photo2=j_obj.getString("photo2");
                    photo3=j_obj.getString("photo3");
                    email=j_obj.getString("email");
                    mobile=j_obj.getString("mobile");
                    reviews=j_obj.getString("no_of_reviews");
                    ratings=j_obj.getString("avg_rating");

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
            setValues();
            progressDialog.dismiss();
        }
    }

    private void setValues() {
     //   name_value,tv_phone_value,tv_email_value,tv_add_value
        name_value.setText(shopname);
        tv_phone_value.setText(mobile);
        tv_email_value.setText(email);
        tv_add_value.setText(address);
        if(!photo1.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.PICURL+photo1).into(pic1);
        } if(!photo2.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.PICURL+photo2).into(pic2);
        } if(!photo3.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.PICURL+photo3).into(pic3);
        }
    }
    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(sp_profile_rel, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#7a2da6"));

        snackbar.show();
    }
    /**
     * Service Provider Edit
     */
    private class EditSP extends AsyncTask<String, Void, Void> {

        String TAG = "Edit SP";
        private boolean is_success = false;
        private ProgressDialog progressDialog = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(SPProfile.this, "Loading", "Please wait...");
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "";
            requestURL = Constants.ONLINEURL + Constants.SHOP_EDIT;

            try {
                String _phone = params[1];
                String _name = params[0];
                String _email = params[2];
                String _address = params[3];
                String _shop_id = params[4];
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("mobile", _phone);
                multipart.addFormField("shopname", _name);
                multipart.addFormField("email", _email);
                multipart.addFormField("address", _address);
                multipart.addFormField("id", _shop_id);
                if (imgfile1 != null) {
                    multipart.addFilePart("photo1", imgfile1);
                }
                if (imgfile2 != null) {
                    multipart.addFilePart("photo2", imgfile2);
                }
                if (imgfile3 != null) {
                    multipart.addFilePart("photo3", imgfile3);

                }
                List<String> response = multipart.finish();
                System.out.println("SERVER REPLIED:");
                String res = "";
                for (String line : response) {
                    res = res + line + "\n";
                }
                Log.i(TAG, res);

                /*
                    "status": 1,
    "message": "Successfully inserted"(
                * */

                if (res != null && res.length() > 0) {
                    JSONObject res_server = new JSONObject(res.trim());
                    JSONObject newObj = new JSONObject(String.valueOf(res_server.getJSONObject("res")));
                    server_status = newObj.optInt("status");
                    if (server_status == 1) {
                        server_message = "Edit Successful";

                    } else {
                        server_message = "Sorry !! Entry failed";
                    }
                }
            } catch (JSONException e) {
                server_message = "Network Error";
                e.printStackTrace();
            } catch (IOException e) {
                server_message = "Network Error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            showSnackBar(server_message);
            progressDialog.dismiss();
            if(server_status==1){
                editable = false;
                editsave.setText("Edit");
                name_value.setEnabled(false);
                tv_add_value.setEnabled(false);
                tv_email_value.setEnabled(false);
                tv_phone_value.setEnabled(false);
            }
        }
    }
}
