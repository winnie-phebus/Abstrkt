package com.example.abstrkt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    private EditText email, password, confirmPassword;
    private Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.lr_register_email);
        password = findViewById(R.id.lr_register_password);
        confirmPassword = findViewById(R.id.lr_register_password_confirm);
        register = findViewById(R.id.lr_register_go);
        login = findViewById(R.id.lr_register_login);

    }
}