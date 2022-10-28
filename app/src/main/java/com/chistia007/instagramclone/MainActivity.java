package com.chistia007.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.chistia007.instagramclone.Fragments.HomeFragment;
import com.chistia007.instagramclone.Fragments.NotificationFragment;
import com.chistia007.instagramclone.Fragments.ProfileFragment;
import com.chistia007.instagramclone.Fragments.SearchFragment;
import com.chistia007.instagramclone.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottom_navigation;
    private Fragment selectorFragment;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectorFragment=new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectorFragment=new SearchFragment();
                        break;
                    case R.id.nav_add:
                        selectorFragment=null;
                        startActivity(new Intent(MainActivity.this,PostActivity.class));
                        break;
                    case R.id.nav_heart:
                        selectorFragment=new NotificationFragment();
                        break;
                    case R.id.nav_profile:
                        selectorFragment=new ProfileFragment();
                        break;
                }
                if(selectorFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectorFragment).commit();
                }
                return true;
            }
        });
        // By default it will run the home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }
}