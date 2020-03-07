package com.example.cst2335.bbc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cst2335.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LatestNewsFragment extends Fragment {

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbc_latest_news, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        new LatestNewsAsync().execute();

        return view;
    }

    class LatestNewsAsync extends AsyncTask<Void, Void, String> {
        String newsAPI = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";
        LatestNewsAsync() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            String data = "";
            try {
                URL mUrl = new URL(newsAPI);
                HttpURLConnection connection = (HttpURLConnection)
                        mUrl.openConnection();

                data = getString(connection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            Log.i("RESPONSE", s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * This function reads the data from HTTP call response
         * */
        private String getString(InputStream is) {
            String line = "";
            InputStreamReader isr = new
                    InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.append(line);
            }
            return sb.toString();
        }
    }
}
