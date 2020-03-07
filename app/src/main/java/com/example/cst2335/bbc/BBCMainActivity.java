package com.example.cst2335.bbc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cst2335.R;
import com.example.cst2335.guardian.GuardianMainActivity;
import com.example.cst2335.guardian.NewsSearchFragment;
import com.example.cst2335.nasa_earth.EarthMainActivity;
import com.example.cst2335.nasa_image.ImageMainActivity;

public class BBCMainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private LatestNewsFragment latestNewsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcmain);
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.button_text_BBC);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.bbc_drawer_open, R.string.bbc_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        latestNewsFragment = new LatestNewsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                latestNewsFragment).commit();
    }

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

    private void viewHelpDialog(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bbc_toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!drawer.isDrawerOpen(GravityCompat.START)){
            super.onBackPressed();
        }else{
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
