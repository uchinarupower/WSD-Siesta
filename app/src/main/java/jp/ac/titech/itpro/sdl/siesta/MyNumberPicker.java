// https://words-hiro.blog.ss-blog.jp/2016-01-27

package jp.ac.titech.itpro.sdl.siesta;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

public class MyNumberPicker extends NumberPicker {

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    private void updateView(View child) {
        if (child instanceof EditText) {
            ((EditText) child).setTextSize(TypedValue.COMPLEX_UNIT_SP, 30.0f);
            ((EditText) child).setTextColor(Color.BLACK);
        }
    }
}