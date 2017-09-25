package com.garydty.a10366827.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.garydty.a10366827.R;
import com.garydty.a10366827.adapters.SummonerAdapter;
import com.garydty.a10366827.interfaces.OnFragmentInteractionListener;
import com.garydty.a10366827.interfaces.SummonerChosenListener;
import com.garydty.a10366827.models.Route;
import com.garydty.a10366827.models.Summoner;
import com.garydty.a10366827.utility.GsonRequest;
import com.garydty.a10366827.utility.NetworkHelper;
import com.garydty.a10366827.utility.RiotRequestQueue;

import java.util.ArrayList;
import java.util.HashMap;

public class SummonerFragment extends Fragment implements SummonerChosenListener, AdapterView.OnItemClickListener{
    private static SummonerAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    //  Volley START
    private static final String ENDPOINT = NetworkHelper.ENDPOINT_HOST +
            "/lol/summoner/v3/summoners/by-name/";
//    private static RequestQueue requestQueue;
    //  Volley END

    public SummonerFragment() {
        // Required empty public constructor
//        setRetainInstance(true);
    }

    public static SummonerFragment newInstance() {
        SummonerFragment fragment = new SummonerFragment();
        return fragment;
    }

//    public void setData(List<Route> data){
//
//    }
//
//    public SummonerAdapter getData(){
//        return mAdapter.;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  VOLLEY END
        Log.i("test", "onViewCreated");
        fetchPosts();

//        if(mAdapter == null){
////            ArrayList<Route> tmp = new ArrayList<Route>();
////            tmp.add(new Route(1));
////            tmp.add(new Route(2));
////            tmp.add(new Route(3));
////            tmp.add(new Route(4));
////            tmp.add(new Route(5));
////            mAdapter = new SummonerAdapter(getContext(), R.id.route_item_text, tmp, this, getActivity());
//        }
//
//        if(mListView != null) {
//            mListView.setAdapter(mAdapter);
//            mListView.setOnItemClickListener(this);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routes, container, false);
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
        Log.i("test", "onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
//            ((NavigationView.OnNavigationItemSelectedListener)context).onNavigationItemSelected(
//                    );
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

    @Override
    public void onSummonerChosen(Route route) {
//        Toast.makeText(getContext(), route.routeNumber, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Summoner summoner = mAdapter.get(position);
        Toast.makeText(getContext(), "Route " + summoner.id, Toast.LENGTH_SHORT).show();
    }


    //  VOLLEY START
    private void fetchPosts() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Riot-Token", NetworkHelper.API_KEY);
        headers.put("Accept-Language", "en-US,en;q=0.8");
        GsonRequest<Summoner> request = new GsonRequest<>((ENDPOINT + "Sempify"), Summoner.class,headers, onPostsLoaded,
                onPostsError);
//        GsonRequest request = new StringRequest(Request.Method.GET, ENDPOINT + "Sempify", onPostsLoaded, onPostsError);
        RiotRequestQueue.getInstance(getActivity()).getRequestQueue().add(request);
    }

    private final Response.Listener<Summoner> onPostsLoaded = new Response.Listener<Summoner>() {
        @Override
        public void onResponse(Summoner response) {
            Log.i("PostActivity", response.name);
            Log.i("PostActivity", "AccountID: " + response.accountId);
            Log.i("PostActivity", "Level: " + response.summonerLevel);

            if(mListView == null)
                mListView = getActivity().findViewById(R.id.summoners_list);

            if(mAdapter == null){
                Log.i("test", "instantiate adapter");
                ArrayList<Summoner> tmp = new ArrayList<>();
                tmp.add(response);
                mAdapter = new SummonerAdapter(
                        getContext(), R.id.route_item_text, tmp, SummonerFragment.this, getActivity());

                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(SummonerFragment.this);
            }
            else{
                Log.i("test", "add to adapter");
//                mAdapter.put(response);
                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(SummonerFragment.this);
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };
    //  VOLLEY END
}
