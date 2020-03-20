package com.example.cst2335.nasa_earth;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cst2335.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class EarthSearchFragment extends Fragment {

    private View view;
    private Button button6;
    private EditText editTextLat, editTextLon;
    private TextView textViewDate;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> titles;
    private ArrayList<EarthImage> earthImages;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.earth_searh_fragment, null);
        button6 = view.findViewById(R.id.button6);
        editTextLat = view.findViewById(R.id.editTextLat);
        editTextLon = view.findViewById(R.id.editTextLon);
        textViewDate = view.findViewById(R.id.textViewDate);
        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progressBar);
        titles = new ArrayList<>();
        earthImages = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);

        //Handle search button click event
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = editTextLat.getText().toString().trim();
                String lon = editTextLon.getText().toString().trim();
                String date = textViewDate.getText().toString();
                if (date.equals(getString(R.string.earth_select_date))) {
                    Toast.makeText(getActivity(), getString(R.string.earth_select_date), Toast.LENGTH_SHORT).show();
                } else if (lat.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.earth_enter_lat), Toast.LENGTH_SHORT).show();
                } else if (lon.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.earth_enter_lon), Toast.LENGTH_SHORT).show();
                } else {
                    //CALL API
                    new SearchImageAsync(lat, lon, date).execute();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowEarthImageActivity.class);
                intent.putExtra("earthData", earthImages.get(position));
                startActivity(intent);
            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        getLastSavedPreferences();
        return view;
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String sMonth = String.valueOf(month + 1);
                if (sMonth.length() == 1) {
                    sMonth = "0" + sMonth;
                }
                String sDay = String.valueOf(dayOfMonth);
                if(sDay.length() == 1){
                    sDay = "0" + sDay;
                }
                String selectedDate = year + "-" + sMonth + "-" + sDay;
                textViewDate.setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Using AsyncTask to call API
     */
    class SearchImageAsync extends AsyncTask<Void, Void, String> {

        String lat, lon, date;
        String url;

        SearchImageAsync(String lat, String lon, String date) {
            this.lat = lat;
            this.lon = lon;
            this.date = date;
//            url = "https://api.nasa.gov/planetary/earth/imagery/?lon="
//                    + lon + "&lat=" + lat +
//                    "&date=2014-02-01&api_key=PPp4h2LZfykWgpaHG7AecMSeyoeEYSMTXpSpCHME";
            url = "https://api.nasa.gov/planetary/earth/imagery/?lon="
                    + lon + "&lat=" + lat +
                    "&date=" + date + "&api_key=PPp4h2LZfykWgpaHG7AecMSeyoeEYSMTXpSpCHME";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (s.equals("")) {
                Toast.makeText(getActivity(), getString(R.string.earth_error_data), Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.has("msg")) {
                        Toast.makeText(getActivity(), getString(R.string.earth_error) + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.has("url")) {
                            earthImages.clear();
                            titles.clear();
                            String date = jsonObject.getString("date");
                            String id = jsonObject.getString("id");
                            String url = jsonObject.getString("url");
                            EarthImage earthImage = new EarthImage();
                            earthImage.setDate(date);
                            earthImage.setId(id);
                            earthImage.setUrl(url);
                            earthImage.setLat(lat);
                            earthImage.setLon(lon);
                            earthImages.add(earthImage);
                            titles.add(date);
                        }
                        adapter.notifyDataSetChanged();
                        saveResultToSharedPreferences(lat, lon, date);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), getString(R.string.earth_error_data), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showing progress bar
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String data = "";
            try {
                URL httpURL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)
                        httpURL.openConnection();
                String singleLine = "";
                InputStreamReader inputStreamReader = new
                        InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader br = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                while (true) {
                    if ((singleLine = br.readLine()) == null) break;
                    sb.append(singleLine);
                }
                data = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    /**
     * Stored the previously search item to the SharedPreferences
     *
     * @param lat Latitude of previously searched item
     * @param lon Longitude of previously searched item
     */
    private void saveResultToSharedPreferences(String lat, String lon, String date) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("last_location_data", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString("lat", lat)
                .putString("lon", lon)
                .putString("date", date)
                .apply();
    }

    /**
     * Get the previously search item from the SharedPreferences
     */
    private void getLastSavedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("last_location_data", Context.MODE_PRIVATE);
        String lat = sharedPreferences.getString("lat", "");
        String lon = sharedPreferences.getString("lon", "");
        String date = sharedPreferences.getString("date", "");
        if (!lat.equals("") && !lon.equals("") && !date.equals("")) {
            editTextLat.setText(lat);
            editTextLon.setText(lon);
            textViewDate.setText(date);
            new SearchImageAsync(lat, lon, date).execute();
        }
    }
}
