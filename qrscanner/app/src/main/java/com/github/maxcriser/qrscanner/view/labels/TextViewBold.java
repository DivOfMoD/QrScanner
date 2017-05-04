package com.github.maxcriser.qrscanner.view.labels;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

class TextViewBold extends android.support.v7.widget.AppCompatTextView {

    public TextViewBold(final Context context) {
        super(context);
        init();
    }

    public TextViewBold(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewBold(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            final Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "InterstatePro-Bold.otf");
            setTypeface(tf);
        }
    }
}
