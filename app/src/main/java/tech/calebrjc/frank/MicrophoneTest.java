package tech.calebrjc.frank;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class MicrophoneTest extends AppCompatActivity {
    // Represents device request code values used in this activity.
    private static final int REQUEST_RECORD_AUDIO = 0;

    // Used to record via the device's microphone.
    private MediaRecorder recorder;

    // Used to play recorded media.
    private MediaPlayer player;

    // Permissions required for this activity.
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    // Indicates whether or not this app has permission to record audio.
    private boolean permissionToRecord;

    // This application's status indicator.
    private TextView statusMessage;

    // This application's bluetooth indicator image.
    private ImageView statusImage;

    // Used to toggle the microphone's recording status.
    private AppCompatToggleButton microphoneToggle;

    // Used to play audio recorded during the microphoneToggle's "on" time.
    private AppCompatToggleButton mediaButton;

    // Used to navigate through the different activities in this application.
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone_test);

        // Initializing all private fields.
        permissionToRecord = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED;

        statusMessage = findViewById(R.id.mic_status_message);
        statusImage = findViewById(R.id.mic_status_image);
        microphoneToggle = findViewById(R.id.mic_toggle);
        mediaButton = findViewById(R.id.playback_toggle);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // Setting initial states for and assigning functions to UI components.

        statusMessage.setText(R.string.mic_ready);

        statusImage.setImageResource(R.drawable.ic_action_mic_off);

        microphoneToggle.setChecked(false);
        microphoneToggle.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) {
                if (permissionToRecord) {
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/frank.3gp");
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } // try-catch

                    recorder.start();
                    statusMessage.setText("Recording...");
                } else {
                    button.setChecked(false);
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO);
                } // if-else
            } else {
                if (permissionToRecord) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    statusMessage.setText(R.string.mic_ready);
                } // if
            } // if-else
        });

        mediaButton.setChecked(false);
        mediaButton.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) {
                if (microphoneToggle.isChecked()) {
                    button.setChecked(false);
                } else {
                    player = new MediaPlayer();
                    player.setOnCompletionListener((player) -> button.setChecked(false));

                    try {
                        player.setDataSource(getExternalCacheDir().getAbsolutePath() + "/frank.3gp");
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } // try-catch
                }
            } else {
                if (!microphoneToggle.isChecked()) {
                    player.release();
                    player = null;
                } // if
            } // if-else
        });

        bottomNavigationView.setSelectedItemId(R.id.test_mic);
        bottomNavigationView.setOnNavigationItemSelectedListener((MenuItem item) -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.home:
                    intent = new Intent(this, Home.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.test_mic:
                    return true;
                case R.id.test_bt:
                    intent = new Intent(this, BluetoothTest.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
            } // switch
            return false;
        });
    } // onCreate()

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            permissionToRecord = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            showSnackbar(R.string.mic_permission_granted);
        } // if
        if (!permissionToRecord) showSnackbar(R.string.mic_request_blocked);
    } // onRequestPermissionsResult()

    private void showSnackbar(@StringRes int resId) {
        View rootLayout = findViewById(R.id.test_mic_root_layout);
        Snackbar.make(rootLayout, resId, Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottom_nav_view).show();
    } // showSnackbar()
} // MicrophoneTest
