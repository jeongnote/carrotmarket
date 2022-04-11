package com.example.carrotMarket;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import com.example.carrotMarket.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText editText;

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    EditText phoneNumberEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phone);

        // editText 주소
        phoneNumberEditText = findViewById(R.id.editText);

        // 파이어베이스 인증 초기화
        mAuth = FirebaseAuth.getInstance();

        // EditText 객체 포커스
        editText = findViewById(R.id.editText);
        editText.requestFocus();

        // 키보드 표시
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // 툴바 + 뒤로가기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        getSupportActionBar().setTitle(R.string.test); //툴바 제목
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        // 11자 이상 입력 후 버튼 클릭시 전화 번호 인증 메소드 실행
        Button authButton = findViewById(R.id.button3);
        authButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int num = editText.getText().toString().length();
                // 10자리 이상 숫자일 시 인증문자 발송
                if(num >= 10) {
                    String phoneNumber = editText.getText().toString();
                    startPhoneNumberVerification(phoneNumber);
                } else {
                    // 10자리 미만일 시 미발송
                    if(num < 10) {

                    }
                }

            }
        });

        // 버튼 개수 변화에 대한 authButton 색상 변화
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String text = charSequence.toString();
                if(text.length() >= 10) {
                    authButton.setBackgroundColor(Color.parseColor("#4d5158"));
                } else {
                    if(text.length() < 10) {
                        authButton.setBackgroundColor(Color.parseColor("#dcdee2"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // 전화 인증 콜백 초기화
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // 이 콜백은 유효하지 않은 검증 요청이 있을 때 호출
                // 예를 들어 전화번호 형식이 유효하지 않은 경우.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // 잘못된 요청
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // 프로젝트의 SMS 할당량이 초과되었습니다.
                }

                // 메시지 표시 및 UI 업데이트
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // SMS 인증 코드는 제공된 전화 번호로 전송
                // 이제 사용자에게 코드를 입력하고 자격 증명을 생성하도록 요청해야 합
                // 코드를 확인 ID와 결합하여
                Log.d(TAG, "onCodeSent:" + verificationId);

                //나중에 사용할 수 있도록 인증 ID 및 재전송 토큰을 저장
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        // [END phone_auth_callbacks]
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            // 뒤로가기 버튼 눌렀을때
            case android.R.id.home: {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);;
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // 전화번호 확인 요청
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    // id를 받아서 PhoneAuthCredential 객체 생성
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // UI 업데이트
                        } else {
                            // 로그인 실패, 메시지 표시 및 UI 업데이트
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // 입력한 인증 코드가 잘못되었습니다.
                            }
                        }
                    }
                });


    }
    // [END sign_in_with_phone]
}
