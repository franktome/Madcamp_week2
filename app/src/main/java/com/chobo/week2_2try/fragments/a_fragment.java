package com.chobo.week2_2try.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.chobo.week2_2try.MainActivity;
import com.chobo.week2_2try.R;
import com.chobo.week2_2try.Real_main;

public class a_fragment extends Fragment {

    public static a_fragment newInstance() {
        return new a_fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        Button before= view.findViewById(R.id.before);
        before.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ((Real_main)requireActivity()).replaceFragment(Reservationfragment.newInstance());
            }
        });


        return view;
    }

}
