package com.example.posturigovro.posturi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posturigovro.posturi.R;
import com.example.posturigovro.posturi.utils.Judet;
import com.example.posturigovro.posturi.utils.Utils;

import java.util.ArrayList;

/**
 * Created by section11 on 07/08/14.
 */
public class SearchFragment extends Fragment {
    public Context listcontext;
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;

    public RadioGroup radioGroupAlegere;
    public SeekBar radiusBar;
    public TextView radiusTextView;
    public Spinner localitatiSearch;
    public CheckBox permanentCheckbox, temporarCheckbox, zile20Checkbox, zile10Checkbox, zileIntreCheckbox;
    public EditText cuvantCheie;

    public ArrayList<Judet> judete;

    public double latitude, longitude;
    public int localitate = 1;
    public String keyword = "";
    String appToken = "7d5f92fa1853cc4b33ca548a3f5ea95d";
    String host = "http://posturi.tbgalati.ro/feed/json?callback=";

    public static SearchFragment newInstance(Context context, double lat, double lng) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setUp(context, lat, lng);
        return fragment;
    }

    private void setUp(Context context, double lat, double lng) {
        listcontext = context;
        latitude = lat;
        longitude = lng;
    }

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createSpinner();
        View rootView = inflater.inflate(R.layout.search, container, false);
        radioGroupAlegere = (RadioGroup) rootView.findViewById(R.id.radioAlegere);
        radiusBar = (SeekBar) rootView.findViewById(R.id.radiusSearch);
        radiusTextView = (TextView) rootView.findViewById(R.id.radiusTextView);
        localitatiSearch = (Spinner) rootView.findViewById(R.id.locatitatiSearch);
        cuvantCheie = (EditText) rootView.findViewById(R.id.cuvantCheie);

        final Utils utils = new Utils();
        ArrayList<String> judeteString = utils.getJudeteInStringArray(judete);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(listcontext, R.layout.spinnerlayout, judeteString);
        localitatiSearch.setAdapter(adapter);

        radioGroupAlegere.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(R.id.locatieCurentaRadioButton == i){
                    localitatiSearch.setVisibility(View.GONE);
                    radiusBar.setVisibility(View.VISIBLE);
                    radiusTextView.setVisibility(View.VISIBLE);
                    localitate = 1;
                }else if(R.id.altaLocatieRadioButton == i){
                    localitatiSearch.setVisibility(View.VISIBLE);
                    radiusBar.setVisibility(View.GONE);
                    radiusTextView.setVisibility(View.GONE);
                    localitate = 2;
                }
            }
        });

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
                radiusTextView.setText("Raza " + progress + " km.");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });

        permanentCheckbox = (CheckBox) rootView.findViewById(R.id.permanentCheckBox);
        temporarCheckbox = (CheckBox) rootView.findViewById(R.id.temporarCheckBox);

        zile20Checkbox = (CheckBox) rootView.findViewById(R.id.zile20);
        zile10Checkbox = (CheckBox) rootView.findViewById(R.id.zile10);
        zileIntreCheckbox = (CheckBox) rootView.findViewById(R.id.intre);

        cuvantCheie.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    keyword = textView.getText().toString();
                }
                return true;
            }
        });

        Button searchButton = (Button) rootView.findViewById(R.id.searchNow);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String function = "search";
                int radius = 10;
                if(localitate == 2){
                    String jud = localitatiSearch.toString();
                    latitude = utils.getLatitude(jud, judete);
                    longitude = utils.getLongitude(jud, judete);
                }else{
                    radius = radiusBar.getProgress();
                }
                if(keyword.length() < 5){
                    Toast.makeText(listcontext, "Cuvantul cheie trebuie sa aiba o lungime de cel putin 5 caractere", Toast.LENGTH_LONG);
                }
                boolean perm = permanentCheckbox.isChecked();
                boolean temp = temporarCheckbox.isChecked();
                boolean zile20 = zile20Checkbox.isChecked();
                boolean zile10 = zile10Checkbox.isChecked();
                boolean intre = zileIntreCheckbox.isChecked();
                keyword = cuvantCheie.getText().toString();
                String URL = host + function + "&appToken=" + appToken + "&lat=" + latitude + "&lng=" + longitude + "&search=" + keyword
                        + "&perm=" + perm + "&temp=" + temp + "&zile20=" + zile20 + "&zile10=" + zile10 + "&intre=" + intre + "&radius=" + radius;
                Log.d("URL", URL);
            }
        });

        return rootView;
    }

    private void createSpinner() {
        judete = new ArrayList<Judet>();
        Judet judet = new Judet("Alba", 46.0183605, 23.4619914);
        judete.add(judet);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
