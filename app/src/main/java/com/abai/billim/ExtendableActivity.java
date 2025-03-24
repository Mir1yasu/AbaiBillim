package com.abai.billim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class ExtendableActivity extends AppCompatActivity {
    private BurgerMenu burger;
    private Button menu;
    private LanguageManager languageManager;
    private BottomSheetDialog dialog;

    @Override
    protected void onResume() {
        super.onResume();
        loadLocale();
        load();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageManager = new LanguageManager(getBaseContext());
        languageManager.updateRes(getSharedPreferences("My_Prefs", MODE_PRIVATE).getString("language", "kk"));
    }
    public void loadLocale() {
        changeLang(getSharedPreferences("My_Prefs", MODE_PRIVATE).getString("language", "kk"));
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
    }
    public void load() {
        burger = findViewById(R.id.burger);
        menu = findViewById(R.id.menu);
        burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        burger.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burger.animatorClose();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burger.animatorOpen();
            }
        });


    }
    public String getLanguage() {
        return getSharedPreferences("My_Prefs", MODE_PRIVATE).getString("language", "kk");
    }
//    private void openDialog() {
//        languageManager = new LanguageManager(getBaseContext());
//        dialog = new BottomSheetDialog(ExtendableActivity.this);
//        dialog.setContentView(R.layout.view_settings);
//        CheckBox kk_box = dialog.findViewById(R.id.kk_box), ru_box = dialog.findViewById(R.id.ru_box);
//        LinearLayout kk = dialog.findViewById(R.id.kk), ru = dialog.findViewById(R.id.ru);
//        switch (getBaseContext().getSharedPreferences("My_Prefs", MODE_PRIVATE).getString("language", "kk")) {
//            case "kk":
//                kk_box.setChecked(true);
//                break;
//            case "ru":
//                ru_box.setChecked(true);
//                break;
//            default:
//                kk_box.setChecked(true);
//                languageManager.updateRes("kk");
//        }
//        kk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!kk_box.isChecked()) {
//                    ru_box.setChecked(false);
//                    kk_box.setChecked(true);
//                    getSharedPreferences("My_Prefs", MODE_PRIVATE).edit().putString("language", "kk").commit();
//                    languageManager.updateRes("kk");
//                    dialog.dismiss();
//                    recreate();
//                }
//            }
//        });
//
//        ru.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!ru_box.isChecked()) {
//                    ru_box.setChecked(true);
//                    kk_box.setChecked(false);
//                    getSharedPreferences("My_Prefs", MODE_PRIVATE).edit().putString("language", "kk").commit();
//                    languageManager.updateRes("ru");
//                    dialog.dismiss();
//                    recreate();
//                }
//            }
//        });
//        dialog.show();
//    }
}
