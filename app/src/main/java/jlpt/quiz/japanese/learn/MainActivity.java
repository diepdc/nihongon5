package jlpt.quiz.japanese.learn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jlpt.quiz.japanese.learn.databinding.ActivityMainBinding;
import jlpt.quiz.japanese.learn.dto.DataDTO;
import jlpt.quiz.japanese.learn.util.Const;
import jlpt.quiz.japanese.learn.util.Utils;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.drawerLayout.setOnClickListener(v -> Utils.hideKeyboard(MainActivity.this));
        binding.tvChoose.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            mainActivityResultLauncher.launch(intent);
        });
        binding.rlPlay.setOnClickListener(v -> {
            noOfrndmElem = Integer.parseInt(binding.etSL.getText().toString().toString());
            if (noOfrndmElem > total) {
                noOfrndmElem = total;
            }
            if (questionList_Test != null && questionList_Test.size() > 0) {
                if (questionList_Test.size() == noOfrndmElem) {
                    Collections.shuffle(questionList_Test);
                } else {
                    questionList_Test = removeIndex(questionList_Test);
                }
                Intent intent = new Intent(this, PlayActivity.class);
                startActivity(intent);
            } else {
                doLoadData(Const.MENU_FREE_FULL);
            }
        });
        initAd();
        doLoadData(Const.MENU_FREE_FULL);
    }

    int noOfrndmElem = 0;

    private ArrayList<DataDTO> removeIndex(ArrayList<DataDTO> araylist) {
        Random rndm = new Random();
        if (araylist.size() <= noOfrndmElem) {
            return araylist;
        } else {
            int rndmIndx = rndm.nextInt(araylist.size());
            araylist.remove(rndmIndx);
            removeIndex(araylist);
        }
        return araylist;
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

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> mainActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            String type = data.getStringExtra("MENU_TYPE");
            if (Utils.isEmptyString(type)) {
                type = Const.MENU_FREE_FULL;
            }
            String text = data.getStringExtra("MENU_NAME");
            binding.tvChoose.setText(text);
            doLoadData(type);
        }
    });

    public static ArrayList<DataDTO> n3bunpou_List = new ArrayList<>();
    public static ArrayList<DataDTO> n3kanji_List = new ArrayList<>();

    public static ArrayList<DataDTO> n4bunpou_List = new ArrayList<>();
    public static ArrayList<DataDTO> n4kanji_List = new ArrayList<>();

    public static ArrayList<DataDTO> n5bunpou_List = new ArrayList<>();
    public static ArrayList<DataDTO> n5kanji_List = new ArrayList<>();

    //dung de test
    public static ArrayList<DataDTO> questionList_Test = new ArrayList<>();
    private int total = 0;

    private ArrayList<DataDTO> getList(String _type) {
        questionList_Test = new ArrayList<>();
        //tu do
        if (Const.MENU_FREE_FULL.equals(_type)) {
            n3bunpou_List = Utils.getAssetJsonData(this, "n3bunpou.js");
            n3kanji_List = Utils.getAssetJsonData(this, "n3kanji.js");

            n4bunpou_List = Utils.getAssetJsonData(this, "n4bunpou.js");
            n4kanji_List = Utils.getAssetJsonData(this, "n4kanji.js");

            n5bunpou_List = Utils.getAssetJsonData(this, "n5bunpou.js");
            n5kanji_List = Utils.getAssetJsonData(this, "n5kanji.js");
        } else if (Const.MENU_FREE_N3.equals(_type)) {
            n3bunpou_List = Utils.getAssetJsonData(this, "n3bunpou.js");
            n3kanji_List = Utils.getAssetJsonData(this, "n3kanji.js");
        } else if (Const.MENU_FREE_N4.equals(_type)) {
            n4bunpou_List = Utils.getAssetJsonData(this, "n4bunpou.js");
            n4kanji_List = Utils.getAssetJsonData(this, "n4kanji.js");
        } else if (Const.MENU_FREE_N5.equals(_type)) {
            n5bunpou_List = Utils.getAssetJsonData(this, "n5bunpou.js");
            n5kanji_List = Utils.getAssetJsonData(this, "n5kanji.js");
        }
        // kanji
        if (Const.MENU_KANJI_FULL.equals(_type)) {
            n3kanji_List = Utils.getAssetJsonData(this, "n3kanji.js");
            n4kanji_List = Utils.getAssetJsonData(this, "n4kanji.js");
            n5kanji_List = Utils.getAssetJsonData(this, "n5kanji.js");
        } else if (Const.MENU_KANJI_N3.equals(_type)) {
            n3kanji_List = Utils.getAssetJsonData(this, "n3kanji.js");
        } else if (Const.MENU_KANJI_N4.equals(_type)) {
            n4kanji_List = Utils.getAssetJsonData(this, "n4kanji.js");
        } else if (Const.MENU_KANJI_N5.equals(_type)) {
            n5kanji_List = Utils.getAssetJsonData(this, "n5kanji.js");
        }
        // ngu phap
        if (Const.MENU_BUNPO_FULL.equals(_type)) {
            n3bunpou_List = Utils.getAssetJsonData(this, "n3bunpou.js");
            n4bunpou_List = Utils.getAssetJsonData(this, "n4bunpou.js");
            n5bunpou_List = Utils.getAssetJsonData(this, "n5bunpou.js");
        } else if (Const.MENU_BUNPO_N3.equals(_type)) {
            n3bunpou_List = Utils.getAssetJsonData(this, "n3bunpou.js");
        } else if (Const.MENU_BUNPO_N4.equals(_type)) {
            n4bunpou_List = Utils.getAssetJsonData(this, "n4bunpou.js");
        } else if (Const.MENU_BUNPO_N5.equals(_type)) {
            n5bunpou_List = Utils.getAssetJsonData(this, "n5bunpou.js");
        }
        //=======================================================
        //tu do
        if (Const.MENU_FREE_FULL.equals(_type)) {
            if (n3bunpou_List != null && n3bunpou_List.size() > 0)
                questionList_Test.addAll(n3bunpou_List);
            if (n3kanji_List != null && n3kanji_List.size() > 0)
                questionList_Test.addAll(n3kanji_List);
            if (n4bunpou_List != null && n4bunpou_List.size() > 0)
                questionList_Test.addAll(n4bunpou_List);
            if (n4kanji_List != null && n4kanji_List.size() > 0)
                questionList_Test.addAll(n4kanji_List);
            if (n5bunpou_List != null && n5bunpou_List.size() > 0)
                questionList_Test.addAll(n5bunpou_List);
            if (n5kanji_List != null && n5kanji_List.size() > 0)
                questionList_Test.addAll(n5kanji_List);
        } else if (Const.MENU_FREE_N3.equals(_type)) {
            if (n3bunpou_List != null && n3bunpou_List.size() > 0)
                questionList_Test.addAll(n3bunpou_List);
            if (n3kanji_List != null && n3kanji_List.size() > 0)
                questionList_Test.addAll(n3kanji_List);
        } else if (Const.MENU_FREE_N4.equals(_type)) {
            if (n4bunpou_List != null && n4bunpou_List.size() > 0)
                questionList_Test.addAll(n4bunpou_List);
            if (n4kanji_List != null && n4kanji_List.size() > 0)
                questionList_Test.addAll(n4kanji_List);
        } else if (Const.MENU_FREE_N5.equals(_type)) {
            if (n5bunpou_List != null && n5bunpou_List.size() > 0)
                questionList_Test.addAll(n5bunpou_List);
            if (n5kanji_List != null && n5kanji_List.size() > 0)
                questionList_Test.addAll(n5kanji_List);
        }

        // kanji
        if (Const.MENU_KANJI_FULL.equals(_type)) {
            if (n3kanji_List != null && n3kanji_List.size() > 0)
                questionList_Test.addAll(n3kanji_List);
            if (n4kanji_List != null && n4kanji_List.size() > 0)
                questionList_Test.addAll(n4kanji_List);
            if (n5kanji_List != null && n5kanji_List.size() > 0)
                questionList_Test.addAll(n5kanji_List);
        } else if (Const.MENU_KANJI_N3.equals(_type)) {
            if (n3kanji_List != null && n3kanji_List.size() > 0)
                questionList_Test.addAll(n3kanji_List);
        } else if (Const.MENU_KANJI_N4.equals(_type)) {
            if (n4kanji_List != null && n4kanji_List.size() > 0)
                questionList_Test.addAll(n4kanji_List);
        } else if (Const.MENU_KANJI_N5.equals(_type)) {
            if (n5kanji_List != null && n5kanji_List.size() > 0)
                questionList_Test.addAll(n5kanji_List);
        }

        // ngu phap
        if (Const.MENU_BUNPO_FULL.equals(_type)) {
            if (n3bunpou_List != null && n3bunpou_List.size() > 0)
                questionList_Test.addAll(n3bunpou_List);
            if (n4bunpou_List != null && n4bunpou_List.size() > 0)
                questionList_Test.addAll(n4bunpou_List);
            if (n5bunpou_List != null && n5bunpou_List.size() > 0)
                questionList_Test.addAll(n5bunpou_List);
        } else if (Const.MENU_BUNPO_N3.equals(_type)) {
            if (n3bunpou_List != null && n3bunpou_List.size() > 0)
                questionList_Test.addAll(n3bunpou_List);
        } else if (Const.MENU_BUNPO_N4.equals(_type)) {
            if (n4bunpou_List != null && n4bunpou_List.size() > 0)
                questionList_Test.addAll(n4bunpou_List);
        } else if (Const.MENU_BUNPO_N5.equals(_type)) {
            if (n5bunpou_List != null && n5bunpou_List.size() > 0)
                questionList_Test.addAll(n5bunpou_List);
        }
        return questionList_Test;
    }

    private void doLoadData(String type) {
        System.out.println("@@@@@@@@ doLoadData " + type);
        new LoadTask(this, type).execute();
    }

    public class LoadTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog pr;
        Activity activity;
        String type;

        public LoadTask(Activity ac, String t) {
            activity = ac;
            type = t;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pr = new ProgressDialog(activity);
            pr.setMessage("ロード中");
            pr.setCanceledOnTouchOutside(false);
            pr.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            getList(type);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                total = questionList_Test.size();
                binding.etSL.setText("" + total);

                pr.dismiss();
                System.out.println("@@@@@@@@ loaddata onPostExecute total " + total);
            } catch (Exception e) {
                System.out.println("@@@@@@@@ loaddata " + e.toString());
            }
        }
    }
}