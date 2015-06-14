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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class SurpriseMe extends ActionBarActivity
{
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beerdisplay);
        pb = (ProgressBar)findViewById(R.id.progressBarNew);
        pb.setMax(100);
        startLoadTask(SurpriseMe.this);
    }

    public void startLoadTask(Context c){
        if (isOnline()) {

            CallAPI task = new CallAPI();
            task.execute();
            pb.setProgress(75);
        } else {
            pb.setVisibility(View.GONE);

            Toast.makeText(c, "Not Online", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private class CallAPI extends AsyncTask<String, String, String> {
        ArrayList<Beer> randomBeer;
        HttpURLConnection urlConnection = null;
        ImageView image = (ImageView) findViewById(R.id.Pic);
        Bitmap bmImage = null;

        @Override
        protected String doInBackground(String... params) {
            String startURL = "https://api.brewerydb.com/v2/";
            String endURL = "key=e1afe81e104ba290bb7507cd693ead92&format=json";
            String dataString = startURL + "beer/random?hasLabels=y&" + endURL;

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
                Log.d("data", fData);


                randomBeer = Beer.getRandomBeer(fData);

                InputStream in = new java.net.URL(randomBeer.get(0).getPic()).openStream();
                bmImage = BitmapFactory.decodeStream(in);


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
                if(bmImage == null)
                {
                    image.setImageDrawable(getResources().getDrawable(R.drawable.unavailable));
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();

                }
            }
            return "";
        }

        protected void onPostExecute(String result) {
            pb.setVisibility(View.GONE);
            if(result != null)
            {
                TextView rName = (TextView) findViewById(R.id.Name);
                TextView rDesc = (TextView) findViewById(R.id.Desc);
                TextView rABV = (TextView)  findViewById(R.id.ABV);

                if(randomBeer != null)
                {
                    rName.setText(randomBeer.get(0).getName());
                    rABV.setText("ABV(" + randomBeer.get(0).getAbv() + "%)");

                    if(randomBeer.get(0).getDescription().length()>0)
                    {
                        rDesc.setText(randomBeer.get(0).getDescription());
                    }
                    else
                    {
                        rDesc.setText("Description is Unavailable");
                    }
                    if(bmImage == null)
                    {
                        image.setImageDrawable(getResources().getDrawable(R.drawable.unavailable));
                    }
                    else {
                        image.setImageBitmap(bmImage);
                    }
                    final TextView rec = (TextView) findViewById(R.id.recommended);

                    rec.setText("View Recommended Beers");
                    rec.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String query = randomBeer.get(0).getStyleId();

                            Intent intent = new Intent(SurpriseMe.this, Recommended.class);
                            intent.putExtra("query", query);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    rName.setText("Error");
                }
            }

        }
    }
}

