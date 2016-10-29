package com.duse.android.connectandplay.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.duse.android.connectandplay.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by earleyneverlate on 10/24/16.
 */

public class AboutFragment extends Fragment {
    private static final String TAG = AboutFragment.class.getSimpleName();
    @BindString(R.string.icon_authors) String mIconAuthorsHtml;
    @BindView(R.id.icon_author_text_view)TextView mIconAuthorTextView;
    public AboutFragment(){
        //required empty constructor
    }

    /**
     * Just inflates the layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, rootView);
        String iconAuthors;
        if (Build.VERSION.SDK_INT > 23) {
            iconAuthors = Html.fromHtml(mIconAuthorsHtml, 0).toString();
        } else {
            iconAuthors = Html.fromHtml(mIconAuthorsHtml).toString();
        }
        Log.d(TAG, "onCreateView: " + iconAuthors);
        mIconAuthorTextView.setText(iconAuthors);
        return rootView;
    }
}
