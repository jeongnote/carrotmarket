package com.example.carrotMarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    Toolbar toolbar2;
    FragmentManager fragmentManager;
    HomeFragment homeFragment;
    ChatFragment chatFragment;
    ProfileFragment profileFragment;
    FragmentTransaction transaction;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // toolbar 등록
        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle("");

        // bottomNavigationView 클릭 이벤트 -> fragment 전환
        homeFragment = new HomeFragment();
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();
        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.tab_icon_home:
                        transaction.replace(R.id.main_frame,homeFragment).commit();
                        break;
                    case R.id.tab_icon_chat:
                        transaction.replace(R.id.main_frame,chatFragment).commit();
                        break;
                    case R.id.tab_icon_profile:
                        transaction.replace(R.id.main_frame,profileFragment).commit();
                }
                return true;
            }
        });
    }

    // toolbar 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // toolbar 메뉴 선택 이벤트
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Toast.makeText(getApplicationContext(), "검색 메뉴 선택됨.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.tab_icon_category:
                Toast.makeText(getApplicationContext(), "탭 메뉴 선택됨.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.notification:
                Toast.makeText(getApplicationContext(), "알림 메뉴 선택됨.", Toast.LENGTH_LONG).show();
                return true;
            default:
                Toast.makeText(getApplicationContext(), "이게 뜨면 안되는데..?", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }
}