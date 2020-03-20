package com.example.cst2335.nasa_image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cst2335.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowImageActivity extends AppCompatActivity {

    //View
    TextView title, explain, date, url;
    ImageView image;
    CoordinatorLayout parentLayout;
    Button saveButton, deleteButton;

    Intent intent;
    String strTitle, strExplain, strDate, strURL, strHdURL;

    Bitmap imageBitmap;

    ImageDatabase database;
    boolean isAlreadyInserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        //getting the context of the Database
        database = new ImageDatabase(this);

        //reading the values from the intent
        intent = getIntent();
        strTitle = intent.getStringExtra("title");
        strExplain = intent.getStringExtra("explain");
        strDate = intent.getStringExtra("date");
        strURL = intent.getStringExtra("url");
        strHdURL = intent.getStringExtra("hdurl");

        //init views
        parentLayout = findViewById(R.id.parentLayout);
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        explain = findViewById(R.id.explain);
        date = findViewById(R.id.date);
        url = findViewById(R.id.url);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        //hide buttons
        saveButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);

        //updating texts on screen
        title.setText(strTitle);
        explain.setText(strExplain);
        date.setText(strDate);
        url.setText(strHdURL);

        //Reading the value from the database
        ImageResponse imageResponse = database.getByDate(strDate);
        isAlreadyInserted = imageResponse.getTitle() != null;

        //If the data has already been stored, Delete option will be shown to a user
        if(isAlreadyInserted){
            showImageFromGallery(imageResponse.getPath());
            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
        }else {
            new ShowImageBitmap(strURL).execute();
            deleteButton.setVisibility(View.GONE);
        }

        //Saving the image data to the database
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saving the image to the gallery and getting the path
                String path = saveImageToGallery();
                if (!path.isEmpty()) {
                    //image saved
                    database.insertImage(strDate, strTitle, strExplain, strHdURL, strURL, path);
                    saveButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.VISIBLE);
                    Snackbar.make(parentLayout, getResources().getString(R.string.image_saved_success), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //Saving the image to the Gallery
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = database.deleteImage(strDate);
                if(count > 0){
                    Snackbar.make(parentLayout, getResources().getString(R.string.image_delete_success), Snackbar.LENGTH_SHORT).show();
                    saveButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Performs MediaStore query to store image
     * @return path where the image has been stored
     * */
    private String saveImageToGallery() {
        if (imageBitmap != null) {
            return MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, strTitle, "");
        }
        return "";
    }

    /**
     * @param path File URI to read the file and set the Bitmap to the
     *             ImageView
     * */
    private void showImageFromGallery(String path) {
        final InputStream imageStream;
        try {
            imageStream = getContentResolver().openInputStream(Uri.parse(path));
            imageBitmap = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(imageBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Using AsyncTask to load the Bitmap from the URL in the
     * background thread
     * */
    class ShowImageBitmap extends AsyncTask<Void, Void, Bitmap> {

        String imageURL;

        ShowImageBitmap(String imageURL) {
            this.imageURL = imageURL;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(imageURL);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {

                //updating the ImageView on a successful result
                image.setImageBitmap(bitmap);
                imageBitmap = bitmap;
                saveButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
