package com.example.carrotMarket;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    int num;
    EditText phoneNumberEditText;
    InputMethodManager imm;
    Toolbar toolbar;
    Button authButton;
    TextView textView4;
    TextView textView5;
    EditText editText2;
    Button authOkButton;
    SignInButton signInButton;
    LoginButton loginButton;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        editText2 = findViewById(R.id.editText2);
        authOkButton = findViewById(R.id.button4);
        signInButton = findViewById(R.id.google_login_button);
        loginButton = findViewById(R.id.facebook_login_button);

        // 파이어베이스 초기화
        mAuth = FirebaseAuth.getInstance();

        // 포커스
        phoneNumberEditText = findViewById(R.id.editText);
        phoneNumberEditText.requestFocus();

        // 키보드 표시
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // 툴바 + 뒤로가기 버튼 생성
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 인증번호 버튼 눌렀을때 이벤트 실행
        authButton = findViewById(R.id.button3);
        authButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                num = phoneNumberEditText.getText().toString().length();

                // 10자리 이상 시
                if(num >= 10) {

                    // 기존 View 및 INVISIBLE View 애니메이션 이동
                    ObjectAnimator animation = ObjectAnimator.ofFloat(textView4,"translationY", -350f);
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(textView5,"translationY", -350f);
                    ObjectAnimator animation3 = ObjectAnimator.ofFloat(phoneNumberEditText,"translationY", -350f);
                    ObjectAnimator animation4 = ObjectAnimator.ofFloat(authButton,"translationY", -350f);
                    ObjectAnimator animation5 = ObjectAnimator.ofFloat(editText2,"translationY", -350f);
                    ObjectAnimator animation6 = ObjectAnimator.ofFloat(authOkButton,"translationY", -350f);
                    ObjectAnimator animation7 = ObjectAnimator.ofFloat(loginButton,"translationY", 200f);
                    ObjectAnimator animation8 = ObjectAnimator.ofFloat(signInButton,"translationY", 200f);
                    animation.setDuration(200);
                    animation2.setDuration(200);
                    animation3.setDuration(200);
                    animation4.setDuration(200);
                    animation5.setDuration(200);
                    animation6.setDuration(200);
                    animation7.setDuration(200);
                    animation8.setDuration(200);
                    animation.start();
                    animation2.start();
                    animation3.start();
                    animation4.start();
                    animation5.start();
                    animation6.start();
                    animation7.start();
                    animation8.start();

                    editText2.setVisibility(View.VISIBLE);
                    authOkButton.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    signInButton.setVisibility(View.GONE);

                    authButton.setText("인증문자 다시 받기");

                    editText2.requestFocus();
                }
            }
        });

        // authOkButton 클릭 시, 화면 이동
        authOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임시
                int num = editText2.getText().toString().length();
                if(num == 6) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);
                }
            }
        });

        // authButton 색상 변화
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String text = charSequence.toString();
                if(text.length() >= 10) {
                    authButton.setBackgroundResource(R.drawable.login_button_round_black);
                } else {
                    if(text.length() < 10) {
                        authButton.setBackgroundResource(R.drawable.login_button_round_gray);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // authOkButton 색상 변화
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if(text.length() == 6) {
                    authOkButton.setBackgroundResource(R.drawable.login_button_round_orange);
                } else {
                    if(text.length() < 6) {
                        authOkButton.setBackgroundResource(R.drawable.login_button_round_gray);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // 구글 버튼 클릭 시, 발생하는 이벤트
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        // 구글 로그인 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "연동 성공!",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "연동 실패!",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager = CallbackManager.Factory.create();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(),"성공했습니다.",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"실패했습니다.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // 뒤로가기 눌렀을때 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);;
                imm.hideSoftInputFromWindow(phoneNumberEditText.getWindowToken(), 0);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}