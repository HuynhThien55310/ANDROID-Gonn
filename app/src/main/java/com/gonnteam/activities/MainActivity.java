package com.gonnteam.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gonnteam.R;
import com.gonnteam.adapters.TabsPagerAdapter;
import com.gonnteam.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Firebase and User declaration
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private User user;

    // ViewPager declaration
    private TabsPagerAdapter adapter;
    private ViewPager pager;

    // Navigation menu declaration
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private boolean isUpdatedUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavigationMenu();
        initViewPager();
    }

    private void initViewPager() {
        adapter = new TabsPagerAdapter(getSupportFragmentManager(),this);
        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);
    }

    private void initNavigationMenu(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initUser(){
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        View header = navigationView.getHeaderView(0);
        TextView txtName = header.findViewById(R.id.txtName);
        ImageView imgAvatar = header.findViewById(R.id.imgAvatar);
        if (mAuth.getCurrentUser() == null){
            isUpdatedUser = false;
            txtName.setText(R.string.txtLogin);
            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(login);
                }
            });
        } else {
            if (isUpdatedUser){
                //User avatar was updated do nothing
                return;
            }
            // Update user profile
            txtName.setText(fUser.getDisplayName());
            Picasso.with(this).load(fUser.getPhotoUrl()).into(imgAvatar);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navSetting) {
            Intent appSetting = new Intent(MainActivity.this, AppSettingActivity.class);
            startActivity(appSetting);
        }
//        } else if (id == R.id.navReport) {
//            Intent accSetting = new Intent(MainActivity.this, AccountSettingActivity.class);
//            startActivity(accSetting);
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUser();
    }
}
