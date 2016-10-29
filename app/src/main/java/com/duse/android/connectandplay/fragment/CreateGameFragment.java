package com.duse.android.connectandplay.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.Utility;
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

    //Strings
    @BindString(R.string.error_message_required_field)String mRequiredFieldErrorMessage;
    @BindString(R.string.error_message_more_descriptive)String mMoreDescriptiveErrorMessage;
    @BindString(R.string.error_message_people_needed)String mPeopleNeededErrorMessage;
    @BindString(R.string.game_location_hint)String mLocationHint;

    private String mTitle;
    private String mDescription;
    private double mLatitude;
    private double mLongitude;
    private int mPeopleNeeded;
    private String mSport;
    private String mLocation;
    private String mTime;
    private String mDate;



    public CreateGameFragment(){
        //required default constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    public long addGameToDB(){
        long gameId = 0;
        //TODO check if current user exists
        //TODO get sport id
        //TODO check if location already exists if not create a new record in location
        //TODO add to games

        return gameId;
    }

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

        Calendar calender = Calendar.getInstance();
        int[] dateInt = currentDateTime();
        String[] date = Utility.normalizeDate(dateInt[0], dateInt[1], dateInt[2]);
        String dateStr = date[0] + " " + date[1] + "," + date[2];
        mDateButton.setText(dateStr);
        mDate = dateStr;

        String[] time = Utility.normalizeTime(dateInt[3], dateInt[4]);
        String timeStr = time[0] + ":" + time[1] + " " + time[2];
        mTimeButton.setText(timeStr);
        mTime = timeStr;


        return rootView;
    }

    public int[] currentDateTime(){
        Calendar calendar = Calendar.getInstance();
        return new int[]{calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE)};

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


    private void setupLocation() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Get info about the selected place.
                Log.i(TAG, "Place: " + place.getAddress());
                mLocation = place.getAddress().toString();
                mLatitude = place.getLatLng().latitude;
                mLongitude = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void setupTime() {
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

    }

    private void setupDate() {
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
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
        if (id == R.id.action_create_game) {
            //TODO create a new record
            Log.d(TAG, mTitle + "; " + mDescription + "; " + mDate + "; " + mTime + "; " +
                    mLocation + "; " + mLatitude + "; " + mLongitude + "; " + mSport + "; " +
                    mPeopleNeeded);
            Toast.makeText(getContext(), "Game Created", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


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
            String dateStr = date[0] + " " + date[1] + "," + date[2];
            mDateButton.setText(dateStr);
            mDate = dateStr;
        }
    };



}
