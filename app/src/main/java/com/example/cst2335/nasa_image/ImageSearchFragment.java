package com.example.cst2335.nasa_image;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSearchFragment extends Fragment {

    public ImageSearchFragment() {
        // Required empty public constructor
    }

    //Views
    View layout;
    TextView textView;
    ProgressBar progressBar3;
    ListView listViewResult;

    //objects
    String selectedDate = "", textDate = "";
    ImageResponse imageResponse;
    ArrayList<String> resultList;
    ArrayAdapter<String> resultListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_image_search, container, false);
        textView = layout.findViewById(R.id.textView);
        progressBar3 = layout.findViewById(R.id.progressBar3);
        listViewResult = layout.findViewById(R.id.listViewResult);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        resultList = new ArrayList<>();
        resultListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, resultList);
        listViewResult.setAdapter(resultListAdapter);
        Button button6 = layout.findViewById(R.id.buttonSearchForImage);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDate.equals("")){
                    //showing error toast
                    Toast.makeText(getActivity(), getResources().getString(R.string.image_toast_select_date), Toast.LENGTH_SHORT).show();
                }else{
                    new GetImageAsync(selectedDate, textDate).execute();
                }
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("IOTD-PREFERENCES", Context.MODE_PRIVATE);
        selectedDate = sharedPreferences.getString("last_date", "");
        textDate = sharedPreferences.getString("text_last_date", "");
        if(!selectedDate.equals("")){
            textView.setText(textDate);
            new GetImageAsync(selectedDate, textDate).execute();
        }

        listViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), ShowImageActivity.class)
                        .putExtra("title", imageResponse.getTitle())
                        .putExtra("explain", imageResponse.getExplanation())
                        .putExtra("date", imageResponse.getDate())
                        .putExtra("url", imageResponse.getUrl())
                        .putExtra("hdurl", imageResponse.getHdurl()));
            }
        });
        return layout;
    }

    /**
     * Displays a Date picker dialog from today's date
     * */
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                //Formatting the selected date to pass into the API url.
                String monthStr;
                month = month + 1;
                if (month < 10) {
                    monthStr = "0" + month;
                } else {
                    monthStr = "" + month;
                }
                String dayStr;
                if (dayOfMonth < 10) {
                    dayStr = "0" + dayOfMonth;
                } else {
                    dayStr = "" + dayOfMonth;
                }
                textDate = dayStr + "-" + monthStr + "-" + year;
                selectedDate = year + "-" + monthStr + "-" + dayStr;
                textView.setText(textDate); //update the text view
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * using AsyncTask for calling the API
     * */
    class GetImageAsync extends AsyncTask<Void, Void, String> {
        String url = "https://api.nasa.gov/planetary/apod?api_key=f0YHYfZV38JGDvfWBhr92h5oRnW5slVx2cRhgdIw&date=";
        String date, textDate;
        GetImageAsync(String date, String textDate) {
            this.date = date;
            this.textDate = textDate;
            url = url + date;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showing progressbar
            progressBar3.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                //calling API
                URL mUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)
                        mUrl.openConnection();

                return readStreamAndGetString(connection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar3.setVisibility(View.GONE);
            if (s.equals("")) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    //parsing json
                    JSONObject jsonObject = new JSONObject(s);
                    imageResponse = new ImageResponse();
                    String date = jsonObject.getString("date");
                    String explanation = jsonObject.getString("explanation");
                    String title = jsonObject.getString("title");
                    String hdurl = jsonObject.getString("hdurl");
                    String url = jsonObject.getString("url");
                    //clear all the records from the listview
                    resultList.clear();
                    //add current data to the list
                    resultList.add(title);
                    imageResponse.setDate(date);
                    imageResponse.setExplanation(explanation);
                    imageResponse.setTitle(title);
                    imageResponse.setHdurl(hdurl);
                    imageResponse.setUrl(url);

                    //saving the data into the SharedPreferences for later viewing the same
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("IOTD-PREFERENCES", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("last_date",this.date).apply();
                    sharedPreferences.edit().putString("text_last_date",this.textDate).apply();

                    //update listview
                    resultListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }

        /**
         * @param is InputStream from the HTTP API call
         * @return String
         * */
        private String readStreamAndGetString(InputStream is) {
            String currentLine = "";
            InputStreamReader inputStreamReader = new
                    InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    if ((currentLine = bufferedReader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(currentLine);
            }
            return stringBuilder.toString();
        }
    }
}
