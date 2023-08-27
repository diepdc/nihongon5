package jlpt.quiz.japanese.learn;

import static jlpt.quiz.japanese.learn.util.Const.MENU_FREE_FULL;
import static jlpt.quiz.japanese.learn.util.Const.MENU_FREE_N3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import jlpt.quiz.japanese.learn.databinding.ActivityMenuBinding;
import jlpt.quiz.japanese.learn.util.Const;

public class MenuActivity extends AppCompatActivity {

    ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //tu do
        binding.btnFull.setOnClickListener(v -> onSelected(Const.MENU_FREE_FULL, getString(R.string.jiyu) + " " + binding.btnFull.getText().toString()));
        binding.btnN3.setOnClickListener(v -> onSelected(Const.MENU_FREE_N3, getString(R.string.jiyu) + " " + binding.btnN3.getText().toString()));
        binding.btnN4.setOnClickListener(v -> onSelected(Const.MENU_FREE_N4, getString(R.string.jiyu) + " " + binding.btnN4.getText().toString()));
        binding.btnN5.setOnClickListener(v -> onSelected(Const.MENU_FREE_N5, getString(R.string.jiyu) + " " + binding.btnN5.getText().toString()));

        binding.btnKanjiFull.setOnClickListener(v -> onSelected(Const.MENU_KANJI_FULL, getString(R.string.kanji) + " " + binding.btnKanjiFull.getText().toString()));
        binding.btnKanjiN3.setOnClickListener(v -> onSelected(Const.MENU_KANJI_N3, getString(R.string.kanji) + " " + binding.btnKanjiN3.getText().toString()));
        binding.btnKanjiN4.setOnClickListener(v -> onSelected(Const.MENU_KANJI_N4, getString(R.string.kanji) + " " + binding.btnKanjiN4.getText().toString()));
        binding.btnKanjiN5.setOnClickListener(v -> onSelected(Const.MENU_KANJI_N5, getString(R.string.kanji) + " " + binding.btnKanjiN5.getText().toString()));

        binding.btnBunpoFull.setOnClickListener(v -> onSelected(Const.MENU_BUNPO_FULL, getString(R.string.bunpo) + " " + binding.btnBunpoFull.getText().toString()));
        binding.btnBunpoN3.setOnClickListener(v -> onSelected(Const.MENU_BUNPO_N3, getString(R.string.bunpo) + " " + binding.btnBunpoN3.getText().toString()));
        binding.btnBunpoN4.setOnClickListener(v -> onSelected(Const.MENU_BUNPO_N4, getString(R.string.bunpo) + " " + binding.btnBunpoN4.getText().toString()));
        binding.btnBunpoN5.setOnClickListener(v -> onSelected(Const.MENU_BUNPO_N5, getString(R.string.bunpo) + " " + binding.btnBunpoN5.getText().toString()));

        initAd();
    }

    private void initAd() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
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
    }

    private void onSelected(String type, String text) {
        Intent intent = new Intent();
        intent.putExtra("MENU_TYPE", type);
        intent.putExtra("MENU_NAME", text);
        setResult(RESULT_OK, intent);
        finish();
    }
}