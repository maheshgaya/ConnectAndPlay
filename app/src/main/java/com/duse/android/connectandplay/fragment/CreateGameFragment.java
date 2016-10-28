package com.duse.android.connectandplay.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.duse.android.connectandplay.Constant;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.data.GamesContract;

import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class CreateGameFragment extends Fragment {
    //Spinner
    @BindView(R.id.create_game_sport_spinner)Spinner mSportSpinner;
    //Autocomplete Location
    @BindView(R.id.create_game_location_autocomplete_text)AutoCompleteTextView mLocationAutoCompleteTextView;

    //TextInputLayout
    @BindView(R.id.game_title_text_input_layout)TextInputLayout mTitleInputLayout;
    @BindView(R.id.game_description_text_input_layout)TextInputLayout mDescriptionInputLayout;
    @BindView(R.id.game_date_text_input_layout)TextInputLayout mDateInputLayout;
    @BindView(R.id.game_time_text_input_layout)TextInputLayout mTimeInputLayout;
    @BindView(R.id.game_people_needed_text_input_layout)TextInputLayout mPeopleNeededInputLayout;

    //Strings
    @BindString(R.string.error_message_required_field)String mRequiredFieldErrorMessage;
    @BindString(R.string.error_message_more_descriptive)String mMoreDescriptiveErrorMessage;
    @BindString(R.string.error_message_people_needed)String mPeopleNeededErrorMessage;
    public CreateGameFragment(){
        //required default constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_game, container, false);
        //initialize the views
        ButterKnife.bind(this, rootView);

        //Initialize the spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sport_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSportSpinner.setAdapter(adapter);

        setupEditTexts();
        mSportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), mSportSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return rootView;
    }

    private void setupEditTexts(){
        setupTitle(); //For Title
        setupDescription(); //For description
        setupDate(); //for date
        setupTime(); //for time
        setupLocation(); //for location
        setupPeopleNeeded(); //for people needed
    }

    private void setupPeopleNeeded() {
        mPeopleNeededInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 0) {
                    mPeopleNeededInputLayout.setError(mRequiredFieldErrorMessage);
                    mPeopleNeededInputLayout.setErrorEnabled(true);
                } else if (Integer.parseInt(text.toString()) > 20) {
                    mPeopleNeededInputLayout.setError(mPeopleNeededErrorMessage);
                    mPeopleNeededInputLayout.setErrorEnabled(true);
                } else {
                    mPeopleNeededInputLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupLocation() {
        mLocationAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupTime() {
        mTimeInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    //link: https://www.tutorialspoint.com/android/android_timepicker_control.htm
                    //TODO use timepicker
                    Toast.makeText(getContext(), "Time tapped", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mTimeInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupDate() {
        mDateInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //link: http://www.tutorialspoint.com/android/android_datepicker_control.htm
                //TODO use datepicker
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupDescription() {
        mDescriptionInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 0) {
                    mDescriptionInputLayout.setError(mRequiredFieldErrorMessage);
                    mDescriptionInputLayout.setErrorEnabled(true);
                } else if (text.length() > 0 && text.length() <= 10) {
                    mDescriptionInputLayout.setError(mMoreDescriptiveErrorMessage);
                    mDescriptionInputLayout.setErrorEnabled(true);
                } else {
                    mDescriptionInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupTitle(){
        //For Title
        mTitleInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 0) {
                    mTitleInputLayout.setError(mRequiredFieldErrorMessage);
                    mTitleInputLayout.setErrorEnabled(true);
                } else if (text.length() > 0 && text.length() <= 4) {
                    mTitleInputLayout.setError(mMoreDescriptiveErrorMessage);
                    mTitleInputLayout.setErrorEnabled(true);
                } else {
                    mTitleInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        if (id == R.id.action_create_game) {
            //TODO create a new record
            Toast.makeText(getContext(), "Game Created", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
