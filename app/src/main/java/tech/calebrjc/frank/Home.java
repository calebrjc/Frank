package tech.calebrjc.frank;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

// This class uses the android.bluetooth API to connect to Frank's HC-05 bluetooth module.
public class Home extends AppCompatActivity {
    // Represents device request code values used in this activity.
    private static final int REQUEST_ENABLE_BT = 0;

    // Used to conduct Bluetooth management on the host device.
    private BluetoothManager bluetoothManager;

    // Represents the host device's bluetooth adapter, if it exists.
    private BluetoothAdapter bluetoothAdapter;

    // This application's status indicator.
    private TextView statusMessage;

    // This application's bluetooth indicator image.
    private ImageView statusImage;

    // Used to control whether or not the host device's bluetooth adapter is enabled.
    private SwitchMaterial bluetoothSwitch;

    // Used to toggle the connection status of the host device to Frank.
    private AppCompatToggleButton connectionToggle;

    // Used to navigate through the different activities in this application.
    private BottomNavigationView bottomNavigationView;

    @Override
    // This method is ran when this activity is created (when the app starts).
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializing all private fields.
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);

        assert bluetoothManager != null;
        bluetoothAdapter = bluetoothManager.getAdapter();

        statusMessage = findViewById(R.id.home_status_message);

        statusImage = findViewById(R.id.home_status_image);

        bluetoothSwitch = findViewById(R.id.bt_switch);

        connectionToggle = findViewById(R.id.connection_toggle);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // Setting initial states for and assigning functions to UI components.
        boolean adapterExists = (bluetoothAdapter != null);

        if (adapterExists) {
            if (bluetoothAdapter.isEnabled()) {
                statusMessage.setText(R.string.device_ready);
                statusImage.setImageResource(R.drawable.ic_action_bt_available);
                bluetoothSwitch.setChecked(true);
            } else {
                statusMessage.setText(R.string.device_not_ready);
                statusImage.setImageResource(R.drawable.ic_action_bt_unavailable);
                bluetoothSwitch.setChecked(false);
            } // if-else
        } else {
            statusMessage.setText(R.string.bt_not_supported);
            statusImage.setImageResource(R.drawable.ic_action_bt_unavailable);
            bluetoothSwitch.setChecked(false);
        } // if-else

        bluetoothSwitch.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) {
                if (adapterExists) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    button.setChecked(false);
                    showSnackbar(R.string.bt_not_supported);
                } // if-else
            } else {
                if (adapterExists) {
                    bluetoothAdapter.disable();
                    statusMessage.setText(R.string.device_not_ready);
                    statusImage.setImageResource(R.drawable.ic_action_bt_unavailable);
                    showSnackbar(R.string.bt_turned_off);
                } // if
            } // if-else
        });

        connectionToggle.setChecked(false);
        connectionToggle.setOnCheckedChangeListener((button, isChecked) -> {
            // TODO (Caleb): Implement this button's functionality
            button.setChecked(false);
            showSnackbar(R.string.not_implemented);
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener((MenuItem item) -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.home:
                    return true;
                case R.id.test_mic:
                    intent = new Intent(this, MicrophoneTest.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText(R.string.device_ready);
                statusImage.setImageResource(R.drawable.ic_action_bt_available);
                showSnackbar(R.string.bt_turned_on);
            } else {
                showSnackbar(R.string.bt_request_blocked);
            } // if-else
        } // switch
        super.onActivityResult(requestCode, resultCode, data);
    } // onActivityResult()

    private void showSnackbar(@StringRes int resId) {
        View rootLayout = findViewById(R.id.home_root_layout);
        Snackbar.make(rootLayout, resId, Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottom_nav_view).show();
    } // showSnackbar()
} // MainActivity
