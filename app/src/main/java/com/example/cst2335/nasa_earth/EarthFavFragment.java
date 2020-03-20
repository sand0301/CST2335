package com.example.cst2335.nasa_earth;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cst2335.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthFavFragment extends Fragment {


    public EarthFavFragment() {
        // Required empty public constructor
    }


    View layout;
    ListView earthFavListView;
    ArrayAdapter<String> adapter;
    ArrayList<EarthImage> earthImages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_earh_fav, container, false);
        earthFavListView = layout.findViewById(R.id.earthFavListView);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Reading data from the database
        //and set to the listview
        ArrayList<String> titles = new ArrayList<>();
        ImageryDB imageryDB = new ImageryDB(getActivity());
        earthImages = imageryDB.getAllImageryData();
        for (int i = 0; i < earthImages.size(); i++) {
            //appending lat and long to show
            titles.add(earthImages.get(i).getLat() + ", " + earthImages.get(i).getLon());
        }
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, titles);
        earthFavListView.setAdapter(adapter);
        //to show details of previously saved lat and lon
        earthFavListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //passing data through intent
                Intent intent = new Intent(getActivity(), ShowEarthImageActivity.class);
                intent.putExtra("earthData", earthImages.get(position));
                startActivity(intent);
            }
        });
    }
}
