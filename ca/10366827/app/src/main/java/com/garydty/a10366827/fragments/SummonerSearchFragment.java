package com.garydty.a10366827.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.garydty.a10366827.R;
import com.garydty.a10366827.models.LeaguePositionDTO;
import com.garydty.a10366827.models.Summoner;
import com.garydty.a10366827.utility.RiotRequestHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SummonerSearchFragment extends Fragment {
    private static final String TAG = SummonerSearchFragment.class.getSimpleName();

    private EditText mSearchInput;
    private TextView mSummonerName;
    private TextView mSummonerLevel;
    private ImageView mSummonerIcon;
    private LinearLayout mSummonerInfoBox;
    private ProgressBar spinner;


    private TextView mQueueType;
    private TextView mRank;
    private TextView mLeaguePoints;
    private TextView mWins;
    private TextView mLosses;
    private TextView mLeagueName;
    private LinearLayout mRankedInfo;

    public SummonerSearchFragment() {
        Log.i(TAG, "constructor");
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //  INPUT INFORMATION
        outState.putString("input_text", mSearchInput.getText().toString());
        outState.putInt("input_cursor_position", mSearchInput.getSelectionEnd());

        //  GENERAL SUMMONER INFORMATION
        outState.putString("summoner_name", mSummonerName.getText().toString());
        outState.putString("summoner_level", mSummonerLevel.getText().toString());

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

        //  RANKED INFORMATION
        outState.putString("league_points", mLeaguePoints.getText().toString());
        outState.putString("wins", mWins.getText().toString());
        outState.putString("lossess", mLosses.getText().toString());
        outState.putString("rank", mRank.getText().toString());
        outState.putString("queue_type", mQueueType.getText().toString());
        outState.putString("league_name", mLeagueName.getText().toString());

        outState.putInt("ranked_info", mRankedInfo.getVisibility());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            Log.i(TAG, "onViewStateRestored - Bundle not null.");

            //  INPUT INFORMATION
            mSearchInput.setText(savedInstanceState.getString("input_text"));
            mSearchInput.setSelection(savedInstanceState.getInt("input_cursor_position"));

            //  GENERAL SUMMONER INFORMATION
            mSummonerName.setText(savedInstanceState.getString("summoner_name"));
            mSummonerLevel.setText(savedInstanceState.getString("summoner_level"));

            if(savedInstanceState.containsKey("summoner_icon"))
                mSummonerIcon.setImageBitmap((Bitmap)savedInstanceState.getParcelable("summoner_icon"));

            int visible = savedInstanceState.getInt("info_visibility");
            mSummonerInfoBox.setVisibility(visible == View.VISIBLE ? View.VISIBLE : View.GONE);

            //  RANKED INFORMATION
            mLeaguePoints.setText(savedInstanceState.getString("league_points"));
            mWins.setText(savedInstanceState.getString("wins"));
            mLosses.setText(savedInstanceState.getString("losses"));
            mRank.setText(savedInstanceState.getString("rank"));
            mQueueType.setText(savedInstanceState.getString("queue_type"));
            mLeagueName.setText(savedInstanceState.getString("league_name"));

            visible = savedInstanceState.getInt("ranked_info");
            mRankedInfo.setVisibility(visible == View.VISIBLE ? View.VISIBLE : View.GONE);
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

        mSummonerName = view.findViewById(R.id.summoner_name);
        mSummonerLevel = view.findViewById(R.id.summoner_level);
        mSummonerIcon = view.findViewById(R.id.summoner_icon);
        mSearchInput = view.findViewById(R.id.search_input);
        mSummonerInfoBox = view.findViewById(R.id.summoner_info);
        spinner = view.findViewById(R.id.summoner_search_progress);

        mLeaguePoints = view.findViewById(R.id.leaguePoints);
        mWins = view.findViewById(R.id.wins);
        mLosses = view.findViewById(R.id.losses);
        mRank = view.findViewById(R.id.rank);
        mQueueType = view.findViewById(R.id.queueType);
        mLeagueName = view.findViewById(R.id.leagueName);
        mRankedInfo = view.findViewById(R.id.ranked_info);

        final Button searchButton = getActivity().findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSearchInput.getText().toString().length() < 3) {
                    Toast.makeText(getContext(), "summoner names must have a minimum length of 3",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                searchForSummoner();
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

    private void searchForSummoner() {
        spinner.setVisibility(View.VISIBLE);
        String summonerName = mSearchInput.getText().toString().trim();
        RiotRequestHelper.getInstance(getActivity()).addGsonRequestToQueue(
                RiotRequestHelper.createUrl(Summoner.PATH, summonerName),
                Summoner.class,
                onSummonerInfoLoaded,
                onVolleyError
        );

        RiotRequestHelper.getInstance(getActivity()).addGetSummonerIconRequestToQueue(summonerName,
                onImageLoaded, onVolleyError);
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

    private final Response.Listener<Summoner> onSummonerInfoLoaded = new Response.Listener<Summoner>() {
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

            RiotRequestHelper.getInstance(getActivity()).addGsonRequestToQueue(
                    RiotRequestHelper.createUrl("/lol/league/v3/positions/by-summoner/", response.id),
                    JsonArray.class,
                    onRankedInfoLoaded,
                    onVolleyError
            );

            spinner.setVisibility(View.GONE);
            mSummonerInfoBox.setVisibility(View.VISIBLE);
        }
    };

    private final Response.Listener<JsonArray> onRankedInfoLoaded = new Response.Listener<JsonArray>(){
        @Override
        public void onResponse(JsonArray response) {
            Log.i(TAG, "Successfully retrieved summoner's ranked information.");
            Gson gson=new Gson();
            TypeToken<List<LeaguePositionDTO>> token = new TypeToken<List<LeaguePositionDTO>>(){};
            List<LeaguePositionDTO> leagueList = gson.fromJson(response, token.getType());
            if(leagueList != null && !leagueList.isEmpty()){
//                for(LeaguePositionDTO pos : personList){
//
//                }
                getActivity().findViewById(R.id.ranked_info).setVisibility(View.VISIBLE);
                LeaguePositionDTO tmpLeague = leagueList.get(0);
                String queueTypeOutput = tmpLeague.queueType.contains("SOLO") ?
                        "Ranked Solo" : "Ranked Flex";
                mQueueType.setText(queueTypeOutput);
                mWins.setText(""+tmpLeague.wins + " wins");
                mLosses.setText(""+tmpLeague.losses + " losses");
                mRank.setText(tmpLeague.tier + " " + tmpLeague.rank);
                mLeaguePoints.setText(""+tmpLeague.leaguePoints + " LP");
                mLeagueName.setText(tmpLeague.leagueName);
            }
            else{
                getActivity().findViewById(R.id.ranked_info).setVisibility(View.INVISIBLE);
            }
        }
    };

    private final Response.ErrorListener onVolleyError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Volley Error: " + error.toString());
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            spinner.setVisibility(View.GONE);
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

            //  INPUT INFORMATION
            mSearchInput.setText(savedInstanceState.getString("input_text"));
            mSearchInput.setSelection(savedInstanceState.getInt("input_cursor_position"));

            //  GENERAL SUMMONER INFORMATION
            mSummonerName.setText(savedInstanceState.getString("summoner_name"));
            mSummonerLevel.setText(savedInstanceState.getString("summoner_level"));

            if(savedInstanceState.containsKey("summoner_icon"))
                mSummonerIcon.setImageBitmap((Bitmap)savedInstanceState.getParcelable("summoner_icon"));

            int visible = savedInstanceState.getInt("info_visibility");
            mSummonerInfoBox.setVisibility(visible == View.VISIBLE ? View.VISIBLE : View.GONE);

            //  RANKED INFORMATION
            mLeaguePoints.setText(savedInstanceState.getString("league_points"));
            mWins.setText(savedInstanceState.getString("wins"));
            mLosses.setText(savedInstanceState.getString("losses"));
            mRank.setText(savedInstanceState.getString("rank"));
            mQueueType.setText(savedInstanceState.getString("queue_type"));
            mLeagueName.setText(savedInstanceState.getString("league_name"));

            visible = savedInstanceState.getInt("ranked_info");
            mRankedInfo.setVisibility(visible == View.VISIBLE ? View.VISIBLE : View.GONE);
        }

        if(mSummonerName.getText().toString().isEmpty()){
            mSummonerInfoBox.setVisibility(View.GONE);
            mRankedInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        Bundle outState = new Bundle();

        //  INPUT INFORMATION
        outState.putString("input_text", mSearchInput.getText().toString());
        outState.putInt("input_cursor_position", mSearchInput.getSelectionEnd());

        //  GENERAL SUMMONER INFORMATION
        outState.putString("summoner_name", mSummonerName.getText().toString());
        outState.putString("summoner_level", mSummonerLevel.getText().toString());

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

        //  RANKED INFORMATION
        outState.putString("league_points", mLeaguePoints.getText().toString());
        outState.putString("wins", mWins.getText().toString());
        outState.putString("lossess", mLosses.getText().toString());
        outState.putString("rank", mRank.getText().toString());
        outState.putString("queue_type", mQueueType.getText().toString());
        outState.putString("league_name", mLeagueName.getText().toString());

        outState.putInt("ranked_info", mRankedInfo.getVisibility());

        getActivity().getIntent().putExtras(outState);

        //  Hide keyboard
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
