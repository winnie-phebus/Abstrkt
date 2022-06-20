package com.example.abstrkt;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

/**
 * References:
 * https://firebase.google.com/docs/auth/android/firebaseui?hl=be#java
 * https://developers.google.com/identity/smartlock-passwords/android/
 */
public class MainActivity extends AppCompatActivity {

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    private TextView appName, pageMessage;
    private View fragmentContainer;

    @Override
    protected void onStart() {
        super.onStart();
        checkAuth();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkAuth();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appName = findViewById(R.id.lr_abstrakt_TV);
        pageMessage = findViewById(R.id.lr_screen_title_TV);
        fragmentContainer = findViewById(R.id.lr_login_register_container);

        pageMessage.setText("Welcome Back!");

        checkAuth();
        // TODO: use the FireBaseUI methods for delete / logOut
    }

    private void checkAuth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            openAuth();
        } else {
            user.reload();
            Intent openUserHome = new Intent(MainActivity.this, HomePage.class);
            startActivity(openUserHome);
        }
    }

    private void openAuth() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false, true) // TODO: delete if adding Google Auth
               // .setTheme(R.style.AppTheme)  TODO: if doing custom theme
                .build().setFlags(FLAG_IMMUTABLE);
        signInLauncher.launch(signInIntent);
    }

    public void returnLogin() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Register")
                .replace(R.id.lr_login_register_container, new LoginFragment())
                .commit();
        pageMessage.setText("Welcome Back!");
    }

    public void returnRegister() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Register")
                .replace(R.id.lr_login_register_container, new RegisterFragment())
                .commit();
        pageMessage.setText("Register Here!");
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}