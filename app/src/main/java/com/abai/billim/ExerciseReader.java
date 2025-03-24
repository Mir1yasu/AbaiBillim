package com.abai.billim;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExerciseReader {
    String file;
    String language;
    String json;
    Context context;

    public ExerciseReader(Context context, String file, String language) {
        this.context = context;
        this.file = file;
        this.language = language;
    }
    public ExercisesItem readJson() {
        try (InputStream is = context.getAssets().open(file)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(json).getJSONObject(language);
            } catch (NullPointerException e) {
                try {
                    jsonObject = new JSONObject(json).getJSONObject("kk");
                } catch (NullPointerException ek) {
                    jsonObject = new JSONObject(json).getJSONObject("ru");
                }
            }
            JSONObject jsonObject1 = jsonObject.getJSONObject("exerciseQuestion");
            List<QuestionItem> items = new ArrayList<>();
            List<ExplanationStringItem> itemList = new ArrayList<>();
            int length = jsonObject1.getJSONArray("questions").length();
            for (int i = 0; i < length; i++) {
                List<String> temp = new ArrayList<>();
                for (int j = 0; j < jsonObject1.getJSONArray("answers").getJSONArray(i).length(); j++) {
                    temp.add(jsonObject1.getJSONArray("answers").getJSONArray(i).getString(j));
                }
                List<String> correctTemp = new ArrayList<>();
                for (int j = 0; j < jsonObject1.getJSONArray("correctAnswers").getJSONArray(i).length(); j++) {
                    correctTemp.add(jsonObject1.getJSONArray("correctAnswers").getJSONArray(i).getString(j));
                }
                items.add(new QuestionItem(
                        jsonObject1.getJSONArray("questions").getString(i),
                        temp,
                        correctTemp
                ));
            }
            for (int i = 0; i < jsonObject.getJSONArray("exerciseExplanation").length(); i++) {
                itemList.add(new ExplanationStringItem(
                        jsonObject.getJSONArray("exerciseExplanation").getJSONObject(i).getString("exerciseText"),
                        jsonObject.getJSONArray("exerciseExplanation").getJSONObject(i).getInt("exerciseSize"),
                        jsonObject.getJSONArray("exerciseExplanation").getJSONObject(i).getBoolean("exerciseIsBold")
                ));
            }
            ExercisesItem item = new ExercisesItem(
                    file.replace(".json", ""),
                    jsonObject.getString("exerciseTheme"),
                    jsonObject.getString("exerciseType"),
                    itemList,
                    items
            );
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ExercisesItem readJsonCustom() {
        File fileDir = new File(context.getFilesDir(), file);
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream is = new FileInputStream(fileDir);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(json).getJSONObject(language);
            } catch (NullPointerException e) {
                try {
                    jsonObject = new JSONObject(json).getJSONObject("kk");
                } catch (NullPointerException ek) {
                    jsonObject = new JSONObject(json).getJSONObject("ru");
                }
            }
            JSONObject jsonObject1 = jsonObject.getJSONObject("exerciseQuestion");
            System.out.println(jsonObject1);
            List<QuestionItem> items = new ArrayList<>();
            List<ExplanationStringItem> itemList = new ArrayList<>();
            int length = jsonObject1.getJSONArray("questions").length();
            for (int i = 0; i < length; i++) {
                List<String> temp = new ArrayList<>();
                for (int j = 0; j < jsonObject1.getJSONArray("answers").getJSONArray(i).length(); j++) {
                    temp.add(jsonObject1.getJSONArray("answers").getJSONArray(i).getString(j));
                }
                List<String> correctTemp = new ArrayList<>();
                for (int j = 0; j < jsonObject1.getJSONArray("correctAnswers").getJSONArray(i).length(); j++) {
                    correctTemp.add(jsonObject1.getJSONArray("correctAnswers").getJSONArray(i).getString(j));
                }
                items.add(new QuestionItem(
                        jsonObject1.getJSONArray("questions").getString(i),
                        temp,
                        correctTemp
                ));
            }
            for (int i = 0; i < jsonObject.getJSONArray("exerciseExplanation").length(); i++) {
                itemList.add(new ExplanationStringItem(
                        jsonObject.getJSONArray("exerciseExplanation").getJSONObject(i).getString("exerciseText"),
                        jsonObject.getJSONArray("exerciseExplanation").getJSONObject(i).getInt("exerciseSize"),
                        jsonObject.getJSONArray("exerciseExplanation").getJSONObject(i).getBoolean("exerciseIsBold")
                ));
            }
            ExercisesItem item = new ExercisesItem(
                    jsonObject.getString("exerciseId"),
                    jsonObject.getString("exerciseTheme"),
                    jsonObject.getString("exerciseType"),
                    itemList,
                    items
            );
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ExercisesShortItem readJsonQuestion() {
        try (InputStream is = context.getAssets().open(file)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer);
            JSONObject jsonObject = new JSONObject(json);
            String exerciseLesson;
            if (jsonObject.getString("exerciseLessonNumber").isEmpty()) {
                exerciseLesson = "Кастомное задание";
            } else {
                exerciseLesson = jsonObject.getString("exerciseLessonNumber");
            }
            ExercisesShortItem temp = new ExercisesShortItem(
                    exerciseLesson,
                    jsonObject.getString("exerciseId"),
                    jsonObject.getString("exerciseTheme"),
                    jsonObject.getInt("exerciseAmount")
            );
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ExercisesShortItem readJsonQuestionCustom() {
        File fileDir = new File(context.getFilesDir(), file);
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream is = new FileInputStream(fileDir);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(json);
            String exerciseLesson;
            try {
                exerciseLesson = jsonObject.getString("exerciseLessonNumber");
            } catch (JSONException e){
                exerciseLesson = "Кастомное задание";
            }
            ExercisesShortItem temp = new ExercisesShortItem(
                    exerciseLesson,
                    jsonObject.getString("exerciseId"),
                    jsonObject.getString("exerciseTheme"),
                    jsonObject.getInt("exerciseAmount")
            );
            return temp;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
