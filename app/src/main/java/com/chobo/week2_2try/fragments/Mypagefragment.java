package com.chobo.week2_2try.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.chobo.week2_2try.HttpCallback;
import com.chobo.week2_2try.HttpRequestor;
import com.chobo.week2_2try.MainActivity;
import com.chobo.week2_2try.R;
import com.chobo.week2_2try.Real_main;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.auth.model.Prompt;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Mypagefragment extends Fragment {

    public static Mypagefragment newInstance() {
        return new Mypagefragment();
    }
    private static String seat ="hi";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        TextView textViewN = view.findViewById(R.id.name_text);
        TextView textViewA = view.findViewById(R.id.seatinfo_text);
        TextView textViewB = view.findViewById(R.id.starttime_text);
        TextView textViewC = view.findViewById(R.id.endtime_text);


        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        textViewN.setText(preferences.getString("nickname"," "));

        OkHttpClient client = new OkHttpClient();
        String jsonData = String.format("{\"user_id\": \"%s\"}", String.valueOf(preferences.getString("user_id"," ")));
        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), jsonData);
        Request request = new Request.Builder()
                .url("http://172.10.7.29:80/show_reservation_info") // Replace with your actual Flask server endpoint
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

                        if (jsonResponse.has("seat_info")) {
                            String seat_info = jsonResponse.getString("seat_info");
                            if (seat_info.equals("해당값이 없음")){
                                getActivity().runOnUiThread(() -> {
                                    textViewA.setText("해당값이 없음");
                                    textViewB.setText("해당값이 없음");
                                    textViewC.setText("해당값이 없음");
                                });
                            }else{
                                String start_time = jsonResponse.getString("start_hour")+":"+jsonResponse.getString("start_min");
                                String end_time = jsonResponse.getString("end_hour")+":"+jsonResponse.getString("end_min");

                                getActivity().runOnUiThread(() -> {
                                    textViewA.setText(seat_info);
                                    textViewB.setText(start_time);
                                    textViewC.setText(end_time);
                                });
                            }
                        } else {

                            String error= jsonResponse.getString("error");
                            Log.d("Mypagefragment_ERR",error);
                        }

                        // Now you can use these values as needed

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        Button logout= view.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                UserApiClient.getInstance().logout((Throwable error) -> {
                    if (error != null) {
                        Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error);
                    } else {
                        Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨");
                    }
                    return null;
                });
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button delete= view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                OkHttpClient client = new OkHttpClient();
                String jsonData = String.format("{\"user_id\": \"%s\"}", String.valueOf(preferences.getString("user_id"," ")));
                RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), jsonData);
                Request request = new Request.Builder()
                        .url("http://172.10.7.29:80/delete_reservation") // Replace with your actual Flask server endpoint
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

                                if (jsonResponse.has("Delete_method")) {
                                    String Delete_method = jsonResponse.getString("Delete_method");
                                    if(Delete_method.equals("YES")){
                                        getActivity().runOnUiThread(() -> {
                                            Toast.makeText(getActivity(),"예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                            textViewA.setText("해당값이 없음");
                                            textViewB.setText("해당값이 없음");
                                            textViewC.setText("해당값이 없음");
                                        });
                                    }else{
                                        getActivity().runOnUiThread(() -> {
                                            Toast.makeText(getActivity(),"취소할 예약이 없습니다.", Toast.LENGTH_SHORT).show();
                                        });
                                    }

                                } else {

                                    String error= jsonResponse.getString("error");
                                    Log.d("Delete_ERR",error);
                                }

                                // Now you can use these values as needed

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });

        return view;
    }
//    private void user_info(String id) {
//        POST("http://172.10.7.29:80/user_info", id, new HttpCallback() {
//            @Override
//            public void onSuccess(String result) {
//                Log.d("Proceduree","Result check login from server :" + result);
//                String item = result;
//            }
//            @Override
//            public void onFailure(Exception e) {
//            }
//        });
//    }
//    static void POST(String baseurl, String data, HttpCallback callback){
//        Log.d("Procedure", "POST Function Start");
//        String result = null;
//        OkHttpClient client = new OkHttpClient();
//        String url = baseurl;
//        RequestBody requestBody = new FormBody.Builder().add("data", data).build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//        try{
//            Log.d("Procedure", "Execute POST");
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    Log.d("Procedure", e.toString());
//                    callback.onFailure(e);
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    String result =  response.body().string();
//                    Log.d("Procedure", "POST Response Result : " + result);
//                    callback.onSuccess(result);
//                }
//            });
//        } catch (Exception e){
//            e.printStackTrace();
//            Log.d("Procedure", e.toString());
//        }
//    }


}
