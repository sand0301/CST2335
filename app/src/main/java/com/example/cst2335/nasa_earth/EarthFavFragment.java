package com.example.cst2335.nasa_earth;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cst2335.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthFavFragment extends Fragment {


    public EarthFavFragment() {
        // Required empty public constructor
    }


    View layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_earh_fav, container, false);

        return layout;
    }

}
