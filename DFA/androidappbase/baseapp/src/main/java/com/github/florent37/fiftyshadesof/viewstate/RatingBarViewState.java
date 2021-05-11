package com.github.florent37.fiftyshadesof.viewstate;

import android.widget.RatingBar;

public class RatingBarViewState extends ViewState<RatingBar> {
    public RatingBarViewState(RatingBar view) {
        super(view);
    }

    @Override
    public void beforeStart() {
        super.beforeStart();
        view.setRating(0);
    }

    @Override
    protected void restore() {
        super.restore();

    }
}
