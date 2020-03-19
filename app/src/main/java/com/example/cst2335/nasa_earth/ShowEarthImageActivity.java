package com.example.cst2335.nasa_earth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.URL;

public class ShowEarthImageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Button buttonSave, buttonDelete;
    private Intent intent;
    private TextView titleDate, titleLat, titleLon;
    private CoordinatorLayout coordinatorLayout;

    private ImageryDB imageryDB;
    private EarthImage earthImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_earth_image);
        //get the received intent
        intent = getIntent();
        //get the reference of the database
        imageryDB = new ImageryDB(this);
        //reading the extras from the intent
        earthImage = (EarthImage) intent.getSerializableExtra("earthData");
        //initializing the views
        imageView = findViewById(R.id.imageView);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        titleDate = findViewById(R.id.titleDate);
        titleLat = findViewById(R.id.titleLat);
        titleLon = findViewById(R.id.titleLon);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        buttonSave.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        //show delete button if the record does not exists
        String id = earthImage.getId();
        if (imageryDB.checkData(id) != 0) {
            buttonSave.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.VISIBLE);
        } else {
            //show save button if the record already exists
            buttonSave.setVisibility(View.VISIBLE);
            buttonDelete.setVisibility(View.GONE);
        }

        titleLon.setText(earthImage.getLon());
        titleLat.setText(earthImage.getLat());
        titleDate.setText(earthImage.getDate());
        //Execute async task to load the bitmap into the imageview
        new ShowImageAsync(earthImage.getUrl()).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                saveImageryData();
                break;
            case R.id.buttonDelete:
                deleteImageryData();
                break;
        }
    }

    /**
     * save the record into the database and show delete button
     * */
    private void saveImageryData() {
        imageryDB.saveImageryData(earthImage);
        showSnackBar(getString(R.string.earth_saved));
        buttonSave.setVisibility(View.GONE);
        buttonDelete.setVisibility(View.VISIBLE);
    }

    /**
     * delete the record from the database and show save button
     * */
    private void deleteImageryData() {
        String id = earthImage.getId();
        imageryDB.deleteImageryData(id);
        showSnackBar(getString(R.string.earth_deleted));
        buttonSave.setVisibility(View.VISIBLE);
        buttonDelete.setVisibility(View.GONE);
    }

    /**
     * Show snackbar
     * */
    private void showSnackBar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Load the bitmap into the imageview by reading the URL.
     * **/
    class ShowImageAsync extends AsyncTask<Void, Void, Bitmap> {

        String imageURL;

        ShowImageAsync(String imageURL) {
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
