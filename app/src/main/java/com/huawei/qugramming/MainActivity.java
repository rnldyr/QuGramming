package com.huawei.qugramming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.qugramming.databinding.ActivityMainBinding;
import com.huawei.qugramming.model.QuizTopic;

public class MainActivity extends AppCompatActivity {

    // 华为帐号登录授权服务，提供静默登录接口silentSignIn，获取前台登录视图getSignInIntent，登出signOut等接口
    // Huawei account service, provides silent signIn API silentSignIn, obtain front-end sign-in view API getSignInIntent, sign out API signOut and other APIs
    private AccountAuthService mAuthService;

    // 华为帐号登录授权参数
    // parameter
    private AccountAuthParams mAuthParam;

    // 用户自定义日志标记
    // User-defined log mark
    private static final String TAG = "Account";

    private ActivityMainBinding binding;
    private QuizTopic selectedTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        setOnClickListener();

        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .createParams();

        mAuthService = AccountAuthManager.getService(this, mAuthParam);

        SharedPref pref = new SharedPref(this);

        HwAds.init(this);

        BannerView bannerView = binding.hwBannerView;
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);

        TextView tvName = binding.tvName;
        tvName.setText("Welcome, " + pref.load());

        binding.HuaweiIdSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();

                pref.delete();
                Intent intent = new Intent(MainActivity.this, QuickStartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signOut() {
        Task<Void> signOutTask = mAuthService.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "signOut Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "signOut fail");
            }
        });
    }

    private void setOnClickListener(){
        binding.llMainmenuHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = QuizTopic.HTML;

                binding.llMainmenuHtml.setBackgroundColor(Color.LTGRAY);
                binding.llMainmenuPhp.setBackgroundColor(Color.WHITE);
                binding.llMainmenuMySql.setBackgroundColor(Color.WHITE);
                binding.llMainmenuBash.setBackgroundColor(Color.WHITE);
            }
        });

        binding.llMainmenuPhp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = QuizTopic.PHP;

                binding.llMainmenuHtml.setBackgroundColor(Color.WHITE);
                binding.llMainmenuPhp.setBackgroundColor(Color.LTGRAY);
                binding.llMainmenuMySql.setBackgroundColor(Color.WHITE);
                binding.llMainmenuBash.setBackgroundColor(Color.WHITE);
            }
        });

        binding.llMainmenuMySql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = QuizTopic.MySql;

                binding.llMainmenuHtml.setBackgroundColor(Color.WHITE);
                binding.llMainmenuPhp.setBackgroundColor(Color.WHITE);
                binding.llMainmenuMySql.setBackgroundColor(Color.LTGRAY);
                binding.llMainmenuBash.setBackgroundColor(Color.WHITE);
            }
        });

        binding.llMainmenuBash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = QuizTopic.Bash;

                binding.llMainmenuHtml.setBackgroundColor(Color.WHITE);
                binding.llMainmenuPhp.setBackgroundColor(Color.WHITE);
                binding.llMainmenuMySql.setBackgroundColor(Color.WHITE);
                binding.llMainmenuBash.setBackgroundColor(Color.LTGRAY);
            }
        });

        binding.btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTopic == null)
                    Toast.makeText(MainActivity.this, "Please choose a Topic", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("selectedTopic", selectedTopic);
                    startActivity(intent);
                }
            }
        });
    }
}