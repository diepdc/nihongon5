package japanese.n5.npth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import japanese.n5.npth.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.tvP2.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        });
        initAd();
        //
        WebSettings webSetting = binding.webView1.getSettings();
        //webSetting.setBuiltInZoomControls(true);
        binding.webView1.setWebViewClient(new WebViewClient());
        binding.webView1.loadUrl("file:///android_asset/np_n5p1.htm");
    }

    private void initAd() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }
}