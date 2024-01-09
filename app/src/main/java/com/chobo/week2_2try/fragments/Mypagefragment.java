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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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

        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        user_info(preferences.getString("user_id"," "));

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

        return view;
    }
    private void user_info(String id) {
        POST("http://172.10.7.29:80/user_info", id, new HttpCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("Proceduree","Result check login from server :" + result);
                String item = result;
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }
    static void POST(String baseurl, String data, HttpCallback callback){
        Log.d("Procedure", "POST Function Start");
        String result = null;
        OkHttpClient client = new OkHttpClient();
        String url = baseurl;
        RequestBody requestBody = new FormBody.Builder().add("data", data).build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try{
            Log.d("Procedure", "Execute POST");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("Procedure", e.toString());
                    callback.onFailure(e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String result =  response.body().string();
                    Log.d("Procedure", "POST Response Result : " + result);
                    callback.onSuccess(result);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            Log.d("Procedure", e.toString());
        }
    }


}
