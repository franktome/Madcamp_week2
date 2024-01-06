package com.chobo.week2_2try;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
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

        KakaoSdk.init(this, "ec44c45fb1f228885a6c33f347c8a266");

        ImageView kakaologin = findViewById(R.id.kakaologin);
        kakaologin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 카카오계정으로 로그인 공통 callback 구성
                Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
                    @Override
                    public Unit invoke(OAuthToken token, Throwable error) {
                        if (error != null) {
                            Log.e(TAG, "카카오계정으로 로그인 실패", error);
                        } else if (token != null) {
                            Log.i(TAG, "카카오계정으로 로그인 성공 " + token.getAccessToken());
                            UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                                @Override
                                public Unit invoke(User user, Throwable throwable) {
                                    if (throwable != null) {
                                        Log.e(TAG, "사용자 정보 가져오기 실패", throwable);
                                    } else if (user != null) {
                                        // 닉네임 가져오기
                                        String nickname = user.getKakaoAccount().getProfile().getNickname();

                                        // 닉네임을 서버에 전달하는 로직 추가
                                        // 예: 서버 통신 코드 작성

                                        Log.i(TAG, "사용자 닉네임: " + nickname);
                                        Intent intent = new Intent(MainActivity.this, Real_main.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    return kotlin.Unit.INSTANCE;
                                }
                            });
                        }
                        return null;
                    }
                };

                // 항상 로그인 화면을 표시
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
                }
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

}