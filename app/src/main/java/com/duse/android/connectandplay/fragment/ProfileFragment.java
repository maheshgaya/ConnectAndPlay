package com.duse.android.connectandplay.fragment;
//package info.androidhive.floatinglabels;

import com.duse.android.connectandplay.R;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by earleyneverlate on 10/24/16.
 */

public class ProfileFragment extends Fragment {
    @BindView(R.id.first_name_edit_text) EditText mFirstNameEditText;
    @BindView(R.id.last_name_edit_text) EditText mLastNameEditText;
    @BindView(R.id.user_name_edit_text) EditText mUsernameEditText;
    @BindView(R.id.bio_edit_text) EditText mBioEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        //return view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
