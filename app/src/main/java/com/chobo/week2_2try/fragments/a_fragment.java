package com.chobo.week2_2try.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.chobo.week2_2try.R;
import com.chobo.week2_2try.Real_main;

import java.util.ArrayList;
import java.util.List;

public class a_fragment extends Fragment {

    public static a_fragment newInstance() {
        return new a_fragment();
    }
    private static int button_num=2131230730;

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



        Button result = view.findViewById(R.id.result);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button_num == 2131230730) {
                    // 선택된 좌석이 없는 경우
                    Log.i(TAG, "선택된 좌석이 없습니다");
                    // 여기에서 사용자에게 메시지를 표시하거나 원하는 동작을 수행할 수 있습니다.
                } else {
                    // 선택된 좌석이 있는 경우 > DB에 보낼때 "a"+buttonId로 보내기
                    Log.i(TAG, "눌려진 버튼의 id: " + button_num);
                    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
                    //여기서 눌려진 버튼이 db에 있는지 확인하고 없다면 "예약되었습니다" 문구 띄우고 넘어가고 있다면 경고문 띄우고 넘어가지 않기//

                    //Toast.makeText(getActivity(),"이미 예약되어 있습니다.", Toast.LENGTH_SHORT).show();
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////
                    ((Real_main) requireActivity()).replaceFragment(Mypagefragment.newInstance());
                    button_num = 2131230730;
                }
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
                    button_num = button.getId();
                    //Log.i(TAG, "눌려진 버튼의 id: " + button_num);
                    Log.i(TAG, "눌려진 버튼의 id: " + button.getId());
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
