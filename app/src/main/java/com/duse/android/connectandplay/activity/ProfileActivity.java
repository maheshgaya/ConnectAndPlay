package com.duse.android.connectandplay.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.duse.android.connectandplay.Manifest;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ProfileActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private EditText mUsernameEditText,mFirstNameEditText, mLastNameEditText,mBiographyEditText;
    private TextInputLayout inputLayoutUName, inputLayoutFName, inputLayoutLName, inputLayoutBiography;
    private ImageButton imageButton;
    private static String userChoosenTask;
    private static final int REQUEST_CAMERA=120;
    private static final int SELECT_FILE=120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        addListenerOnButton();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayoutUName = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputLayoutFName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        inputLayoutLName = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        inputLayoutBiography = (TextInputLayout) findViewById(R.id.input_layout_bio);
        mUsernameEditText = (EditText) findViewById(R.id.input_username);
        mFirstNameEditText = (EditText) findViewById(R.id.input_first_name);
        mLastNameEditText = (EditText) findViewById(R.id.input_last_name);
        mBiographyEditText = (EditText) findViewById(R.id.input_bio);

                mUsernameEditText.addTextChangedListener(new MyTextWatcher(mUsernameEditText));
                mFirstNameEditText.addTextChangedListener(new MyTextWatcher(mFirstNameEditText));
                mLastNameEditText.addTextChangedListener(new MyTextWatcher(mLastNameEditText));
                mBiographyEditText.addTextChangedListener(new MyTextWatcher(mBiographyEditText));
    }

    public void addListenerOnButton() {

        imageButton = (ImageButton) findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(ProfileActivity.this, "Add a profile picture", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void selectImage() {

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(ProfileActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//

        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    //Handling Image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       imageButton.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageButton.setImageBitmap(thumbnail);
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
        if (!validateBio()) {
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
                case R.id.input_bio:
                    validateBio();
                    break;
            }
        }
    }
}

