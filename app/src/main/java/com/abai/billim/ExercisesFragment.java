package com.abai.billim;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExercisesFragment extends Fragment {
    private RecyclerView RV;
    private ExercicesAdapter adapter;
    private List<ExercisesShortItem> item = new ArrayList<>();
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_exercises, container, false);

        boolean errorOccured = false;
        for (int i = 0; !errorOccured; i++) {
            ExerciseReader jsonReader = new ExerciseReader(getContext(), "exercise" + i + ".json", ((Main) getContext()).getLanguage());
            if (jsonReader.readJsonQuestion() == null)
                errorOccured = true;
            else
                item.add(jsonReader.readJsonQuestion());
        }
        errorOccured = false;
        for (int i = 0; !errorOccured; i++) {
            String id;
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
            id = idHex.toString();
            if (getContext().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString(id, null) != null) {
                ExerciseReader jsonReader = new ExerciseReader(getContext(), getContext().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString(id, null) + ".json", ((Main) getContext()).getLanguage());
                if (jsonReader.readJsonQuestionCustom() != null)
                    item.add(jsonReader.readJsonQuestionCustom());
                else
                    errorOccured = true;
            } else
                break;

        }
        RV = root.findViewById(R.id.RV);
        Map<String, Integer> completed = new HashMap<>();
        for (int i = 0; i < ((Main) getContext()).getUser().getItems().size(); i++) {
            completed.put(((Main) getContext()).getUser().getItems().get(i).id, ((Main) getContext()).getUser().getItems().get(i).completed);
        }
        adapter = new ExercicesAdapter(getContext(), item, completed, ((Main) getContext()).getAnsweredQuestion());
        RV.setLayoutManager(new LinearLayoutManager(getContext()));
        RV.setAdapter(adapter);
        adapter.setOnExerciseClickListener(new ExercicesAdapter.OnExerciseClickListener() {
            @Override
            public void OnExerciseClick(int position) {
                ((Main) getContext()).setChosenId(item.get(position).getId());
                ((Main) getContext()).setPosition(position);
                ((Main) getContext()).replaceFragment(new ExplanationFragment());
            }
        });
        return root;
    }
}