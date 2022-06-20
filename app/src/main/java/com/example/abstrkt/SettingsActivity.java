package com.example.abstrkt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch darkMode, notifications;
    private Button logOut, deleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkMode = findViewById(R.id.settings_darkMode);
        notifications = findViewById(R.id.settings_notifications);
        logOut = findViewById(R.id.settings_logOut);
        deleteAccount = findViewById(R.id.settings_deleteAccount);
    }
}