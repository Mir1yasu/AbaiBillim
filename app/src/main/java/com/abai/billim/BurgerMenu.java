package com.abai.billim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BurgerMenu extends FrameLayout {
    public Button main, lessons, settings, account, close, admin;
    private LanguageManager languageManager;
    private ObjectAnimator animatorAppear, animatorDissapear, animatorOpen, animatorClose;
    private boolean isOpen = false;
    private float burgerWidth;
    private Context context;
    public BurgerMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.burger_menu, this);
        BurgerMenu burger = this;
        main = findViewById(R.id.main);
        lessons = findViewById(R.id.lessons);
        settings = findViewById(R.id.settings);
        account = findViewById(R.id.account);
        close = findViewById(R.id.close);
        admin = findViewById(R.id.admin);
        if (burger.getViewTreeObserver().isAlive()) {
            burger.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    burgerWidth = burger.getWidth() * -1;
                    burger.setScaleY(1);
                    burger.setX(0);
                    //burger.getResources().getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                    burger.setX(-1 * burger.getWidth());
                    animatorOpen = ObjectAnimator.ofFloat(burger, "x", burgerWidth, 0);
                    animatorOpen.setInterpolator(new DecelerateInterpolator());
                    animatorOpen.setDuration(500);
                    animatorClose = ObjectAnimator.ofFloat(burger, "x", 0, burgerWidth);
                    animatorClose.setInterpolator(new DecelerateInterpolator());
                    animatorClose.setDuration(500);
                    burger.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        animatorAppear = ObjectAnimator.ofFloat(burger, "alpha", 0, 1);
        animatorAppear.setInterpolator(new DecelerateInterpolator());
        animatorAppear.setDuration(500);
        animatorDissapear = ObjectAnimator.ofFloat(burger, "alpha", 1, 0);
        animatorDissapear.setInterpolator(new DecelerateInterpolator());
        animatorDissapear.setDuration(500);
        main.setOnClickListener(v -> {
            ((Main) getContext()).replaceFragment(new HomeFragment());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animatorClose();
                }
            }, 50);
        });
        lessons.setOnClickListener(v -> {
            ((Main) getContext()).replaceFragment(new ExercisesFragment());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animatorClose();
                }
            }, 50);
        });
        account.setOnClickListener(v-> {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.create_account_popup);
            ImageView accountPicture = dialog.findViewById(R.id.accountPicture);
            EditText nameInput = dialog.findViewById(R.id.nameInput);
            EditText secondNameInput = dialog.findViewById(R.id.secondNameInput);
            Spinner roleChoose = dialog.findViewById(R.id.roleChoose);
            nameInput.setText(((Main) getContext()).getUser().getName());
            secondNameInput.setText(((Main) getContext()).getUser().getSecondName());
            roleChoose.setSelection(((Main) getContext()).getUser().isTeacher ? 1 : 0);
            ((FrameLayout)accountPicture.getParent()).setClipToOutline(true);
            if (((Main) getContext()).getUser().getPhoto() != null) accountPicture.setImageBitmap(((Main) getContext()).getUser().getPhoto());
            accountPicture.setOnClickListener(vv-> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ((Main) getContext()).startActivityForResult(intent, 1122);
            });
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    accountPicture.setImageBitmap(((Main) getContext()).getUser().getPhoto());
                    handler.postDelayed(this, 250);
                }
            };
            handler.postDelayed(runnable, 250);
            nameInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ((Main) getContext()).getUser().setName(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    ((Main) getContext()).saveUser();
                }
            });
            secondNameInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ((Main) getContext()).getUser().setSecondName(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    ((Main) getContext()).saveUser();
                }
            });
            roleChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getItemAtPosition(i).toString().equals("Ученик")) {
                        ((Main) getContext()).getUser().setTeacher(false);
                        admin.setVisibility(GONE);
                    } else {
                        ((Main) getContext()).getUser().setTeacher(true);
                        admin.setVisibility(VISIBLE);
                    }
                    ((Main) getContext()).saveUser();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.show();
        });
        admin.setOnClickListener(v-> {
            ((Main) getContext()).replaceFragment(new CreateExreciseFragment());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animatorClose();
                }
            }, 50);
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        this.context = context;
    }
    public void animatorClose() {
        if (isOpen || this.getX() >= -1) {
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(animatorDissapear, animatorClose);
            animator.start();
            isOpen = false;
        }
    }
    public void animatorOpen() {
        if (!isOpen) {
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(animatorAppear, animatorOpen);
            animator.start();
            isOpen = true;
        }
    }
    public void openDialog() {
        languageManager = new LanguageManager(context);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(R.layout.view_settings);

        CheckBox kk_box = dialog.findViewById(R.id.kk_box), ru_box = dialog.findViewById(R.id.ru_box);
        LinearLayout kk = dialog.findViewById(R.id.kk), ru = dialog.findViewById(R.id.ru);
        switch (context.getSharedPreferences("My_Prefs", context.MODE_PRIVATE).getString("language", "kk")) {
            case "kk":
                kk_box.setChecked(true);
                break;
            case "ru":
                ru_box.setChecked(true);
                break;
            default:
                kk_box.setChecked(true);
                languageManager.updateRes("kk");
        }
        kk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!kk_box.isChecked()) {
                    ru_box.setChecked(false);
                    kk_box.setChecked(true);
                    context.getSharedPreferences("My_Prefs", context.MODE_PRIVATE).edit().putString("language", "kk").commit();
                    languageManager.updateRes("kk");
                    dialog.dismiss();
                    ((Activity) context).recreate();
                }
            }
        });
