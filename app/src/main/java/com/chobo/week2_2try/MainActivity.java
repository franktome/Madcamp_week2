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
//        String kakaoHashKey = KakaoSdk.INSTANCE.getKeyHash();
//        Log.d("kakaoHashKey", kakaoHashKey);


        ImageView kakaologin = findViewById(R.id.kakaologin);
        kakaologin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 카카오계정으로 로그인 공통 callback 구성
                // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
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
                                    }
                                    return kotlin.Unit.INSTANCE;
                                }
                            });
                        }
                        return null;
                    }
                };

                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, (token, error) -> {
                        if (error != null) {
                            Log.e(TAG, "카카오톡으로 로그인 실패", error);

                            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                            if (error instanceof ClientError && ((ClientError) error).getReason() == ClientErrorCause.Cancelled) {
                                return null;
                            }

                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, Arrays.asList(Prompt.LOGIN), callback);
                        } else if (token != null) {
                            Log.i(TAG, "카카오톡으로 로그인 성공 " + token.getAccessToken());
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
                    });
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
                }

                Intent intent = new Intent(MainActivity.this, Real_main.class);
                startActivity(intent);
                finish();
            }
        });

        Button go_main= findViewById(R.id.gotomain);
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