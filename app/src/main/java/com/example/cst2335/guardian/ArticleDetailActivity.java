package com.example.cst2335.guardian;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * Shows details of a selected article from the ListView on previous activity
 */
public class ArticleDetailActivity extends AppCompatActivity {

    Article article;
    boolean isAlreadySaved;
    GuardianDB guardianDB;
    Button button5;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        //Using toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_detail);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        article = (Article) getIntent().getSerializableExtra("article");
        String title = article.getWebTitle();
        String section = article.getSectionName();
        String dateTime = article.getWebPublicationDate();
        String url = article.getWebUrl();
        final String id = article.getId();

        TextView textView = findViewById(R.id.textView);
        textView.setText(title);
        TextView textView6 = findViewById(R.id.textView6);
        textView6.setText(section);
        TextView textView8 = findViewById(R.id.textView8);
        textView8.setText(dateTime);
        TextView textView10 = findViewById(R.id.textView10);
        textView10.setText(url);
        button5 = findViewById(R.id.button5);

        guardianDB = new GuardianDB(this);
        isAlreadySaved = guardianDB.isSaved(id);

        String action;
        if (isAlreadySaved) {
            action = getString(R.string.action_delete);
        } else {
            action = getString(R.string.action_save);
        }
        button5.setText(action);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAlreadySaved) {
                    showAlertDialog(id);
                } else {
                    guardianDB.saveArticle(article);
                    isAlreadySaved = guardianDB.isSaved(id);
                    //Show a Snackbar
                    Snackbar.make(coordinatorLayout, getString(R.string.article_saved), Snackbar.LENGTH_SHORT).show();
                    String action;
                    if (isAlreadySaved) {
                        action = getString(R.string.action_delete);
                    } else {
                        action = getString(R.string.action_save);
                    }
                    button5.setText(action);
                }
            }
        });
    }

    /**
     * Shows AlertDialog
     * */
    private void showAlertDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.article_delete_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guardianDB.deleteArticle(id);
                isAlreadySaved = guardianDB.isSaved(id);
                Snackbar.make(coordinatorLayout, getString(R.string.article_deleted), Snackbar.LENGTH_SHORT).show();
                String action;
                if (isAlreadySaved) {
                    action = getString(R.string.action_delete);
                } else {
                    action = getString(R.string.action_save);
                }
                button5.setText(action);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.article_delete_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(getString(R.string.action_delete));
        builder.setMessage(getString(R.string.article_delete_message));
        builder.show();
    }
}
