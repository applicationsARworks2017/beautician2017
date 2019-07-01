package beautician.com.sapplication.Tabs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import beautician.com.sapplication.Activity.Login_Activity;
import beautician.com.sapplication.Activity.MapActivity;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.CheckInternet;
import beautician.com.sapplication.Utils.Constants;
import beautician.com.sapplication.Utils.MultipartUtility;

import static android.app.Activity.RESULT_OK;
import static beautician.com.sapplication.Utils.Constants.modifyOrientation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SPSignup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SPSignup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SPSignup extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context _context;
    RelativeLayout sp_signup;
    private OnFragmentInteractionListener mListener;
    public static Double latitude,longitude;
    String sign_lat,sign_long;

    private EditText et_sp_name,et_sp_phone,et_sp_mail,et_sp_pass;
    public static EditText et_latlong;
    public static EditText et_sp_address;
    private ImageView iv_pic1,iv_pic2,iv_pic3,iv_pic4;
    Button bt_setails_submit;
    ProgressBar loader_signup;
    private int server_status;
    private String server_message;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int imageclick,filechooser,gal1,gal2,gal3,gal4;
    private static final int CAMERA_REQUEST = 1888;
    String imPath;
    Geocoder geocoder;
    File imageFile,imgfile1,imgfile2,imgfile3,imgfile4;
    Uri picUri=null;
    Boolean picAvailable=false;
    KeyListener variable;
    TextView tv_gomap;
    String lang;
    int RESULT_LOAD_IMAGE=1;


    public SPSignup() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SPSignup(String lat, String lng) {
        this.sign_lat=lat;
        this.sign_long=lng;

    }



    public static SPSignup newInstance(String param1, String param2) {
        SPSignup fragment = new SPSignup();
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
        View v=inflater.inflate(R.layout.fragment_spsignup, container, false);
        lang = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);


        loader_signup=(ProgressBar)v.findViewById(R.id.loader_signup);
        et_latlong=(EditText)v.findViewById(R.id.et_sp_latlong);
       // et_latlong.setText(latitude+","+longitude);
        et_sp_name=(EditText)v.findViewById(R.id.et_shop_name);
        et_sp_phone=(EditText)v.findViewById(R.id.et_sp_phone);
        et_sp_address=(EditText)v.findViewById(R.id.et_sp_address);
      //  et_sp_address.setFocusable(false);
        et_sp_pass=(EditText)v.findViewById(R.id.et_sp_password);
        et_sp_mail=(EditText)v.findViewById(R.id.et_sp_email);
        iv_pic1=(ImageView)v.findViewById(R.id.img1);
        iv_pic2=(ImageView)v.findViewById(R.id.img2);
        iv_pic3=(ImageView)v.findViewById(R.id.img3);
        iv_pic4=(ImageView)v.findViewById(R.id.img4);
        sp_signup=(RelativeLayout)v.findViewById(R.id.sp_signup);
        bt_setails_submit=(Button)v.findViewById(R.id.bt_sp_submit);
        tv_gomap=(TextView)v.findViewById(R.id.tv_gomap);

        if(lang.contentEquals("Arabic")){
            et_sp_name.setHint("اسم المحل");
            et_sp_phone.setHint("رقم الجوال");
            et_sp_pass.setHint("الرقم السري");
            et_sp_mail.setHint("البريد الاكتروني");
            et_sp_address.setHint("عنوان المحل");
            bt_setails_submit.setHint("موافق");

        }
        else{
            et_sp_name.setHint("Shop Name");
            et_sp_phone.setHint("Phone Number");
            et_sp_pass.setHint("Password");
            et_sp_mail.setHint("Email");
            et_sp_address.setHint("Shop Address");
            bt_setails_submit.setHint("submit");

        }

        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        if(sign_lat==null || sign_long==null){

        }
        else {
            try {
                addresses = geocoder.getFromLocation(Double.valueOf(sign_lat), Double.valueOf(sign_long), 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                et_sp_address.setText(address + "," + city + "," + state);
                et_latlong.setText(sign_lat + "," + sign_long);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bt_setails_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.getNetworkConnectivityStatus(getActivity())){
                   // new getcategoryList().execute();
                    validate_fields();
                }
                else{
                    Constants.noInternetDialouge(_context,"Kindly Check Your Internet Connection");
                }
            }
        });
        tv_gomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("page","signup");

                startActivity(intent);

            }
        });
        iv_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*imageclick=1;
                captureImage(1);*/
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.chooseaction);
                TextView choosecamera=(TextView) dialog.findViewById(R.id.select_camera);
                TextView choosegeller=(TextView) dialog.findViewById(R.id.select_gallery);
                dialog.show();
                choosecamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        imageclick = 1;
                        filechooser=1;
                        captureImage(1,"camera");
                    }
                });
                choosegeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        imageclick = 1;
                        filechooser=2;
                        gal1=1;
                        captureImage(1,"gallery");
                    }
                });
            }
        });
        iv_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*imageclick=2;
                captureImage(2);*/
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.chooseaction);
                TextView choosecamera=(TextView) dialog.findViewById(R.id.select_camera);
                TextView choosegeller=(TextView) dialog.findViewById(R.id.select_gallery);
                dialog.show();
                choosecamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        imageclick = 2;
                        filechooser=1;
                        captureImage(2,"camera");
                    }
                });
                choosegeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        imageclick = 2;
                        filechooser=2;
                        gal2=1;
                        captureImage(2,"gallery");
                    }
                });
            }
        });
        iv_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* imageclick=3;
                captureImage(3);*/
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.chooseaction);
                TextView choosecamera=(TextView) dialog.findViewById(R.id.select_camera);
                TextView choosegeller=(TextView) dialog.findViewById(R.id.select_gallery);
                dialog.show();
                choosecamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        imageclick = 3;
                        filechooser=1;
                        captureImage(3,"camera");
                    }
                });
                choosegeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        imageclick = 3;
                        filechooser=2;
                        gal3=1;
                        captureImage(3,"gallery");
                    }
                });
            }
        });
        iv_pic4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*imageclick=4;
                    captureImage(4);*/
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.chooseaction);
                    TextView choosecamera=(TextView) dialog.findViewById(R.id.select_camera);
                    TextView choosegeller=(TextView) dialog.findViewById(R.id.select_gallery);
                    dialog.show();
                    choosecamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            imageclick = 4;
                            filechooser=1;
                            captureImage(4,"camera");
                        }
                    });
                    choosegeller.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            imageclick = 4;
                            filechooser=2;
                            gal4=1;
                            captureImage(4,"gallery");
                        }
                    });

                }
        });


        return v;
    }

    private void captureImage(int i, String action) {
        if(action.contentEquals("camera")) {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile(i);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
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
    private File createImageFile(int i) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        else if(i==3) {
            imgfile3=imageFile;
        }
        else{
            imgfile4=imageFile;
        }
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
                  //  Bitmap c_photo = Bitmap.createScaledBitmap(photo, 300, 300, true);
                    //Bitmap perfectImage = modifyOrientation(photo, imPath);
                    Bitmap perfectImage = photo;

                    picAvailable = true;
                    if (imageclick == 1) {
                        iv_pic1.setImageBitmap(perfectImage);
                    } else if (imageclick == 2) {
                        iv_pic2.setImageBitmap(perfectImage);
                    } else if (imageclick == 3) {
                        iv_pic3.setImageBitmap(perfectImage);
                    } else {
                        iv_pic4.setImageBitmap(perfectImage);
                    }
                    //    profileImage=img1.toString();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        else {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                if (imageclick == 1) {
                    iv_pic1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                } else if (imageclick == 2) {
                    iv_pic2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                } else if (imageclick == 3) {
                    iv_pic3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                } else if (imageclick == 4) {
                    iv_pic4.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }


            }
        }
    }
    private  File persistImage(Bitmap bitmap, String name) {
        File filesDir = getActivity().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    private void validate_fields() {
        String shop_name=et_sp_name.getText().toString().trim();
        String shop_number=et_sp_phone.getText().toString().trim();
        String shop_mail=et_sp_mail.getText().toString().trim();
        String shop_address=et_sp_address.getText().toString().trim();
        String shop_lat_long=et_latlong.getText().toString().trim();
        String shop_password=et_sp_pass.getText().toString().trim();

        if(gal1==1) {
            if (iv_pic1.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) iv_pic1.getDrawable()).getBitmap();
                imgfile1 = persistImage(bitmap, shop_name+"1");
            }
        }
        if(gal2==1) {
            if (iv_pic2.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) iv_pic2.getDrawable()).getBitmap();
                imgfile2 = persistImage(bitmap, shop_name+"2");
            }
        }
        if(gal3==1) {
            if (iv_pic3.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) iv_pic3.getDrawable()).getBitmap();
                imgfile3 = persistImage(bitmap, shop_name+"3");
            }
        }
        if(gal4==1) {
            if (iv_pic4.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) iv_pic4.getDrawable()).getBitmap();
                imgfile4 = persistImage(bitmap, shop_name+"4");
            }
        }

        if(shop_name.length()<=0){
            showSnackBar("Enter Name");
        }
        else if(shop_number.length()<0){
            showSnackBar("Enter Phone");
        }
        else if(shop_mail.length()<=0){
            showSnackBar("Enter Mail");
        }
        else if(Constants.validate(shop_mail)==false){
            showSnackBar("Enter Valid Email");
        }
        else if(shop_address.length()<=0){
            showSnackBar("Enter Address");
        }
        else if(shop_password.length()<=0){
            showSnackBar("Enter Password");
        }
        else if(shop_lat_long.length()<=0){
            showSnackBar("Latitude Longitude not found");
        }
        else{
            spSignupAsyntask asyntask=new spSignupAsyntask();
            asyntask.execute(shop_name,shop_number,shop_mail,shop_address,shop_password,shop_lat_long);
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
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(sp_signup, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#7a2da6"));

        snackbar.show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /**
     * Service Provider Registration
     */
    private class spSignupAsyntask extends AsyncTask<String, Void, Void> {

        String TAG = "Sign Up";
        private boolean is_success = false;
        private ProgressDialog progressDialog = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
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
            requestURL = Constants.ONLINEURL + Constants.SHOP_REGISTRATION;

            try {
                String _phone = params[1];
                String _name = params[0];
                String _email = params[2];
                String _password = params[4];
                String _address = params[3];
                String _latlong = params[5];
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("mobile", _phone);
                multipart.addFormField("shopname", _name);
                multipart.addFormField("email", _email);
                multipart.addFormField("password", _password);
                multipart.addFormField("address", _address);
                multipart.addFormField("latitudelongitude", _latlong);
                multipart.addFormField("usertype", "serviceprovider");
                if(imgfile1!=null){
                    multipart.addFilePart("photo1",imgfile1 );
                }
                if(imgfile2!=null){
                    multipart.addFilePart("photo2",imgfile2 );
                }
                if(imgfile3!=null){
                    multipart.addFilePart("photo3",imgfile3 );

                }
                if(imgfile4!=null){
                    multipart.addFilePart("photo4",imgfile4 );

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
                    JSONObject newObj=new JSONObject(String.valueOf(res_server.getJSONObject("res")));
                    server_status = newObj.optInt("status");
                    if (server_status == 1) {
                        if(lang.contentEquals("Arabic")){
                            server_message = "تم التسجيل بنجاح";

                        }else{
                            server_message = "Registration Successful";

                        }

                    }
                    else if(server_status==2)  {
                        if(lang.contentEquals("Arabic")){
                            server_message = "رقم الجوال او الإيميل مسجله مسبقا";

                        }
                        else{
                            server_message = "Mobile or email already exist";

                        }

                    }else {
                        if(lang.contentEquals("Arabic")){
                            server_message = "آسف!! فشل الدخول";

                        }else{
                            server_message = "Sorry !! Entry failed";

                        }
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
            if (server_status == 1) {
                Intent intent = new Intent(getActivity(), Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            showSnackBar(server_message);
            progressDialog.cancel();
            //Toast.makeText(UploadAssets.this,server_message,Toast.LENGTH_LONG).show();
        }
    }


}
