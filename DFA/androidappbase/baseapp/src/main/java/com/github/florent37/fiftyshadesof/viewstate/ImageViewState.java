package com.github.florent37.fiftyshadesof.viewstate;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by f.champigny on 30/08/16.
 */
public class ImageViewState extends ViewState<ImageView> {
    Drawable source;
    private Drawable empty;

    public ImageViewState(ImageView imageView) {
        super(imageView);
    }

    @Override
    public void beforeStart() {
        super.beforeStart();
        this.source = view.getDrawable();
        view.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        empty = view.getDrawable();
    }

    @Override
    protected void restore() {
        if (view.getDrawable() == empty) {
            this.view.setImageDrawable(source);
        }
    }
}
