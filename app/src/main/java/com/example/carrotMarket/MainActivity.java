package com.example.carrotMarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;

import com.example.carrotMarket.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버튼 글씨의 일부 색 바꾸기.
        Button button4 = findViewById(R.id.button2);
        String content = button4.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "로그인";
        int start = content.indexOf(word);
        int end = start + word.length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7E36")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button4.setText(spannableString);

        // 클릭 이벤트 -> 로그인 페이지로 넘어감.
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}