//        {
//            "kk": {
//            "exerciseId": 2,
//                    "exerciseTheme": "Есімдіктер",
//                    "exerciseExplanation": [
//            {
//                "exerciseText": "",
//                    "exerciseSize": 2,
//                    "exerciseIsBold": true
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            }
//
//    ],
//            "exerciseType": "",
//                    "exerciseQuestion": {
//                "questions": [
//                "",
//                        "",
//                        "",
//                        "",
//                        ""
//      ],
//                "answers": [
//        [
//                "",
//                        "",
//                        ""
//        ],
//        [
//                "",
//                        "",
//                        ""
//        ],
//        [
//                "",
//                        "",
//                        ""
//        ],
//        [
//                "",
//                        "",
//                        ""
//        ],
//        [
//                "",
//                        "",
//                        ""
//        ]
//      ],
//                "correctAnswers": [
//
//      ]
//            }
//        },
//            "ru": {
//            "exerciseId": 2,
//                    "exerciseTheme": "Есімдіктер",
//                    "exerciseExplanation": [
//
//            {
//                "exerciseText": "Местоимения в казахском языке являются словами, которые заменяют существительные и помогают избежать их повторения. В казахском языке местоимения делятся на несколько категорий:",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "1. Жекелік есімдіктер:",
//                    "exerciseSize": 2,
//                    "exerciseIsBold": true
//            },
//            {
//                "exerciseText": "Я – Мен",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "Ты – Сен\nОн/Она/Оно – Ол\nМы – Біз\nВы – Сіз (уважительное), Сендер (неофициальное)\nОни – Олар",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "Он/Она/Оно – Ол\nМы – Біз\nВы – Сіз (уважительное), Сендер (неофициальное)\nОни – Олар",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "Мы – Біз\nВы – Сіз (уважительное), Сендер (неофициальное)\nОни – Олар",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "Вы – Сіз (уважительное), Сендер (неофициальное)\nОни – Олар",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//
//            {
//                "exerciseText": "",
//                    "exerciseSize": 2,
//                    "exerciseIsBold": true
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 2,
//                    "exerciseIsBold": true
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 2,
//                    "exerciseIsBold": true
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 2,
//                    "exerciseIsBold": true
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 1.1,
//                    "exerciseIsBold": false
//            },
//            {
//                "exerciseText": "",
//                    "exerciseSize": 2,
//                    "exerciseIsBold": true
//            }
//    ],
//            "exerciseType": "",
//                    "exerciseQuestion": {
//                "questions": [
//                "",
//                        "",
//                        "",
//                        "",
//                        ""
//      ],
//                "answers": [
//        [
//                "",
//                        "",
//                        ""
//        ],
//        [
//                "",
//                        "",
//                        ""
//        ],
//        [
//                "",
//                        "",
//                        ""
//        ],
//        [
//                "",
//                        "",
//                        ""
//        ],
//        [
//                "",
//                        "",
//                        ""
//        ]
//      ],
//                "correctAnswers": [
//
//      ]
//            }
//        }
//        }

        ru.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ru_box.isChecked()) {
                    ru_box.setChecked(true);
                    kk_box.setChecked(false);
                    context.getSharedPreferences("My_Prefs", context.MODE_PRIVATE).edit().putString("language", "kk").commit();
                    languageManager.updateRes("ru");
                    dialog.dismiss();
                    ((Activity) context).recreate();
                }
            }
        });
        dialog.show();
    }
}
