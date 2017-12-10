package beautician.com.sapplication.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.widget.ImageView;

import java.text.DecimalFormat;

import beautician.com.sapplication.Activity.HomeActivity;
import beautician.com.sapplication.R;

/**
 * Created by Amaresh on 11/1/17.
 */

public class Constants {
    public static String ONLINEURL="http://applicationworld.net/beautician/";
    public static String CATEGORYLIST="categories/index.json";
    public static String CATEGORYLIST_SHOPWISE="ShopDetails/shopListCat";
    public static String SUBCATEGORYLIST_SHOPWISE="ShopDetails/shopListSubCat";
    public static String SUB_CATEGORYLIST="sub-categories/index.json";
    public static String USER_REGISTRATION="users/add.json";
    public static String SHOP_REGISTRATION="shops/add.json";
    public static String LOGIN_USER="users/loginCheck.json";
    public static String LOGIN_SHOP="shops/loginCheck.json";
    public static String SUBCATEGORY_PRICE="ShopDetails/shopPriceList";
    public static String PRICE_LIST="ShopDetails/shopPriceListNew.json";
    public static String PRICE_SET="ShopDetails/add.json";
    public static String SERVICE_REQUEST="service-requests/addNew.json";
    public static String COMMENTS="reviews/add.json";
    public static String ADD_PROPOSAL="service-purposal/add.json";
    public static String PROPOSALS="service-purposal/index.json";
    public static String SERVICE_REQUEST_LIST="service-requests/serviceRequestList.json";
    public static String VIEW_PROPSAL="ServiceRequests/view.json";
    public static String CREATE_OFFER="offers/add.json";
    public static String LIST_OFFER="offers/index.json";
    public static String SEARCH_SHOP="ShopDetails/shopList";
    public static String NEW_SEARCH_SHOP="Shops/index.json";
    public static String UPDATE_SERVICE_REQUEST="service-requests/edit.json";
    public static String EDIT_PROPSAL="ServicePurposal/edit.json";
    public static String INDICISUAL_REQUEST="service-indivisual-requests/add.json";
    public static String INDICISUAL_REQUEST_LIST="service-indivisual-requests/index.json";
    public static String PICURL="http://applicationworld.net/beautician/webroot/files/profile/";
    public static String SHOP_DETAILS="shops/view.json";
    public static String USER_BALANCE="UserWallets/index.json";
    public static String SHOP_BALANCE="wallets/index.json";
    public static String USER_WALLLET_UPDATE="UserWallets/add.json";


    public static final String SHAREDPREFERENCE_KEY = "beautician" ;
    public static final String USER_NAME = "username" ;
    public static final String USER_ID = "userid" ;
    public static final String USER_TYPE = "user_type" ;
    public static String FCM_ID="fcmid";



    public static void noInternetDialouge(Context _context,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle("Oops !");
        builder.setMessage(Message);
        ImageView showImage = new ImageView(_context);
        Resources res = _context.getResources();
        Drawable drawable = res.getDrawable(R.mipmap.ic_warning_black_24dp);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, _context.getResources().getColor(R.color.colorPrimaryDark));
        showImage.setImageDrawable(drawable);
        builder.setView(showImage);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
                //finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public static double CalculationByDistance(Double latitude,Double longitude) {
        int Radius = 6371;// radius of earth in Km
        double lat1 =latitude;
        double lat2 =longitude;
        double lon1 = Double.valueOf(HomeActivity.latitude);
        double lon2 =Double.valueOf(HomeActivity.longitude);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double orgKmeter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(orgKmeter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        //return Radius * c;
        return meterInDec;
    }

    public static int generatePIN()
    {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;

        //Store integer in a string
        return randomPIN;
    }
}
