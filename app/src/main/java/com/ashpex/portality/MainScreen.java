package com.ashpex.portality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainScreen extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar toolBarUser;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        mappingControls();
        eventToolBar();


    }


    private void eventToolBar() {
        setSupportActionBar(toolBarUser);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        toolBarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                eventClickNav(item);
                return true;
            }
        });

    }

    private void eventClickNav(MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
    }

    private void mappingControls() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolBarUser = findViewById(R.id.toolBarUser);
        navigationView = findViewById(R.id.nav_view_main);
    }
}