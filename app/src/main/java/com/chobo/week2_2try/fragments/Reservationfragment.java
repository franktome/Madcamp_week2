package com.chobo.week2_2try.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;
import android.widget.TimePicker;

import com.chobo.week2_2try.MainActivity;
import com.chobo.week2_2try.R;
import com.chobo.week2_2try.Real_main;

public class Reservationfragment extends Fragment {

    public static Reservationfragment newInstance() {
        return new Reservationfragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        // timepicker 24시간제로 변경
        TimePicker timePicker1 = view.findViewById(R.id.startTimePicker);
        timePicker1.setIs24HourView(true);
        TimePicker timePicker2 = view.findViewById(R.id.endTimePicker);
        timePicker2.setIs24HourView(true);

        Button a_btn= view.findViewById(R.id.buttonA);
        a_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ((Real_main)requireActivity()).replaceFragment(a_fragment.newInstance());
            }
        });

        Button b_btn= view.findViewById(R.id.buttonB);
        b_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ((Real_main)requireActivity()).replaceFragment(b_fragment.newInstance());
            }
        });
        Button c_btn= view.findViewById(R.id.buttonC);
        c_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ((Real_main)requireActivity()).replaceFragment(c_fragment.newInstance());
            }
        });

        return view;
    }
}
