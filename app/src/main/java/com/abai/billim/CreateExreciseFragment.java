package com.abai.billim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

public class CreateExreciseFragment extends Fragment {

    private CreateExerciseAnswerAdapter adapter;
    private SpannableStringBuilder builderKk = new SpannableStringBuilder(), builderRu = new SpannableStringBuilder();
    private SpannableString spannableString;
    private EditText theoryRu, theoryKk, setSize, questionKk, questionRu, themeKk, themeRu;
    private Button setBold, isSecondLanguage, changeLanguage, addTheory, addQuestion, previewExercise, submitExercise;
    private ImageButton back, forward;
    private boolean isSecond = false, isKk = true, isBold = false, isInit = false;
    private boolean[] isAnimationPlaying = {false};
    private int currentPage = 0;
    private String brown = "#3E0005", white = "#FFFFFF", id;
    private Map<Integer, Map<Integer, String>> questionsAnswersKk = new HashMap<>();
    private Map<Integer, Map<Integer, String>> questionsAnswersRu = new HashMap<>();
    private Map<Integer, List<String>> correctAnswersKk = new HashMap<>();
    private Map<Integer, List<String>> correctAnswersRu = new HashMap<>();
    private Map<Integer, String> questionsQuestionKk = new HashMap<>();
    private Map<Integer, String> questionsQuestionRu = new HashMap<>();
    private List<String> explanationTextKk = new ArrayList<>();
    private List<String> explanationTextRu = new ArrayList<>();
    private List<Float> explanationSizeKk = new ArrayList<>();
    private List<Float> explanationSizeRu = new ArrayList<>();
    private List<Boolean> explanationIsBoldKk = new ArrayList<>();
    private List<Boolean> explanationIsBoldRu = new ArrayList<>();
    private Map<Integer, Integer> size = new HashMap<>();
    private float textSize = 11f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_exrecise, container, false);
        theoryRu = root.findViewById(R.id.theoryMaterialRu);
        theoryKk = root.findViewById(R.id.theoryMaterialKk);
        size.put(0, 3);
        setSize = root.findViewById(R.id.setSize);
        Map<Integer, String> temp = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            temp.put(i, "");
        }
        questionsQuestionKk.put(0, "");
        questionsQuestionRu.put(0, "");
        questionsAnswersKk.put(0, temp);
        questionsAnswersRu.put(0, temp);
        theoryRu.setTypeface(ResourcesCompat.getFont(getContext(), isBold ? R.font.noto_sans_bold : R.font.noto_sans_regular));
        theoryKk.setTypeface(ResourcesCompat.getFont(getContext(), isBold ? R.font.noto_sans_bold : R.font.noto_sans_regular));
        initRecycler(root);
        initButtons(root);
        initListeners();

        return root;
    }
    private void initListeners() {
        setSize.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String sEdit = setSize.getText().toString();

                    if (!sEdit.isEmpty()) {
                        try {
                            int size = Integer.parseInt(sEdit);
                            if (size >= 5 && size <= 50) {
                                textSize = (float) size;
                                theoryRu.setTextSize(textSize);
                                theoryKk.setTextSize(textSize);
                            } else {
                                Toast.makeText(getContext(), "Пожалуйста, введите значение в пределах 5-50", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Пожалуйста, введите численное значение", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        setBold.setOnClickListener(v -> {
            if (isBold) {
                setBold.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(brown)));
                setBold.setTextColor(Color.parseColor(white));
            } else {
                setBold.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(white)));
                setBold.setTextColor(Color.parseColor(brown));
            }
            isBold = !isBold;
            theoryRu.setTypeface(ResourcesCompat.getFont(getContext(), isBold ? R.font.noto_sans_bold : R.font.noto_sans_regular));
            theoryKk.setTypeface(ResourcesCompat.getFont(getContext(), isBold ? R.font.noto_sans_bold : R.font.noto_sans_regular));
        });
        addQuestion.setOnClickListener(v->{
            Map<Integer, String> temp = new HashMap<>();
            List<String> temp1 = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                temp.put(i, "");
                temp1.add(null);
            }
            questionsAnswersKk.put(questionsAnswersKk.size(), temp);
            questionsAnswersRu.put(questionsAnswersRu.size(), temp);
            questionsQuestionKk.put(size.size(), "");
            questionsQuestionKk.put(size.size(), "");
            correctAnswersKk.put(size.size(), temp1);
            correctAnswersRu.put(size.size(), temp1);
            size.put(size.size(), 3);
            questionsAnswersKk.put(currentPage, adapter.getItemsKk());
            questionsAnswersRu.put(currentPage, adapter.getItemsRu());
        });
        isSecondLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAnimationPlaying[0]) {
                    isSecond = !isSecond;

                    while (questionsAnswersKk.size() > questionsAnswersRu.size()) {
                        Map<Integer, String> temp = questionsAnswersKk.get(questionsAnswersRu.size());
                        for (int i = 0; i < temp.size(); i++) {
                            questionsAnswersRu.put(i, temp);
                        }
                    }
                    while (questionsAnswersKk.size() < questionsAnswersRu.size()) {
                        questionsAnswersRu.remove(questionsAnswersKk.size());
                    }
                    adapter.setSecond(isSecond);
                    if (!isKk) {
                        ((Main) getContext()).startAnimation(theoryKk, theoryRu, isAnimationPlaying);
                        isAnimationPlaying[0] = false;
                        ((Main) getContext()).startAnimation(questionKk, questionRu, isAnimationPlaying);

                    }
                    if (isSecond) {
                        questionRu.setVisibility(View.VISIBLE);
                        isSecondLanguage.setText("KZ|RU");
                        isAnimationPlaying[0] = false;
                    } else {
                        questionRu.setVisibility(View.GONE);
                        isSecondLanguage.setText("KZ");
                        isAnimationPlaying[0] = false;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isSecond) {
                                theoryRu.setVisibility(View.VISIBLE);
                                themeRu.setVisibility(View.VISIBLE);
                                changeLanguage.setVisibility(View.VISIBLE);
                            } else {
                                theoryRu.setVisibility(View.GONE);
                                themeRu.setVisibility(View.GONE);
                                changeLanguage.setVisibility(View.GONE);
                                isKk = true;
                            }
                        }
                    }, !isKk ? 500 : 0);
                }
            }
        });

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size.put(currentPage, adapter.getItemCount());
                questionsAnswersKk.put(currentPage, adapter.getItemsKk());
                questionsAnswersRu.put(currentPage, adapter.getItemsRu());
                correctAnswersKk.put(currentPage, adapter.getCorrectAnswerKk());
                correctAnswersRu.put(currentPage, adapter.getCorrectAnswerRu());

                if (!isInit) {
                    ((Main) getContext()).setXY(theoryRu.getX(), theoryRu.getY(), theoryKk.getX(), theoryKk.getY());
                    isInit = true;
                }
                if (!isAnimationPlaying[0]) {
                    if (isKk) {
                        changeLanguage.setText("RU");
                        changeLanguage.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 255, 255)));
                        changeLanguage.setTextColor(Color.rgb(62, 0, 5));
                        ((Main) getContext()).startAnimation(theoryRu, theoryKk, isAnimationPlaying);
                        isAnimationPlaying[0] = false;
                        ((Main) getContext()).startAnimation(questionRu, questionKk, isAnimationPlaying);
                        isAnimationPlaying[0] = false;
                        ((Main) getContext()).startAnimation(questionRu, questionKk, isAnimationPlaying);
                        isAnimationPlaying[0] = false;
                        ((Main) getContext()).startAnimation(themeRu, themeKk, isAnimationPlaying);
                        adapter.setAnimation(isKk, isSecond);
                    } else {
                        changeLanguage.setText("KZ");
                        changeLanguage.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(62, 0, 5)));
                        changeLanguage.setTextColor(Color.rgb(255, 255, 255));

                        ((Main) getContext()).startAnimation(theoryKk, theoryRu, isAnimationPlaying);
                        isAnimationPlaying[0] = false;
                        ((Main) getContext()).startAnimation(questionKk, questionRu, isAnimationPlaying);
                        isAnimationPlaying[0] = false;
                        ((Main) getContext()).startAnimation(questionKk, questionRu, isAnimationPlaying);
                        isAnimationPlaying[0] = false;
                        ((Main) getContext()).startAnimation(themeKk, themeRu, isAnimationPlaying);
                        adapter.setAnimation(isKk, isSecond);
                    }
                    isKk = !isKk;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        addTheory.setOnClickListener(v-> {
            if (isKk) {
                addTheoryMethod(theoryKk, explanationTextKk, explanationSizeKk, explanationIsBoldKk, builderKk, true);
            } else {
                addTheoryMethod(theoryRu, explanationTextRu, explanationSizeRu, explanationIsBoldRu, builderRu, false);
            }
        });
        previewExercise.setOnClickListener(v->{
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.create_exercise_preview_dialog);
            TextView adminPreviewText = dialog.findViewById(R.id.adminPreviewText);
            adminPreviewText.setText(isKk ? builderKk : builderRu, TextView.BufferType.SPANNABLE);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.show();
        });
        submitExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCorrect = false;
                Map<Integer, String> hashKk = new HashMap<>();
                Map<Integer, String> hashRu = new HashMap<>();
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    hashKk.put(i, adapter.getItemsKk().get(i));
                    hashRu.put(i, adapter.getItemsRu().get(i));
                }
                questionsQuestionKk.put(currentPage, questionKk.getText().toString());
                questionsQuestionRu.put(currentPage, questionRu.getText().toString());
                questionsAnswersKk.put(currentPage, hashKk);
                questionsAnswersRu.put(currentPage, hashRu);
                correctAnswersKk.put(currentPage, adapter.getCorrectAnswerKk());
                correctAnswersRu.put(currentPage, adapter.getCorrectAnswerKk());
                isCorrect = checkIfCorrect(explanationTextKk, questionsAnswersKk, correctAnswersKk, questionsQuestionKk, themeKk);
                if (isSecond) {
                    isCorrect = isCorrect && checkIfCorrect(explanationTextRu, questionsAnswersRu, correctAnswersRu, questionsQuestionRu, themeRu);
                }
                if (isCorrect) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if (isSecond) {
                            jsonObject
                                    .put("kk", save(questionsQuestionKk, questionsAnswersKk, correctAnswersKk, explanationTextKk, explanationSizeKk, explanationIsBoldKk, themeKk,"kk"))
                                    .put("ru", save(questionsQuestionRu, questionsAnswersRu, correctAnswersRu, explanationTextRu, explanationSizeRu, explanationIsBoldRu, themeRu, "ru"));
                        } else {
                            jsonObject.put("kk", save(questionsQuestionKk, questionsAnswersKk, correctAnswersKk, explanationTextKk, explanationSizeKk, explanationIsBoldKk, themeKk, "kk"));
                        }

                        File file = new File(getContext().getFilesDir(), id + ".json");
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            fos.write(jsonObject.toString().getBytes());
                        } catch (IOException e) {}
                        System.out.println(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (isSecond) {

                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 0 && !isAnimationPlaying[0]) {
                    Map<Integer, String> hashKk = new HashMap<>();
                    Map<Integer, String> hashRu = new HashMap<>();
                    List<String> listKk = new ArrayList<>();
                    List<String> listRu = new ArrayList<>();
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        hashKk.put(i, adapter.getItemsKk().get(i));
                        hashRu.put(i, adapter.getItemsRu().get(i));
                    }
                    for (int i = 0; i < adapter.getCorrectAnswerKk().size(); i++) {
                        listKk.add(adapter.getCorrectAnswerKk().get(i));
                        listRu.add(adapter.getCorrectAnswerRu().get(i));
                    }
                    questionsQuestionKk.put(currentPage, questionKk.getText().toString());
                    questionsQuestionRu.put(currentPage, questionRu.getText().toString());
                    questionsAnswersKk.put(currentPage, hashKk);
                    questionsAnswersRu.put(currentPage, hashRu);
                    correctAnswersKk.put(currentPage, listKk);
                    correctAnswersRu.put(currentPage, listRu);
                    size.put(currentPage, adapter.getItemCount());
                    currentPage--;
                    adapter.setItems(questionsAnswersKk.get(currentPage), questionsAnswersRu.get(currentPage), correctAnswersKk.get(currentPage), correctAnswersRu.get(currentPage), size.get(currentPage));
                    questionKk.setText(questionsQuestionKk.get(currentPage));
                    questionRu.setText(questionsQuestionRu.get(currentPage));
                    adapter.notifyDataSetChanged();
                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < questionsAnswersKk.size() - 1 && !isAnimationPlaying[0]) {
                    Map<Integer, String> hashKk = new HashMap<>();
                    Map<Integer, String> hashRu = new HashMap<>();
                    List<String> listKk = new ArrayList<>();
                    List<String> listRu = new ArrayList<>();
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        hashKk.put(i, adapter.getItemsKk().get(i));
                        hashRu.put(i, adapter.getItemsRu().get(i));
                    }
                    for (int i = 0; i < adapter.getCorrectAnswerKk().size(); i++) {
                        listKk.add(adapter.getCorrectAnswerKk().get(i));
                        listRu.add(adapter.getCorrectAnswerRu().get(i));
                    }
                    questionsQuestionKk.put(currentPage, questionKk.getText().toString());
                    questionsQuestionRu.put(currentPage, questionRu.getText().toString());
                    questionsAnswersKk.put(currentPage, hashKk);
                    questionsAnswersRu.put(currentPage, hashRu);
                    correctAnswersKk.put(currentPage, listKk);
                    correctAnswersRu.put(currentPage, listRu);
                    System.out.println(adapter.getCorrectAnswerKk()+"LD:ASD:LASKD");
                    size.put(currentPage, adapter.getItemCount());
                    currentPage++;
                    System.out.println(correctAnswersKk.get(currentPage) +"LASSLAL:DLK");
                    adapter.setItems(questionsAnswersKk.get(currentPage), questionsAnswersRu.get(currentPage), correctAnswersKk.get(currentPage), correctAnswersRu.get(currentPage), size.get(currentPage));
                    questionKk.setText(questionsQuestionKk.get(currentPage));
                    questionRu.setText(questionsQuestionRu.get(currentPage));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private JSONObject save(Map<Integer, String> questionsQuestion, Map<Integer, Map<Integer, String>> questionsAnswers, Map<Integer, List<String>> correctAnswer, List<String> explanationText, List<Float> explanationSize, List<Boolean> explanationIsBold, EditText theme, String save) {
        try {
            JSONObject shortJson = new JSONObject();
            JSONObject languageObject = new JSONObject();
            JSONArray exerciseExplanation = new JSONArray();
            for (int i = 0; i < explanationSize.size(); i++) {
                exerciseExplanation.put(new JSONObject()
                        .put("exerciseText", explanationText.get(i))
                        .put("exerciseSize", explanationSize.get(i))
                        .put("exerciseIsBold", explanationIsBold.get(i)));
            }
            JSONObject exerciseQuestion = new JSONObject();
            JSONArray questions = new JSONArray();
            JSONArray answers = new JSONArray();
            JSONArray correctAnswers = new JSONArray();
            for (int i = 0; i < size.size(); i++) {
                JSONArray jsonArray0 = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();
                for (int j = 0; j < questionsAnswers.get(i).size(); j++) {
                    jsonArray0.put(questionsAnswers.get(i).get(j));
                    if (correctAnswer.get(i).get(j) != null && !correctAnswer.get(i).get(j).replaceAll(" ", "").isEmpty())jsonArray1.put(correctAnswer.get(i).get(j));
                }
                answers.put(jsonArray0);
                questions.put(questionsQuestion.get(i));
                correctAnswers.put(jsonArray1);
            }
            exerciseQuestion
                    .put("questions", questions)
                    .put("answers", answers)
                    .put("correctAnswers", correctAnswers);
            StringBuilder hexString = new StringBuilder();
            if (save.equals("kk")) {
                try {
                    // Создаем экземпляр MessageDigest с выбранным алгоритмом (SHA-256)
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");

                    // Вычисляем хэш для входных данных
                    byte[] hash = digest.digest(new SimpleDateFormat("YYYY:MM:dd:HH:mm:ss:SSS").format(new Date()).getBytes());

                    // Преобразуем байтовый массив в строку шестнадцатеричного представления
                    for (byte b : hash) {
                        String hex = Integer.toHexString(0xff & b);
                        if (hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                    }
                    id = hexString.toString();
                } catch (NoSuchAlgorithmException e) {
                    return null;
                }
            }
            languageObject
                    .put("exerciseId", id)
                    .put("exerciseTheme", theme.getText().toString())
                    .put("exerciseExplanation", exerciseExplanation)
                    .put("exerciseType", "Сұрақтар және жауаптар")
                    .put("exerciseQuestion", exerciseQuestion);

            String id;
            for (int i = 0;; i++) {
                StringBuilder idHex = new StringBuilder();
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(("id" + i).getBytes());
                    for (byte b : hash) {
                        String hex = Integer.toHexString(0xff & b);
                        if (hex.length() == 1) idHex.append('0');
                        idHex.append(hex);
                    }
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("Алгоритм хэширования не найден.");
                }
                if (getContext().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString(idHex.toString(), null) == null) {
                    id = idHex.toString();
                    break;
                }
            }
            if (save.equals("kk")) {
                shortJson.put("exerciseId", hexString.toString())
                        .put("exerciseTheme", theme.getText().toString())
                        .put("exerciseAmount", answers.length());
                File file = new File(getContext().getFilesDir(), id + ".json");
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(shortJson.toString().getBytes());
                } catch (IOException e) {
                }
                getContext().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).edit().putString(id, id).apply();
            }
            return languageObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void addTheoryMethod(EditText theory, List<String> explanationText, List<Float> explanationSize, List<Boolean> explanationIsBold, SpannableStringBuilder builder, boolean isKk) {
        if (theory.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, введите теоретический материал!", Toast.LENGTH_SHORT).show();
        } else {
            explanationText.add(theory.getText().toString());
            spannableString = new SpannableString(theory.getText().toString() + "\n");
            if (isBold) {
                spannableString.setSpan(new CustomTypefaceSpan(R.font.noto_sans_bold, getContext()), 0, spannableString.length(), 0);
            } else {
                spannableString.setSpan(new CustomTypefaceSpan(R.font.noto_sans_regular, getContext()), 0, spannableString.length(), 0);
            }
            spannableString.setSpan(new RelativeSizeSpan(textSize / 10), 0, spannableString.length(), 0);
            spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
            builder.append(spannableString);
            if (isKk) {
                explanationSize.add(textSize / 10);
                explanationIsBold.add(isBold);
            }
            theory.setText("");
        }
    }
    public boolean checkIfCorrect(List<String> explanationText, Map<Integer, Map<Integer, String>> questionsAnswers, Map<Integer, List<String>> correctAnswers, Map<Integer, String> questionsQuestion, EditText theme) {
        if (explanationText.isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, добавьте теорию", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (theme.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, добавьте тему", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < size.size(); i++) {
            if (Objects.requireNonNull(questionsQuestion.get(i)).replaceAll(" ", "").isEmpty()) {
                Toast.makeText(getContext(), "Пожалуйста, заполните все поля с вопросами", Toast.LENGTH_LONG).show();
                return false;
            }
            for (int j = 0; j < questionsAnswers.get(i).size(); j++) {
                if (Objects.requireNonNull(questionsAnswers.get(i).get(j)).replaceAll(" ", "").isEmpty()) {
                    Toast.makeText(getContext(), "Пожалуйста, заполните все поля с ответами", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            boolean hasAny = false;
            for (int j = 0; j < correctAnswers.get(i).size(); j++) {
                if (correctAnswers.get(i).get(j) != null) {
                    hasAny = true;
                }
            }
            if (!hasAny) {
                Toast.makeText(getContext(), "Пожалуйста, выберите правильный ответ", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
    private void initButtons(View root) {
        addQuestion = root.findViewById(R.id.addQuestion);
        addTheory = root.findViewById(R.id.addTheory);
        setBold = root.findViewById(R.id.setBold);
        changeLanguage = root.findViewById(R.id.changeLanguage);
        previewExercise = root.findViewById(R.id.previewExercise);
        submitExercise = root.findViewById(R.id.submitExercise);
        questionKk = root.findViewById(R.id.questionCreatingQuestionKk);
        questionRu = root.findViewById(R.id.questionCreatingQuestionRu);
        isSecondLanguage = root.findViewById(R.id.isSecondLanguage);
        back = root.findViewById(R.id.backQuestion);
        forward = root.findViewById(R.id.forwardQuestion);
        themeKk = root.findViewById(R.id.themeMaterialKk);
        themeRu = root.findViewById(R.id.themeMaterialRu);
    }

    private void initRecycler(View root) {
        adapter = new CreateExerciseAnswerAdapter(getContext());

        RecyclerView questionsList = root.findViewById(R.id.questionAnswer);
        questionsList.setLayoutManager(new LinearLayoutManager(getContext()));
        questionsList.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsList);

    }

}