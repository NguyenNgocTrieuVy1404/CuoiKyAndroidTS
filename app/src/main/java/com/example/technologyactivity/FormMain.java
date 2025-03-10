package com.example.technologyactivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.adapter.NotificationAdapter;
import com.example.modeldata.Notification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class FormMain extends AppCompatActivity {
    private List<Notification> notificationList;
    private NotificationAdapter notificationAdapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trang Chủ");

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    fragment = new HomeFragment();
                    getSupportActionBar().setTitle("Trang Chủ");
                } else if (itemId == R.id.navigation_search) {
                    fragment = new SearchFragment();
                    getSupportActionBar().setTitle("Tìm Kiếm");
                } else if (itemId == R.id.navigation_cart) {
                    fragment = new CartFragment(notificationList, notificationAdapter);
                    getSupportActionBar().setTitle("Giỏ Hàng");
                } else if (itemId == R.id.navigation_profile) {
                    getSupportActionBar().setTitle("Hồ Sơ");
                    fragment = new ProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    fragment.setArguments(bundle);
                }
                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                } else {
                    return false;
                }
            }
        });

        loadFragment(new HomeFragment());

        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_cart) {
            loadFragment(new CartFragment(notificationList, notificationAdapter));
            getSupportActionBar().setTitle("Giỏ Hàng");
            return true;
        } else if (itemId == R.id.action_bell) {
            loadFragment(new NotificationFragment(notificationList, notificationAdapter));
            getSupportActionBar().setTitle("Thông Báo");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
