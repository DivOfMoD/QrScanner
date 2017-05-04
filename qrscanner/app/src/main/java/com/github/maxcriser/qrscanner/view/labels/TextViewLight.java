package com.github.maxcriser.qrscanner.view.labels;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

class TextViewLight extends android.support.v7.widget.AppCompatTextView {

    public TextViewLight(final Context context) {
        super(context);
        init();
    }

    public TextViewLight(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewLight(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "InterstatePro-Light.otf");
            setTypeface(tf);
        }
    }
}
