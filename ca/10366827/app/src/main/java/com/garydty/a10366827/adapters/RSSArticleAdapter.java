package com.garydty.a10366827.adapters;

/**
 * Created by Gary Doherty on 25/09/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.garydty.a10366827.R;
import com.garydty.a10366827.interfaces.SummonerChosenListener;
import com.garydty.a10366827.models.Summoner;
import com.prof.rssparser.Article;

import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RSSArticleAdapter extends ArrayAdapter<Article> {
    private ArrayList<Article> mArticleList;

    public RSSArticleAdapter(Context context, int textViewResourceId, ArrayList<Article> list) {
        super(context, textViewResourceId, list);
        this.mArticleList = list;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Article article = mArticleList.get(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.article_item, null);
        TextView title = v.findViewById(R.id.article_title);
        title.setText(article.getTitle());

        TextView description = v.findViewById(R.id.article_description);
//        String desc = Html.fromHtml(article.getDescription()).toString();
        String desc = Jsoup.parse(article.getDescription()).text();
        description.setText(
                desc
            );

        TextView date = v.findViewById(R.id.article_date);

        date.setText(
                new SimpleDateFormat("MM-dd-yyyy").format(article.getPubDate()));

        // the view must be returned to our activity
        return v;
    }

    public Article get(int index){
        return mArticleList.get(index);
    }

    public void put(Article article){
        mArticleList.add(article);
        notifyDataSetChanged();
    }
}
