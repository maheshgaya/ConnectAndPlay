package com.duse.android.connectandplay.fragment;
//package info.androidhive.floatinglabels;

import com.duse.android.connectandplay.Constant;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.Utility;
import com.duse.android.connectandplay.activity.ExploreGamesActivity;
import com.duse.android.connectandplay.activity.ProfileActivity;
import com.duse.android.connectandplay.data.GamesContract;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by earleyneverlate on 10/24/16.
 */

public class ProfileFragment extends Fragment{
    private static final String TAG = ProfileFragment.class.getSimpleName();
    @BindView(R.id.input_username) EditText mUsernameEditText;
    @BindView(R.id.input_first_name) EditText mFirstNameEditText;
    @BindView(R.id.input_last_name) EditText mLastNameEditText;
    @BindView(R.id.input_bio) EditText mBiographyEditText;
    @BindView(R.id.input_layout_username) TextInputLayout inputLayoutUName;
    @BindView(R.id.input_layout_first_name) TextInputLayout inputLayoutFName;
    @BindView(R.id.input_layout_last_name) TextInputLayout inputLayoutLName;
    @BindView(R.id.input_layout_bio) TextInputLayout inputLayoutBiography;
    @BindView(R.id.imageButton) ImageButton imageButton;

    @BindString(R.string.error_message_toast_user_not_updated)String userErrorMessage;

    private Cursor userCursor;
    private boolean currentUser = false; //check to see if user is new or current

    private String mUsername = "";
    private String mFirstName = "";
    private String mLastName = "";
    private String mBiography = "";

//    private String userChoosenTask;
//    private static final int REQUEST_CAMERA=120;
//    private static final int SELECT_FILE=121;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }
    /**
     * inflate options menu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_game_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * deals with options menu item
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //adds or update the profile
        if (id == R.id.action_create) {
            //update record
            boolean result = validateOnSubmit();
            if (result) {
                try {
                    if (userCursor.moveToFirst() && currentUser) {
                        //updates the profile
                        ContentValues userValues = new ContentValues();
                        userValues.put(GamesContract.UserEntry.COLUMN_USERNAME, mUsername);
                        userValues.put(GamesContract.UserEntry.COLUMN_FIRST_NAME, mFirstName);
                        userValues.put(GamesContract.UserEntry.COLUMN_LAST_NAME, mLastName);
                        userValues.put(GamesContract.UserEntry.COLUMN_BIOGRAPHY, mBiography);
                        int updateUri = getContext().getContentResolver().update(
                                GamesContract.UserEntry.CONTENT_URI,
                                userValues,
                                GamesContract.UserEntry.COLUMN_CURRENT_USER + " = ? ",
                                new String[]{"1"}
                        );
                        Log.d(TAG, "onOptionsItemSelected: User Updated" + updateUri);
                        getActivity().finish();

                    } else if (!currentUser) {
                        //creates the user if not already exists
                        ContentValues userValues = new ContentValues();
                        userValues.put(GamesContract.UserEntry.COLUMN_USERNAME, mUsername);
                        userValues.put(GamesContract.UserEntry.COLUMN_FIRST_NAME, mFirstName);
                        userValues.put(GamesContract.UserEntry.COLUMN_LAST_NAME, mLastName);
                        userValues.put(GamesContract.UserEntry.COLUMN_BIOGRAPHY, mBiography);
                        userValues.put(GamesContract.UserEntry.COLUMN_CURRENT_USER, 1);

                        Uri insertUri = getContext().getContentResolver().insert(
                                GamesContract.UserEntry.CONTENT_URI,
                                userValues
                        );
                        Log.d(TAG, "onOptionsItemSelected: User added: " + insertUri);
                        Intent intent = new Intent(getActivity(), ExploreGamesActivity.class);
                        startActivity(intent);
                    }
                } finally {
                    userCursor.close();
                }

            } else {
                Toast.makeText(getContext(), userErrorMessage, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        //close cursor to avoid leaks
        if (userCursor.getCount() != 0){
            userCursor.close();
        }
        super.onStop();
    }

    /**
     * initializes the views
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        userCursor = getContext().getContentResolver().query(GamesContract.UserEntry.CONTENT_URI,
                Constant.USER_PROJECTION,
                GamesContract.UserEntry.COLUMN_CURRENT_USER + " = ? ",
                new String[]{"1"},
                null);
        //Check if current user exists. If this is the case
        //then get the information and display it
        if (userCursor.moveToFirst()) {
            currentUser = true; //current user exists
            mUsername = userCursor.getString(Constant.COLUMN_USER_USERNAME);
            mFirstName = userCursor.getString(Constant.COLUMN_USER_FIRST_NAME);
            mLastName = userCursor.getString(Constant.COLUMN_USER_LAST_NAME);
            mBiography = userCursor.getString(Constant.COLUMN_USER_BIOGRAPHY);
            mUsernameEditText.setText(mUsername);
            mFirstNameEditText.setText(mFirstName);
            mLastNameEditText.setText(mLastName);
            mBiographyEditText.setText(mBiography);
            mUsernameEditText.setEnabled(false);
        } else {
            currentUser = false; //new user
        }

        mUsernameEditText.addTextChangedListener(new MyTextWatcher(mUsernameEditText));

        mFirstNameEditText.addTextChangedListener(new MyTextWatcher(mFirstNameEditText));
        mLastNameEditText.addTextChangedListener(new MyTextWatcher(mLastNameEditText));
        mBiographyEditText.addTextChangedListener(new MyTextWatcher(mBiographyEditText));
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(getContext(), "Image upload to be implemented in version 2.0.0", Toast.LENGTH_SHORT).show();

            }
        });
        return rootView;
    }


    /**
     * Validating form
     */
    private boolean validateOnSubmit() {
        if (!validateUName()) {
            return false;
        }

        if (!validateFName()) {
            return false;
        }

        if (!validateLName()) {
            return false;
        }
        if (!validateBio()) {
            return false;
        }
        return true;
    }

