package beautician.com.sapplication.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import beautician.com.sapplication.Pojo.Shops;
import beautician.com.sapplication.R;
import beautician.com.sapplication.Utils.Constants;
import beautician.com.sapplication.Utils.MultipartUtility;

import static beautician.com.sapplication.Utils.Constants.modifyOrientation;

public class SPProfile extends AppCompatActivity implements android.location.LocationListener {
    Button button,editsave;
    String shop_id;
   public static String id;
    public static String shopname;
    public static String address;
    public static String latlong;
    public static String photo1;
    public static String photo2;
    public static String photo3;
    public static String photo4;
    public static String email;
    public static String mobile;
    public static String reviews;
    public static String ratings;
    ImageView pic1,pic2,pic3,pic4;
   public static EditText name_value,tv_phone_value,tv_email_value,tv_add_value,tv_latlng;
    Boolean editable=false;
    RelativeLayout sp_profile_rel;
    int imageclick;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    File imageFile,imgfile1,imgfile2,imgfile3,imgfile4;
    Uri picUri=null;
    Boolean picAvailable=false;
    private static final int CAMERA_REQUEST = 1888;
    String imPath,server_message,lang,user_type;
    int server_status,filechooser,gal1,gal2,gal3,gal4;
    private static int RESULT_LOAD_IMAGE = 1;
    TextView hd_name,hd_phone,hd_email,hd_address,lang_english,lang_arabic,tv_showmap,tvlatlng;
    Geocoder geocoder;
    String sign_lat,sign_long;
    public static String spprofile="profile";
    public  static String latitude, longitude;
    LocationManager locationManager;
    Boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private String provider,fcm_id;



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
        tv_latlng=(EditText) findViewById(R.id.tv_latlng);
        hd_name=(TextView) findViewById(R.id.hd_name);
        hd_phone=(TextView) findViewById(R.id.hd_phone);
        hd_email=(TextView) findViewById(R.id.hd_email);
        hd_address=(TextView) findViewById(R.id.hd_address);
        lang_english=(TextView) findViewById(R.id.tv_english);
        lang_arabic=(TextView) findViewById(R.id.tv_arabic);
        tv_showmap=(TextView) findViewById(R.id.tv_showmap);
        tvlatlng=(TextView) findViewById(R.id.tvlatlng);
        sp_profile_rel=(RelativeLayout) findViewById(R.id.sp_profile_rel);
        pic1=(ImageView)findViewById(R.id.photo1);
        pic2=(ImageView)findViewById(R.id.photo2);
        pic3=(ImageView)findViewById(R.id.photo3);
        pic4=(ImageView)findViewById(R.id.photo4);
        name_value.setEnabled(false);
        tv_phone_value.setEnabled(false);
        tv_email_value.setEnabled(false);
        tv_add_value.setEnabled(false);
        tv_latlng.setEnabled(false);


