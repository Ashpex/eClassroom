package com.ashpex.portality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ashpex.portality.fragment.CalendarFragment;
import com.ashpex.portality.fragment.FeeFragment;
import com.ashpex.portality.fragment.ForumFragment;
import com.ashpex.portality.fragment.InfoCourseFragment;
import com.ashpex.portality.fragment.SignUpCourseFragment;
import com.ashpex.portality.fragment.TaskFragment;
import com.ashpex.portality.fragment.ProfileFragment;
import com.ashpex.portality.model.InfoUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class MainScreen extends AppCompatActivity implements ActionForumInterface {
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar toolBarUser;
    private NavigationView navigationView;
    private FrameLayout frMain;
    private ImageButton btnNoti;
    private Intent intent;
    private InfoUser infoUser;
    private BottomNavigationView bottomNavigation;
    private TextView user_name;
    private TextView user_email;
    private TextView name;
    private int type ;
    private int course_id;
    FloatingActionButton btnSignUP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        setData();
        mappingControls();
        setHeaderBar();
        settingBottomNavigation();
        eventToolBar();
        eventNoti();
        firstEvents();

        btnSignUP = findViewById(R.id.btnSignUP);
        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action();
            }
        });
        if(course_id !=-1) {
            InfoCourseFragment infoCourseFragment = new InfoCourseFragment(course_id);
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frMain, infoCourseFragment);
            transaction.commit();
        }
    }

    private void firstEvents() {
        navigationView.getMenu().findItem(R.id.menuHome).setChecked(true);
        bottomNavigation.getMenu().findItem(R.id.menuBotHome).setChecked(true);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        ForumFragment f = new ForumFragment();
        f.setActionForumInterface(this);
        transaction.replace(R.id.frMain, f);
        transaction.commit();
    }

    private void action() {
        navigationView.getMenu().findItem(R.id.menuCourseSignUp).setChecked(true);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frMain, new SignUpCourseFragment());
        transaction.commit();
        drawerLayout.closeDrawers();
    }

    private void setHeaderBar() {
        View headerView = navigationView.getHeaderView(0);
        user_name = headerView.findViewById(R.id.user_name);
        user_email = headerView.findViewById(R.id.user_email);
        name = headerView.findViewById(R.id.name);

        SharedPreferences sharedPref = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String _name = sharedPref.getString("user_name", "null");
        type = sharedPref.getInt("user_type", 1);

        String ref = "";
        for(int i=_name.length() - 1;i>=0;i--) {

            if(_name.charAt(i) == ' ' ) {
                if(i == _name.length() - 1)
                    continue;
                if(ref.length()<2) {
                    ref = String.valueOf(_name.charAt(i+1)) + ref;
                }
                else
                    break;
            }
        }
        if(ref.length() == 0) {
            ref = String.valueOf(_name.charAt(0));
        }

        name.setText(ref);
        user_name.setText(sharedPref.getString("user_name", "null"));
        user_email.setText(sharedPref.getString("user_email", "null"));
    }

    private void settingBottomNavigation() {
        bottomNavigation.setBackground(null);
    }

    private void setData() {
        intent = getIntent();
        course_id = intent.getIntExtra("course_id", -1);
    }

    private void eventNoti() {
        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }


    private void eventToolBar() {
        setSupportActionBar(toolBarUser);
        ActionBar actionBar = getSupportActionBar();


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

        String temp = "";
        String temp1 = "";
        if(type==1) {
            temp = "Tạo học phần";
            temp1 ="Lương";
        }
        else {
            temp = "Đăng ký học phần";
            temp1 ="Học phí";
        }
        navigationView.getMenu().findItem(R.id.menuCourseSignUp).setTitle(temp);
        navigationView.getMenu().findItem(R.id.menuFeeUser).setTitle(temp1);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                eventClickNav(item);
                return true;
            }
        });


    }

    private void eventClickNav(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()){
            case R.id.menuHome:{
                bottomNavigation.getMenu().findItem(R.id.menuBotHome).setChecked(true);
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                ForumFragment f = new ForumFragment();
                f.setActionForumInterface(this);
                transaction.replace(R.id.frMain, f);
                transaction.commit();
                drawerLayout.closeDrawers();
                return;
            }
            case R.id.menuTask:{
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frMain, new TaskFragment());
                transaction.commit();
                drawerLayout.closeDrawers();
                return;
            }
            case R.id.menuCourseSignUp:{
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frMain, new SignUpCourseFragment());
                transaction.commit();
                drawerLayout.closeDrawers();
                return;
            }

            case R.id.menuFeeUser:{
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frMain, new FeeFragment());
                transaction.commit();
                drawerLayout.closeDrawers();
                return;
            }
            case R.id.menuSchedule:{
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frMain, new CalendarFragment());
                transaction.commit();
                drawerLayout.closeDrawers();
                return;
            }

            case R.id.menuUser:{
                bottomNavigation.getMenu().findItem(R.id.menuBotUser).setChecked(true);
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frMain, new ProfileFragment());
                transaction.commit();
                drawerLayout.closeDrawers();
                return;
            }

            case R.id.menuExit:{
                drawerLayout.closeDrawers();
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                sharedPref.edit().clear().commit();

                finish();
            }
            case R.id.menuBotHome:{
                navigationView.getMenu().findItem(R.id.menuHome).setChecked(true);
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frMain, new ForumFragment());
                transaction.commit();
                return;
            }
            case R.id.menuBotUser:{
                navigationView.getMenu().findItem(R.id.menuUser).setChecked(true);
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frMain, new ProfileFragment());
                transaction.commit();
                return;
            }
            default: {
                drawerLayout.closeDrawers();
                return;
            }
        }


    }

    private void mappingControls() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolBarUser = findViewById(R.id.toolBarUser);
        navigationView = findViewById(R.id.nav_view_main);
        frMain = findViewById(R.id.frMain);
        btnNoti = findViewById(R.id.btnNoti);
        bottomNavigation = findViewById(R.id.bottomNavigation);

    }

    @Override
    public void setChecked(int pos) {
        if(pos==0)
            navigationView.getMenu().findItem(R.id.menuSchedule).setChecked(true);
        if(pos==1)
            navigationView.getMenu().findItem(R.id.menuTask).setChecked(true);
    }
}