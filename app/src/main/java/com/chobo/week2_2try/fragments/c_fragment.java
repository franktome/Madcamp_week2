
package com.chobo.week2_2try.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.chobo.week2_2try.MainActivity;
import com.chobo.week2_2try.R;
import com.chobo.week2_2try.Real_main;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;

public class c_fragment extends Fragment {

    public static c_fragment newInstance() {
        return new c_fragment();
    }

    private String start_time;
    private String  end_time;
    private JSONArray notAvailableArray;

    private Button current_seat = null;
    private Button my_seat_btn = null;

    private TextView stateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c, container, false);

        Button before= view.findViewById(R.id.before);
        Button result= view.findViewById(R.id.result);

        stateTextView = view.findViewById(R.id.state);


        Bundle bundle = getArguments();
        if (bundle != null) {
            start_time = bundle.getString("start_time");
            end_time = bundle.getString("end_time");
            stateTextView.setText("현재 설정 시간: " + start_time + " ~ " + end_time);
        }
        before.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ((Real_main)requireActivity()).replaceFragment(Reservationfragment.newInstance());
            }
        });

        final List<Button> buttons = new ArrayList<>();
        final List<Button> notavailablebuttons = new ArrayList<>();
        //post 날려서 상태 받아와서 Click 가능 여부, 색깔 바꾸기
        //start_time, end_time, room 넘겨서 seat_no 받아오기
        OkHttpClient client = new OkHttpClient();
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id",null);
        String jsonData = String.format("{\"start_time\": \"%s\", \"end_time\": \"%s\", \"room\": \"C\", \"user_id\": \"%s\"}",start_time,end_time, user_id);
        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), jsonData);
        Request request = new Request.Builder()
                .url("http://172.10.7.29:80/room_state") // Replace with your actual Flask server endpoint
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Handle failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                // Handle the response from the server
                if (response.isSuccessful()) {
                    // Handle successful response

                    // Convert the response body to a string
                    String responseBody = response.body().string();

                    try {
                        // Parse the JSON response
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        if (jsonResponse.has("not_available")) {
                            notAvailableArray = jsonResponse.getJSONArray("not_available");
                            String my_seat = jsonResponse.getString("my_seat");

                            getActivity().runOnUiThread(() -> {
                                for (int i = 1; i <= 16; i++) {
                                    int buttonId = getResources().getIdentifier("Button" + i, "id", requireActivity().getPackageName());
                                    final Button button = view.findViewById(buttonId);
                                    buttons.add(button);


                                    //array에 i가 있을 때? 클릭 불가 버튼 색깔 회색
                                    if (isButtonNotAvailable(i, notAvailableArray)) {
                                        // Button is not available, disable and change color
                                        notavailablebuttons.add(button);
                                        button.setEnabled(false);
                                        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))); // Change to your desired color resource
                                    }

                                    if(!my_seat.equals("no")){
                                        if(i == Integer.parseInt(my_seat)){
                                            button.setEnabled(true);
                                            button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green)));
                                            my_seat_btn = button;
                                        }
                                    }

                                    // 각 버튼에 대한 클릭 이벤트 처리
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // 클릭된 버튼에 대한 처리
                                            handleButtonClick(button);
                                            current_seat = button;

                                            // 다른 버튼들의 색상을 원래대로 돌려주는 처리
                                            resetOtherButtons(buttons, notavailablebuttons, button, my_seat_btn);
                                        }
                                    });
                                }

                            });

                        } else {
                            String error= jsonResponse.getString("error");
                            Log.d("C_fragmentERR",error);
                        }

                        // Now you can use these values as needed

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        result.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(current_seat==null){
                    Toast.makeText(getActivity(),"자리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    String current_seat_text = current_seat.getText().toString();
                    String current_seat_no = current_seat_text.substring(1);

                    SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    String user_id = preferences.getString("user_id",null);
                    //occupied_users(user_id,A,seat_no), occupied_seatsA(user_id,seat_no, start_time, end_time
                    OkHttpClient client = new OkHttpClient();
                    String jsonData = String.format("{\"start_time\": \"%s\", \"end_time\": \"%s\", \"room\": \"C\", \"seat_no\": \"%s\", \"user_id\": \"%s\" }",start_time,end_time,current_seat_no, user_id);
                    RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), jsonData);
                    Request request = new Request.Builder()
                            .url("http://172.10.7.29:80/insert_reservation") // Replace with your actual Flask server endpoint
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            // Handle failure
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                            // Handle the response from the server
                            if (response.isSuccessful()) {
                                // Handle successful response

                                // Convert the response body to a string
                                String responseBody = response.body().string();

                                try {
                                    // Parse the JSON response
                                    JSONObject jsonResponse = new JSONObject(responseBody);

                                    if (jsonResponse.has("Reservation_method")) {

                                        String method = jsonResponse.getString("Reservation_method");

                                        getActivity().runOnUiThread(() -> {
                                            if (method.equals("insert")){
                                                Toast.makeText(getActivity(),"예약이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getActivity(),"예약이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });



                                    } else {
                                        String error= jsonResponse.getString("error");
                                        Log.d("C_Reservation_ERR",error);
                                    }

                                    // Now you can use these values as needed

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                }
            }
        });
        return view;
    }
    private void handleButtonClick(Button button) {
        // 클릭된 버튼에 대한 처리 추가
        // 예: 색상 변경 등
        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue)));
    }

    private void resetOtherButtons(List<Button> buttons, List<Button> notavailablebuttons, Button clickedButton, Button my_current_seat) {
        // 다른 버튼들의 색상을 원래대로 돌려주는 처리
        for (Button button : buttons) {
            if (button != clickedButton) {
                if (button == my_current_seat){
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green)));
                }else {
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
                    for (Button notavailablebutton: notavailablebuttons){
                        if(notavailablebutton == button) {
                            button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isButtonNotAvailable(int buttonNumber, JSONArray notAvailableArray) {
        try {
            if(notAvailableArray.length() == 0){
                return false;
            }
            for (int i = 0; i < notAvailableArray.length(); i++) {
                if (notAvailableArray.getInt(i) == buttonNumber) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}