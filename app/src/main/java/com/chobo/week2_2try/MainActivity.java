package com.chobo.week2_2try;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.auth.model.Prompt;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import android.widget.EditText;

import android.util.Log;
import android.widget.ImageView;

import java.util.Arrays;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        KakaoSdk.init(this, "ec44c45fb1f228885a6c33f347c8a266");

        ImageView kakaologin = findViewById(R.id.kakaologin);
        kakaologin.setOnClickListener(view -> {
            boolean isKakaoTalkLoginAvailable = UserApiClient.Companion.getInstance().isKakaoTalkLoginAvailable(MainActivity.this);
            Log.d(TAG, "카카오톡 로그인 가능 여부: " + isKakaoTalkLoginAvailable);

            // 이 부분에서 callback을 선언하고 초기화합니다.
            Function2<OAuthToken, Throwable, Unit> callback = (token, error) -> {
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error);
                    // 로그인 실패 처리
                } else if (token != null) {
                    Log.i(TAG, "로그인 성공 " + token.getAccessToken());

                    // 로그인 성공 시 사용자 정보 가져오기
                    UserApiClient.Companion.getInstance().me((user, throwable) -> {
                        if (throwable != null) {
                            Log.e(TAG, "사용자 정보 가져오기 실패", throwable);
                        } else if (user != null) {
                            // 사용자 정보 성공적으로 가져옴
                            String userId = String.valueOf(user.getId());
                            Log.i(TAG, "사용자 ID: " + userId);
                            // 닉네임 가져오기
                            String nickname = user.getKakaoAccount().getProfile().getNickname();
                            Log.i(TAG, "사용자 닉네임: " + nickname);
                            adduserinfo(userId, nickname);
                            editor.putString("user_id", userId);
                            editor.putString("nickname", nickname);
                            editor.apply();

                            // 원하는 작업 수행 후 메인 액티비티로 이동
                            Intent intent = new Intent(MainActivity.this, Real_main.class);
                            startActivity(intent);
                            finish();
                        }
                        return kotlin.Unit.INSTANCE;
                    });
                }
                return null;
            };

            if (isKakaoTalkLoginAvailable) {
                UserApiClient.Companion.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
            } else {
                UserApiClient.Companion.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
            }
        });

        Button go_main = findViewById(R.id.gotomain);
        go_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Real_main.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void adduserinfo(String id, String nickname) {
        HttpRequestor.POST2("http://172.10.7.29:80/save_userinfo", id, nickname, new HttpCallback() {
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

    private void requestProfileUsingId(String nickname){
        HttpRequestor.GET("http://172.10.7.29:80/handle_post_request", nickname, new HttpCallback() {
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

//    private void handleProfileResult(String item){
//        profile = item;
//        return;
//    }
}