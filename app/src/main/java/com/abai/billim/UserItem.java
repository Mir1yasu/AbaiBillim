package com.abai.billim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.util.List;

public class UserItem implements Serializable {
    String name, secondName, password, userId;
    byte[] picture;
    List<CompletedItem> items;
    List<String> id;
    List<TeacherItem> teacherItems;
    boolean isTeacher = false;
    public UserItem(String userId, String name, String secondName, String password, List<CompletedItem> items, List<String> id) {
        this.userId = userId;
        this.name = name;
        this.secondName = secondName;
        this.password = password;
        this.items = items;
        this.id = id;
    }
    public List<CompletedItem> getItems() {
        return items;
    }
    public List<TeacherItem> getTeacherItems() {
        if (isTeacher) return teacherItems;
        return null;
    }
    public void setItems(List<CompletedItem> items, List<String> id) {
        this.items = items;
        this.id = id;
    }
    public void addTeacher(TeacherItem item) {
        teacherItems.add(item);
    }
    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public String getName() {
        return name;
    }
    public Bitmap getPhoto() {
        try {
            return BitmapFactory.decodeByteArray(picture, 0, picture.length);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<String> getId() {
        return id;
    }
}
