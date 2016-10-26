package com.duse.android.connectandplay.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.duse.android.connectandplay.R;


public class ProfileActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private EditText mUsernameEditText,mFirstNameEditText, mLastNameEditText, mBioEditText;
    private TextInputLayout inputLayoutUName, inputLayoutFName, inputLayoutLName, inputLayoutBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayoutUName = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputLayoutFName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        inputLayoutLName = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        inputLayoutBio = (TextInputLayout) findViewById(R.id.input_layout_bio);
        mUsernameEditText = (EditText) findViewById(R.id.input_username);
        mFirstNameEditText = (EditText) findViewById(R.id.input_first_name);
        mLastNameEditText = (EditText) findViewById(R.id.input_last_name);
        mBioEditText = (EditText) findViewById(R.id.input_bio);
                mUsernameEditText.addTextChangedListener(new MyTextWatcher(mUsernameEditText));
                mFirstNameEditText.addTextChangedListener(new MyTextWatcher(mFirstNameEditText));
                mLastNameEditText.addTextChangedListener(new MyTextWatcher(mLastNameEditText));
                mBioEditText.addTextChangedListener(new MyTextWatcher(mBioEditText));
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateUName()) {
            return;
        }

        if (!validateFName()) {
            return;
        }

        if (!validateLName()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_username:
                    validateUName();
                    break;
                case R.id.input_first_name:
                    validateFName();
                    break;
                case R.id.input_last_name:
                    validateLName();
                    break;
            }
        }
    }
}

