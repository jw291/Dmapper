package com.example.dmapper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    //카카오톡 간편 로그인 관련 변수
    private com.kakao.usermgmt.LoginButton kakaologinButton;//kakaoAPI버튼
    private Button fakebuttonkakao;//커스텀한 가짜 kakao버튼
    private SessionCallback callback;
    private String user_name, user_email;

    //구글 간편 로그인 관련 변수(Firebase 사용)
    SignInButton googleloginButton;
    private Button fakebuttongoogle;
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**카카오톡 관련 변수 초기화**/
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        fakebuttonkakao = (Button)findViewById(R.id.fake_kakao_login_button);
        fakebuttonkakao.setOnClickListener(this);
        kakaologinButton = (com.kakao.usermgmt.LoginButton) findViewById(R.id.kakao_login_button);

        /**구글 관련 변수 초기화**/
        fakebuttongoogle = (Button)findViewById(R.id.fake_google_login_button);
        fakebuttongoogle.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mAuth = FirebaseAuth.getInstance();
        googleloginButton = findViewById(R.id.Google_Login_button);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fake_kakao_login_button:
                kakaologinButton.performClick();
                break;
            case R.id.fake_google_login_button:
                googleloginButton.performClick();
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
                break;
        }
    }

    //인터넷 연결상태 확인
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Toast.makeText(this, "들어옴", Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else{
                Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show();
            }
        }

        /**카카오 관련**/

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "구글 로그인 인증 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /** 카카오톡 간편로그인 콜백 메서드 **/
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            //access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태. 일반적으로 로그인 후의 다음 activity로 이동한다.
            if(Session.getCurrentSession().isOpened()){ // 한 번더 세션을 체크해주었습니다.
                requestMe();
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }
    private void requestLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("onFailure", errorResult + "");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("onSessionClosed",errorResult + "");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("onSuccess",userProfile.toString());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                user_name = userProfile.getNickname();
                user_email = userProfile.getEmail();
                //System.out.println(" "+user_name + " "+user_email);
                KakaoinfoPutextra(intent,user_name,user_email);
            }

            @Override
            public void onNotSignedUp() {
                Log.e("onNotSignedUp","onNotSignedUp");
            }
        });
    }

    public void KakaoinfoPutextra(Intent intent , String kakaoname, String kakaoimg){
        intent.putExtra("myname",kakaoname);
        intent.putExtra("profileimg",kakaoimg);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

}
