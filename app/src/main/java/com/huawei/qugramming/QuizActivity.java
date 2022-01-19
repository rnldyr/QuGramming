package com.huawei.qugramming;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.callback.HwAudioConfigCallBack;
import com.huawei.hms.audiokit.player.manager.HwAudioConfigManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManagerFactory;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerConfig;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerManager;
import com.huawei.hms.audiokit.player.manager.HwAudioQueueManager;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator;
import com.huawei.qugramming.api.ApiInterface;
import com.huawei.qugramming.databinding.ActivityQuizBinding;
import com.huawei.qugramming.model.AnswerOption;
import com.huawei.qugramming.model.Question;
import com.huawei.qugramming.model.QuizTopic;
import com.huawei.qugramming.model.UserAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizActivity extends AppCompatActivity {

    public static final int NUMBER_OF_QUESTIONS = 10;

    private Random rnd;
    private ActivityQuizBinding binding;
    private Timer timer;
    private int minutes;
    private int seconds;
    private int currentQuestionPosition;
    private ArrayList<Question> questions;
    private ArrayList<UserAnswer> answers;
    private Question currentQuestion;
    private AnswerOption selectedAnswer;
    private QuizTopic topic;
    private MLRemoteTranslator mlRemoteTranslator;

    private void translateFunction(final String question){
        MLApplication.getInstance().setApiKey("DAEDAJiRoKJfPgvYBb7xCfXNgjYi6h6UQssss/pYf/z4H4yS7jFiYh8IAbHdZrgCx9o/AEw/seFVjkkfh2Bx7RWGLNoBqMOGzRnSuQ==");
        MLRemoteTranslateSetting setting = new MLRemoteTranslateSetting
                .Factory().setSourceLangCode("en")
                .setTargetLangCode("id")
                .create();
        mlRemoteTranslator = MLTranslatorFactory.getInstance().getRemoteTranslator(setting);
        final Task<String> task = mlRemoteTranslator.asyncTranslate(question);
        task.addOnSuccessListener((text) -> {
            binding.tvQuestion.setText(text);
        }).addOnFailureListener((e) ->{
            try {
                MLException mlException = (MLException)e;
                // Obtain the result code. You can process the result code and customize respective messages displayed to users.
                int errorCode = mlException.getErrCode();
                // Obtain the error information. You can quickly locate the fault based on the result code.
                String errorMessage = mlException.getMessage();
            } catch (Exception error) {
                // Handle the conversion error.
            }
        });
        if (mlRemoteTranslator!= null) {
            mlRemoteTranslator.stop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initFinalVariables();
        setOnClickListener();
    }

    private void initFinalVariables() {
        binding = DataBindingUtil.setContentView(QuizActivity.this, R.layout.activity_quiz);
        topic = (QuizTopic) getIntent().getSerializableExtra("selectedTopic");
        minutes = 1;
        seconds = 0;
        rnd = new Random();
        timer = new Timer();
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        currentQuestionPosition = 0;
        getQuestionsList();
    }

    private void setViewValues() {
        binding.tvSelectedTopic.setText(topic.name());
        binding.tvProgress.setText((currentQuestionPosition + 1) + "/" + NUMBER_OF_QUESTIONS);
        binding.cvAnswerA.setVisibility(View.VISIBLE);
        binding.cvAnswerA.setCardBackgroundColor(Color.WHITE);
        binding.cvAnswerB.setVisibility(View.VISIBLE);
        binding.cvAnswerB.setCardBackgroundColor(Color.WHITE);
        binding.cvAnswerC.setVisibility(View.VISIBLE);
        binding.cvAnswerC.setCardBackgroundColor(Color.WHITE);
        binding.cvAnswerD.setVisibility(View.VISIBLE);
        binding.cvAnswerD.setCardBackgroundColor(Color.WHITE);
        binding.cvAnswerE.setVisibility(View.VISIBLE);
        binding.cvAnswerE.setCardBackgroundColor(Color.WHITE);
        binding.cvAnswerF.setVisibility(View.VISIBLE);
        binding.cvAnswerF.setCardBackgroundColor(Color.WHITE);
        setQuestionView();
    }

    private void setQuestionView() {
        int res = (rnd.nextInt(1000) % questions.size());
        currentQuestion = questions.get(res);

        translateFunction(currentQuestion.getQuestion());
        if (currentQuestion.getAnswers().getAnswer_a() != null)
            binding.tvAnswerA.setText(currentQuestion.getAnswers().getAnswer_a());
        else
            binding.cvAnswerA.setVisibility(View.GONE);
        if (currentQuestion.getAnswers().getAnswer_b() != null)
            binding.tvAnswerB.setText(currentQuestion.getAnswers().getAnswer_b());
        else
            binding.cvAnswerB.setVisibility(View.GONE);
        if (currentQuestion.getAnswers().getAnswer_c() != null)
            binding.tvAnswerC.setText(currentQuestion.getAnswers().getAnswer_c());
        else
            binding.cvAnswerC.setVisibility(View.GONE);
        if (currentQuestion.getAnswers().getAnswer_d() != null)
            binding.tvAnswerD.setText(currentQuestion.getAnswers().getAnswer_d());
        else
            binding.cvAnswerD.setVisibility(View.GONE);
        if (currentQuestion.getAnswers().getAnswer_e() != null)
            binding.tvAnswerE.setText(currentQuestion.getAnswers().getAnswer_e());
        else
            binding.cvAnswerE.setVisibility(View.GONE);
        if (currentQuestion.getAnswers().getAnswer_f() != null)
            binding.tvAnswerF.setText(currentQuestion.getAnswers().getAnswer_f());
        else
            binding.cvAnswerF.setVisibility(View.GONE);
    }

    private void changeNextQuestion() {
        ++currentQuestionPosition;
        selectedAnswer = null;

        if ((currentQuestionPosition + 1) == NUMBER_OF_QUESTIONS) {
            binding.btnNextQuestion.setText("Submit Quiz");
        }
        if (currentQuestionPosition == NUMBER_OF_QUESTIONS) {
            gotoQuizResult();
            return;
        }
        setViewValues();
    }

    private void setOnClickListener() {
        binding.ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });

        binding.cvAnswerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = AnswerOption.answer_a;

                binding.cvAnswerA.setCardBackgroundColor(Color.LTGRAY);
                binding.cvAnswerB.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerC.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerD.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerE.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerF.setCardBackgroundColor(Color.WHITE);
            }
        });

        binding.cvAnswerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = AnswerOption.answer_b;

                binding.cvAnswerA.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerB.setCardBackgroundColor(Color.LTGRAY);
                binding.cvAnswerC.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerD.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerE.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerF.setCardBackgroundColor(Color.WHITE);
            }
        });

        binding.cvAnswerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = AnswerOption.answer_c;

                binding.cvAnswerA.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerB.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerC.setCardBackgroundColor(Color.LTGRAY);
                binding.cvAnswerD.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerE.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerF.setCardBackgroundColor(Color.WHITE);
            }
        });

        binding.cvAnswerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = AnswerOption.answer_d;

                binding.cvAnswerA.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerB.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerC.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerD.setCardBackgroundColor(Color.LTGRAY);
                binding.cvAnswerE.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerF.setCardBackgroundColor(Color.WHITE);
            }
        });

        binding.cvAnswerE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = AnswerOption.answer_e;

                binding.cvAnswerA.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerB.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerC.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerD.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerE.setCardBackgroundColor(Color.LTGRAY);
                binding.cvAnswerF.setCardBackgroundColor(Color.WHITE);
            }
        });

        binding.cvAnswerF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = AnswerOption.answer_f;

                binding.cvAnswerA.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerB.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerC.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerD.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerE.setCardBackgroundColor(Color.WHITE);
                binding.cvAnswerF.setCardBackgroundColor(Color.LTGRAY);
            }
        });

        binding.btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer == null)
                    Toast.makeText(QuizActivity.this, "Please choose an Answer", Toast.LENGTH_SHORT).show();
                else {
                    answers.add(new UserAnswer(currentQuestion, selectedAnswer));
                    changeNextQuestion();
                }
            }
        });
    }

    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (seconds == 0 && minutes == 0) {
                    timer.purge();
                    timer.cancel();
                } else if (seconds == 0) {
                    --minutes;
                    seconds = 59;
                } else
                    --seconds;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (seconds == 0 && minutes == 0) {
                            Toast.makeText(QuizActivity.this, "Time's Up!", Toast.LENGTH_SHORT).show();
                            gotoQuizResult();
                        }

                        String fMinutes = String.valueOf(minutes);
                        String fSeconds = String.valueOf(seconds);

                        if (fMinutes.length() == 1)
                            fMinutes = "0" + fMinutes;

                        if (fSeconds.length() == 1)
                            fSeconds = "0" + fSeconds;

                        binding.tvTimer.setText(fMinutes + ":" + fSeconds);
                    }
                });
            }
        }, 1000, 1000);
    }

    private int getCorrectAnswer() {
        int correctAnswers = 0;

        for (UserAnswer ans : answers)
            if (ans.isAnswerCorrect())
                ++correctAnswers;

        return correctAnswers;
    }

    private void getQuestionsList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://quizapi.io/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String category = topic != QuizTopic.MySql ? "code" : "sql";
        String tag = topic != QuizTopic.MySql ? topic.name() : "";

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Question>> listCall = apiInterface.getQuestions("X7FVILp9ALwGgyzh6IP0BkxLh0BpUEFOvw80k9SN", category, tag);

        listCall.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(QuizActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                questions = (ArrayList<Question>) response.body();
                setViewValues();
                startTimer();
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoQuizResult() {
        timer.purge();
        timer.cancel();
        Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
        intent.putExtra("correctAns", getCorrectAnswer());
        startActivity(intent);
        finish();
    }

    private void backToMain() {
        timer.purge();
        timer.cancel();

        startActivity(new Intent(QuizActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMain();
    }
}