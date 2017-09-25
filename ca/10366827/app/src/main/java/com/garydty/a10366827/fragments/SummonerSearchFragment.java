package com.garydty.a10366827.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Visibility;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.garydty.a10366827.R;
import com.garydty.a10366827.models.Summoner;
import com.garydty.a10366827.utility.GsonRequest;
import com.garydty.a10366827.utility.NetworkHelper;
import com.garydty.a10366827.utility.RiotRequestHelper;

import java.util.HashMap;
import java.util.List;

public class SummonerSearchFragment extends Fragment {
    private static final String TAG = SummonerSearchFragment.class.getSimpleName();

    private EditText mSearchInput;
    private TextView mSummonerName;
    private TextView mSummonerLevel;
    private ImageView mSummonerIcon;
    private LinearLayout mSummonerInfoBox;
    private ProgressBar spinner;

    public SummonerSearchFragment() {
        Log.i(TAG, "constructor");
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("input_text", mSearchInput.getText().toString());
        outState.putString("summoner_name", mSummonerName.getText().toString());
        outState.putString("summoner_level", mSummonerLevel.getText().toString());
        outState.putInt("input_cursor_position", mSearchInput.getSelectionEnd());
        if(mSummonerIcon != null){
            Drawable tmpDrawable = mSummonerIcon.getDrawable();
            if(!(tmpDrawable instanceof StateListDrawable)){
                BitmapDrawable drawable = (BitmapDrawable) tmpDrawable;
                if(drawable != null){
                    outState.putParcelable("summoner_icon", drawable.getBitmap());
                }
            }
        }
        outState.putInt("info_visibility", mSummonerInfoBox.getVisibility());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            Log.i(TAG, "onViewStateRestored - Bundle not null.");
            mSearchInput.setText(savedInstanceState.getString("input_text"));
            mSearchInput.setSelection(savedInstanceState.getInt("input_cursor_position"));
            mSummonerName.setText(savedInstanceState.getString("summoner_name"));
            mSummonerLevel.setText(savedInstanceState.getString("summoner_level"));

            if(savedInstanceState.containsKey("summoner_icon"))
                mSummonerIcon.setImageBitmap((Bitmap)savedInstanceState.getParcelable("summoner_icon"));

            int visible = savedInstanceState.getInt("info_visibility");
            mSummonerInfoBox.setVisibility(visible == View.VISIBLE ? View.VISIBLE : View.GONE);
        }
    }

    public static SummonerSearchFragment newInstance() {
        return ( new SummonerSearchFragment() );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");

        mSummonerName = getActivity().findViewById(R.id.summoner_name);
        mSummonerLevel = getActivity().findViewById(R.id.summoner_level);
        mSummonerIcon = getActivity().findViewById(R.id.summoner_icon);
        mSearchInput = getActivity().findViewById(R.id.search_input);
        mSummonerInfoBox = getActivity().findViewById(R.id.summoner_info);
        spinner = getActivity().findViewById(R.id.summoner_search_progress);

        final Button searchButton = getActivity().findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSearchInput.getText().toString().length() < 3) {
                    Toast.makeText(getContext(), "summoner names must have a minimum length of 3",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                fetchPosts();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.summoner_search, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //  VOLLEY START
    private void fetchPosts() {
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("X-Riot-Token", NetworkHelper.API_KEY);
//        headers.put("Accept-Language", "en-US,en;q=0.8");
//        GsonRequest<Summoner> request = new GsonRequest<>(
//                RiotRequestHelper.generateSummonerSearchUrl(Summoner.PATH, "Sempify")
//                , Summoner.class,
//                headers,        //  headers passe
//                onPostsLoaded,  //  on response listener
//                onPostsError);  //  on error listener
////        GsonRequest request = new StringRequest(Request.Method.GET, ENDPOINT + "Sempify", onPostsLoaded, onPostsError);
//        RiotRequestHelper.getInstance(getActivity()).addToRequestQueue(request);
        spinner.setVisibility(View.VISIBLE);
        String summonerName = mSearchInput.getText().toString().trim();
        RiotRequestHelper.getInstance(getActivity()).addGsonRequestToQueue(
                RiotRequestHelper.createUrl(Summoner.PATH, summonerName),
                Summoner.class,
                onPostsLoaded,
                onPostsError
        );

        RiotRequestHelper.getInstance(getActivity()).addGetSummonerIconRequestToQueue(summonerName,
                onImageLoaded, onPostsError);
//        RiotRequestHelper.getInstance(getActivity()).getImageLoader()
//                .get("https://avatar.leagueoflegends.com/euw/sempify.png",
//                        new ImageLoader.ImageListener(){
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                            }
//
//                            @Override
//                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                                Bitmap result = response.getBitmap();
//                                if(result != null)
//                                    mSummonerIcon.setImageBitmap(response.getBitmap());
//                            }
//                        });
    }

    private final Response.Listener<Bitmap> onImageLoaded = new Response.Listener<Bitmap>() {
        @Override
        public void onResponse(Bitmap result) {
            Log.i(TAG, result == null ? "image null" : "image not null!");
            if(result != null){
                if(mSummonerIcon == null)
                    mSummonerIcon = getActivity().findViewById(R.id.summoner_icon);
                mSummonerIcon.setImageBitmap(result);
            }
        }
    };

    private final Response.Listener<Summoner> onPostsLoaded = new Response.Listener<Summoner>() {
        @Override
        public void onResponse(Summoner response) {
            Log.i(TAG, "ID" + response.id);
            Log.i(TAG, "Summoner Name:" + response.name);
            Log.i(TAG, "AccountID: " + response.accountId);
            Log.i(TAG, "Level: " + response.summonerLevel);
            Log.i(TAG, "IconID: " + response.profileIconId);
            if(mSummonerName == null)
                mSummonerName = getActivity().findViewById(R.id.summoner_name);
            mSummonerName.setText(response.name);
            if(mSummonerLevel == null)
                mSummonerLevel = getActivity().findViewById(R.id.summoner_level);
            String tmpLvl = "Level " + response.summonerLevel;
            mSummonerLevel.setText(tmpLvl);

            spinner.setVisibility(View.GONE);
            mSummonerInfoBox.setVisibility(View.VISIBLE);
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Volley Error: " + error.toString());
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        Bundle savedInstanceState = getActivity().getIntent().getExtras();
        if (savedInstanceState != null) {
            Log.i(TAG, "onResume - Bundle not null.");
            mSearchInput.setText(savedInstanceState.getString("input_text"));
            mSearchInput.setSelection(savedInstanceState.getInt("input_cursor_position"));
            mSummonerName.setText(savedInstanceState.getString("summoner_name"));
            mSummonerLevel.setText(savedInstanceState.getString("summoner_level"));

            if (savedInstanceState.containsKey("summoner_icon"))
                mSummonerIcon.setImageBitmap((Bitmap) savedInstanceState.getParcelable("summoner_icon"));

            int visible = savedInstanceState.getInt("info_visibility");
            mSummonerInfoBox.setVisibility(visible == View.VISIBLE ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onPause() {
        Bundle outState = new Bundle();
        outState.putString("input_text", mSearchInput.getText().toString());
        outState.putString("summoner_name", mSummonerName.getText().toString());
        outState.putString("summoner_level", mSummonerLevel.getText().toString());
        outState.putInt("input_cursor_position", mSearchInput.getSelectionEnd());
        if(mSummonerIcon != null){
            Drawable tmpDrawable = mSummonerIcon.getDrawable();
            if(!(tmpDrawable instanceof StateListDrawable)){
                BitmapDrawable drawable = (BitmapDrawable) tmpDrawable;
                if(drawable != null){
                    outState.putParcelable("summoner_icon", drawable.getBitmap());
                }
            }
        }
        outState.putInt("info_visibility", mSummonerInfoBox.getVisibility());
        getActivity().getIntent().putExtras(outState);
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            Log.i(TAG, "onActivityCreated - Bundle not null.");
//            mSearchInput.setText(savedInstanceState.getString("input_text"));
//            mSearchInput.setSelection(savedInstanceState.getInt("input_cursor_position"));
//            mSummonerName.setText(savedInstanceState.getString("summoner_name"));
//            mSummonerLevel.setText(savedInstanceState.getString("summoner_level"));
//
//            if (savedInstanceState.containsKey("summoner_icon"))
//                mSummonerIcon.setImageBitmap((Bitmap) savedInstanceState.getParcelable("summoner_icon"));
//
//            int visible = savedInstanceState.getInt("info_visibility");
//            mSummonerInfoBox.setVisibility(visible == View.VISIBLE ? View.VISIBLE : View.GONE);
//        }
    }

    //  VOLLEY END
}
