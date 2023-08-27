package jlpt.quiz.japanese.learn;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import jlpt.quiz.japanese.learn.databinding.ActivityPlayBinding;
import jlpt.quiz.japanese.learn.dto.DataDTO;
import jlpt.quiz.japanese.learn.util.Utils;

public class PlayActivity extends AppCompatActivity {
    ActivityPlayBinding binding;
    CountDownTimer countDownTimer;
    int totalQuestion = 10;
    int currentIndex = 0;
    int correctResult = 0;
    ArrayList<DataDTO> listQuest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initAd();

        binding.rlBack.setOnClickListener(v -> onBackPressed());

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                binding.progressBar.setVisibility(View.GONE);
                loadData();
            } catch (Exception e) {
            }
        }, 400);
    }

    private void loadData() {
        listQuest = MainActivity.questionList_Test;
        if (listQuest == null) {
            Toast.makeText(this, "再スタート", Toast.LENGTH_LONG).show();
            finish();
        }
        //listQuest = Utils.getAssetJsonData(this, "n3kanji.js");
        totalQuestion = 0;
        if (listQuest != null && listQuest.size() > 0) {
            totalQuestion = listQuest.size();
            //load xong data
            setTime();
            nextQuest();
            binding.tv1.setOnClickListener(v -> onChooseDA(1));
            binding.tv2.setOnClickListener(v -> onChooseDA(2));
            binding.tv3.setOnClickListener(v -> onChooseDA(3));
            binding.tv4.setOnClickListener(v -> onChooseDA(4));
        }
    }

    @Override
    protected void onPause() {
        binding.adView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.adView.resume();
    }

    @Override
    protected void onDestroy() {
        binding.adView.destroy();
        super.onDestroy();
        try {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            if (delayTime != null) {
                delayTime.cancel();
            }
        } catch (Exception e) {
        }
    }

    CountDownTimer delayTime;

    private void delayTime() {
        binding.tv1.setEnabled(false);
        binding.tv2.setEnabled(false);
        binding.tv3.setEnabled(false);
        binding.tv4.setEnabled(false);
        delayTime = new CountDownTimer(2500, 1000) {
            public void onTick(long millis) {
            }

            public void onFinish() {
                try {
                    binding.tv1.setEnabled(true);
                    binding.tv2.setEnabled(true);
                    binding.tv3.setEnabled(true);
                    binding.tv4.setEnabled(true);
                    nextQuest();
                } catch (Exception e) {

                }
            }
        };
        delayTime.start();
    }

    private void setTime() {
        int minutes = totalQuestion * 2;
        long m = minutes * 60 * 1000;
        countDownTimer = new CountDownTimer(m, 1000) {
            public void onTick(long milliseconds) {
                int seconds = (int) (milliseconds / 1000) % 60;
                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

                String str_seconds = "" + seconds;
                if (seconds < 10) {
                    str_seconds = "0" + seconds;
                }
                String str_minutes = "" + minutes;
                if (minutes < 10) {
                    str_minutes = "0" + minutes;
                }
                String str_hours = "" + hours;
                if (hours < 10) {
                    str_hours = "0" + hours;
                }

                String time = "00:00:" + str_seconds;
                if (minutes > 0) {
                    time = "00:" + str_minutes + ":" + str_seconds;
                }
                if (hours > 0) {
                    time = str_hours + ":" + str_minutes + ":" + str_seconds;
                }
                binding.tvTimer.setText(time);
            }

            public void onFinish() {
                binding.tvTimer.setText("Done");
            }
        };
        countDownTimer.start();
    }

    private void setKQ(int daOK, int daClick) {
        binding.tv1.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_white_border_white));
        binding.tv2.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_white_border_white));
        binding.tv3.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_white_border_white));
        binding.tv4.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_white_border_white));

        if (daClick == 1) {
            binding.tv1.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_red_border_red));
        } else if (daClick == 2) {
            binding.tv2.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_red_border_red));
        } else if (daClick == 3) {
            binding.tv3.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_red_border_red));
        } else if (daClick == 4) {
            binding.tv4.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_red_border_red));
        }

        if (daOK == 1) {
            binding.tv1.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_green_border_green));
        } else if (daOK == 2) {
            binding.tv2.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_green_border_green));
        } else if (daOK == 3) {
            binding.tv3.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_green_border_green));
        } else if (daOK == 4) {
            binding.tv4.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_shapre_conner_bg_green_border_green));
        }
    }

    private void onChooseDA(int daClick) {
        //check kq
        if (currentData != null) {
            if (currentData.answer == daClick) {
                correctResult++;
                binding.tvResultOK.setText("" + correctResult);
            }
            setKQ(currentData.answer, daClick);
            currentIndex++;
            delayTime();
        }
        if (currentIndex >= totalQuestion) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            if (delayTime != null) {
                delayTime.cancel();
            }
            String mess = "結果: " + binding.tvResultOK.getText().toString() + "/" + totalQuestion;
            new AlertDialog.Builder(this).setTitle("テスト完了").setMessage(mess).setPositiveButton("はい", (dialog, which) -> {
                onBackPressed();
            }).show();
        }
    }

    DataDTO currentData;

    private void nextQuest() {
        setKQ(-1, -1);
        int displayIndex = currentIndex + 1;
        binding.tvSL.setText(displayIndex + "/" + totalQuestion);

        currentData = listQuest.get(currentIndex);

        binding.tvCH.setVisibility(Utils.isEmptyString(currentData.question) ? View.GONE : View.VISIBLE);
        binding.tvCH.setText(currentData.question);

        binding.tv1.setVisibility(Utils.isEmptyString(currentData.choice1) ? View.GONE : View.VISIBLE);
        binding.tv1.setText(currentData.choice1);

        binding.tv2.setVisibility(Utils.isEmptyString(currentData.choice2) ? View.GONE : View.VISIBLE);
        binding.tv2.setText(currentData.choice2);

        binding.tv3.setVisibility(Utils.isEmptyString(currentData.choice3) ? View.GONE : View.VISIBLE);
        binding.tv3.setText(currentData.choice3);

        binding.tv4.setVisibility(Utils.isEmptyString(currentData.choice4) ? View.GONE : View.VISIBLE);
        binding.tv4.setText(currentData.choice4);
    }

    //////////////////////////
    //    conditions for not showing ads multiple times
    boolean isInterstitialApplyShown = false;
    boolean isInterstitialSaveShown = false;
    boolean isRewardedApplyShown = false;
    boolean isRewardedSaveShown = false;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    private AdView bannerAdView;

    private boolean isProcessCompleted = false;
    private String message;

    @Override
    public void onBackPressed() {
        boolean ads = checkShowAds();
        if (ads) {
            processStart("");
        } else {
            super.onBackPressed();
        }
    }

    private void processStart(String message) {
        message = "少々お待ちください。";
        binding.progressBar.setVisibility(View.VISIBLE);
        this.message = message;
        isProcessCompleted = true;
    }

    private void processStopIfDone() {
        // if (isProcessCompleted) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "ありがとう", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
            }
        }, 800);
        //  }
        //   isProcessCompleted = false;
    }

    private boolean checkShowAds() {
        int random = ThreadLocalRandom.current().nextInt(0, 2);
        boolean isAds = random == 1 ? true : false;
        System.out.println("@@@@@ isAds " + isAds);
        if (isAds) {
            loadInterstitial();
        }
        return isAds;
    }

