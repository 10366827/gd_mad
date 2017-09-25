package com.garydty.a10366827.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.garydty.a10366827.R;
import com.garydty.a10366827.interfaces.OnFragmentInteractionListener;
import com.garydty.a10366827.models.Summoner;
import com.garydty.a10366827.utility.GsonRequest;
import com.garydty.a10366827.utility.JSONParser;
import com.garydty.a10366827.utility.NetworkHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class StopFragment_Volley_Working extends Fragment {
    //  Volley START
    private static final String ENDPOINT = NetworkHelper.ENDPOINT_HOST +
            "/lol/summoner/v3/summoners/by-name/";
    private RequestQueue requestQueue;
    //  Volley END

    private OnFragmentInteractionListener mListener;
    private static JSONObject mJson;

    public StopFragment_Volley_Working() {
        // Required empty public constructor
    }

    public static StopFragment_Volley_Working newInstance() {
        return new StopFragment_Volley_Working();
    }

    private TextView stopID;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        stopID = (TextView) view.findViewById(R.id.stop_test_text);
//        if(mJson == null){
//            if(stopID != null)
//                new UserLoginTask().execute();
//        }
//        else{
//            try {
//                stopID.setText("" + mJson.getLong("summonerLevel"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    //  VOLLEY START
    private void fetchPosts() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Riot-Token", NetworkHelper.API_KEY);
        headers.put("Accept-Language", "en-US,en;q=0.8");
        GsonRequest<Summoner> request = new GsonRequest<>((ENDPOINT + "Sempify"), Summoner.class,headers, onPostsLoaded, onPostsError);
//        GsonRequest request = new StringRequest(Request.Method.GET, ENDPOINT + "Sempify", onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<Summoner> onPostsLoaded = new Response.Listener<Summoner>() {
        @Override
        public void onResponse(Summoner response) {
            Log.i("PostActivity", response.name);
            Log.i("PostActivity", "AccountID: " + response.accountId);
            Log.i("PostActivity", "Level: " + response.summonerLevel);
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };
    //  VOLLEY END

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  VOLLEY START
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        fetchPosts();
        //  VOLLEY END


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
                StopFragment_Volley_Working.mJson = result;
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
