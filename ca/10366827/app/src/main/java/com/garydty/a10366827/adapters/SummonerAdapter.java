package com.garydty.a10366827.adapters;

/**
 * Created by Gary Doherty on 17/09/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.garydty.a10366827.R;
import com.garydty.a10366827.interfaces.SummonerChosenListener;
import com.garydty.a10366827.models.Summoner;

import java.util.ArrayList;

public class SummonerAdapter extends ArrayAdapter<Summoner> {
    private ArrayList<Summoner> mSummonerList;
    private SummonerChosenListener mSummonerListener;

    public SummonerAdapter(Context context, int textViewResourceId, ArrayList<Summoner> list,
                           SummonerChosenListener mSummonerListener, Activity activity) {
        super(context, textViewResourceId, list);
        this.mSummonerList = list;
        this.mSummonerListener = mSummonerListener;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Summoner summoner = mSummonerList.get(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.route_item, null);

        TextView routeText = (TextView) v.findViewById(R.id.route_item_text);
        routeText.setText("Route " + summoner.name);

        // the view must be returned to our activity
        return v;
    }

//    public static void sortList(){
//        if(!isNullOrEmpty())
//            Collections.sort(mSummonerList);
//    }

    public Summoner get(int index){
        return mSummonerList.get(index);
    }

    public void put(Summoner summoner){
        mSummonerList.add(summoner);
        notifyDataSetChanged();
    }

//    public boolean isNullOrEmpty(){
//        return mSummonerList == null || mSummonerList.isEmpty();
//    }

//    public int size(){
//        return mSummonerList != null ? mSummonerList.size() : 0;
//    }
//
//    public int getIndexOf(Route route){
//        for(int i = 0; i < mSummonerList.size(); i++)
//            if(mSummonerList.get(i).routeNumber == route.routeNumber)
//                return i;
//
//        return -1;
//    }

//    public class AcceptFriendTask extends AsyncTask<Void, Void, RequestResult> {
//
//        private final String username;
//        private final String password;
//        private final Contact contact;
//
//        public AcceptFriendTask(String username, String password, Contact contact) {
//            this.username = username;
//            this.password = password;
//            this.contact = contact;
//        }
//
//        @Override
//        protected RequestResult doInBackground(Void... param) {
//            HashMap<String,String> params = new HashMap<String,String>();
//            params.put(USERNAME, username);
//            params.put(PASSWORD, password);
//            params.put(Constants.CONTACT_ID, ""+contact.getId());
//
//            Log.i("AcceptFriend", "contact username:" + contact.getUsername());
//            RequestResult result =  httpsPostRequest(activity.getApplicationContext(),
//                    ACCEPT_CONTACT_URL, params);
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(final RequestResult result) {
//            if (result != null && (result.getCode() == HttpsURLConnection.HTTP_OK)) {
//                DatabaseHelper dbh = new DatabaseHelper(activity.getApplicationContext());
//                contact.setStatus(Contact.STATUS_ACCEPTED);
//                dbh.updateContact(contact);
//                notifyContactAccepted(contact.getId());
//            } else {
//                Log.i("AcceptContact", "Failed");
//            }
//            acceptFriendTask = null;
//        }
//    }
}
