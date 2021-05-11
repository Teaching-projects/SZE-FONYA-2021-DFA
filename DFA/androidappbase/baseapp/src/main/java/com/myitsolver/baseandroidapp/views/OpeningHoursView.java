package com.myitsolver.baseandroidapp.views;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.myitsolver.baseandroidapp.R;
import com.myitsolver.baseandroidapp.util.OpeningHoursParser;

import java.util.List;

// import butterknife.ButterKnife;

/**
 * Created by Peter on 2017. 09. 28..
 */

public class OpeningHoursView extends BaseView {
    public OpeningHoursView(Context context) {
        super(context);
    }

    public OpeningHoursView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OpeningHoursView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initialize() {
        setOrientation(VERTICAL);
    }

    public void setData(List<OpeningHoursParser.OneDayOpeningHours> days){
        removeAllViews();
        int i=0;
        for (OpeningHoursParser.OneDayOpeningHours day : days){
            addView(new OpeningHoursRowView(getContext(),getDayName(i),generateName(day)));
            i++;
        }
    }

    private String getDayName(int pos){
        switch (pos){
            case 0: return getContext().getString(R.string.monday);
            case 1: return getContext().getString(R.string.tuesday);
            case 2: return getContext().getString(R.string.wednesday);
            case 3: return getContext().getString(R.string.thursday);
            case 4: return getContext().getString(R.string.friday);
            case 5: return getContext().getString(R.string.saturday);
            case 6: return getContext().getString(R.string.sunday);
        }
        return "";
    }

    private String generateName(OpeningHoursParser.OneDayOpeningHours day){
        if (day.isClosedAllDay()){
            return getContext().getString(R.string.closed);
        }
        if (day.isOpenAllDay()){
            return "00:00 - 24:00";
        }
        return getNumberAsString(day.getStartHour())+":"+getNumberAsString(day.getStartMinute())+" - "+getNumberAsString(day.getEndHour())+":"+getNumberAsString(day.getEndMinute());
    }

    private String getNumberAsString(int num){
        return num<10?"0"+num:""+num;
    }
}
