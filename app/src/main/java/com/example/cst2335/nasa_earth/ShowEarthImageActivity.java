package com.example.cst2335.nasa_earth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cst2335.R;

import java.io.IOException;
import java.net.URL;

public class ShowEarthImageActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private Button buttonSave, buttonDelete;
    private Intent intent;
    private TextView titleDate, titleLat, titleLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_earth_image);
        intent = getIntent();
        EarthImage earthImage = (EarthImage) intent.getSerializableExtra("earthData");
        imageView = findViewById(R.id.imageView);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        titleDate = findViewById(R.id.titleDate);
        titleLat = findViewById(R.id.titleLat);
        titleLon = findViewById(R.id.titleLon);
        buttonSave.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        titleLon.setText(earthImage.getLon());
        titleLat.setText(earthImage.getLat());
        titleDate.setText(earthImage.getDate());
        new ShowImageAsync(earthImage.getUrl()).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSave:
                break;
            case R.id.buttonDelete:
                break;
        }
    }

    class ShowImageAsync extends AsyncTask<Void, Void, Bitmap> {

        String imageURL;
        ShowImageAsync(String imageURL){
            this.imageURL = imageURL;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new URL(imageURL)
                        .openConnection().
                                getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
