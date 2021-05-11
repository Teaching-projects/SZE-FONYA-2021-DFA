package com.myitsolver.baseandroidapp.views.old;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.myitsolver.baseandroidapp.R;
import com.myitsolver.baseandroidapp.views.BaseView;
import com.myitsolver.baseandroidapp.views.old.BaseNavigationDrawerItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 2017. 09. 30..
 */

public abstract class BaseNavigationDrawerView extends BaseView {
//    //@BindView(R2.id.layoutItemList)
    LinearLayout layoutItemList;

    private List<BaseNavigationDrawerItemView> views;

    protected OnNavigationDrawerItemClickListener listener;

    public BaseNavigationDrawerView(Context context) {
        super(context);
    }

    public BaseNavigationDrawerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseNavigationDrawerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void initialize() {
        views = new ArrayList<>();
        layoutItemList = findViewById(R.id.layoutItemList);
    }

    public void clearAllItem() {
        views.clear();
        layoutItemList.removeAllViews();
    }

    public List<BaseNavigationDrawerItemView> getItems() {
        return views;
    }

    public void addMenuItem(final BaseNavigationDrawerItemView itemView){
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(itemView);
            }
        });
        views.add(itemView);
        layoutItemList.addView(itemView);
    }

    private void onItemClicked(BaseNavigationDrawerItemView itemView){
        if(listener != null){
            if (listener.onNavigationDrawerItemSelected(itemView.getId())){
                setChildClicked(itemView);
            }
        }else{
            setChildClicked(itemView);
        }
    }

    private void setChildClicked(BaseNavigationDrawerItemView itemView){
        for (BaseNavigationDrawerItemView c : views){
            c.setClicked(false);
        }
        itemView.setClicked(true);
    }

    public void setListener(OnNavigationDrawerItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnNavigationDrawerItemClickListener{
        boolean onNavigationDrawerItemSelected(@NonNull int id);
    }
}
