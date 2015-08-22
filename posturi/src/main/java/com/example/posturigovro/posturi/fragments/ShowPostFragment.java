package com.example.posturigovro.posturi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.posturigovro.posturi.R;
import com.example.posturigovro.posturi.adapters.PosturiArrayAdapter;
import com.example.posturigovro.posturi.utils.Favorit;
import com.example.posturigovro.posturi.utils.Post;
import com.example.posturigovro.posturi.utils.Utils;
import com.google.gson.Gson;

/**
 * Created by section11 on 07/08/14.
 */
public class ShowPostFragment extends Fragment {

    private static final String ARG_SECTION_POST = "section_post";
    public Context listcontext;
    PosturiArrayAdapter posturiArrayAdapter;
    Utils utils = new Utils();
    Post obj;
    TextView titluPost, tipPost, judetPost, dataPost, institutiePost, continutPost;
    Button addToFavourites, paginaPrecedenta;
    Favorit favorit;
    ShowPostFragmentCallbacks showPostFragmentCallbacks;
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;

    public static ShowPostFragment newInstance(String post, Context context) {
        ShowPostFragment fragment = new ShowPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_POST, post);
        fragment.setArguments(args);
        fragment.setUp(post, context);
        return fragment;
    }

    private void setUp(String post, Context context) {
        Gson gson = new Gson();
        obj = gson.fromJson(post, Post.class);
        listcontext = context;
    }

    public ShowPostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.showpost, container, false);

        titluPost = (TextView) rootView.findViewById(R.id.titluPost);
        titluPost.setText(obj.getTitlu());
        tipPost = (TextView) rootView.findViewById(R.id.tipPost);
        tipPost.setText(obj.getTipjob());
        judetPost = (TextView) rootView.findViewById(R.id.judetPost);
        judetPost.setText(obj.getLocatie());
        dataPost = (TextView) rootView.findViewById(R.id.dataPost);
        dataPost.setText(obj.getData());
        institutiePost = (TextView) rootView.findViewById(R.id.institutiePost);
        institutiePost.setText(obj.getInstitutie());
        continutPost = (TextView) rootView.findViewById(R.id.continutPost);
        continutPost.setText(Html.fromHtml(obj.getContinut()));
        continutPost.setMovementMethod(LinkMovementMethod.getInstance());
        addToFavourites = (Button) rootView.findViewById(R.id.addToFavourites);
        paginaPrecedenta = (Button) rootView.findViewById(R.id.buttonPaginaPrecedenta);
        String id = Integer.toString(obj.getId());
        favorit = new Favorit(listcontext);
        if(favorit.checkId(id) == 1){
            addToFavourites.setEnabled(false);
            addToFavourites.setText("Favorit");
        }
        addToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorit.addPost(obj);
                addToFavourites.setEnabled(false);
                addToFavourites.setText("Favorit");
            }
        });

        paginaPrecedenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPostFragmentCallbacks.onBackPressed2();
            }
        });
        //Log.d("URL", obj.getContinut());
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
            showPostFragmentCallbacks = (ShowPostFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    public static interface ShowPostFragmentCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onBackPressed2();
    }
}