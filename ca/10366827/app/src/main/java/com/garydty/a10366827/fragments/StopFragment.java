package com.garydty.a10366827.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garydty.a10366827.R;
import com.garydty.a10366827.interfaces.OnFragmentInteractionListener;
import com.garydty.a10366827.utility.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class StopFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private static JSONObject mJson;

    public StopFragment() {
        // Required empty public constructor
    }

    public static StopFragment newInstance() {
        return new StopFragment();
    }

    private TextView stopID;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stopID = (TextView) view.findViewById(R.id.stop_test_text);
        if(mJson == null){
            if(stopID != null)
                new UserLoginTask().execute();
        }
        else{
            try {
                stopID.setText("" + mJson.getLong("summonerLevel"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stop, container, false);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, JSONObject> {

        UserLoginTask() {
        }

        @Override
        protected JSONObject doInBackground(Void... param) {
            String result = null;


            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
//                json = parser.getJSONFromUrl("https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation?stopid=7602&format=json");
//                result = json.getString("stopid");
                json = parser.getJSONFromUrl(
                        "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/Sempify"
                );
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            if(result != null){
                StopFragment.mJson = result;
                try {
                    stopID.setText("" + mJson.getLong("summonerLevel"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("StopFragment", "Completed request.");
        }
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
    }
}
