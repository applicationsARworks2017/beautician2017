package beautician.com.sapplication.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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

import beautician.com.sapplication.Activity.HomeActivity;
import beautician.com.sapplication.Activity.Login_Activity;
import beautician.com.sapplication.R;
import beautician.com.sapplication.SplashScreen;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;
import beautician.com.sapplication.Utils.MultipartUtility;

import static android.app.Activity.RESULT_OK;
import static beautician.com.sapplication.Utils.Constants.modifyOrientation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button button,editsave;
    EditText name_value,et_phone_value,et_email_value;
    Boolean editable=false;
    private File imageFile;
    private static final int CAMERA_REQUEST = 1888;
    Uri picUri = null;
    String imPath;
    String user_id;
    File photoFile = null;
    Boolean picAvailable = false;
    int photo_status=0;
    CircularImageView prifilimage;
    RelativeLayout rel_profile;
    int server_status,filechooser;
    String server_response;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String Uname,id,photo,email,mobile,lang;
    TextView hd_name,hd_phone,hd_email,lang_arabic,lang_english;
    private static int RESULT_LOAD_IMAGE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Profile() {
        // Required empty public constructor
    }
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        user_id = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        lang = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);

        name_value=(EditText) v.findViewById(R.id.name_value);
        et_phone_value=(EditText) v.findViewById(R.id.et_phone_value);
        et_email_value=(EditText) v.findViewById(R.id.et_email_value);
        button=(Button)v.findViewById(R.id.logout);
        prifilimage=(CircularImageView)v.findViewById(R.id.prifilimage);
        rel_profile=(RelativeLayout) v.findViewById(R.id.rel_profile);
        editsave=(Button)v.findViewById(R.id.editsave);
        hd_name=(TextView)v. findViewById(R.id.hd_name);
        hd_phone=(TextView)v. findViewById(R.id.hd_phone);
        hd_email=(TextView)v. findViewById(R.id.hd_email);
        lang_english=(TextView)v. findViewById(R.id.tv_english);
        lang_arabic=(TextView)v. findViewById(R.id.tv_arabic);
        name_value.setEnabled(false);
        et_phone_value.setEnabled(false);
        et_email_value.setEnabled(false);

        if(lang.contentEquals("Arabic")){
            hd_name.setText("الاسم");
            hd_phone.setText("رقم الجوال");
            hd_email.setText("البريد الإلكتروني");
            button.setText("خروج");
            editsave.setText("تعديل");
        }
        else{
            hd_name.setText("Name");
            hd_phone.setText("Phone");
            hd_email.setText("Email");
            button.setText("Logout");
            editsave.setText("Edit");
        }

        lang_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Change Keyboard");
                builder.setMessage("For change the keyboard long press to space bar");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //check own balance
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                lang="English";
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.LANG_TYPE, lang);
                editor.commit();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);



            }
        });
         lang_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Change Keyboard");
                builder.setMessage("For change the keyboard long press to space bar");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //check own balance
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                lang="Arabic";
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString(Constants.LANG_TYPE, lang);
                editor.commit();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });
        prifilimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editable==true) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.chooseaction);
                    TextView choosecamera=(TextView) dialog.findViewById(R.id.select_camera);
                    TextView choosegeller=(TextView) dialog.findViewById(R.id.select_gallery);
                    dialog.show();
                   // captureImage();
                    choosecamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            filechooser=1;
                            captureImage("camera");
                        }
                    });
                    choosegeller.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            filechooser=2;
                            captureImage("gallery");
                        }
                    });
                }

            }
        });
       // getUserDetails();
        setValues();



        editsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editsave.getText().toString().toLowerCase().contentEquals("edit")) {
                    editable = true;
                    editsave.setText("Save");
                    name_value.setEnabled(true);
                    et_phone_value.setEnabled(true);
                    et_email_value.setEnabled(true);
                }
                else{

                    savedata();
                    //Toast.makeText(getActivity(),"Saved",Toast.LENGTH_LONG).show();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent intent=new Intent(getActivity(),Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return v;
    }
    private void getUserDetails() {
        if(CheckInternet.getNetworkConnectivityStatus(getActivity())){
            userView();
        }
        else{
            Constants.noInternetDialouge(getActivity(),"No Internet");
        }
    }
    private void userView() {
        ViewUser viewUser=new ViewUser();
        viewUser.execute(user_id);
    }
    private void savedata() {
        String name=name_value.getText().toString().trim();
        String phone=et_phone_value.getText().toString().trim();
        String email=et_email_value.getText().toString().trim();
        if(filechooser==2){
            if (prifilimage.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) prifilimage.getDrawable()).getBitmap();
                imageFile = persistImage(bitmap, name);
            }
        }
        if(name.contains("") && name.length()<=0){
            showSnackBar("Enter Name ");
        }
        else if(phone.contains("") && phone.length()<=0){
            showSnackBar("Enter Phone");
        }
        else if(email.contains("") && email.length()<=0){
            showSnackBar("Enter email");
        }
        else if(!email.matches(emailPattern)){
            showSnackBar("Enter Valid Email");
        }
        else {
            if (prifilimage.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) prifilimage.getDrawable()).getBitmap();
                imageFile = persistImage(bitmap, name);
            }
            new EditProfile().execute(name, phone, email,user_id);
        }
    }
    private  File persistImage(Bitmap bitmap, String name) {
        File filesDir = getActivity().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }
    private void captureImage(String action) {
        if(action.contentEquals("camera")) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "beautician.com.sapplication",
                                photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            } else {
                imPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                imageFile = new File(imPath);
                picUri = Uri.fromFile(imageFile); // convert path to Uri
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
        else{
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imPath = image.getAbsolutePath();
        imageFile = new File(imPath);
        picUri = Uri.fromFile(image); // convert path to Uri
        return image;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(filechooser==1) {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                // imPath=picUri.getPath();
                // Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    Bitmap c_photo = Bitmap.createScaledBitmap(photo, 300, 300, true);
                    Bitmap perfectImage = modifyOrientation(c_photo, imPath);
                    picAvailable = true;
                    prifilimage.setImageBitmap(perfectImage);
                    photo_status = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        else{
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                prifilimage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }

        }
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
    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(rel_profile, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#7a2da6"));

        snackbar.show();
    }
    /**
     * User Profile edit
     */
    private class EditProfile extends AsyncTask<String, Void, Void> {

        String TAG = "Edit Profile";
        private boolean is_success = false;
        private ProgressDialog progressDialog = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(getActivity(), "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...");

                }
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "";
            //requestURL = Config.API_BASE_URL + Config.POST_CITIZEN_NEWS;
            requestURL = Constants.ONLINEURL + Constants.USER_EDIT;

            try {
                String _phone = params[1];
                String _name = params[0];
                String _email = params[2];
                String _userid = params[3];
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("mobile", _phone);
                multipart.addFormField("id", _userid);
                multipart.addFormField("name", _name);
                multipart.addFormField("email", _email);

                // after completion of image work please enable this
                if (imageFile != null) {
                    multipart.addFilePart("photo", imageFile);
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
                        server_response = "Edit Profile Completed";

                    } else {
                        server_response = "Sorry !! Entry failed";
                    }
                }
            } catch (JSONException e) {
                server_response = "Network Error";
                e.printStackTrace();
            } catch (IOException e) {
                server_response = "Network Error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            showSnackBar(server_response);
            progressDialog.dismiss();
            if(server_status==1){
                editable = false;
                editsave.setText("Edit");
                name_value.setEnabled(false);
                et_phone_value.setEnabled(false);
                et_email_value.setEnabled(false);
                getUserDetails();

            }
        }
    }
    private class ViewUser extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;
        ProgressDialog progressDialog=null;

        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...");
            }            super.onPreExecute();

            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _SID = params[0];
                InputStream in = null;
                int resCode = -1;

                String link =Constants.ONLINEURL+ Constants.USER_DETAILS ;
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
                        .appendQueryParameter("user_id", _SID);

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
               * {
    "user": {
        "id": 10,
        "name": "avinash pathak",
        "email": "avinasha@yahoo.com",
        "mobile": "7205674061",
        "photo": null,
        "created": "1988-01-23T00:00:00+05:30",
        "modified": "1988-01-23T00:00:00+05:30",
        "usertype": "test"
    }
}
               * */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject j_obj=res.getJSONObject("user");
                    Uname=j_obj.getString("name");
                    id=j_obj.getString("id");
                    photo=j_obj.getString("photo");
                    email=j_obj.getString("email");
                    mobile=j_obj.getString("mobile");

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

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.USER_ID, id);
            editor.putString(Constants.USER_TYPE, "custumer");
            editor.putString(Constants.USER_NAME, Uname);
            editor.putString(Constants.USER_EMAIL, email);
            editor.putString(Constants.USER_MOBILE, mobile);
            editor.putString(Constants.USER_PHOTO, photo);
            //name,email,mobile,photo
            editor.commit();
            setValues();
            progressDialog.dismiss();
        }
    }

    private void setValues() {
        //TextView shop_name,shop_email,shop_address,shop_reviws;
        String user_name = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_NAME, null);
        String user_email = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_EMAIL, null);
        String user_mobile = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_MOBILE, null);
        String user_photo = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_PHOTO, null);


        name_value.setText(user_name);
        et_email_value.setText(user_email);
        et_phone_value.setText(user_mobile);
        if( user_photo==null || user_photo.contentEquals("null") || user_photo.contentEquals("")) {
        }
        else{
            Picasso.with(getActivity()).load(Constants.PICURL+user_photo)
                    .resize(300,300).into(prifilimage);

        }

    }
}
