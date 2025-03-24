package com.abai.billim;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private Button startButton;
    private ImageView banner0, banner1;
    private TextView grammar, alphabet;
    private int[] imageId = {R.drawable.person0, R.drawable.person1, R.drawable.person2};
    private int currentBanner = 0;
    private ObjectAnimator animator0, animator1;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            animator0.start();
            animator1.start();
            banner0.setImageResource(imageId[currentBanner]);
            banner1.setImageResource(imageId[currentBanner == 2 ? 0 : currentBanner + 1]);
            switch (currentBanner) {
                case 2:
                    currentBanner = 0;
                    break;
                default:
                    currentBanner++;
                    break;
            }
            handler.postDelayed(this, 3000);
        }
    };

    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_home, container, false);
        startButton = root.findViewById(R.id.startButton);
        banner0 = root.findViewById(R.id.banner0);
        banner1 = root.findViewById(R.id.banner1);
        grammar = root.findViewById(R.id.grammar);
        alphabet = root.findViewById(R.id.alphabet);
        int gram = 0, alph = 0, gramMax = 0, alphMax = 0;
        for (int i = 0; i < ((Main) getContext()).getUser().items.size(); i++) {
            gram += ((Main) getContext()).getUser().items.get(i).correctAt.size();
            alph += ((Main) getContext()).getUser().items.get(i).correctAt.size();
        }
        grammar.setText(gram + "/" + 30);
        alphabet.setText(0 + "/" + 33);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main) getContext()).replaceFragment(new ExercisesFragment());
            }
        });
        animator0 = ObjectAnimator.ofFloat(banner0, "alpha", currentBanner % 2 != 0 ? 0 : 1, currentBanner % 2 != 0 ? 1 : 0);
        animator1 = ObjectAnimator.ofFloat(banner1, "alpha", currentBanner % 2 == 0 ? 0 : 1, currentBanner % 2 == 0 ? 1 : 0);
        animator0.setInterpolator(new AccelerateDecelerateInterpolator());
        animator1.setInterpolator(new AccelerateDecelerateInterpolator());
        animator0.setDuration(1200);
        animator1.setDuration(1200);
        handler.postDelayed(runnable, 5000);
        return root;
    }
}