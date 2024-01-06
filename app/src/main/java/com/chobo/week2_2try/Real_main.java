package com.chobo.week2_2try;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chobo.week2_2try.fragments.Mypagefragment;
import com.chobo.week2_2try.fragments.Reservationfragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Real_main extends AppCompatActivity {

    Mypagefragment mypagefragment= new Mypagefragment();
    Reservationfragment reservationfragment = new Reservationfragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realmain);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, reservationfragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.reserve_seat) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, reservationfragment).commit();
            } else if (item.getItemId() == R.id.my_page) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mypagefragment).commit();
            }
            return true;
        });

        // 초기 화면으로 Fragment1을 로드
        loadFragment(new Reservationfragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }
}