package com.abai.billim;

import java.io.Serializable;

public class ExplanationStringItem implements Serializable {
    String text;
    int size;
    boolean isBold;

    public ExplanationStringItem(String text, int size, boolean isBold) {
        this.text = text;
        this.size = size;
        this.isBold = isBold;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }
}
