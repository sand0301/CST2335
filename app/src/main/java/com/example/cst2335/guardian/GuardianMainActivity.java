package com.example.cst2335.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cst2335.R;
import com.example.cst2335.bbc.BBCMainActivity;
import com.example.cst2335.nasa_earth.EarthMainActivity;
import com.example.cst2335.nasa_image.ImageMainActivity;
import com.google.android.material.navigation.NavigationView;

public class GuardianMainActivity extends AppCompatActivity {

    //To use navigation drawer
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_main);

        //Using toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.button_text_guardian);
        setSupportActionBar(toolbar);

        //setting up navigation drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.drawerMenuSearch);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new NewsSearchFragment()).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawerMenuSearch:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new NewsSearchFragment()).commit();
                        break;
                    case R.id.drawerMenuSaved:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new NewsSavedFragment()).commit();
                        break;
                    case R.id.drawerMenuNasaImage:
                        Intent intent1 = new Intent(GuardianMainActivity.this, ImageMainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.drawerMenuNasaEarth:
                        Intent intent2 = new Intent(GuardianMainActivity.this, EarthMainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.drawerMenuBBCNews:
                        Intent intent3 = new Intent(GuardianMainActivity.this, BBCMainActivity.class);
                        startActivity(intent3);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    /**
     * If the drawer is opened, the drawer will be closed, otherwise
     * the activity will be finished
     * */
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}
