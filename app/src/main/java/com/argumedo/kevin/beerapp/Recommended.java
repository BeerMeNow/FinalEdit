package com.argumedo.kevin.beerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Recommended extends ActionBarActivity
{
    ListView listView;
    ArrayList<Beer> beers1;
    String query;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_list);

        Intent intent = getIntent();
        query = intent.getStringExtra("query");
        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setMax(100);
        startLoadTask(Recommended.this);
    }

    public void startLoadTask(Context c){
        if (isOnline()) {
            pb.setProgress(75);
            CallAPI task = new CallAPI();
            task.execute();

        } else {
            pb.setVisibility(View.GONE);
            Toast.makeText(c, "Not online", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private class CallAPI extends AsyncTask<String, String, String> {
        ArrayList<Beer> beers;
        HttpURLConnection urlConnection = null;

        @Override
        protected String doInBackground(String... params) {

            String startURL = "https://api.brewerydb.com/v2/";
            String endURL = "key=e1afe81e104ba290bb7507cd693ead92&format=json";
            String dataString = startURL + "beers/?styleId="+ query + "&hasLabels=y&" + endURL;
            pb.setVisibility(View.GONE);

            try {
                URL dataURL = new URL(dataString);

                urlConnection = (HttpURLConnection) dataURL.openConnection();
                urlConnection.connect();
                int status = urlConnection.getResponseCode();
                Log.i(status + "", status + "");

                InputStream inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

                String response;
                StringBuilder sb = new StringBuilder();

                while ((response = bReader.readLine()) != null) {
                    sb = sb.append(response);
                }
                String fData = sb.toString();
                beers = Beer.getBeers(fData);

            }  catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return "";
        }

        protected void onPostExecute(String result) {
            if(result != null)
            {
                beers1 = beers;
            }
            String[] beernames = new String[beers.size()];
            for(int i = 0; i < beers.size(); i++)
            {
                beernames[i] = beers.get(i).getName();
            }

            listView = (ListView) findViewById(R.id.list);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Recommended.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, beernames);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    int itemPosition     = position;


                    Beer vBeer = beers.get(itemPosition);

                    Intent intent = new Intent(Recommended.this, BeerDisplay.class);
                    intent.putExtra("vBeer", vBeer );
                    startActivity(intent);
                }



            });




        }
    }
}

