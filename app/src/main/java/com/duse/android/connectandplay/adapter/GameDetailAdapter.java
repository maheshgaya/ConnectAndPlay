package com.duse.android.connectandplay.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class GameDetailAdapter extends CursorAdapter {
    private static final String TAG = GameDetailAdapter.class.getSimpleName();

    public GameDetailAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
