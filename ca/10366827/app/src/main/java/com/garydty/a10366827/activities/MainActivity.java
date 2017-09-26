package com.garydty.a10366827.activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.garydty.a10366827.R;
import com.garydty.a10366827.fragments.ArticleFragment;
import com.garydty.a10366827.fragments.CameraFragment;
import com.garydty.a10366827.fragments.SummonerFragment;
import com.garydty.a10366827.fragments.StopFragment;
import com.garydty.a10366827.fragments.SummonerSearchFragment;
import com.garydty.a10366827.interfaces.OnFragmentInteractionListener;
import com.garydty.a10366827.models.Summoner;
import com.garydty.a10366827.utility.GsonRequest;
import com.garydty.a10366827.utility.RiotRequestHelper;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final String LAST_ITEM = "last_item_selected";
//    private static final String TAG_CAMERA_FRAGMENT = "CameraFragment";
    private static final String TAG_SUMMONER_SEARCH = "SummonerSearchFragment";
    private static final String TAG_STOP_FRAGMENT = "StopFragment";
    private static final String TAG_ARTICLE_FRAGMENT = "ArticleFragment";
//    private CameraFragment mCameraFragment;
    private SummonerSearchFragment mSummonerFragment;
    private ArticleFragment mArticleFragment;
    private int lastItemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private final Response.Listener<String> onRSSFeedRetrieved = new Response.Listener<String>() {
        @Override
        public void onResponse(String xmlResponse) {

        }
    };

    private final Response.ErrorListener onVolleyError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Volley Error: " + error.toString());
        }
    };

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
            loadSummonerSearchFragment();
        }
        else if(id == R.id.nav_view_stop){
            loadStopFragment();
        }
        else if (id == R.id.nav_camera) {
            MainActivityPermissionsDispatcher.loadCameraFragmentWithPermissionCheck(MainActivity.this);
        }
//        else if (id == R.id.nav_gallery) {
//            //  Handle the gallery action
//        }

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
        outState.putInt(LAST_ITEM, lastItemSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state of menu
        if(savedInstanceState != null){
            // navigationView.setCheckedItem(R.id.nav_view_routes);
        }
    }

    private void loadSummonerSearchFragment(){
        getSupportActionBar().setTitle("Find Summoner");
        FragmentManager fm = getSupportFragmentManager();
        mSummonerFragment = (SummonerSearchFragment)fm.findFragmentByTag(TAG_SUMMONER_SEARCH);

        // create the fragment and data the first time
        if (mSummonerFragment == null)
            mSummonerFragment = SummonerSearchFragment.newInstance();

        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, mSummonerFragment, TAG_SUMMONER_SEARCH);
//        fragmentTransaction.addToBackStack(TAG_SUMMONER_SEARCH);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

//        replaceFragment(mSummonerFragment);
    }

    private void loadStopFragment(){
        getSupportActionBar().setTitle("News");
        FragmentManager fm = getSupportFragmentManager();
        mArticleFragment = (ArticleFragment)fm.findFragmentByTag(TAG_ARTICLE_FRAGMENT);
        // create the fragment and data the first time
        if (mArticleFragment == null) {
            Log.i("LoadFragment", "StopFragment");
            // add the fragment
            mArticleFragment = ArticleFragment.newInstance();
//            fm.beginTransaction().add(mStopFragment, TAG_STOP_FRAGMENT).commit();
//            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
//            // load data from a data source or perform any calculation
//            mRetainedFragment.setData(loadMyData());
        }
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, mArticleFragment, TAG_ARTICLE_FRAGMENT);
//        fragmentTransaction.addToBackStack(TAG_STOP_FRAGMENT);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
//        replaceFragment(mStopFragment);
    }

    @NeedsPermission({ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA })
    public void loadCameraFragment(){
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, CameraFragment.newInstance());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
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
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA })
    void onStorageDenied() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA })
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA })
    void showRationaleForCamera(PermissionRequest request) {
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }
}
