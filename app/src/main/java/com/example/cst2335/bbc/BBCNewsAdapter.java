package com.example.cst2335.bbc;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class BBCNewsAdapter extends BaseAdapter {

    Activity activity;
    static LayoutInflater inflater;
    List<BBCNewsItem> newsItems;
    public BBCNewsAdapter(Activity activity, List<BBCNewsItem> newsItems){
        this.activity = activity;
        this.newsItems = newsItems;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return newsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
