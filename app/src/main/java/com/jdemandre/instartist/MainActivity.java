package com.jdemandre.instartist;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jdemandre.instartist.Fragments.HomeFragment;
import com.jdemandre.instartist.Fragments.NotificationFragment;
import com.jdemandre.instartist.Fragments.ProfileFragment;
import com.jdemandre.instartist.Fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    Fragment selectedfragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        setContentView(R.layout.activity_main);


        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

        private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.nav_home:
                                selectedfragment = new HomeFragment();
                                break;
                            case R.id.nav_search:
                                selectedfragment = new SearchFragment();
                                break;
                            case R.id.nav_add:
                                selectedfragment = null;
                                startActivity(new Intent(MainActivity.this, PostActivity.class));
                                break;
                            case R.id.nav_heart:
                                selectedfragment = new NotificationFragment();
                                break;
                            case R.id.nav_profile:
                                selectedfragment = new ProfileFragment();
                                break;
                        }
                        if (selectedfragment != null) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedfragment).commit();
                        }
                        return true;
                    }
                };
    }

