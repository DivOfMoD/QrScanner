package com.github.maxcriser.qrscanner.view.labels;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

class TextViewRegular extends android.support.v7.widget.AppCompatTextView {

    public TextViewRegular(final Context context) {
        super(context);
        init();
    }

    public TextViewRegular(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRegular(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "InterstatePro-Regular.otf");
            setTypeface(tf);
        }
    }
}
