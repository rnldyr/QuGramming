package com.huawei.qugramming;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.huawei.qugramming.databinding.ActivityQuizResultBinding;

public class QuizResultActivity extends AppCompatActivity {

    private ActivityQuizResultBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        binding = DataBindingUtil.setContentView(QuizResultActivity.this, R.layout.activity_quiz_result);
        int correct = getIntent().getIntExtra("correctAns", 0);

        binding.circularProgressBar.setProgress(correct);
        binding.circularProgressBar.setProgressMax(com.huawei.qugramming.QuizActivity.NUMBER_OF_QUESTIONS);
        binding.tvResult.setText(correct + "/" + com.huawei.qugramming.QuizActivity.NUMBER_OF_QUESTIONS);
        binding.tvMessage.setText(correct > 6 ? "Good Job!" : "Try Harder!");
        binding.tvMessage.setTextColor(correct > 6 ? Color.BLUE : Color.RED);

        binding.btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}