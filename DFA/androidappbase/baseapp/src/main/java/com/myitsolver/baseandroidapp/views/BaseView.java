package com.myitsolver.baseandroidapp.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;


public abstract class BaseView extends LinearLayout {
    public BaseView(Context context) {
        super(context);
        setLayout(context);
        initialize();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayout(context);
        initialize();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout(context);
        initialize();
    }



    protected void setLayout(Context context){
        inflate(getContext(),getLayoutId(),this);
    }

    protected void initialize(){
        //nothing to do here
    }
    protected abstract int getLayoutId();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        /// ..
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new CustomOutline(w, h));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class CustomOutline extends ViewOutlineProvider {

        int width;
        int height;

        CustomOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, 0, width, height);
        }
    }
}
