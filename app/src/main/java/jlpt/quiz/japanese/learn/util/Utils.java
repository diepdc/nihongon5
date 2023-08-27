package jlpt.quiz.japanese.learn.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jlpt.quiz.japanese.learn.dto.DataDTO;


public class Utils {
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

    public static ArrayList<DataDTO> getAssetJsonData(Context context, String fileName) {
        String json = null;
        ArrayList<DataDTO> list = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            StringBuilder validJson = new StringBuilder();

            String[] lineByLine = json.split(",");
            for (int i = 0; i < lineByLine.length; i++) {
                String[] lineSplit = lineByLine[i].split(":");
                try {
                    Integer.parseInt(lineSplit[1].trim()); //check la dap an dung moi xu ly
                    validJson
                            .append('"')
                            .append(lineSplit[0].trim()).append('"')
                            .append(":")
                            .append(lineSplit[1].trim());
                } catch (Exception e) {
                    validJson.append(lineByLine[i]).append(",");
                }
            }
            String finishedJSON = validJson.toString();
            finishedJSON = finishedJSON.replace("null", "");
            int index = finishedJSON.lastIndexOf(",");
            if (index > 0) {
                finishedJSON = finishedJSON.substring(0, index);
            }
            JSONArray jarray = new JSONArray(finishedJSON);
            for (int i = 0; i < jarray.length(); i++) {
                try {
                    if (jarray != null && jarray.length() > 0 && jarray.get(i) != null) {
                        JSONObject jb = (JSONObject) jarray.get(i);
                        DataDTO item = new DataDTO();
                        if (jb.has("id")) {
                            item.id = jb.getString("id");//: "I0402",
                        }
                        if (jb.has("question")) {
                            item.question = jb.getString("question");//: "しけんがありますから、５(じかん)もべんきょうしました。",
                        }
                        if (jb.has("choice1")) {
                            item.choice1 = jb.getString("choice1");//: "待間",
                        }
                        if (jb.has("choice2")) {
                            item.choice2 = jb.getString("choice2");//: "待問",
                        }
                        if (jb.has("choice3")) {
                            item.choice3 = jb.getString("choice3");//: "時間",
                        }
                        if (jb.has("choice4")) {
                            item.choice4 = jb.getString("choice4");//: "時間",
                        }
                        if (jb.has("answer")) {
                            item.answer = jb.getInt("answer");//: 3,
                        }
                        list.add(item);
                    }
                } catch (Exception e) {
                    System.out.println(i + "   ==== list " + list.size());
                }
            }
        } catch (JSONException e1) {
            System.out.println("@@@@@ exception1 fileName " + fileName);
            e1.printStackTrace();
            return null;
        } catch (IOException e2) {
            System.out.println("@@@@@ exception2 fileName " + fileName);
            e2.printStackTrace();
            return null;
        } catch (Exception ex) {
            System.out.println("@@@@@ exception3 fileName " + fileName);
            ex.printStackTrace();
            return null;
        }
        return list;

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
