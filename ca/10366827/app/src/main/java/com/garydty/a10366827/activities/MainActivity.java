package com.garydty.a10366827.activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.garydty.a10366827.R;
import com.garydty.a10366827.fragments.CameraFragment;
import com.garydty.a10366827.fragments.RoutesFragment;
import com.garydty.a10366827.fragments.StopFragment;
import com.garydty.a10366827.interfaces.OnFragmentInteractionListener;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private static final String LAST_ITEM = "last_item_selected";
    private static final String TAG_CAMERA_FRAGMENT = "CameraFragment";
    private static final String TAG_ROUTES_FRAGMENT = "RoutesFragment";
    private static final String TAG_STOP_FRAGMENT = "StopFragment";
    private CameraFragment mCameraFragment;
    private RoutesFragment mRoutesFragment;
    private StopFragment mStopFragment;
    private int lastItemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        navigationView.setCheckedItem(R.id.nav_view_routes);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragment);
//        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        lastItemSelected = id;
        if(id == R.id.nav_view_routes){
            loadRoutesFragment();
        }
        else if(id == R.id.nav_view_stop){
            loadStopFragment();
        }
        else if (id == R.id.nav_camera) {
            MainActivityPermissionsDispatcher.loadCameraFragmentWithPermissionCheck(MainActivity.this);
        } else if (id == R.id.nav_gallery) {
            //  Handle the gallery action
        }

//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //No call for super(). Bug on API Level > 11.
        outState.putInt(LAST_ITEM, lastItemSelected);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state of menu

    }

    private void loadRoutesFragment(){
        FragmentManager fm = getSupportFragmentManager();
        mRoutesFragment = (RoutesFragment)fm.findFragmentByTag(TAG_ROUTES_FRAGMENT);


        // create the fragment and data the first time
        if (mRoutesFragment == null) {
            Log.i("LoadFragment", "RoutesFragment");
            // add the fragment
            mRoutesFragment = RoutesFragment.newInstance();
//            fragmentTransaction.addToBackStack(TAG_ROUTES_FRAGMENT);
//            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
//            // load data from a data source or perform any calculation
//            mRetainedFragment.setData(loadMyData());
        }
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, mRoutesFragment, TAG_ROUTES_FRAGMENT);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commit();

//        replaceFragment(mRoutesFragment);
    }

    private void loadStopFragment(){
        FragmentManager fm = getSupportFragmentManager();
        mStopFragment = (StopFragment)fm.findFragmentByTag(TAG_STOP_FRAGMENT);
        // create the fragment and data the first time
        if (mStopFragment == null) {
            Log.i("LoadFragment", "StopFragment");
            // add the fragment
            mStopFragment = StopFragment.newInstance();
//            fm.beginTransaction().add(mStopFragment, TAG_STOP_FRAGMENT).commit();
//            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
//            // load data from a data source or perform any calculation
//            mRetainedFragment.setData(loadMyData());
        }
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, mStopFragment, TAG_STOP_FRAGMENT);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commit();


//        replaceFragment(mStopFragment);
    }

    @NeedsPermission({ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA })
    public void loadCameraFragment(){
        FragmentManager fm = getSupportFragmentManager();
        mCameraFragment = (CameraFragment) fm.findFragmentByTag(TAG_CAMERA_FRAGMENT);
        // create the fragment and data the first time
        if (mCameraFragment == null) {
            Log.i("LoadFragment", "CameraFragment");
            // add the fragment
            mCameraFragment = CameraFragment.newInstance();
//            fm.beginTransaction().add(mCameraFragment, TAG_CAMERA_FRAGMENT).commit();
//            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
//            // load data from a data source or perform any calculation
//            mRetainedFragment.setData(loadMyData());
        }

        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, mCameraFragment, TAG_CAMERA_FRAGMENT);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commit();

//        replaceFragment(CameraFragment.newInstance());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

//    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    void showRationaleForStorage(final PermissionRequest request) {
//        showRationaleDialog(R.string.permission_storage_rationale, request);
//    }

    @OnPermissionDenied({ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA })
    void onStorageDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality

        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA })
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA })
    void showRationaleForCamera(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }
}
