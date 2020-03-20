package com.example.cst2335.bbc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335.R;
import com.google.android.material.snackbar.Snackbar;

public class BBCDetailNewsActivity extends AppCompatActivity {

    TextView titleText, descText, linkText, dateText;
    Button saveButton, deleteButton;
    CoordinatorLayout coordinatorLayout;
    BBCDatabase bbcDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcdetail_news);
        Intent intent = getIntent();

        /**
         * checking if the activity is called from shared preferences data
         * (showing toast if it is)
         */
        if(intent.hasExtra("from_saved")){
            String from_saved = intent.getStringExtra("from_saved");
            if (from_saved != null && from_saved.equals("yes")) {
                Toast.makeText(this, getString(R.string.bbc_toast_last_viewed), Toast.LENGTH_LONG).show();
            }
        }
        /**
         * Reading intent value to show news details
         * */
        final BBCNewsItem bbcNewsItem = (BBCNewsItem) intent.getSerializableExtra("bbc_news_data");
        String title = bbcNewsItem.getTitle();
        String description = bbcNewsItem.getDescription();
        final String link = bbcNewsItem.getLink();
        String pubDate = bbcNewsItem.getPubDate();

        bbcDatabase = new BBCDatabase(this);

        titleText = findViewById(R.id.textViewTitle);
        descText = findViewById(R.id.textViewDesc);
        linkText = findViewById(R.id.textViewLink);
        dateText = findViewById(R.id.textViewPubDate);
        saveButton = findViewById(R.id.buttonSave);
        deleteButton = findViewById(R.id.buttonDelete);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        titleText.setText(title);
        descText.setText(description);
        linkText.setText(link);
        dateText.setText(pubDate);

        /**
         * if the record already exists, save button will not be visible.
         * otherwise delete button will not be visible.
         * */
        if(bbcDatabase.exists(link)){
            saveButton.setVisibility(View.GONE);
        }else{
            deleteButton.setVisibility(View.GONE);
        }

        /**
         * Saving the record to the database
         * */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbcDatabase.saveNews(bbcNewsItem);
                saveButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.VISIBLE);

                //Showing snackbar
                Snackbar.make(coordinatorLayout, getString(R.string.bbc_snack_saved), Snackbar.LENGTH_SHORT).show();
            }
        });

        /**
         * deleting the record from the database
         * */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbcDatabase.deleteNews(link);
                deleteButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);

                //Showing snackbar
                Snackbar.make(coordinatorLayout, getString(R.string.bbc_snack_deleted), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
