package tech.calebrjc.frank;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatToggleButton;

// This class uses the android.bluetooth API to connect to Frank's HC-05 bluetooth module.
//
// This application works on the assumption that the host device has already paired to Frank's
// HC-05 manually via their normal bluetooth connection method and renamed the connection to match
// the String constant FRANKS_NAME, defined below.
public class MainActivity extends AppCompatActivity {
    // FRANKS_NAME, as mentioned above. This value is subject to change with future commits.
    private static final String FRANKS_NAME = "HC-05";

    // Represents device request code values used in this application.
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    // Represents the host device's bluetooth adapter, if it exists.
    BluetoothAdapter btAdapter;

    // This application's status indicator.
    private TextView statusMessage;

    // This application's bluetooth indicator image.
    private ImageView statusImage;

    // Used to control whether or not the host device's bluetooth adapter is enabled.
    private AppCompatToggleButton btToggle;

    private AppCompatToggleButton connectionToggle;

    @Override
    // This method is ran when this activity is created (when the app starts).
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the bluetooth adapter.
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        boolean btExists, btEnabled;
        btExists = !(btAdapter == null);
        if (btExists) {
            btEnabled = btAdapter.isEnabled();
        } else {
            btEnabled = false;
        } // if-else

        // Setting up this activity's UI elements and setting defaults.
        statusMessage = findViewById(R.id.status_message);
        int setupMessage;
        if (btExists) {
            if (btEnabled) {
                setupMessage = R.string.device_ready;
            } else {
                setupMessage = R.string.device_not_ready;
            } // if
        } else {
            setupMessage = R.string.bt_not_supported;
        } // if-else
        statusMessage.setText(setupMessage);

        statusImage = findViewById(R.id.bt_status_image);
        int setupImage;
        if (btExists) {
            if (btEnabled) {
                setupImage = R.drawable.ic_action_bt_available;
            } else {
                setupImage = R.drawable.ic_action_bt_unavailable;
            } // if
        } else {
            setupImage = R.drawable.ic_action_bt_unavailable;
        } // if-else
        statusImage.setImageResource(setupImage);

        // Button that is used to toggle the host device's bluetooth adapter on/off.
        btToggle = findViewById(R.id.bt_toggle);
        btToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (btAdapter == null) {
                showToast("This device is not bluetooth compatible.");
                btToggle.setChecked(false);
                return;
            } // if

            if (isChecked) {
                if (!btAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } // if-else
            } else {
                if (btAdapter.isEnabled()) {
                    btAdapter.disable();
                    showToast("Bluetooth turned off.");
                } // if

                if (btAdapter == null) {
                    statusMessage.setText(R.string.bt_not_supported);
                } else {
                    statusMessage.setText(R.string.device_not_ready);
                } // if-else

                statusImage.setImageResource(R.drawable.ic_action_bt_unavailable);
            } // if-else
        });
        btToggle.setChecked(btEnabled);

        // Changes the connection status of this app to Frank, when appropriate.
        connectionToggle = findViewById(R.id.connection_toggle);
        connectionToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (btAdapter == null) {
                connectionToggle.setChecked(false);
                showToast("This device is not bluetooth compatible.");
                return;
            } // if

            if (!btAdapter.isEnabled()) {
                connectionToggle.setChecked(false);
                showToast("Frank needs you to enable bluetooth.");
            } // if

            if (isChecked) {
                connectToFrank();
            } else {
                disconnectFromFrank();
            } // if-else
        });

        // Button that initiates this application's bluetooth testing.
        AppCompatButton testBtButton = findViewById(R.id.test_bt);
        testBtButton.setOnClickListener((view) -> {
        }); // TODO: Make BT tester method.

        // Button that initiates this application's microphone testing.
        AppCompatButton testMicButton = findViewById(R.id.test_mic);
        testMicButton.setOnClickListener((view) -> {
        }); // TODO: Make mic tester method.

        // Setting up the host device's bluetooth adapter.
        //prepareDevice();
    } // onCreate()

    // Attempts to establish a connection to Frank's HC-05 module.
    private void connectToFrank() {
        // TODO: Implement this.
    } // searchForFrank()

    // Disconnects from Frank's HC-05 module if the host is currently connected.
    private void disconnectFromFrank() {
        // TODO: Implement this.
    } // disconnectFromFrank()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    statusMessage.setText(R.string.device_ready);
                    statusImage.setImageResource(R.drawable.ic_action_bt_available);
                    showToast("Bluetooth turned on.");
                } else {
                    btToggle.setChecked(false);
                    showToast("Couldn't turn bluetooth on.");
                } // if-else
                break;
            case REQUEST_DISCOVER_BT:
                if (resultCode == RESULT_OK) {
                    showToast("Your device is now discoverable.");
                } else {
                    showToast("Can't make your device discoverable.");
                }  // if-else
                break;
        } // switch
        super.onActivityResult(requestCode, resultCode, data);
    } // onActivityResult()

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    } // showToast()
} // MainActivity
