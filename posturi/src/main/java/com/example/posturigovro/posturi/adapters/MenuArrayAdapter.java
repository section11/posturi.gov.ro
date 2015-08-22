package com.example.posturigovro.posturi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.posturigovro.posturi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by section11 on 05/08/14.
 */
public class MenuArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private List<String> values = new ArrayList<String>();

    public MenuArrayAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        values.clear();
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.navrowlayout, parent, false);

        TextView navRow = (TextView) rowView.findViewById(R.id.navRowTextView);
        navRow.setText(values.get(position));

        return rowView;
    }
}
