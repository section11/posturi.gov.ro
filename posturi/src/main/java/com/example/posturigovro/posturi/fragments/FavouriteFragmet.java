package com.example.posturigovro.posturi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.posturigovro.posturi.R;
import com.example.posturigovro.posturi.adapters.FavouriteArrayAdapter;
import com.example.posturigovro.posturi.utils.Favorit;
import com.example.posturigovro.posturi.utils.Post;
import com.example.posturigovro.posturi.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by section11 on 07/08/14.
 */
public class FavouriteFragmet  extends Fragment {

    private static final String ARG_SECTION_POST = "section_post";
    public Context listcontext;
    FavouriteArrayAdapter favoriteArrayAdapter;
    Utils utils = new Utils();
    Favorit favorit;
    ArrayList<Post> favorite;
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
    public FavouriteFragmentCallbacks favouriteFragmentCallbacks;

    public static FavouriteFragmet newInstance(Context context) {
        FavouriteFragmet fragment = new FavouriteFragmet();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setUp(context);
        return fragment;
    }

    private void setUp(Context context) {

        listcontext = context;
    }

    public FavouriteFragmet() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.favouritelist, container, false);
        ListView listaFavorite = (ListView) rootView.findViewById(R.id.listaPosturiFavorite);
        Button searchButton2 = (Button) rootView.findViewById(R.id.searchButton2);
        searchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onNavigationDrawerItemSelected("Cautare posturi", 1);
            }
        });
        favorit = new Favorit(listcontext);
        favorite = favorit.getFavorite();
        favoriteArrayAdapter = new FavouriteArrayAdapter(listcontext, R.layout.rowlayout, favorite);
        listaFavorite.setAdapter(favoriteArrayAdapter);

        listaFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gson gson = new Gson();
                String post = gson.toJson(favorite.get(i));
                favouriteFragmentCallbacks.onFavouriteFragmentItemSelected("Favourite Fragment", post);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
            favouriteFragmentCallbacks = (FavouriteFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static interface FavouriteFragmentCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onFavouriteFragmentItemSelected(String item, String stringObject);

    }
}


