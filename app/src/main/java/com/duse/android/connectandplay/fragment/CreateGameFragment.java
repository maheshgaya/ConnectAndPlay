package com.duse.android.connectandplay.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.duse.android.connectandplay.Constant;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.Utility;
import com.duse.android.connectandplay.data.GamesContract;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;


import java.sql.Time;
import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class CreateGameFragment extends Fragment {
    private static final String TAG = CreateGameFragment.class.getSimpleName();
    //Spinner
    @BindView(R.id.create_game_sport_spinner)Spinner mSportSpinner;
    //Autocomplete Location
    SupportPlaceAutocompleteFragment autocompleteFragment;
    //TextInputLayout
    @BindView(R.id.game_title_text_input_layout)TextInputLayout mTitleInputLayout;
    @BindView(R.id.game_description_text_input_layout)TextInputLayout mDescriptionInputLayout;
    @BindView(R.id.create_game_date_button)Button mDateButton;
    @BindView(R.id.create_game_time_button)Button mTimeButton;
    @BindView(R.id.game_people_needed_text_input_layout)TextInputLayout mPeopleNeededInputLayout;
    @BindView(R.id.location_error_text)TextView mLocationErrorText;

    //Strings
    @BindString(R.string.error_message_required_field)String mRequiredFieldErrorMessage;
    @BindString(R.string.error_message_more_descriptive)String mMoreDescriptiveErrorMessage;
    @BindString(R.string.error_message_people_needed)String mPeopleNeededErrorMessage;
    @BindString(R.string.game_location_hint)String mLocationHint;
    @BindString(R.string.error_message_toast_not_created)String mSubmitErrorMessage;

    private String mTitle = "";
    private String mDescription = "";
    private double mLatitude;
    private double mLongitude;
    private int mPeopleNeeded;
    private String mSport = "";
    private String mLocation = "";
    private String mTime = "";
    private String mDate = "";



    public CreateGameFragment(){
        //required default constructor
    }

    /**
     * Will use menu here for the check box (Save button)
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    /**
     * Adds Game to Database
     * @return gameId
     */
    public long addGameToDB(){
        long gameId = 0;
        long sportId = 0;
        long userId = 0;
        long locationId = 0;
        //check if current user exists
        Cursor userCursor = getContext().getContentResolver().query(
                GamesContract.UserEntry.CONTENT_URI,
                new String[]{GamesContract.UserEntry._ID},
                GamesContract.UserEntry.COLUMN_CURRENT_USER + " = ? ",
                new String[]{"1"},
                null
        );
        //get sport id
        Cursor sportCursor = getContext().getContentResolver().query(
                GamesContract.SportEntry.CONTENT_URI,
                new String[]{GamesContract.SportEntry._ID},
                GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                new String[]{mSport},
                null
        );
        //check if location already exists if not create a new record in location
        Cursor locationCursor = getContext().getContentResolver().query(
                GamesContract.LocationEntry.CONTENT_URI,
                new String[]{GamesContract.LocationEntry._ID},
                GamesContract.LocationEntry.COLUMN_ADDRESS + " = ? ",
                new String[]{mLocation},
                null
        );

        if (sportCursor.moveToFirst()){
            int sportIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
            sportId = sportCursor.getLong(sportIndex);
        }
        if (userCursor.moveToFirst()){
            int userIndex = userCursor.getColumnIndex(GamesContract.UserEntry._ID);
            userId = userCursor.getLong(userIndex);
        }
        if (locationCursor.moveToFirst()) {
            //get location if exists
            int locationIndex = locationCursor.getColumnIndex(GamesContract.LocationEntry._ID);
            locationId = locationCursor.getLong(locationIndex);
        } else {
            ContentValues locationValues = new ContentValues();
            locationValues.put(GamesContract.LocationEntry.COLUMN_ADDRESS, mLocation);
            locationValues.put(GamesContract.LocationEntry.COLUMN_LATITUDE, mLatitude);
            locationValues.put(GamesContract.LocationEntry.COLUMN_LONGITUDE, mLongitude);
            Uri locationUri = getContext().getContentResolver().insert(
                    GamesContract.LocationEntry.CONTENT_URI,
                    locationValues
            );
            locationId = ContentUris.parseId(locationUri);
        }
        //add to game
        try {
            if (userCursor.getCount() > 0 && sportCursor.getCount() > 0 && locationId != 0) {
                Log.d(TAG, "addGameToDB: " + sportId + ": " + locationId + ": " + userId);
                //Create contentValue to hold the data
                ContentValues gameValues = new ContentValues();
                gameValues.put(GamesContract.GameEntry.COLUMN_GAME_NAME, mTitle);
                gameValues.put(GamesContract.GameEntry.COLUMN_SPORT_ID, sportId);
                gameValues.put(GamesContract.GameEntry.COLUMN_ORGANIZER_ID, userId);
                gameValues.put(GamesContract.GameEntry.COLUMN_TIME, mTime);
                gameValues.put(GamesContract.GameEntry.COLUMN_DATE, mDate);
                gameValues.put(GamesContract.GameEntry.COLUMN_LOCATION_ID, locationId);
                gameValues.put(GamesContract.GameEntry.COLUMN_SHORT_DESC, mDescription);
                gameValues.put(GamesContract.GameEntry.COLUMN_PEOPLE_NEEDED, mPeopleNeeded);

                //insert the record into database
                Uri insertUri = getContext().getContentResolver().insert(
                        GamesContract.GameEntry.CONTENT_URI,
                        gameValues
                );
                gameId = ContentUris.parseId(insertUri);
            }


        } finally {
            sportCursor.close();
            userCursor.close();
            locationCursor.close();
        }


        return gameId;
    }

    /**
     * Initializes the views
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_game, container, false);
        //initialize the views
        ButterKnife.bind(this, rootView);

        autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint(mLocationHint);

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
                mSport = mSportSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //initializes the date on the buttons for time and date
        //this shows the current date
        Calendar calender = Calendar.getInstance();
        int[] dateInt = Utility.currentDateTime();
        String[] date = Utility.normalizeDate(dateInt[0], dateInt[1], dateInt[2]);
        String dateStr = date[0] + " " + date[1] + ", " + date[2];
        mDateButton.setText(dateStr);
        mDate = dateStr;

        String[] time = Utility.normalizeTime(dateInt[3], dateInt[4]);
        String timeStr = time[0] + ":" + time[1] + " " + time[2];
        mTimeButton.setText(timeStr);
        mTime = timeStr;


        return rootView;
    }



    /**
     * Setups and adds listeners for the edit text
     */
    private void setupEditTexts(){
        setupTitle(); //For Title
        setupDescription(); //For description
        setupDate(); //for date
        setupTime(); //for time
        setupLocation(); //for location
        setupPeopleNeeded(); //for people needed
    }

    /**
     * setup people needed
     * listens for changes in text
     */
    private void setupPeopleNeeded() {
        mPeopleNeededInputLayout.getEditText().setText("0");
        mPeopleNeededInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() == 0) {
                    mPeopleNeededInputLayout.setError(mRequiredFieldErrorMessage);
                    mPeopleNeededInputLayout.setErrorEnabled(true);
                } else {
                    mPeopleNeededInputLayout.setErrorEnabled(false);
                }
                if (text.length() != 0) {
                    mPeopleNeeded = Integer.parseInt(text.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * setup location
     * opens Google Places API search fragment to search for addresses
     */
    private void setupLocation() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Get info about the selected place.
                Log.i(TAG, "Place: " + place.getAddress());
                mLocation = place.getAddress().toString();
                mLatitude = place.getLatLng().latitude;
                mLongitude = place.getLatLng().longitude;
                if (mLocation.equals("")){
                    mLocationErrorText.setText(mRequiredFieldErrorMessage);
                    mLocationErrorText.setVisibility(View.VISIBLE);
                }else {
                    mLocationErrorText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    /**
     * Opens dialog box for time
     */
    private void setupTime() {
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

    }

    /**
     * opens dialog box for date
     */
    private void setupDate() {
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
    }

    /**
     * setups description
     * listens for changes in text
     */
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
                    mDescriptionInputLayout.setError(mMoreDescriptiveErrorMessage + " (" + text.length() + "/10)");
                    mDescriptionInputLayout.setErrorEnabled(true);
                } else {
                    mDescriptionInputLayout.setErrorEnabled(false);
                }
                mDescription = text.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * setups the title
     * listens for changes in title
     */
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
                    mTitleInputLayout.setError(mMoreDescriptiveErrorMessage + " (" + text.length() + "/4)");
                    mTitleInputLayout.setErrorEnabled(true);
                } else {
                    mTitleInputLayout.setErrorEnabled(false);
                }
                mTitle = text.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        //Creates a new game
        if (id == R.id.action_create) {
            //create a new record
            Log.d(TAG, mTitle + "; " + mDescription + "; " + mDate + "; " + mTime + "; " +
                    mLocation + "; " + mLatitude + "; " + mLongitude + "; " + mSport + "; " +
                    mPeopleNeeded);
            //if good add to db else throw error
            if (!mTitle.equals("") && mTitle.length() > 4 &&
                    !mDescription.equals("") && mDescription.length() > 10 &&
                    !mDate.equals("") && !mTime.equals("") && mLocation != null && !mLocation.equals("") &&
                    !mSport.equals("") && mSport != null &&
                    mPeopleNeeded != 0){
                //add to database
                long gameId = addGameToDB();

                Log.d(TAG, "onOptionsItemSelected: Game added to database @record: " + gameId);
                getActivity().finish();
            } else {
                //throws error
                //error for title
                if (mTitle.equals("")){
                    mTitleInputLayout.setError(mRequiredFieldErrorMessage);
                    mTitleInputLayout.setErrorEnabled(true);
                }else if (mTitle.length() > 0 && mTitle.length() <= 4) {
                    mTitleInputLayout.setError(mMoreDescriptiveErrorMessage + " (" + mTitle.length() + "/4)");
                    mTitleInputLayout.setErrorEnabled(true);
                } else {
                    mTitleInputLayout.setErrorEnabled(false);
                }
                //error for description
                if (mDescription.equals("")) {
                    mDescriptionInputLayout.setError(mRequiredFieldErrorMessage);
                    mDescriptionInputLayout.setErrorEnabled(true);
                } else if (mDescription.length() > 0 && mDescription.length() <= 10) {
                    mDescriptionInputLayout.setError(mMoreDescriptiveErrorMessage + " (" + mDescription.length() + "/10)");
                    mDescriptionInputLayout.setErrorEnabled(true);
                } else {
                    mDescriptionInputLayout.setErrorEnabled(false);
                }

                //error for people needed
                if (mPeopleNeeded == 0) {
                    mPeopleNeededInputLayout.setError(mRequiredFieldErrorMessage + " " + mPeopleNeededErrorMessage);
                    mPeopleNeededInputLayout.setErrorEnabled(true);
                } else {
                    mPeopleNeededInputLayout.setErrorEnabled(false);
                }

                //error for location
                if (mLocation.equals("")){
                    mLocationErrorText.setText(mRequiredFieldErrorMessage);
                    mLocationErrorText.setVisibility(View.VISIBLE);
                }else {
                    mLocationErrorText.setVisibility(View.GONE);
                }
                Toast.makeText(getContext(), mSubmitErrorMessage, Toast.LENGTH_SHORT).show();

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**********************
     * TIME DIALOG
     **********************/
    public static class TimePickerFragment extends DialogFragment {

        TimePickerDialog.OnTimeSetListener onTimeSet;
        private int hourOfDay, minute;
        public TimePickerFragment() {}

        public void setCallBack(TimePickerDialog.OnTimeSetListener ontime) {
            onTimeSet = ontime;
        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            hourOfDay = args.getInt("hourOfDay");
            minute = args.getInt("minute");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(), onTimeSet, hourOfDay, minute, false);
        }
    }

    public void showTimePickerDialog(View view) {
        TimePickerFragment time = new TimePickerFragment();
        /**
         * Set Up current time Into dialog
         */
        //TODO get the current value of the button
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("minute", calender.get(Calendar.MINUTE));
        args.putInt("hourOfDay", calender.get(Calendar.HOUR_OF_DAY));
        time.setArguments(args);
        /**
         * Set Call back to capture selected time
         */
        time.setCallBack(ontime);
        time.show(getFragmentManager(), "Time Picker");
    }

    TimePickerDialog.OnTimeSetListener ontime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String[] time = Utility.normalizeTime(hourOfDay, minute);
            String timeStr = time[0] + ":" + time[1] + " " + time[2];
            mTimeButton.setText(timeStr);
            mTime = timeStr;
        }
    };

    /*******************
     * DATE DIALOG
     *******************/
    public static class DatePickerFragment extends DialogFragment {
        //http://stackoverflow.com/questions/20673609/implement-a-datepicker-inside-a-fragment
        DatePickerDialog.OnDateSetListener ondateSet;
        private int year, month, day;
        public DatePickerFragment() {}

        public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
            ondateSet = ondate;
        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }


    }
    public void showDatePickerDialog(View view) {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        //TODO get the current value of the button
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String[] date = Utility.normalizeDate(monthOfYear, dayOfMonth, year);
            String dateStr = date[0] + " " + date[1] + ", " + date[2];
            mDateButton.setText(dateStr);
            mDate = dateStr;
        }
    };



}
