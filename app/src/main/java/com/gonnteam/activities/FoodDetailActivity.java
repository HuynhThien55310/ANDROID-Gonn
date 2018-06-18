package com.gonnteam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gonnteam.R;
import com.gonnteam.fragments.FoodCommentFragment;
import com.gonnteam.fragments.FoodDetailFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FoodDetailActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FirebaseUser fuser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (fuser == null){
                    Intent login = new Intent(FoodDetailActivity.this, LoginActivity.class);
                    startActivity(login);
                } else {
                    Intent add_menu = new Intent(FoodDetailActivity.this, MenuActivity.class);
                    add_menu.putExtra("uid", fuser.getUid());
                    add_menu.putExtra("foodID", getIntent().getStringExtra("foodID"));
                    startActivityForResult(add_menu,1);
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        FoodDetailFragment foodDetail = new FoodDetailFragment();
        FoodCommentFragment foodCmt = new FoodCommentFragment();
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0: return foodDetail;
                case 1: return foodCmt;
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
