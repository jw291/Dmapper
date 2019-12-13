package com.example.dmapper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private com.kakao.usermgmt.LoginButton kakaologinButton;//kakaoAPI버튼
    private Button fakebuttonkakao;//커스텀한 가짜 kakao버튼
    SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fakebuttonkakao = (Button)findViewById(R.id.fake_kakao_login_button);
        fakebuttonkakao.setOnClickListener(this);
        kakaologinButton = (com.kakao.usermgmt.LoginButton) findViewById(R.id.kakao_login_button);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**카카오톡 로그아웃 요청**/
        //한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출합니다.
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fake_kakao_login_button:
                kakaologinButton.performClick();
                break;/*구글 추가
            case R.id.fake_facebook_login_button:
                facebookloginButton.performClick();
                break;*/
        }
    }
    public void KakaoinfoPutextra(Intent intent , String kakaoname, String kakaoimg){
        intent.putExtra("myname",kakaoname);
        intent.putExtra("profileimg",kakaoimg);
        startActivity(intent);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                    Log.e("UserProfile", userProfile.toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    String kakaoname = userProfile.getNickname();
                    String kakaoimg = userProfile.getProfileImagePath();
                    KakaoinfoPutextra(intent,kakaoname,kakaoimg);
                    finish();
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
            // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
        }
    }
}
