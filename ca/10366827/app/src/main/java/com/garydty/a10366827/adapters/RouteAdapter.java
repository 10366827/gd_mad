package com.garydty.a10366827.adapters;

/**
 * Created by Gary Doherty on 17/09/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.garydty.a10366827.R;
import com.garydty.a10366827.interfaces.RouteHandler;
import com.garydty.a10366827.models.Route;

import java.util.ArrayList;

public class RouteAdapter extends ArrayAdapter<Route> {
    ArrayList<Route> routeList;
    RouteHandler routeHandler;
    private Activity activity;
//    private AcceptFriendTask acceptFriendTask;

    public RouteAdapter(Context context, int textViewResourceId, ArrayList<Route> list,
                          RouteHandler routeHandler, Activity activity) {
        super(context, textViewResourceId, list);
        this.routeList = list;
        this.routeHandler = routeHandler;
        this.activity = activity;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Route route = routeList.get(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.route_item, null);

        TextView routeText = (TextView) v.findViewById(R.id.route_item_text);
        routeText.setText("Route " + route.routeNumber);

        // the view must be returned to our activity
        return v;
    }

//    public static void sortList(){
//        if(!isNullOrEmpty())
//            Collections.sort(routeList);
//    }

    public Route get(int index){
        return routeList.get(index);
    }

    public void put(Route route){
        routeList.add(route);
        notifyDataSetChanged();
    }

//    public boolean isNullOrEmpty(){
//        return routeList == null || routeList.isEmpty();
//    }

//    public int size(){
//        return routeList != null ? routeList.size() : 0;
//    }
//
//    public int getIndexOf(Route route){
//        for(int i = 0; i < routeList.size(); i++)
//            if(routeList.get(i).routeNumber == route.routeNumber)
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
