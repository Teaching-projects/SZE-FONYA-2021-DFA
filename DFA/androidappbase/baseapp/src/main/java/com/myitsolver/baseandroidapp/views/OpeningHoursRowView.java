package com.myitsolver.baseandroidapp.views;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.myitsolver.baseandroidapp.R;

// import butterknife.BindView;

/**
 * Created by Peter on 2017. 09. 28..
 */

public class OpeningHoursRowView extends BaseView {
    //@BindView(R2.id.tvDayName)
    TextView tvDayName;
    //@BindView(R2.id.tvDayOpen)
    TextView tvDayOpen;

    public OpeningHoursRowView(Context context) {
        super(context);
    }

    public OpeningHoursRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OpeningHoursRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_opening_hours_row;
    }

    public OpeningHoursRowView(Context context,String dayName, String dayOpen){
        this(context);
        tvDayName.setText(dayName);
        tvDayOpen.setText(dayOpen);
    }
}
