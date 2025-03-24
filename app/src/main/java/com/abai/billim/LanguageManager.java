package com.abai.billim;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context context;

    public LanguageManager(Context context) {
        this.context = context;
    }
    public void updateRes(String lang) {
        context.getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).edit().putString("language", lang).apply();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
