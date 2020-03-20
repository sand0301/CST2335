package com.example.cst2335.bbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cst2335.R;
import java.util.List;

public class SavedNewsFragment extends Fragment {

    ListView listViewSavedNews;
    BBCNewsAdapter adapter;
    List<BBCNewsItem> newsItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbc_saved_news, container, false);
        listViewSavedNews = view.findViewById(R.id.listViewSavedNews);
        listViewSavedNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Show detail of a news and save current clicked news to shared prefs
                Intent intent = new Intent(getActivity(), BBCDetailNewsActivity.class);
                BBCNewsItem newsItem = newsItems.get(position);
                saveToSharedPreferences(newsItem);
                intent.putExtra("bbc_news_data", newsItem);
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * To store the data into the SharePreferences
     * @param newsItem collection detail of a news item
     * */
    void saveToSharedPreferences(BBCNewsItem newsItem){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("bbc_sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String link = newsItem.getLink();
        String title = newsItem.getTitle();
        String desc = newsItem.getDescription();
        String pubDate = newsItem.getPubDate();
        editor.putString("link",link);
        editor.putString("title",title);
        editor.putString("desc",desc);
        editor.putString("pubDate",pubDate);
        editor.apply();
    }

    /**
     * Getting data from the database
     * [SELECT * FROM <table>] and showing data into the listview using adapter.
     * */
    @Override
    public void onResume() {
        super.onResume();
        BBCDatabase bbcDatabase = new BBCDatabase(getActivity());
        newsItems = bbcDatabase.getAllSavedNews();
        adapter = new BBCNewsAdapter(getActivity(), newsItems);
        listViewSavedNews.setAdapter(adapter);
    }
}
