package com.gonnteam.activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Base64;
import android.util.Log;
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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Firebase and User
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private User user;

    // ViewPager
    private TabsPagerAdapter adapter;
    private ViewPager pager;
    private String tag;
    // Navigation menu
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private boolean isUpdatedUser = false;

    // Search view
    private MaterialSearchView searchView;
    private FirebaseRecyclerAdapter fbadapter;
    private Query searchViewQuery;
    private List<Food> data;
    private String[] dataFilter;
    private ArrayList<String> dataSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationMenu();
        tag = "all";
        initViewPager();
        initSearchView();
    }

    private void initViewPager() {
        adapter = new TabsPagerAdapter(getSupportFragmentManager(), this, tag);
        adapter.notifyDataSetChanged();
        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);

    }

    private void initNavigationMenu() {
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

    private void initUser() {
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        View header = navigationView.getHeaderView(0);
        final TextView txtName = header.findViewById(R.id.txtName);
        final ImageView imgAvatar = header.findViewById(R.id.imgAvatar);
        if (mAuth.getCurrentUser() == null) {
            isUpdatedUser = false;
            txtName.setText(R.string.txtLogin);
            imgAvatar.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            });
        } else {
            if (isUpdatedUser) {
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
                            if (user.getAvatar().contains("data:image/jpeg;base64")) {
                                String ava = user.getAvatar().substring(user.getAvatar().indexOf(",") + 1);
                                byte[] decodedString = Base64.decode(ava, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imgAvatar.setImageBitmap(decodedByte);
                            } else {
                                Picasso.with(MainActivity.this).load(user.getAvatar()).into(imgAvatar);
                            }

                        }
                    });
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent accountSetting = new Intent(MainActivity.this, AccountSettingActivity.class);
                    startActivity(accountSetting);
                }
            });
        }
    }

    private void initSearchView() {

        // create search view
        searchView = findViewById(R.id.search_view);
        searchView.setEllipsize(true);
        searchViewQuery = FirebaseFirestore.getInstance().collection("foods");

        searchViewQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    //data = new ArrayList<>();
                try {
                    data = documentSnapshots.toObjects(Food.class);
                }catch (RuntimeException r){
                    Log.d("CRASH ID: ",r.getMessage());
                }



            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                dataSuggestion = new ArrayList<>();
                String alias = createAlias(newText);
                for(int i=0; i < data.size();i++){
                    if (data.get(i).getAlias().contains(alias)){
                        dataSuggestion.add(data.get(i).getTitle());
                        Log.d("TRUE", i + " id: " + data.get(i).getId() +
                                " alias: " + alias + " size" + dataSuggestion.size() + " text: " + newText);
                    }
                }
                dataFilter = new String[dataSuggestion.size()];
                dataFilter = dataSuggestion.toArray(dataFilter);
                searchView.setSuggestions(dataFilter);
                //searchView.setAdapter(new SearchAdapter(getApplicationContext(),dataFilter));
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

    private String createAlias(String title) {
        title = title.toLowerCase();
        title = title.replaceAll("\\s+", "");
        String nfdNormalizedString = Normalizer.normalize(title, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
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
        switch (id) {
            case R.id.navMenu:
                Intent menu = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(menu);
                break;
            case R.id.navSetting:
                Intent appSetting = new Intent(MainActivity.this, AppSettingActivity.class);
                startActivity(appSetting);
                break;
            case R.id.navMix:
                tag = "all";
                adapter.setTag(tag);
                adapter.notifyDataSetChanged();
                break;
            case R.id.navSoup:
                tag = "tag.Canh";
                adapter.setTag(tag);
                adapter.notifyDataSetChanged();
                break;
            case R.id.navGrill:
                tag = "tag.Nướng";
                adapter.setTag(tag);
                adapter.notifyDataSetChanged();
                break;
            case R.id.navFry:
                tag = "tag.Chiên";
                adapter.setTag(tag);
                adapter.notifyDataSetChanged();
                break;
            case R.id.navSteam:
                tag = "tag.Hấp";
                adapter.setTag(tag);
                adapter.notifyDataSetChanged();
                break;
            case R.id.navVegetable:
                tag = "tag.Chay";
                adapter.setTag(tag);
                adapter.notifyDataSetChanged();
                break;
            case R.id.navDessert:
                tag = "tag.Tráng miệng";
                adapter.setTag(tag);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
//        if (id == R.id.navSetting) {
//            Intent appSetting = new Intent(MainActivity.this, AppSettingActivity.class);
//            startActivity(appSetting);
//        }
//        if (id == R.id.navMix) {
//            Intent appSetting = new Intent(MainActivity.this, AppSettingActivity.class);
//            startActivity(appSetting);
//        }
//        if (id == R.id.navSetting) {
//            Intent appSetting = new Intent(MainActivity.this, AppSettingActivity.class);
//            startActivity(appSetting);
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
