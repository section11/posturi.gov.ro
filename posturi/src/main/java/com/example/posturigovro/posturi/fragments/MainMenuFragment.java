package com.example.posturigovro.posturi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.posturigovro.posturi.LoadMoreListView;
import com.example.posturigovro.posturi.R;
import com.example.posturigovro.posturi.adapters.PosturiArrayAdapter;
import com.example.posturigovro.posturi.utils.Post;
import com.example.posturigovro.posturi.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by section11 on 05/08/14.
 */
public class MainMenuFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_LATITUDE_NUMBER = "latitude_number";
    private static final String ARG_LONGITUDE_NUMBER = "longitude_number";

    public ListView dashlistview;
    public Context listcontext;
    LoadMoreListView listaPosturi;
    PosturiArrayAdapter posturiArrayAdapter;
    Utils utils = new Utils();
    Gson gson;

    String appToken = "7d5f92fa1853cc4b33ca548a3f5ea95d";
    String host = "http://posturi.tbgalati.ro/feed/json?callback=";
    int page = 0;
    public double latitude, longitude;
    public String oras;
    public ArrayList<Post> posturi;


    private MainFragmentCallbacks mCallbacks;
    private NavigationDrawerFragment.NavigationDrawerCallbacks navigationDrawerCallbacks;
    public AsyncTaskRunner getData;
    public static MainMenuFragment newInstance(int sectionNumber, double lat, double lng, String oras) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putDouble(ARG_LATITUDE_NUMBER, lat);
        args.putDouble(ARG_LONGITUDE_NUMBER, lng);
        fragment.setArguments(args);
        fragment.setUp(lat, lng, oras);
        return fragment;
    }

    private void setUp(double lat, double lng, String oras) {
        this.latitude = lat;
        this.longitude = lng;
        this.oras = oras;
    }

    public MainMenuFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listaPosturi = (LoadMoreListView) rootView.findViewById(R.id.listaPosturi);
        TextView locationDisplay = (TextView) rootView.findViewById(R.id.locationDisplay);
        locationDisplay.setText(Html.fromHtml("Oferte de angajare din zona <u><b>" + oras + "</b></u>"));
        Button searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationDrawerCallbacks.onNavigationDrawerItemSelected("Cautare posturi", 1);
            }
        });
        listcontext = listaPosturi.getContext();

        if(posturiArrayAdapter != null){
            listaPosturi.setAdapter(posturiArrayAdapter);
        }else {
            posturi = new ArrayList<Post>();
            for (page = 0; page < 3; page++) {
                String function = "getPosturi";
                String URL = host + function + "&appToken=" + appToken + "&lat=" + latitude + "&lng=" + longitude + "&page=" + page;
                getData = new AsyncTaskRunner();
                getData.execute(URL);
            }
        }
        page--;
        listaPosturi.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                String function = "getPosturi";
                String URL = host + function + "&appToken=" + appToken + "&lat=" + latitude + "&lng=" + longitude + "&page=" + page;
                getData = new AsyncTaskRunner();
                getData.execute(URL);
            }
        });
        listaPosturi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gson gson = new Gson();
                String post = gson.toJson(posturi.get(i));
                mCallbacks.onMainFragmentItemSelected("Main Menu Fragment", post);
            }
        });

        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SharedPreferences orderData = getActivity().getSharedPreferences("fragment_main", Context.MODE_APPEND);
        Log.d("msg", ""+orderData.getBoolean("saved", false));
        if(orderData.getBoolean("saved", false)){
            String obj = orderData.getString("posturi_main", "");
            gson = new Gson();
            posturi = new ArrayList<Post>();
            posturi = gson.fromJson(obj, new TypeToken<ArrayList<Post>>(){}.getType());
            if(posturi.size() == 0) {
                posturiArrayAdapter = new PosturiArrayAdapter(listcontext, R.layout.rowlayout, posturi);
                page = 2;
            }
        }
        try {
            mCallbacks = (MainFragmentCallbacks) activity;
            navigationDrawerCallbacks = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement MainCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        getData.cancel(true);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        gson = new Gson();
        Type listOfTestObject = new TypeToken<ArrayList<Post>>(){}.getType();
        String s = gson.toJson(posturi, listOfTestObject);
        savedInstanceState.putString("posturi", s);
        Log.d("URL", "1+");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        saveData();
    }

    public void saveData() {
        Log.d("msg", "Save Instance");
        SharedPreferences.Editor outState = getActivity().getSharedPreferences("fragment_main", Context.MODE_APPEND).edit();
        gson = new Gson();
        Type listOfTestObject = new TypeToken<ArrayList<Post>>(){}.getType();
        String s = gson.toJson(posturi, listOfTestObject);
        Log.d("URL", s);
        outState.putString("posturi_main", s);
        outState.putBoolean("saved", true);
        outState.commit();
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp = "";
        ProgressDialog pDialog;
        protected void onPreExecute(){

        }
        @Override
        protected String doInBackground(String... strings) {

            if (isCancelled()) {
                return null;
            }

            String url = strings[0];
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    resp += s;
                }
                JSONObject response = new JSONObject(resp);
                JSONArray results = response.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject item = results.getJSONObject(i);
                    Integer id = item.getInt("id");
                    String titlu = item.getString("titlu");
                    String institutie = item.getString("institutie");
                    String data = item.getString("data");
                    String locatie = item.getString("locatie");
                    String continut = item.getString("descriere");
                    String tipjob = item.getString("tipjob");
                    continut = utils.replaceContent(continut);
                    Post post = new Post(titlu, institutie, data, id, locatie, continut, tipjob);
                    posturi.add(post);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return  null;
        }

        protected void onPostExecute(String data){
            if(page == 2){
                posturiArrayAdapter = new PosturiArrayAdapter(listcontext, R.layout.rowlayout, posturi);
                listaPosturi.setAdapter(posturiArrayAdapter);

            }
            if(page > 2){
                posturiArrayAdapter.notifyDataSetChanged();
                listaPosturi.onLoadMoreComplete();
            }

        }
        @Override
        protected void onCancelled() {
            // Notify the loading more operation has finished
            listaPosturi.onLoadMoreComplete();
        }
    }

    public static interface MainFragmentCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onMainFragmentItemSelected(String item, String stringObject);

    }

}