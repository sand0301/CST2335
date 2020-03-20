package com.example.cst2335.bbc;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cst2335.R;

import java.util.List;

/**
 * List adapter for showing news items.
 * */
public class BBCNewsAdapter extends BaseAdapter {

    Activity activity;
    static LayoutInflater inflater;
    List<BBCNewsItem> newsItems;

    /**
     * @param activity to get system layout inflater service
     * @param newsItems data to be displayed
     * */
    public BBCNewsAdapter(Activity activity, List<BBCNewsItem> newsItems){
        this.activity = activity;
        this.newsItems = newsItems;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @return number of elements into current adapter.
     * */
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_bbc_news, null);
        }
        BBCNewsItem item = (BBCNewsItem) getItem(position);
        String title = item.getTitle();
        String date = item.getPubDate();
        TextView titleText = convertView.findViewById(R.id.textViewTitle);
        TextView dateText = convertView.findViewById(R.id.textViewPubDate);
        titleText.setText(title);
        dateText.setText(date);
        return convertView;
    }
}
