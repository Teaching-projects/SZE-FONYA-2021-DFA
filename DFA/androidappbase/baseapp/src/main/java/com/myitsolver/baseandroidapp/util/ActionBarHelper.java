package com.myitsolver.baseandroidapp.util;

import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import com.myitsolver.baseandroidapp.R;


/**
 * Created by Kodam on 2018. 03. 20..
 */

public class ActionBarHelper {

    private SearchView mSearchView;

    public SearchView getSearchView() {
        return mSearchView;
    }

    public void setupSearchView(Menu menu, MenuInflater inflater, SearchView.OnQueryTextListener listener, SearchView.OnCloseListener closeListener, boolean iconified, View.OnClickListener openListener) {
        inflater.inflate(R.menu.searchmenu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setIconifiedByDefault(iconified);
        mSearchView.setOnQueryTextListener(listener);
        mSearchView.setOnCloseListener(closeListener);
        mSearchView.setOnSearchClickListener(openListener);
        mSearchView.setQueryHint("Keresés");
    }

    public boolean onBackPressed() {

        if (mSearchView != null && !mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            return true;
        }
        return false;
    }
}
