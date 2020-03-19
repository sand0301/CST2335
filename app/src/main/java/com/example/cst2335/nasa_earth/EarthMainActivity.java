package com.example.cst2335.nasa_earth;

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
import com.example.cst2335.nasa_image.ImageMainActivity;
import com.google.android.material.navigation.NavigationView;

/**
 * Main activity for the Earth Imagery
 * */
public class EarthMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout earthDrawer;
    Toolbar toolbar;
    NavigationView earthNavDrawer;

    //Search fragment
    EarthSearchFragment earthSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_main);

        earthDrawer = findViewById(R.id.earthDrawer);
        toolbar = findViewById(R.id.earthToolbar);
        toolbar.setTitle(getString(R.string.button_text_nasa_earth));
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, earthDrawer, toolbar,
                R.string.earth_drawer_open, R.string.earth_drawer_close);
        earthDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //show search fragment on the screen initially
        earthSearchFragment = new EarthSearchFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.earthFragmentContainer, earthSearchFragment).commit();
        earthNavDrawer = findViewById(R.id.earthNavDrawer);
        earthNavDrawer.setNavigationItemSelectedListener(this);

    }

    /**
     * Inflating toolbar menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.earth_toolbar_menu, menu);
        return true;
    }

    /**
     * Handling actions on menu item click event
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuGuardian:
                Intent intent1 = new Intent(EarthMainActivity.this, GuardianMainActivity.class);
                startActivity(intent1);
                break;
            case R.id.menuImage:
                Intent intent2 = new Intent(EarthMainActivity.this, ImageMainActivity.class);
                startActivity(intent2);
                break;
            case R.id.menuBBC:
                Intent intent3 = new Intent(EarthMainActivity.this, BBCMainActivity.class);
                startActivity(intent3);
                break;
            case R.id.menuHelp:
                //showing help dialog
                showHelp();
                break;
        }
        return true;
    }

    /**
     * AlertDialog with message to help the user to use the User Interface
     * */
    void showHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.help));
        builder.setMessage(getString(R.string.earth_help));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * To handle click events of the navigation drawer
     * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawerEarthMenuSearch:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.earthFragmentContainer, earthSearchFragment).commit();
                break;
            case R.id.drawerEarthMenuFav:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.earthFragmentContainer, new EarthFavFragment()).commit();
                break;
            case R.id.drawerMenuGuardian:
                Intent intent1 = new Intent(this, GuardianMainActivity.class);
                startActivity(intent1);
                break;
            case R.id.drawerMenuNasaImage:
                Intent intent2 = new Intent(this, ImageMainActivity.class);
                startActivity(intent2);
                break;
            case R.id.drawerMenuBBCNews:
                Intent intent3 = new Intent(this, BBCMainActivity.class);
                startActivity(intent3);
                break;

        }
        //close the drawer on each item click
        earthDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
