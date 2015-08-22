package com.example.posturigovro.posturi.utils;

/**
 * Created by section11 on 06/08/14.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Segarceanu Calin on 3/9/14.
 */
public class Favorit{

    private Context context;
    public ArrayList<Post> favorite;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson gson;
    public Favorit(Context context){
        this.context = context;
        favorite = new ArrayList<Post>();
        pref = context.getSharedPreferences("Favorite", MODE_PRIVATE);
        editor = pref.edit();
        String obj = pref.getString("objJSON", "");
        gson = new Gson();
        favorite = gson.fromJson(obj, new TypeToken<ArrayList<Post>>(){}.getType());
        if(favorite == null){
            favorite = new ArrayList<Post>();
        }
    }

    public void addPost(Post post){
        favorite.add(post);
        Type listOfTestObject = new TypeToken<ArrayList<Post>>(){}.getType();
        String s = gson.toJson(favorite, listOfTestObject);
        editor.remove("objJSON");
        editor.putString("objJSON", s);
        editor.apply();
        String obj = pref.getString("objJSON", null);
    }

    public void removePost(Post post2){
        String id = Integer.toString(post2.getId());
        int i = 0;
        for (Post post : favorite) {
            String id2 = Integer.toString(post.getId());
            if (id2.equals(id)) {
                favorite.remove(i);
                break;
            }
            i++;
        }
        Type listOfTestObject = new TypeToken<ArrayList<Post>>(){}.getType();
        String s = gson.toJson(favorite, listOfTestObject);
        editor.remove("objJSON");
        editor.putString("objJSON", s);
        editor.apply();
    }

    public int checkId(String id){
        if(favorite != null) {
            for (Post post : favorite) {
                String id2 = Integer.toString(post.getId());
                if (id2.equals(id)) {
                    return 1;
                }
            }
        }
        return 0;
    }

    public ArrayList<Post> getFavorite(){
        return favorite;
    }

}