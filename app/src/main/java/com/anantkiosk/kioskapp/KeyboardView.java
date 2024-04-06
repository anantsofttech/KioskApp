package com.anantkiosk.kioskapp;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.anantkiosk.kioskapp.Utils.UtilsGlobal;

public class KeyboardView extends FrameLayout implements View.OnClickListener {

    private EditText mPasswordField;
    Context context;

    public KeyboardView(Context context,EditText mPasswordFieldAdded) {
        super(context);
        this.context=context;
        mPasswordField=mPasswordFieldAdded;
        init();
    }

    public void setKeybordInputArea(Context context,EditText mPasswordFieldAdded){
        mPasswordFieldAdded=mPasswordField;
        new KeyboardView(context,mPasswordFieldAdded);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.keyboard, this);
        initViews();
    }

    private void initViews() {

        $(R.id.t9_key_0).setOnClickListener(this);
        $(R.id.t9_key_1).setOnClickListener(this);
        $(R.id.t9_key_2).setOnClickListener(this);
        $(R.id.t9_key_3).setOnClickListener(this);
        $(R.id.t9_key_4).setOnClickListener(this);
        $(R.id.t9_key_5).setOnClickListener(this);
        $(R.id.t9_key_6).setOnClickListener(this);
        $(R.id.t9_key_7).setOnClickListener(this);
        $(R.id.t9_key_8).setOnClickListener(this);
        $(R.id.t9_key_9).setOnClickListener(this);
        $(R.id.t9_key_clear).setOnClickListener(this);
        $(R.id.t9_key_backspace).setOnClickListener(this);

        ((TextView)$(R.id.t9_key_0)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_1)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_2)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_3)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_4)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_5)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_6)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_7)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_8)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_9)).setTypeface(UtilsGlobal.setFontSemiBold(context));
        ((TextView)$(R.id.t9_key_clear)).setTypeface(UtilsGlobal.setFontRegular(context));
        ((TextView)$(R.id.t9_key_backspace)).setTypeface(UtilsGlobal.setFontRegular(context));
    }

    @Override
    public void onClick(View v) {
        // handle number button click
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            try {
                MainActivity.contex.isDeleted = false;
            }catch (Exception e){

            }
            mPasswordField.append(((TextView) v).getText());
            return;
        }
        switch (v.getId()) {
            case R.id.t9_key_clear: { // h
                // andle clear button
                try {
                    MainActivity.contex.isDeleted = false;
                }catch (Exception e){

                }
                mPasswordField.setText(null);
            }
            break;
            case R.id.t9_key_backspace: { // handle backspace button
                // delete one character
                try {
                    MainActivity.contex.isDeleted = true;
                }catch (Exception e){

                }
                Editable editable = mPasswordField.getText();
                int charCount = editable.length();
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount);
                }
            }
            break;
        }
    }

    public String getInputText() {
        return mPasswordField.getText().toString();
    }

    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }
}