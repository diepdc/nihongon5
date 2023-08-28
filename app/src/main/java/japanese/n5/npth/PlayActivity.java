package japanese.n5.npth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.concurrent.ThreadLocalRandom;

import japanese.n5.npth.databinding.ActivityNp2Binding;


public class PlayActivity extends AppCompatActivity {
    ActivityNp2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNp2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initAd();

        binding.rlBack.setOnClickListener(v -> onBackPressed());

        WebSettings webSetting = binding.webView1.getSettings();
        //webSetting.setBuiltInZoomControls(true);
        binding.webView1.setWebViewClient(new WebViewClient());
        binding.webView1.loadUrl("file:///android_asset/np_n5_p2.htm");
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
        boolean isAds = true;//random == 1 ? true : false;
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