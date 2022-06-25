package com.example.abstrkt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private Switch darkMode, notifications;
    private Button logOut, deleteAccount;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        darkMode = rootView.findViewById(R.id.settings_darkModeSwitch);
        notifications = rootView.findViewById(R.id.settings_notifSwitch);
        logOut = rootView.findViewById(R.id.settings_logOutButton);
        deleteAccount = rootView.findViewById(R.id.settings_deleteButton);

        darkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO change themes
        });

        logOut.setOnClickListener(v -> {
            Utils.signOutOfAccount();
            Intent leaveAccount = new Intent(getActivity(), MainActivity.class);
            startActivity(leaveAccount);
        });

        deleteAccount.setOnClickListener(v -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("no", "OK! Works fine!");
                    Intent leaveAccount = new Intent(getActivity(), MainActivity.class);
                    startActivity(leaveAccount);
                }
            }).addOnFailureListener(e -> Log.e("no", "Ocurrio un error durante la eliminación del usuario", e));


        });


        return rootView;
    }
}