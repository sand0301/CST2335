package com.example.cst2335.bbc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cst2335.R;
import com.example.cst2335.guardian.GuardianMainActivity;
import com.example.cst2335.guardian.NewsSavedFragment;
import com.example.cst2335.guardian.NewsSearchFragment;
import com.example.cst2335.nasa_earth.EarthMainActivity;
import com.example.cst2335.nasa_image.ImageMainActivity;
import com.google.android.material.navigation.NavigationView;

public class BBCMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private LatestNewsFragment latestNewsFragment;
    private NavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcmain);

        drawer = findViewById(R.id.drawer);
        nav = findViewById(R.id.nav);

        //set toolbar as actionbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.button_text_BBC);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.bbc_drawer_open, R.string.bbc_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        //syncing state of action bar icon to open and close drawer
        actionBarDrawerToggle.syncState();

        //showing latest news fragment first as the activity gets started
        latestNewsFragment = new LatestNewsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                latestNewsFragment).commit();
        nav.setCheckedItem(R.id.drawerBBCLatestNews);
        nav.setNavigationItemSelectedListener(this);
        readSharedPreferences();
    }

    /**
     * Reads shared preferences
     * and starts the BBCDetailNewsActivity with the saved record in the
     * SharedPreferences
     * */
    void readSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("bbc_sp", Context.MODE_PRIVATE);
        String link = sharedPreferences.getString("link","");
        String title = sharedPreferences.getString("title","");
        String desc = sharedPreferences.getString("desc","");
        String pubDate = sharedPreferences.getString("pubDate","");
        if(!link.isEmpty() && !title.isEmpty() && !desc.isEmpty() && !pubDate.isEmpty()){
            BBCNewsItem newsItem= new BBCNewsItem();
            newsItem.setTitle(title);
            newsItem.setDescription(desc);
            newsItem.setPubDate(pubDate);
            newsItem.setLink(link);
            Intent intent = new Intent(this, BBCDetailNewsActivity.class);
            intent.putExtra("bbc_news_data", newsItem);
            intent.putExtra("from_saved", "yes");
            startActivity(intent);
        }
    }

    /**
     * Handling click events of navigation drawer
     * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawerBBCLatestNews:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        latestNewsFragment).commit();
                break;
            case R.id.drawerBBCSavedNews:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new SavedNewsFragment()).commit();
                break;
            case R.id.drawerMenuGuardian:
                Intent intent1 = new Intent(BBCMainActivity.this, GuardianMainActivity.class);
                startActivity(intent1);
                break;
            case R.id.drawerMenuNasaImage:
                Intent intent2 = new Intent(BBCMainActivity.this, ImageMainActivity.class);
                startActivity(intent2);
                break;
            case R.id.drawerMenuNasaEarth:
                Intent intent3 = new Intent(BBCMainActivity.this, EarthMainActivity.class);
                startActivity(intent3);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Handling click events of toolbar menu
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuGuardian:
                Intent intent3 = new Intent(this, GuardianMainActivity.class);
                startActivity(intent3);
                return true;
            case R.id.menuImage:
                Intent intent1 = new Intent(this, ImageMainActivity.class);
                startActivity(intent1);
                return true;
            case R.id.menuEarth:
                Intent intent2 = new Intent(this, EarthMainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.menuHelp:
                viewHelpDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Shows help Dialog.
     * */
    private void viewHelpDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BBCMainActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.help));
        alertDialogBuilder.setMessage(getString(R.string.bbc_help));
        alertDialogBuilder.setPositiveButton(getString(R.string.bbc_got_it), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    /**
     * Inflating menu with the toolbar
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bbc_toolbar_menu, menu);
        return true;
    }

    /***
     * Closes the navigation drawer before finishing the activity.
     * */
    @Override
    public void onBackPressed() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
