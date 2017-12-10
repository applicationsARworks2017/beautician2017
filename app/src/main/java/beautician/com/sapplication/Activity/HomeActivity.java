package beautician.com.sapplication.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.Toast;

import beautician.com.sapplication.Fragment.HomeFragment;
import beautician.com.sapplication.Fragment.Profile;
import beautician.com.sapplication.Fragment.ServiceList;
import beautician.com.sapplication.R;

public class HomeActivity extends AppCompatActivity  implements android.location.LocationListener{
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";
    LocationManager locationManager;
    String provider;
    public  static String latitude, longitude;
    int server_status;
    private TelephonyManager mTelephonyManager;
    Boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppUserTheme);
        setContentView(R.layout.activity_home);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default

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

        getgpsloc();


        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });
        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

    }
    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.home:
                // Visitors Exit
                frag=new HomeFragment();
                item.setCheckable(true);

//                toolbar.setTitle("Exit");
                break;
            case R.id.mylist:
                frag=new ServiceList();
                item.setCheckable(true);

                break;

            case R.id.profile:
                frag=new Profile();
                item.setCheckable(true);
                break;
            default:
                frag=new HomeFragment();
                item.setCheckable(true);

        }
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        /*for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }*/

        updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }
    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latitude = String.valueOf(""+lat);
        longitude = String.valueOf(""+lng);
       // Toast.makeText(HomeActivity.this,latitude+longitude,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
    public void getgpsloc() {
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
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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
}
