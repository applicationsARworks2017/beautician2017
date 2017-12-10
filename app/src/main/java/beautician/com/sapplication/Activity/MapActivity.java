package beautician.com.sapplication.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import beautician.com.sapplication.R;
import beautician.com.sapplication.Tabs.SPSignup;

import static beautician.com.sapplication.R.drawable.map;

public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    GoogleMap googleMap;
    SupportMapFragment fm;
    private String provider;
    Geocoder geocoder;
    MarkerOptions markerOptions;
    String address,city,state;
    String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.self_map);
        fm.getMapAsync(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        MapActivity.this.finish();
    }

    @Override
    public void onMapReady(GoogleMap Map) {
        this.googleMap=Map;
        LatLng latLng = new LatLng(Double.valueOf(SignupActivity.latitude),Double.valueOf(SignupActivity.longitude));

        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(SignupActivity.latitude),Double.valueOf(SignupActivity.longitude)) , 14.0f) );
        googleMap.addMarker(new MarkerOptions().position(latLng));
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        googleMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Custom location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                //SPSignup.latitude=latLng.latitude;
                //SPSignup.longitude=latLng.longitude;
                SPSignup.et_latlong.setText(latLng.latitude+","+latLng.longitude);
                List<Address> addresses;
                geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    address=addresses.get(0).getAddressLine(0);
                    city=addresses.get(0).getLocality();
                    state=addresses.get(0).getAdminArea();
                    SPSignup.et_sp_address.setText(address+","+city+","+state);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MapActivity.this,address+","+city+","+state,Toast.LENGTH_LONG).show();

            }
        });


    }
}
