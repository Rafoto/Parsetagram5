package com.example.parsetagram5;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.parsetagram5.fragments.ComposeFragment;
import com.example.parsetagram5.fragments.ProfileFragment;
import com.example.parsetagram5.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.OnComposeFragmentInteractionListener, TimelineFragment.OnFragmentInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener {
    public final String APP_TAG = "Parsetagram5";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PROFILE_IMAGE_ACTIVITY_REQUEST_CODE = 583;
    public String photoFileName = "photo.jpg";
    public static final int RESULT_OK = -1;
    private File profilePhotoFile;
    private Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContainer, new TimelineFragment()).commit();
        // define your fragments here
        final Fragment fragment1 = new ComposeFragment();
        final Fragment fragment2 = new TimelineFragment();
        final Fragment fragment3 = new ProfileFragment();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.btnPost:
                        fragment = fragment1;
                        break;
                    case R.id.btnHome:
                        fragment = fragment2;
                        break;
                    case R.id.btnLogin:
                        default:
                        fragment = fragment3;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        }
        );




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                ComposeFragment fragment = (ComposeFragment) getSupportFragmentManager().findFragmentById(R.id.flContainer);
                fragment.onPhotoTaken();

            } else if (requestCode == PROFILE_IMAGE_ACTIVITY_REQUEST_CODE){
                ParseFile parseFile = new ParseFile(profilePhotoFile);
                ParseUser user = ParseUser.getCurrentUser();
                user.put("profilePicture", parseFile);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            Log.d(APP_TAG, "Successful Profile Picture");
                        }
                    }
                });
            }
        } else {
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    public void createProfilePicture(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        profilePhotoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(TimelineActivity.this, "com.codepath.fileprovider", profilePhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, PROFILE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onComposeFragmentInteraction() {

    }

    @Override
    public void onTimelineFragmentInteraction() {

    }

    public void onLogOutClick(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onChangeProfilePicture(MenuItem item) {
        createProfilePicture(item.getActionView());
    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }
}
