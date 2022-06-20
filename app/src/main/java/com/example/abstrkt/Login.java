package com.example.abstrkt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private TextView appName, pageMessage;
    private EditText usernameInput, passwordInput;
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appName = findViewById(R.id.textView_abstract);
        pageMessage = findViewById(R.id.textView_welcome);
        usernameInput = findViewById(R.id.editText_inputEmail);
        passwordInput = findViewById(R.id.editText_inputPassword);
        login = findViewById(R.id.button_login);
        register = findViewById(R.id.button_register);
    }
}