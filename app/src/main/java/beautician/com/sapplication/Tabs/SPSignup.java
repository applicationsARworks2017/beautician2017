package beautician.com.sapplication.Tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import java.io.IOException;
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
    private ImageView iv_pic1,iv_pic2,iv_pic3;
    Button bt_setails_submit;
    ProgressBar loader_signup;
    private int server_status;
    private String server_message;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int imageclick;
    private static final int CAMERA_REQUEST = 1888;
    String imPath;
    Geocoder geocoder;
    File imageFile,imgfile1,imgfile2,imgfile3;
    Uri picUri=null;
    Boolean picAvailable=false;
    KeyListener variable;
    TextView tv_gomap;


    public SPSignup() {
        // Required empty public constructor
    }

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
        sp_signup=(RelativeLayout)v.findViewById(R.id.sp_signup);
        bt_setails_submit=(Button)v.findViewById(R.id.bt_sp_submit);
        tv_gomap=(TextView)v.findViewById(R.id.tv_gomap);


        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Double.valueOf(sign_lat),Double.valueOf(sign_long), 1);
            String address=addresses.get(0).getAddressLine(0);
            String city=addresses.get(0).getLocality();
            String state=addresses.get(0).getAdminArea();
            et_sp_address.setText(address+","+city+","+state);
            et_latlong.setText(sign_lat+","+sign_long);
        } catch (IOException e) {
            e.printStackTrace();
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
                    startActivity(intent);

            }
        });
        iv_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageclick=1;
                captureImage(1);
            }
        });
        iv_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageclick=2;
                captureImage(2);
            }
        });
        iv_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageclick=3;
                captureImage(3);
            }
        });


        return v;
    }

    private void captureImage(int i) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
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
                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),picUri);
                picAvailable=true;
                if(imageclick==1) {
                    iv_pic1.setImageBitmap(photo);
                }
                else if(imageclick==2){
                    iv_pic2.setImageBitmap(photo);
                }
                else{
                    iv_pic3.setImageBitmap(photo);
                }
                //    profileImage=img1.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void validate_fields() {
        String shop_name=et_sp_name.getText().toString().trim();
        String shop_number=et_sp_phone.getText().toString().trim();
        String shop_mail=et_sp_mail.getText().toString().trim();
        String shop_address=et_sp_address.getText().toString().trim();
        String shop_lat_long=et_latlong.getText().toString().trim();
        String shop_password=et_sp_pass.getText().toString().trim();

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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                        server_message = "Registration Successful";

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
            if (server_status == 1) {
                Intent intent = new Intent(getActivity(), Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            showSnackBar(server_message);
            //Toast.makeText(UploadAssets.this,server_message,Toast.LENGTH_LONG).show();
        }
    }


}
