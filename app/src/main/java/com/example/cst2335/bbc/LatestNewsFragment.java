package com.example.cst2335.bbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cst2335.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LatestNewsFragment extends Fragment {

    BBCNewsAdapter adapter;
    List<BBCNewsItem> newsItems;
    ProgressBar progressBar;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbc_latest_news, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        listView = view.findViewById(R.id.listViewLatestNews);
        newsItems = new ArrayList<>();
        adapter = new BBCNewsAdapter(getActivity(), newsItems);
        listView.setAdapter(adapter);
        new LatestNewsAsync().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BBCDetailNewsActivity.class);
                BBCNewsItem newsItem = newsItems.get(position);
                saveToSharedPreferences(newsItem);
                intent.putExtra("bbc_news_data", newsItem);
                startActivity(intent);
            }
        });
        return view;
    }

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

    class LatestNewsAsync extends AsyncTask<Void, Void, InputStream> {
        String newsAPI = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";

        @Override
        protected InputStream doInBackground(Void... voids) {
            InputStream inputStream = null;
            try {
                URL mUrl = new URL(newsAPI);
                HttpURLConnection connection = (HttpURLConnection)
                        mUrl.openConnection();

                inputStream = connection.getInputStream();
                try {
                    XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser pullParser = pullParserFactory.newPullParser();
                    pullParser.setInput(inputStream, null);
                    int currentElement = pullParser.getEventType();
                    boolean insideItem = false;
                    newsItems.clear();
                    BBCNewsItem bbcNewsItem = null;
                    while (currentElement != XmlPullParser.END_DOCUMENT){
                        String tag = pullParser.getName();
                        switch (currentElement){
                            case XmlPullParser.START_TAG:
                                if(tag.equals("item")){
                                    insideItem = true;
                                    bbcNewsItem= new BBCNewsItem();
                                }else if(tag.equals("title")){
                                    if(insideItem){
                                        String title = pullParser.nextText();
                                        bbcNewsItem.setTitle(title);
                                    }
                                }else if(tag.equals("description")){
                                    if(insideItem){
                                        String description = pullParser.nextText();
                                        bbcNewsItem.setDescription(description);
                                    }
                                }else if(tag.equals("link")){
                                    if(insideItem){
                                        String link = pullParser.nextText();
                                        bbcNewsItem.setLink(link);
                                    }
                                }else if(tag.equals("pubDate")){
                                    if(insideItem){
                                        String pubDate = pullParser.nextText();
                                        bbcNewsItem.setPubDate(pubDate);
                                    }
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                if(tag.equals("item")){
                                    newsItems.add(bbcNewsItem);
                                    insideItem = false;
                                }
                                break;
                        }
                        currentElement = pullParser.next();
                    }
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
