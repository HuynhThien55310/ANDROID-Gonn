package com.gonnteam.activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.gonnteam.R;
import com.gonnteam.adapters.TabsPagerAdapter;
import com.gonnteam.models.Food;
import com.gonnteam.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.SearchAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Firebase and User
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private User user;

    // ViewPager
    private TabsPagerAdapter adapter;
    private ViewPager pager;

    // Navigation menu
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private boolean isUpdatedUser = false;

    // Search view
    private MaterialSearchView searchView;
    private FirebaseRecyclerAdapter fbadapter;
    private CollectionReference mFoodRef;
    private Query query;
    private List<Food> data;
    private String[] dataFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationMenu();
        initViewPager();
        initSearchView();
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
        final TextView txtName = header.findViewById(R.id.txtName);
        final ImageView imgAvatar = header.findViewById(R.id.imgAvatar);
        if (mAuth.getCurrentUser() == null){
            isUpdatedUser = false;
            txtName.setText(R.string.txtLogin);
            imgAvatar.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
            header.setOnClickListener(new View.OnClickListener() {
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
            CollectionReference mUserRef = FirebaseFirestore.getInstance().collection("users");
            mUserRef.document(fUser.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                            User user = documentSnapshot.toObject(User.class);
                            txtName.setText(user.getDisplayName());
                            Picasso.with(MainActivity.this).load(user.getAvatar()).into(imgAvatar);
                        }
                    });
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent accountSetting = new Intent(MainActivity.this,AccountSettingActivity.class);
                    startActivity(accountSetting);
                }
            });
        }
    }

    private void initSearchView(){
        // connect to firebase
        mFoodRef = FirebaseFirestore.getInstance().collection("foods");

        // create search view
        searchView = findViewById(R.id.search_view);
        searchView.setEllipsize(true);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(),"Không tìm thấy kết quả",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = mFoodRef.orderBy("title").startAt(newText).endAt(newText + "\uf8ff").limit(5);
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        data = new ArrayList<>();
                        data = documentSnapshots.toObjects(Food.class);
                        dataFilter = new String[data.size()];
                        for(int i = 0; i < data.size(); i ++){
                            dataFilter[i] = data.get(i).getTitle();
                        }
                        //searchView.setSuggestions(dataFilter);
                        searchView.setAdapter(new SearchAdapter(getApplicationContext(),dataFilter));
                    }
                });
                return false;
            }
        });

        searchView.setHint("Nhập tên món ăn");
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detail = new Intent(getApplicationContext(), FoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("food", data.get(i));
                detail.putExtra("bundle", bundle);
                startActivity(detail);
            }
        });
    }

    private void filterFood(String title){

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else {
            super.onBackPressed();
        }
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            return;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView.setMenuItem(search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
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
