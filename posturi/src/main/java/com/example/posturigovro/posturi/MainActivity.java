package com.example.posturigovro.posturi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.posturigovro.posturi.fragments.FavouriteFragmet;
import com.example.posturigovro.posturi.fragments.MainMenuFragment;
import com.example.posturigovro.posturi.fragments.NavigationDrawerFragment;
import com.example.posturigovro.posturi.fragments.SearchFragment;
import com.example.posturigovro.posturi.fragments.ShowFavouritePostFragment;
import com.example.posturigovro.posturi.fragments.ShowPostFragment;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements LocationListener, NavigationDrawerFragment.NavigationDrawerCallbacks, MainMenuFragment.MainFragmentCallbacks, ShowPostFragment.ShowPostFragmentCallbacks, FavouriteFragmet.FavouriteFragmentCallbacks {

    Geocoder geocoder;
    LocationManager locationManager;
    Location location;
    FragmentManager fragmentManager;
    public Context context;
    public DrawerLayout mDrawerLayout;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    public double latitude, longitude;
    public String oras="";
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean webConnection = isInternetConnection();
        if (webConnection == false) {
            //In case that there isn't any internet connection, it displays a message and retry button
            setContentView(R.layout.internet_connection);

            Button retry = (Button) findViewById(R.id.retryInternetConnection);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(i);
                }
            });

        } else {
            setContentView(R.layout.activity_main);
            View mainView = findViewById(android.R.id.content);
            mainView = mainView.getRootView();

            mainView.refreshDrawableState();
            mainView.setDrawingCacheEnabled(true);
            mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            setDrawerLeftEdgeSize(this, mDrawerLayout, 0.1f);

            fragmentManager = getFragmentManager();
            initializeData();
        }
    }

    private void initializeData() {
        geocoder = new Geocoder(this, Locale.getDefault());
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        fragmentManager.beginTransaction().replace(R.id.container, MainMenuFragment.newInstance(1, 44.4378258, 26.0946376, "Bucuresti")).commit();
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Address address = addresses.get(0);
                oras = address.getLocality();
            }catch (NullPointerException e){
                e.getMessage();
            }
            if(oras.equals("Bucharest")){
                oras = "Bucuresti";
            }
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, MainMenuFragment.newInstance(1, latitude, longitude, oras)).addToBackStack(null).commit();
        }
        else {
            // Request a new location if necessary
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null)
            return;

        try {
            // find ViewDragHelper and set it accessible
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            // find edgesize and set is accessible
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            // set new edgesize
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x * displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalArgumentException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        }
    }
    @Override
    public void onNavigationDrawerItemSelected(String item, int position) {
        if(item.equals("Acasa")){
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, MainMenuFragment.newInstance(1, latitude, longitude, oras)).addToBackStack(null).commit();

        }else if(item.equals("Posturi favorite")) {
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FavouriteFragmet.newInstance(getApplicationContext()))
                    .addToBackStack(null)
                    .commit();
        }else if(item.equals("Cautare posturi")){
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SearchFragment.newInstance(getApplicationContext(), latitude, longitude))
                        .addToBackStack(null)
                        .commit();
            }
        return;
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        restoreActionBar();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setIcon(R.drawable.block);
        actionBar.setCustomView(R.layout.actionbar_custom_view_home);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#004990")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onMainFragmentItemSelected(String item, String stringObject) {
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, ShowPostFragment.newInstance(stringObject, this))
                .addToBackStack(null)
                .commit();
        return;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackPressed2() {
        this.onBackPressed();
    }

    @Override
    public void onFavouriteFragmentItemSelected(String item, String stringObject) {
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, ShowFavouritePostFragment.newInstance(stringObject, this))
                .addToBackStack(null)
                .commit();
        return;
    }
}
