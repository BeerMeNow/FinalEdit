package com.argumedo.kevin.beerapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import android.support.v4.app.FragmentTransaction;
import android.widget.ProgressBar;


public class Favorites extends ActionBarActivity
{
    //kevin sucks!
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.favorites);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container2, new beerFragment());

        ft.commit();

    }


}
