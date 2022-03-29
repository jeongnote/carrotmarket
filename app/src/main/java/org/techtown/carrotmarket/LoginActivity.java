package org.techtown.carrotmarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phone);

        //EditText 객체 포커스
        editText = findViewById(R.id.editText);
        editText.requestFocus();

        //키보드 표시
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //툴바 + 뒤로가기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        getSupportActionBar().setTitle(R.string.test); //툴바 제목
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            //뒤로가기 버튼 눌렀을때
            case android.R.id.home: {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);;
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
