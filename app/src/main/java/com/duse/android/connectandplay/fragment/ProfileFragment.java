package com.duse.android.connectandplay.fragment;
//package info.androidhive.floatinglabels;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.Utility;
import com.duse.android.connectandplay.activity.ProfileActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by earleyneverlate on 10/24/16.
 */

public class ProfileFragment extends Fragment {
    @BindView(R.id.input_username) EditText mUsernameEditText;
    @BindView(R.id.input_first_name) EditText mFirstNameEditText;
    @BindView(R.id.input_last_name) EditText mLastNameEditText;
    @BindView(R.id.input_bio) EditText mBiographyEditText;
    @BindView(R.id.input_layout_username) TextInputLayout inputLayoutUName;
    @BindView(R.id.input_layout_first_name) TextInputLayout inputLayoutFName;
    @BindView(R.id.input_layout_last_name) TextInputLayout inputLayoutLName;
    @BindView(R.id.input_layout_bio) TextInputLayout inputLayoutBiography;
    @BindView(R.id.imageButton) ImageButton imageButton;
    private static String userChoosenTask;
    private static final int REQUEST_CAMERA=120;
    private static final int SELECT_FILE=120;

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
        if (id == R.id.action_create_game) {
            //TODO create a new record
            Toast.makeText(getContext(), "Game Created", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);

        mUsernameEditText.addTextChangedListener(new MyTextWatcher(mUsernameEditText));
        mFirstNameEditText.addTextChangedListener(new MyTextWatcher(mFirstNameEditText));
        mLastNameEditText.addTextChangedListener(new MyTextWatcher(mLastNameEditText));
        mBiographyEditText.addTextChangedListener(new MyTextWatcher(mBiographyEditText));
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                selectImage();

            }
        });
        return rootView;
    }


    private void selectImage() {

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getContext());
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
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
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
        Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
