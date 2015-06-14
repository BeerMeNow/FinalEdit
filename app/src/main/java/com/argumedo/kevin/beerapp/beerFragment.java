package com.argumedo.kevin.beerapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class beerFragment extends Fragment implements AdapterView.OnItemClickListener{
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fav_list, container, false);

        DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
        cursor = dbHelper.getAllRows();

        //will change this later when we use a CursorLoader
        getActivity().startManagingCursor(cursor);

        favoritesAdapter adapter = new favoritesAdapter(getActivity(), cursor);

        ListView lv =(ListView) view.findViewById(R.id.list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        //noinspection deprecation
//        cursor.requery();
//        adapter.notifyDataSetChanged();

        return view;
    }
    //(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Cursor cursor
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getActivity(), mTitles[i], Toast.LENGTH_LONG).show();
     //   cursor.moveToFirst();
     //   cursor.move(i);
     //   Log.d("TAG", "status " + i);

        // public static String getURL(String farm, String server, String id, String secret, boolean big)
      //  PhotoFragment pf = new PhotoFragment();
      //  Bundle args = new Bundle();
      //  pf.setArguments(args);
      //  FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
     //   ft.replace(R.id.container, pf);
        //ft.addToBackStack("Image");
      //  ft.commit();
    }


}
