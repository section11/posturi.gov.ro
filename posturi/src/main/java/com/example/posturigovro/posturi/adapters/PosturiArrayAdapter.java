package com.example.posturigovro.posturi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.posturigovro.posturi.R;
import com.example.posturigovro.posturi.utils.Post;
import com.example.posturigovro.posturi.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by section11 on 01/08/14.
 */
public class PosturiArrayAdapter extends ArrayAdapter<Post> {

    private final Context context;
    private List<Post> values = new ArrayList<Post>();
    private int height;

    public PosturiArrayAdapter(Context context, int resource, ArrayList<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        values.clear();
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView titluPost = (TextView) rowView.findViewById(R.id.titluPost);
        TextView miniContinutPost = (TextView) rowView.findViewById(R.id.miniContinutPost);
        String titlu = values.get(position).getTitlu();
        String continut = values.get(position).getContinut();
        Utils utils = new Utils();
        String miniContinut = utils.getMiniContinut(continut);
        titluPost.setText(titlu);
        miniContinutPost.setText(values.get(position).getInstitutie());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Date date2 = null;
        DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        try{
            date2 = formatter2.parse(formattedDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(date2);
        //Start Date
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(values.get(position).getData());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(date);
        long days = utils.daysBetween(calendarStart, calendarEnd);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.buttonColorBar);

        //green
        if(30 - days >= 20) {
            imageView.setBackground(context.getResources().getDrawable(R.drawable.greenbutton));
        }
        else if(30 - days >= 10){
            //yellow
            imageView.setBackground(context.getResources().getDrawable(R.drawable.yellowbutton));
        }else if(30 - days >= 0){
            //red
            imageView.setBackground(context.getResources().getDrawable(R.drawable.redbutton));
        }else{
            //gray
            imageView.setBackground(context.getResources().getDrawable(R.drawable.graybutton));
        }
        return rowView;
    }
}
