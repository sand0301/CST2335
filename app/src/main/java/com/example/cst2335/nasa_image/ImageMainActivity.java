package com.example.cst2335.nasa_image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cst2335.R;
import com.example.cst2335.bbc.BBCMainActivity;
import com.example.cst2335.guardian.GuardianMainActivity;
import com.example.cst2335.nasa_earth.EarthMainActivity;
import com.google.android.material.navigation.NavigationView;

public class ImageMainActivity extends AppCompatActivity {

    //navigation drawer
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_main);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.button_text_nasa_image);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //animating the actionbar icon
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        final ImageSearchFragment imageSearchFragment = new ImageSearchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                imageSearchFragment).commit();

        //drawer item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.imageDrawerSearch:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                imageSearchFragment).commit();
                        break;
                    case R.id.imageDrawerSaved:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                                new SavedImageFragment()).commit();
                        break;
                    case R.id.drawerMenuGuardian:
                        Intent intent1 = new Intent(ImageMainActivity.this, GuardianMainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.drawerMenuNasaEarth:
                        Intent intent2 = new Intent(ImageMainActivity.this, EarthMainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.drawerMenuBBCNews:
                        Intent intent3 = new Intent(ImageMainActivity.this, BBCMainActivity.class);
                        startActivity(intent3);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    /**
     * Toolbar menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_toolbar_menu, menu);
        return true;
    }

    /**
     * toolbar menu item clicks
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuGuardian:
                Intent intent1 = new Intent(this, GuardianMainActivity.class);
                startActivity(intent1);
                return true;
            case R.id.menuEarth:
                Intent intent2 = new Intent(this, EarthMainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.menuBBC:
                Intent intent3 = new Intent(this, BBCMainActivity.class);
                startActivity(intent3);
                return true;
            case R.id.menuHelp:
                //showing alert dialog for help
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.help));
                builder.setPositiveButton(getResources().getString(R.string.image_okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setMessage(getResources().getString(R.string.image_help));
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //close the drawer before exit
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
