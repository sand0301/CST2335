package com.example.cst2335.guardian;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cst2335.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
/**
 * this Fragment shows the Articles from the database
 * */
public class NewsSavedFragment extends Fragment {


    public NewsSavedFragment() {
        // Required empty public constructor
    }


    private List<Article> articleList;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_news, container, false);
        listView = view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                intent.putExtra("article", articleList.get(position));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG","onResume called");
        //Read the data from the database
        GuardianDB guardianDB = new GuardianDB(getActivity());
        articleList = guardianDB.getAllSavedArticle();
        ArticleListAdapter articleListAdapter = new ArticleListAdapter(getActivity(), articleList);
        listView.setAdapter(articleListAdapter);
    }
}
