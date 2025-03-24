package com.abai.billim;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

public class ExplanationFragment extends Fragment {
    private TextView explanation;
    private ExerciseReader jsonReader;
    private ExercisesItem item = null;
    private Button goToQuestions;
    private View root;
    private TextView id, theme;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_explanation, container, false);
        explanation = root.findViewById(R.id.explanation);
        goToQuestions = root.findViewById(R.id.goToQuestions);
        id = root.findViewById(R.id.id);
        theme = root.findViewById(R.id.theme);
        jsonReader = new ExerciseReader(getContext(), ((Main) getContext()).getChosenId() + ".json", ((Main) getContext()).getLanguage());
        try {
            item = jsonReader.readJson();
            id.setText(((Main) getContext()).getPosition() + 1 + " сабақ");
            if (item == null) {
                item = jsonReader.readJsonCustom();
            }
        } catch (Exception e) {
            item = jsonReader.readJsonCustom();
            id.setText(((Main) getContext()).getPosition() + 1 + " сабақ");
        }
        if (item != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            for (int i = 0; i < item.explanation.size(); i++) {
                SpannableString temp = new SpannableString(item.explanation.get(i).text + "\n");
                if (item.explanation.get(i).isBold) {
                    temp.setSpan(new CustomTypefaceSpan(R.font.noto_sans_bold, getContext()), 0, temp.length(), 0);
                } else {
                    temp.setSpan(new CustomTypefaceSpan(R.font.noto_sans_regular, getContext()), 0, temp.length(), 0);
                }
                temp.setSpan(new RelativeSizeSpan(item.explanation.get(i).size), 0, temp.length(), 0);
                temp.setSpan(new ForegroundColorSpan(Color.BLACK), 0, temp.length(), 0);
                builder.append(temp);
            }
            explanation.setText(builder, TextView.BufferType.SPANNABLE);
            goToQuestions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Main) getContext()).replaceFragment(new QuestionsFragment());
                }
            });
        }
        theme.setText(item.theme);
        return root;
    }
}