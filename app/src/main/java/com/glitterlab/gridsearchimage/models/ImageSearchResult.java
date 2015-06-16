package com.glitterlab.gridsearchimage.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageSearchResult implements Serializable {
public static String fullurl;
    public static String thumburl;
    public static String title;

    // new ImageSearchResult(..row json..)
    public ImageSearchResult(JSONObject json){
        try{
            this.fullurl= json.getString("url");
            this.thumburl= json.getString("tbUrl");
            this.title= json.getString("title");
        }catch (JSONException e){
                e.printStackTrace();
        }

    }
    //take an array of json image and return arraylist of image result
//imageresult.fromJSONArray([...,...])
    public static ArrayList<ImageSearchResult> fromJSONarray(JSONArray array){
ArrayList<ImageSearchResult> result= new ArrayList<ImageSearchResult>();
        for ( int i=0; i<array.length();i++){
            try {
                result.add( new ImageSearchResult(array.getJSONObject(i)));

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  result;
    }

}
