package com.garydty.a10366827.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.garydty.a10366827.R;
import com.garydty.a10366827.adapters.RSSArticleAdapter;
import com.garydty.a10366827.interfaces.OnFragmentInteractionListener;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

/**
 * Created by Gary Doherty on 25/09/2017.
 */

public class ArticleFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String TAG = "ArticleFragment";
    private static RSSArticleAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    public ArticleFragment() {
        // Required empty public constructor
    }

    public static ArticleFragment newInstance() {
        return new ArticleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  VOLLEY END
        Log.i("test", "onViewCreated");

        if(mAdapter == null) {
            //url of RSS feed
            String riotRSSUrl = "http://euw.leagueoflegends.com/en/rss.xml";
            Parser parser = new Parser();
            final ProgressBar progress = getActivity().findViewById(R.id.rss_load_progress);
            progress.setVisibility(View.VISIBLE);
            parser.execute(riotRSSUrl);
            parser.onFinish(new Parser.OnTaskCompleted() {

                @Override
                public void onTaskCompleted(ArrayList<Article> list) {
                    Log.i(TAG, "XML Retrieved");
                    mAdapter = new RSSArticleAdapter(getContext(), R.layout.article_item, list);
                    progress.setVisibility(View.GONE);
                    if (mListView == null)
                        mListView = getActivity().findViewById(R.id.item_list);
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(ArticleFragment.this);
                }

                @Override
                public void onError() {
                    //what to do in case of error
                    Log.e(TAG, "RSS Parser Retrieval Error.");
                }
            });
        }
        else if(mListView == null){
            mListView = getActivity().findViewById(R.id.item_list);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(ArticleFragment.this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Article summoner = mAdapter.get(position);
//        Toast.makeText(getContext(), summoner.getLink(), Toast.LENGTH_SHORT).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(summoner.getLink()));
        startActivity(browserIntent);
    }
}