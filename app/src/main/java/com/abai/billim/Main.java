package com.abai.billim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main extends ExtendableActivity {
    private String chosenId;
    private UserItem user;
    private SharedPreferences sp;
    private Context context;
    private float showX, showY, hideX, hideY;
    private int position, answeredQuestion = -1;
    private List<String> jsonId = new ArrayList<>();
    private List<JSONObject> objects = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
        context = this;
        user = loadUser();
        sp = getSharedPreferences("My_Prefs", MODE_PRIVATE);
        for (int i = 0; i < sp.getInt("jsonId" + i, 0); i++) {
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
            if (getBaseContext().getSharedPreferences("My_Prefs", Context.MODE_PRIVATE).getString(idHex.toString(), null) == null) {
                break;
            } else {

            }
            try {
                objects.add(new JSONObject(loadJson(idHex.toString())));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        replaceFragment(new HomeFragment());
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }
    private UserItem loadUser() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(getFilesDir(), "user.ser")))) {
            return (UserItem) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new UserItem(null, null, null, null, new ArrayList<>(), new ArrayList<>());
        }
    }
    public void saveUser() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(), "user.ser")))){
            outputStream.writeObject(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String loadJson(String hash) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(hash + ".json")))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            return jsonContent.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public void saveJson(String hash, JSONObject object) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(hash + ".json")))) {
            writer.write(object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<JSONObject> getObjects() {
        return objects;
    }

    public UserItem getUser() {
        return user;
    }
    public String getChosenId() {
        return chosenId;
    }

    public void setChosenId(String chosenId) {
        this.chosenId = chosenId;
    }

    public void setXY(float showX, float showY, float hideX, float hideY) {
        this.showX = showX;
        this.showY = showY;
        this.hideX = hideX;
        this.hideY = hideY;
    }


    public void startAnimation(EditText show, EditText hide, boolean[] isAnimationPlaying) {
        if (!isAnimationPlaying[0]) {
            isAnimationPlaying[0] = true;
            ObjectAnimator animShowX, animShowY, animShowAlpha, animGoneX, animGoneY, animGoneAlpha;
            animShowX = ObjectAnimator.ofFloat(show, "x", showX, hideX);
            animShowY = ObjectAnimator.ofFloat(show, "y", showY, hideY);
            animShowAlpha = ObjectAnimator.ofFloat(show, View.ALPHA, 0.45f, 1f);

            animGoneX = ObjectAnimator.ofFloat(hide, "x", hideX, showX);
            animGoneY = ObjectAnimator.ofFloat(hide, "y", hideY, showY);
            animGoneAlpha = ObjectAnimator.ofFloat(hide, "alpha", 1f, 0.45f);

            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animShowX, animShowY, animShowAlpha, animGoneX, animGoneY, animGoneAlpha);
            animSet.setInterpolator(new DecelerateInterpolator());
            animSet.setDuration(500);
            animSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isAnimationPlaying[0] = false;
                        }
                    },150);
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animator) {

                }
            });
            show.setElevation(2f);
            show.setFocusable(true);
            show.setFocusableInTouchMode(true);
            hide.setElevation(1f);
            hide.setFocusable(false);
            hide.setFocusableInTouchMode(false);
            animSet.start();
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAnsweredQuestion() {
        return answeredQuestion;
    }

    public void setAnsweredQuestion() {
        answeredQuestion = position;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1122 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                ByteArrayOutputStream stream_edit_post_image = new ByteArrayOutputStream();
                MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), data.getData()).compress(Bitmap.CompressFormat.PNG, 100, stream_edit_post_image);
                user.setPicture(stream_edit_post_image.toByteArray());
                saveUser();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}