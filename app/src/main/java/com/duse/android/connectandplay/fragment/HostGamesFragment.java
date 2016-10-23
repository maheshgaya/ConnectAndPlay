package com.duse.android.connectandplay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duse.android.connectandplay.R;

/**
 * Created by Mahesh Gaya on 10/23/16.
 */

public class HostGamesFragment extends Fragment{
    public HostGamesFragment(){
        //required empty constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_hosted_games, container, false);
    }
}
