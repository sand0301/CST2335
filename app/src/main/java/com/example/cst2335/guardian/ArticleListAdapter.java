package com.example.cst2335.guardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cst2335.R;

import java.util.List;

/**
 * Custom List adapter
 */
public class ArticleListAdapter extends BaseAdapter {

    List<Article> articles;
    static LayoutInflater inflater;

    ArticleListAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_guardian_article, null);
        }
        Article article = articles.get(position);
        String title = article.getWebTitle();
        String dateTime = article.getWebPublicationDate();
        String section = article.getSectionName();

        TextView textView2 = convertView.findViewById(R.id.textView2);
        TextView textView3 = convertView.findViewById(R.id.textView3);
        TextView textView4 = convertView.findViewById(R.id.textView4);

        textView2.setText(title);
        textView3.setText(dateTime);
        textView4.setText(section);
        return convertView;
    }
}
