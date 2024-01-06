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

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

public class b_fragment extends Fragment {

    public static b_fragment newInstance() {
        return new b_fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        Button before= view.findViewById(R.id.before);
        before.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ((Real_main)requireActivity()).replaceFragment(Reservationfragment.newInstance());
            }
        });

        final List<Button> buttons = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            int buttonId = getResources().getIdentifier("Button" + i, "id", requireActivity().getPackageName());
            final Button button = view.findViewById(buttonId);
            buttons.add(button);

            // 각 버튼에 대한 클릭 이벤트 처리
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 클릭된 버튼에 대한 처리
                    handleButtonClick(button);

                    // 다른 버튼들의 색상을 원래대로 돌려주는 처리
                    resetOtherButtons(buttons, button);
                }
            });
        }

        return view;
    }
    private void handleButtonClick(Button button) {
        // 클릭된 버튼에 대한 처리 추가
        // 예: 색상 변경 등
        button.setBackgroundColor(Color.BLACK);
    }

    private void resetOtherButtons(List<Button> buttons, Button clickedButton) {
        // 다른 버튼들의 색상을 원래대로 돌려주는 처리
        for (Button button : buttons) {
            if (button != clickedButton) {
                // 클릭된 버튼이 아닌 경우, 색상을 원래대로 돌려줌
                button.setBackgroundColor(Color.rgb(103, 80, 164)); // 여기에 원래 색상을 지정
            }
        }
    }



}
