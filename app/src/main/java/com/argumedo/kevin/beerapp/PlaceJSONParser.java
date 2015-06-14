package com.argumedo.kevin.beerapp;

/**
 * Created by Daniel on 5/25/2015.
 *
 * Credits:
 * PlaceJSONParser:
 *      -George Mathew
 *      link http://wptrafficanalyzer.in/blog/showing-nearby-places-using-google-places-api-and-google-map-android-api-v2/
 *      Note: although the code for this file was based on his work,
 *      it was heavily modified in order to parse items from Google Place API
 */
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceJSONParser {

    public List<HashMap<String,String>> parse(JSONObject jObject){

        JSONArray jPlaces = null;
        try {

            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jPlaces);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;

        for(int i=0; i<placesCount;i++){
            try {
                place = getPlace((JSONObject)jPlaces.get(i));
                placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject jPlace){

        HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longitude="";
        Boolean containsBar = false;

//        ArrayList<String> types = new ArrayList<>();
        JSONArray types = new JSONArray();

        try {
            // Extracting Place name, if available
            if(!jPlace.isNull("name")){
                placeName = jPlace.getString("name");
            }

            // Extracting Place Vicinity, if available
            if(!jPlace.isNull("vicinity")){
                vicinity = jPlace.getString("vicinity");
            }

            latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");

            types = jPlace.getJSONArray("types");

            String test = "JSON Parser: ";
            for(int i = 0; i < types.length(); i++){
                if("bar".equals(types.getString(i))){
                    containsBar = true;
                }
//                Log.i(test, types.getString(i));
            }

            //adds extracted/parsed data to the hash table in order to use them with google maps.
            place.put("place_name", placeName);
            place.put("vicinity", vicinity);
            place.put("lat", latitude);
            place.put("lng", longitude);
            place.put("hasBar", String.valueOf(containsBar));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}