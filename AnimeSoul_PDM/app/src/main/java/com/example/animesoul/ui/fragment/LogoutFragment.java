package com.example.animesoul.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.animesoul.R;
import com.example.animesoul.ui.activity.LoginActivity;
import com.example.animesoul.ui.activity.TermsConditionsActivity;
import com.example.animesoul.utils.Constants;
import com.example.animesoul.viewmodel.LogoutViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;
    private RadioGroup languageRadioGroup;
    private RadioButton radioDefault, radioJapanese;
    private Button saveLanguageButton, logoutButton, resetPasswordButton, termsButton;
    private TextView titleTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        logoutViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);

        titleTextView = view.findViewById(R.id.textView);
        languageRadioGroup = view.findViewById(R.id.languageRadioGroup);
        radioDefault = view.findViewById(R.id.radioDefault);
        radioJapanese = view.findViewById(R.id.radioJapanese);
        saveLanguageButton = view.findViewById(R.id.button_save_language);
        logoutButton = view.findViewById(R.id.button_logout);
        resetPasswordButton = view.findViewById(R.id.resetPassword);
        termsButton = view.findViewById(R.id.terminiecondizioni);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Osserva i messaggi di toast dal ViewModel


        if (currentUser != null) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.SHARED_FILE, Context.MODE_PRIVATE);
            String savedLanguage = sharedPreferences.getString("title_language", Constants.DEFAULT);


            if (Constants.JAPANESE.equals(savedLanguage)) {
                radioJapanese.setChecked(true);
                titleTextView.setText(Constants.DEMONSLAYER_JAP);
            } else {
                radioDefault.setChecked(true);
                titleTextView.setText(Constants.DEMONSLAYER_DEF);
            }

            languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.radioDefault) {
                    titleTextView.setText(Constants.DEMONSLAYER_DEF);
                } else if (checkedId == R.id.radioJapanese) {
                    titleTextView.setText(Constants.DEMONSLAYER_JAP);
                }
            });

            saveLanguageButton.setOnClickListener(v -> {
                String selectedLanguage = radioDefault.isChecked() ? Constants.DEFAULT : Constants.JAPANESE;
                logoutViewModel.saveLanguagePreference(currentUser.getUid(), selectedLanguage, isSuccess -> {
                    if (isSuccess) {
                        Toast.makeText(getContext(), "SAVED", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "FAILED to save preferences", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            resetPasswordButton.setOnClickListener(v -> {
                logoutViewModel.sendResetPasswordEmail(currentUser.getEmail());
            });

            termsButton.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), TermsConditionsActivity.class);
                startActivity(intent);
            });

            logoutButton.setOnClickListener(v -> {
                logoutViewModel.saveUserPreferences(currentUser.getUid());
                logoutViewModel.logoutUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                requireActivity().finish();
            });
        }

        return view;
    }
}
