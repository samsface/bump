package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class EmojiView extends android.support.v7.widget.AppCompatTextView {

    public EmojiView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public EmojiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public EmojiView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        // Do your staff
    }

    public void setFeelings() {

    }
}
