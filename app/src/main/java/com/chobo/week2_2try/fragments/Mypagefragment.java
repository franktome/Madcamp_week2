package com.chobo.week2_2try.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


public class Mypagefragment extends Fragment {

    public static Mypagefragment newInstance() {
        return new Mypagefragment();
    }
    private static String name ="hi";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

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

        Button viewresult= view.findViewById(R.id.viewinfo);
        viewresult.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                requestname("nickname");
                Log.i(TAG, name);
            }
        });

        return view;
    }
    private void requestname(String nickname){
        HttpRequestor.GET("http://172.10.7.29:80/save_user", nickname, new HttpCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("Proceduree","Result check login from server :" + result);
                String item = result;
                //handleProfileResult(item);
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }
    private void handleProfileResult(String item){
        name = item;
        return;}
}
