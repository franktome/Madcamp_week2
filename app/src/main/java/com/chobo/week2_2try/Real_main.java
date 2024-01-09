package com.chobo.week2_2try;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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

    @Override
    public void onBackPressed() {
        showExitDialog();
        super.onBackPressed();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 예 버튼을 눌렀을 때 앱 종료
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 아니오 버튼을 눌렀을 때 아무 동작 없음
                        dialog.dismiss();
                    }
                })
                .show();
    }
}