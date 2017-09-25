package com.garydty.a10366827.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.garydty.a10366827.R;
import com.garydty.a10366827.adapters.RouteAdapter;
import com.garydty.a10366827.interfaces.OnFragmentInteractionListener;
import com.garydty.a10366827.interfaces.RouteHandler;
import com.garydty.a10366827.models.Route;

import java.util.ArrayList;
import java.util.List;

public class RoutesFragment extends Fragment implements RouteHandler, AdapterView.OnItemClickListener{
    private static RouteAdapter adapter;
    private static ListView mListView;

    private OnFragmentInteractionListener mListener;

    public RoutesFragment() {
        // Required empty public constructor
//        setRetainInstance(true);
    }

    public static RoutesFragment newInstance() {
        RoutesFragment fragment = new RoutesFragment();
        return fragment;
    }

//    public void setData(List<Route> data){
//
//    }
//
//    public RouteAdapter getData(){
//        return adapter.;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView= getActivity().findViewById(R.id.routes_list);
        if(adapter == null){
            ArrayList<Route> tmp = new ArrayList<Route>();
            tmp.add(new Route(1));
            tmp.add(new Route(2));
            tmp.add(new Route(3));
            tmp.add(new Route(4));
            tmp.add(new Route(5));
            adapter = new RouteAdapter(getContext(), R.id.route_item_text, tmp, this, getActivity());
        }

        if(mListView != null) {
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(this);
        }
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
    public void OnRouteChosen(Route route) {
        Toast.makeText(getContext(), route.routeNumber, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Route tmp = adapter.get(position);
        Toast.makeText(getContext(), "Route " + tmp.routeNumber, Toast.LENGTH_SHORT).show();
        adapter.put(new Route(6));
    }
}