        shop_id = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        lang = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0).getString(Constants.LANG_TYPE, null);
        user_type=getSharedPreferences(Constants.SHAREDPREFERENCE_BEAUTICIAN, 0).getString(Constants.BEAUTICIAN_TYPE, null);

        if(lang.contentEquals("Arabic")){
            hd_name.setText("الاسم");
            hd_phone.setText("رقم الجوال");
            hd_email.setText("البريد الإلكتروني");
            hd_address.setText("العنوان");
            button.setText("خروج");
            editsave.setText("تعديل");
            tvlatlng.setText("خطوط الطول طويلة");
            tv_showmap.setText("عرض الخريطة");
            tv_showmap.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            setTitle("الملف الشخصي");

        }
        else{
            hd_name.setText("Name");
            hd_phone.setText("Phone");
            hd_email.setText("Email");
            hd_address.setText("Address");
            button.setText("Logout");
            editsave.setText("Edit");
            tvlatlng.setText("LatLong");
            tv_showmap.setText("ShowMap");
            setTitle("Profile");
            tv_showmap.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
        if(user_type.contentEquals("SP")){
            tv_latlng.setVisibility(View.VISIBLE);
            tv_showmap.setVisibility(View.VISIBLE);
            tvlatlng.setVisibility(View.VISIBLE);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            // Call your Alert message
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please switch on your phone GPS")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {
                /*Latitude.setText("Device can't founf the loc");
                Longitude.setText("Device can't founf the loc");*/
            }

        } else {
            // Toast.makeText(HomeActivity.this, "Allow to GPS", Toast.LENGTH_LONG).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {
                /*Latitude.setText("Device can't founf the loc");
                Longitude.setText("Device can't founf the loc");*/
            }

        } else {
            // Toast.makeText(HomeActivity.this, "Allow to GPS", Toast.LENGTH_LONG).show();
        }


       // getThedetails();
        setValues();
        getgpsloc();
        lang_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SPProfile.this);
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
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.LANG_TYPE, lang);
                editor.commit();




            }
        });
        lang_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SPProfile.this);
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
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERENCE_LANGUAGE, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString(Constants.LANG_TYPE, lang);
                editor.commit();

            }
        });
        tv_showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable==true) {
                    Intent intent = new Intent(SPProfile.this, MapActivity.class);
                    intent.putExtra("page", "profile");
                    startActivity(intent);
                }
            }
            });

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
                    tv_latlng.setEnabled(true);
                    tvlatlng.setEnabled(true);
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
                    final Dialog dialog = new Dialog(SPProfile.this);
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
                else{
                    String shop_pic1 = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_PIC_ONE, null);
                    Constants.showImagedialoug(Constants.SHOP_PICURL+shop_pic1,SPProfile.this);
                }
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable==true) {
                    final Dialog dialog = new Dialog(SPProfile.this);
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
                else{
                    String shop_pic2 = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_PIC_TWO, null);
                    Constants.showImagedialoug(Constants.SHOP_PICURL+shop_pic2,SPProfile.this);
                }
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable==true) {
                    final Dialog dialog = new Dialog(SPProfile.this);
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
                else {
                    String shop_pic3 = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_PIC_THREE, null);
                    Constants.showImagedialoug(Constants.SHOP_PICURL+shop_pic3,SPProfile.this);
                }
            }
        });
        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editable==true) {
                    final Dialog dialog = new Dialog(SPProfile.this);
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
                else {
                    String shop_pic4 = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_PIC_FOUR, null);
                    Constants.showImagedialoug(Constants.SHOP_PICURL+shop_pic4,SPProfile.this);
                }
            }
        });
    }

    private void getgpsloc() {
        ///gps code start
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no GPS Provider and no network provider is enabled
            } else {   // Either GPS provider or network provider is enabled

                // First get location from Network Provider
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            latitude = String.valueOf(lat);
                            longitude = String.valueOf(lon);
                            this.canGetLocation = true;
                            //   Toast.makeText(HomeActivity.this,latitude+longitude,Toast.LENGTH_LONG).show();


                        }
                    }
                }
            }// End of IF network enabled

            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,  this);
                if (locationManager != null)
                {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null)
                    {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        latitude=String.valueOf(lat);
                        longitude=String.valueOf(lon);
                        this.canGetLocation = true;
                        //    Toast.makeText(HomeActivity.this,latitude+longitude,Toast.LENGTH_LONG).show();


                    }
                }

            }// End of if GPS Enabled
        }// End of Either GPS provider or network provider is enabled



        //gps code end
    }

    private void captureImage(int i, String action) {
        if(action.contentEquals("camera")) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
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
        }else if(i==3) {
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
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                // imPath=picUri.getPath();
                // Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(SPProfile.this.getContentResolver(), picUri);
                   // Bitmap c_photo = Bitmap.createScaledBitmap(photo, 200, 200, true);
                    Bitmap perfectImage = modifyOrientation(photo, imPath);

                    picAvailable = true;
                    if (imageclick == 1) {
                        pic1.setImageBitmap(perfectImage);
                    } else if (imageclick == 2) {
                        pic2.setImageBitmap(perfectImage);
                    } else if (imageclick == 3) {
                        pic3.setImageBitmap(perfectImage);
                    } else {
                        pic4.setImageBitmap(perfectImage);
                    }
                    //    profileImage=img1.toString();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        else{
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                if(imageclick==1) {
                    pic1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                else if(imageclick==2) {
                    pic2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                else if(imageclick==3) {
                    pic3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }else if(imageclick==4) {
                    pic4.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }


            }
        }
    }
    private  File persistImage(Bitmap bitmap, String name) {
        File filesDir = SPProfile.this.getFilesDir();
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
    private void savedata() {
        String shop_name=name_value.getText().toString().trim();
        String shop_number=tv_phone_value.getText().toString().trim();
        String shop_mail=tv_email_value.getText().toString().trim();
        String shop_address=tv_add_value.getText().toString().trim();
        String shop_latlng=tv_latlng.getText().toString().trim();
        if(gal1==1) {
            if (pic1.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) pic1.getDrawable()).getBitmap();
                imgfile1 = persistImage(bitmap, shop_name+"1");
            }
        }
        if(gal2==1) {
            if (pic2.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) pic2.getDrawable()).getBitmap();
                imgfile2 = persistImage(bitmap, shop_name+"2");
            }
        }
        if(gal3==1) {
            if (pic3.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) pic3.getDrawable()).getBitmap();
                imgfile3 = persistImage(bitmap, shop_name+"3");
            }
        }
        if(gal4==1) {
            if (pic4.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) pic4.getDrawable()).getBitmap();
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
        else if(!shop_mail.matches(emailPattern)){
            showSnackBar("Enter Valid Email");
        }
        else if(shop_address.length()<=0){
            showSnackBar("Enter Address");
        }
        else{
            EditSP asyntask=new EditSP();
            asyntask.execute(shop_name,shop_number,shop_mail,shop_address,shop_id,shop_latlng);
        }
    }

    private void getThedetails() {
        ViewShops viewShops=new ViewShops();
        viewShops.execute(shop_id);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class ViewShops extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Propsal details";
        String server_message;
        ProgressDialog progressDialog=null;

        int server_status;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(SPProfile.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(SPProfile.this, "Loading", "Please wait...");

                }
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
                    photo4=j_obj.getString("photo4");
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


            SharedPreferences sharedPreferences = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.USER_ID, id);
            editor.putString(Constants.SHOP_NAME, shopname);
            editor.putString(Constants.SHOP_ADD, address);
            editor.putString(Constants.SHOP_latlong, latlong);
            editor.putString(Constants.SHOP_PIC_ONE, photo1);
            editor.putString(Constants.SHOP_PIC_TWO, photo2);
            editor.putString(Constants.SHOP_PIC_THREE, photo3);
            editor.putString(Constants.SHOP_PIC_FOUR, photo4);
            editor.putString(Constants.SHOP_EMAIL, email);
            editor.putString(Constants.SHOP_MOBILE, mobile);
            editor.putString(Constants.USER_TYPE, "service_provider");
            editor.commit();

            setValues();
            progressDialog.dismiss();
        }
    }

    private void setValues() {
        String shop_name = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_NAME, null);
        String shop_phone = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_MOBILE, null);
        String shop_email = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_EMAIL, null);
        String shop_address = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_ADD, null);
        String shop_latlong = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_latlong, null);
        String shop_pic1 = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_PIC_ONE, null);
        String shop_pic2 = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_PIC_TWO, null);
        String shop_pic3 = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_PIC_THREE, null);
        String shop_pic4 = SPProfile.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHOP_PIC_FOUR, null);
        name_value.setText(shop_name);
        tv_phone_value.setText(shop_phone);
        tv_email_value.setText(shop_email);
        tv_add_value.setText(shop_address);
        tv_latlng.setText(shop_latlong);
        if(!shop_pic1.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.SHOP_PICURL+shop_pic1)
                    .resize(300, 300)
                    .into(pic1);
        }
        if(!shop_pic2.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.SHOP_PICURL+shop_pic2)
                    .resize(300, 300)
                    .into(pic2);
        } if(!shop_pic3.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.SHOP_PICURL+shop_pic3)
                    .resize(300, 300)
                    .into(pic3);
        }if(!shop_pic4.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.SHOP_PICURL+shop_pic4)
                    .resize(300, 300)
                    .into(pic4);
        }

        //   name_value,tv_phone_value,tv_email_value,tv_add_value

        /*name_value.setText(shopname);
        tv_phone_value.setText(mobile);
        tv_email_value.setText(email);
        tv_add_value.setText(address);
        tv_latlng.setText(latlong);
        if(!photo1.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.SHOP_PICURL+photo1).into(pic1);
        }
        if(!photo2.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.SHOP_PICURL+photo2).into(pic2);
        } if(!photo3.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.SHOP_PICURL+photo3).into(pic3);
        }if(!photo4.isEmpty()) {
            Picasso.with(SPProfile.this).load(Constants.SHOP_PICURL+photo4).into(pic4);
        }*/
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
                if(lang.contentEquals("Arabic")){
                    progressDialog = ProgressDialog.show(SPProfile.this, "جار التحميل", "يرجى الإنتظار...");

                }
                else{
                    progressDialog = ProgressDialog.show(SPProfile.this, "Loading", "Please wait...");

                }
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
                multipart.addFormField("latitudelongitude", params[5]);
                if (imgfile1 != null) {
                    multipart.addFilePart("photo1", imgfile1);
                }
                if (imgfile2 != null) {
                    multipart.addFilePart("photo2", imgfile2);
                }
                if (imgfile3 != null) {
                    multipart.addFilePart("photo3", imgfile3);

                }
                if (imgfile4 != null) {
                    multipart.addFilePart("photo4", imgfile4);

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
                tv_latlng.setEnabled(false);
                getThedetails();
            }
            else{
                showSnackBar(server_message);
            }
        }
    }
}
