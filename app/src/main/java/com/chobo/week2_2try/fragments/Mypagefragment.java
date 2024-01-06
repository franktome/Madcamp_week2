package com.chobo.week2_2try.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.chobo.week2_2try.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Mypagefragment extends Fragment {

    public Mypagefragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        return view;
    }


}
