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
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.widget.Toast;


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

        Button status_btn= view.findViewById(R.id.viewStatusButton);
        TextView textViewA = view.findViewById(R.id.textViewA);
        TextView textViewB = view.findViewById(R.id.textViewB);
        TextView textViewC = view.findViewById(R.id.textViewC);
        status_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                // timePicker1의 시간을 가져옴
                int hour1 = timePicker1.getCurrentHour();
                int minute1 = timePicker1.getCurrentMinute();

                // timePicker2의 시간을 가져옴
                int hour2 = timePicker2.getCurrentHour();
                int minute2 = timePicker2.getCurrentMinute();

                // 시간 비교
                if (hour1 > hour2 || (hour1 == hour2 && minute1 > minute2)) {
                    // timePicker1의 시간이 timePicker2의 시간보다 뒤일 때 AlertDialog를 통해 경고 메시지 표시
                    Toast.makeText(getActivity(),"시간을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    OkHttpClient client = new OkHttpClient();
                    String jsonData = String.format("{\"hour1\": \"%s\", \"minute1\": \"%s\", \"hour2\": \"%s\", \"minute2\": \"%s\" }",String.valueOf(hour1), String.valueOf(minute1), String.valueOf(hour2), String.valueOf(minute2));
                    RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), jsonData);
                    Request request = new Request.Builder()
                            .url("http://172.10.7.29:80/check_available_seats") // Replace with your actual Flask server endpoint
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

                                    if (jsonResponse.has("resultA")) {
                                        int resultA = jsonResponse.getInt("resultA");
                                        int resultB = jsonResponse.getInt("resultB");
                                        int resultC = jsonResponse.getInt("resultC");
                                        int availableA = 16 - resultA;
                                        int availableB = 16 - resultB;
                                        int availableC = 16 - resultC;

                                        getActivity().runOnUiThread(() -> {
                                            textViewA.setText(availableA + "/16");
                                            textViewB.setText(availableB + "/16");
                                            textViewC.setText(availableC + "/16");
                                        });

                                    } else {
                                        String error= jsonResponse.getString("error");
                                        Log.d("Reservationfragment_ERR",error);
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

        Button a_btn= view.findViewById(R.id.buttonA);
        a_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int hour1 = timePicker1.getCurrentHour();
                int minute1 = timePicker1.getCurrentMinute();

                // timePicker2의 시간을 가져옴
                int hour2 = timePicker2.getCurrentHour();
                int minute2 = timePicker2.getCurrentMinute();

                if (hour1 > hour2 || (hour1 == hour2 && minute1 >= minute2)||(hour1 == hour2 && minute1 == minute2)) {
                    // timePicker1의 시간이 timePicker2의 시간보다 뒤일 때 AlertDialog를 통해 경고 메시지 표시
                    Toast.makeText(getActivity(),"시간을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    String start_time =  Integer.toString(hour1)+":"+Integer.toString(minute1)+":00";
                    String end_time =  Integer.toString(hour2)+":"+Integer.toString(minute2)+":00";

                    Bundle bundle = new Bundle();
                    bundle.putString("start_time", start_time); // 데이터를 번들에 추가
                    bundle.putString("end_time", end_time);

                    a_fragment nextFragment = a_fragment.newInstance();
                    nextFragment.setArguments(bundle);

                    ((Real_main) requireActivity()).replaceFragment(nextFragment);
                }

            }
        });

        Button b_btn= view.findViewById(R.id.buttonB);
        b_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int hour1 = timePicker1.getCurrentHour();
                int minute1 = timePicker1.getCurrentMinute();

                // timePicker2의 시간을 가져옴
                int hour2 = timePicker2.getCurrentHour();
                int minute2 = timePicker2.getCurrentMinute();

                if (hour1 > hour2 || (hour1 == hour2 && minute1 >= minute2)||(hour1 == hour2 && minute1 == minute2)) {
                    // timePicker1의 시간이 timePicker2의 시간보다 뒤일 때 AlertDialog를 통해 경고 메시지 표시
                    Toast.makeText(getActivity(),"시간을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    String start_time =  Integer.toString(hour1)+":"+Integer.toString(minute1)+":00";
                    String end_time =  Integer.toString(hour2)+":"+Integer.toString(minute2)+":00";

                    Bundle bundle = new Bundle();
                    bundle.putString("start_time", start_time); // 데이터를 번들에 추가
                    bundle.putString("end_time", end_time);

                    b_fragment nextFragment = b_fragment.newInstance();
                    nextFragment.setArguments(bundle);

                    ((Real_main) requireActivity()).replaceFragment(nextFragment);
                }
            }
        });
        Button c_btn= view.findViewById(R.id.buttonC);
        c_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int hour1 = timePicker1.getCurrentHour();
                int minute1 = timePicker1.getCurrentMinute();

                // timePicker2의 시간을 가져옴
                int hour2 = timePicker2.getCurrentHour();
                int minute2 = timePicker2.getCurrentMinute();

                if (hour1 > hour2 || (hour1 == hour2 && minute1 >= minute2)||(hour1 == hour2 && minute1 == minute2)) {
                    // timePicker1의 시간이 timePicker2의 시간보다 뒤일 때 AlertDialog를 통해 경고 메시지 표시
                    Toast.makeText(getActivity(),"시간을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    String start_time =  Integer.toString(hour1)+":"+Integer.toString(minute1)+":00";
                    String end_time =  Integer.toString(hour2)+":"+Integer.toString(minute2)+":00";

                    Bundle bundle = new Bundle();
                    bundle.putString("start_time", start_time); // 데이터를 번들에 추가
                    bundle.putString("end_time", end_time);

                    c_fragment nextFragment = c_fragment.newInstance();
                    nextFragment.setArguments(bundle);

                    ((Real_main) requireActivity()).replaceFragment(nextFragment);

                }


            }
        });

        return view;
    }
}