package com.argumedo.kevin.beerapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Beer implements Serializable{
    private String beerId, name, description, abv, pic, styleId,type = "0";
    public Beer(JSONObject jsonBeer, String Pic) throws JSONException
    {
        this.beerId = (String) jsonBeer.optString("id");
        this.name = (String) jsonBeer.optString("name");
        this.description = (String) jsonBeer.optString("description");
        this.abv = (String) jsonBeer.optString("abv");
        this.pic = Pic;
        this.styleId = jsonBeer.optString("styleId");
    }

    public static ArrayList<Beer> getFeaturedBeer(String featuredData) throws JSONException
    {
        ArrayList<Beer> featuredBeer = new ArrayList<>();
        JSONObject results = new JSONObject(featuredData);
        JSONObject data = results.optJSONObject("data");
        JSONObject fBeer = data.optJSONObject("beer");
        JSONObject label = fBeer.optJSONObject("labels");
        String pic = label.optString("large");
        Beer BotW = new Beer(fBeer, pic);
        featuredBeer.add(BotW);

        return featuredBeer;
    }


    public static ArrayList<Beer> getRandomBeer(String featuredData) throws JSONException
    {
        ArrayList<Beer> randomBeer = new ArrayList<>();
        JSONObject results = new JSONObject(featuredData);
        JSONObject data = results.optJSONObject("data");
        Log.d("DATADATA", data.toString());
        //catches error if picture does not exist
        try
        {
            JSONObject labels = data.optJSONObject("labels");
            Log.d("PICTURE", labels +"");
            String pic = labels.optString("large");
            Log.d("PICTURE", pic);
            Beer Random = new Beer(data, pic);
            randomBeer.add(Random);
        }
        catch(NullPointerException E) {
            String pic = "";
            Beer Random = new Beer(data, pic);
            randomBeer.add(Random);
        }
        return randomBeer;

    }

    public static ArrayList<Beer> getBeers(String featuredData) throws JSONException
    {
        ArrayList<Beer> beers = new ArrayList<>();
        JSONObject results = new JSONObject(featuredData);
        JSONArray data = results.optJSONArray("data");
        String pic = "";
        for(int i = 0; i < data.length(); i++)
        {
            JSONObject beer =  (JSONObject) data.get(i);
            try
            {
                JSONObject labels = beer.optJSONObject("labels");
                pic = labels.optString("large");
            }
            catch(Exception E)
            {
                pic = "";
            }
            Beer cBeer = new Beer(beer, pic);
            beers.add(cBeer);
        }

        return beers;

    }

    public String getName() {
        return name;
    }

    public String getBeerId() {
        return beerId;
    }

    public String getAbv() {
        return abv;
    }

    public String getDescription() {

        return description;
    }

    public String getStyleId() {
        return styleId;
    }

    public String getPic() {
        return pic;
    }

    public String getType(){return type; }

    public void setType(String type){this.type = type; }
}
