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

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    int num;
    EditText phoneNumberEditText;
    InputMethodManager imm;
    Toolbar toolbar;
    Button authButton;
    LinearLayout ll;
    TextView textView4;
    TextView textView5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // 포커스
        phoneNumberEditText = findViewById(R.id.editText);
        phoneNumberEditText.requestFocus();

        // 키보드 표시
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // 툴바 + 뒤로가기
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 허용

        // 인증번호 버튼 눌렀을때
        authButton = findViewById(R.id.button3);
        authButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                num = phoneNumberEditText.getText().toString().length();

                if(num >= 10) {

                    ll = findViewById(R.id.ll);
                    textView4 = findViewById(R.id.textView4);
                    textView5 = findViewById(R.id.textView5);

                    // 기존 View 애니메이션 이동
                    ObjectAnimator animation = ObjectAnimator.ofFloat(phoneNumberEditText,"translationY", -350f);
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(authButton,"translationY", -350f);
                    ObjectAnimator animation3 = ObjectAnimator.ofFloat(textView4,"translationY", -350f);
                    ObjectAnimator animation4 = ObjectAnimator.ofFloat(textView5,"translationY", -350f);
                    animation.setDuration(200);
                    animation2.setDuration(200);
                    animation3.setDuration(200);
                    animation4.setDuration(200);
                    animation.start();
                    animation2.start();
                    animation3.start();
                    animation4.start();

                    // 동적 인증번호 입력 editText 생성
                    EditText tempEtt = new EditText(getApplicationContext());
                    tempEtt.setHint("인증번호 입력");
                    tempEtt.setBackgroundResource(R.drawable.login_edittext_round);
                    tempEtt.setTextSize(16);
                    tempEtt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 130));
                    tempEtt.setY(-300);
                    tempEtt.requestFocus();
                    tempEtt.setPadding(30,0,0,0);
                    tempEtt.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    // 동적 주의 textView 생성
                    TextView tempTvw = new TextView(getApplicationContext());
                    tempTvw.setText("어떤 경우에도 타인에게 공유하지 마세요!");
                    tempTvw.setTextSize(14);
                    tempTvw.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 130));
                    tempTvw.setY(-270);
                    tempTvw.onEditorAction(3);

                    // 동적 인증번호 확인 button 생성
                    Button tempBtn = new Button(getApplicationContext());
                    tempBtn.setText("인증번호 확인");
                    tempBtn.setBackgroundResource(R.drawable.login_button_round_gray);
                    tempBtn.setTextSize(18);
                    tempBtn.setTypeface(Typeface.DEFAULT_BOLD);
                    tempBtn.setTextColor(Color.parseColor("#FFFFFF"));
                    tempBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 130));
                    tempBtn.setY(-300);

                    // 리니어 레이아웃에 추가
                    ll.addView(tempEtt);
                    ll.addView(tempTvw);
                    ll.addView(tempBtn);
                }
            }
        });

        /*
        // 인증번호 확인 버튼
        tempEtt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if(text.length() >= 6) {
                    tempBtn.setBackgroundResource(R.drawable.login_button_round_orange);
                } else {
                    if(text.length() < 6) {
                        tempBtn.setBackgroundResource(R.drawable.login_button_round_gray);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        })*/

        // 버튼 개수 변화에 대한 authButton 색상 변화
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
    }

    // 뒤로가기 버튼 눌렀을때
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