    /**
     * validate username
     * @return boolean
     */
    private boolean validateUName() {
        if ( mUsernameEditText.getText().toString().trim().isEmpty()) {
            inputLayoutUName.setError(getString(R.string.err_msg_Uname));
            requestFocus(mUsernameEditText);
            return false;
        } else {
            inputLayoutUName.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * validate first name
     * @return boolean
     */
    private boolean validateFName() {
        if (mFirstNameEditText.getText().toString().trim().isEmpty()) {
            inputLayoutFName.setError(getString(R.string.err_msg_Fname));
            requestFocus(mFirstNameEditText);
            return false;
        } else {
            inputLayoutFName.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * validate last name
     * @return boolean
     */
    private boolean validateLName() {
        if ( mLastNameEditText.getText().toString().trim().isEmpty()) {
            inputLayoutLName.setError(getString(R.string.err_msg_Lname));
            requestFocus(mLastNameEditText);
            return false;
        } else {
            inputLayoutLName.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * validate biography
     * @return boolean
     */
    private boolean validateBio() {
        if ( mBiographyEditText.getText().toString().trim().isEmpty()) {
            inputLayoutBiography.setError(getString(R.string.err_msg_Bio));
            requestFocus(mBiographyEditText);
            return false;
        } else {
            inputLayoutBiography.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * gets focus on edit text
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    /**
     * listens for changes in edit texts
     */
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence text, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.input_username:
                    validateUName();
                    mUsername = text.toString();
                    break;
                case R.id.input_first_name:
                    validateFName();
                    mFirstName = text.toString();
                    break;
                case R.id.input_last_name:
                    validateLName();
                    mLastName = text.toString();
                    break;
                case R.id.input_bio:
                    validateBio();
                    mBiography = text.toString();
                    break;
            }
        }

        public void afterTextChanged(Editable editable) {

        }
    }
}
