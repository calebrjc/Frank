package tech.calebrjc.frank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BluetoothTest extends AppCompatActivity {
    // Used to navigate through the different activities in this application.
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.test_bt);

        bottomNavigationView.setOnNavigationItemSelectedListener((MenuItem item) -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.home:
                    intent = new Intent(this, Home.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.test_mic:
                    intent = new Intent(this, MicrophoneTest.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.test_bt:
                    return true;
            } // switch
            return false;
        });
    }
}