/*    @Override
    public void finish() {
        boolean ads = checkShowAds();
        if (ads) {
            processStart("");
        } else {
            super.finish();
        }
    }*/

    private void initAd() {
        bannerAdView = binding.adView;
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);
    }

    private void loadInterstitial() {
        InterstitialAd.load(this, getString(R.string.set_wallpaper_interstitial_id), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        processStopIfDone();
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mInterstitialAd = null;
                    processStopIfDone();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    mInterstitialAd = null;
                    processStopIfDone();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    isInterstitialSaveShown = true;
                    isInterstitialApplyShown = true;
                }
            });
            mInterstitialAd.show(this);
        } else {
            processStopIfDone();
        }
    }

    private void loadRewardedAd(boolean isForSaveImage) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.rewarded_ad_id), adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(PlayActivity.this, "Failed to load Ad.", Toast.LENGTH_SHORT).show();
                mRewardedAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                binding.progressBar.setVisibility(View.GONE);
                mRewardedAd = rewardedAd;
                showRewardedAd(isForSaveImage);
            }
        });
    }

    private void showRewardedAd(boolean isForSaveImage) {
        if (mRewardedAd != null) {
            mRewardedAd.show(this, rewardItem -> {
                if (isForSaveImage) {
                    isRewardedSaveShown = true;
                    //  saveImage();
                } else {
                    isRewardedApplyShown = true;
                    //  askOrApplyWallpaper();
                }
                processStopIfDone();
            });
        } else {
            Log.d("", "The rewarded ad wasn't ready yet.");
        }
    }
}