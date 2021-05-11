package com.myitsolver.baseandroidapp.views.old;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import com.myitsolver.baseandroidapp.views.BaseView;

// import butterknife.BindView;

/**
 * Created by Peter on 2017. 09. 30..
 */

public abstract class BaseNavigationDrawerItemView extends BaseView {


    public BaseNavigationDrawerItemView(Context context) {
        super(context);
    }

    public BaseNavigationDrawerItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseNavigationDrawerItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setClicked(boolean clicked);

    public abstract int getId();


}
