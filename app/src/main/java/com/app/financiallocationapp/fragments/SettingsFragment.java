package com.app.financiallocationapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.app.financiallocationapp.AdditionalCategoriesActivity;
import com.app.financiallocationapp.LoginActivity;
import com.app.financiallocationapp.R;
import com.app.financiallocationapp.budget.ProfileModel;
import com.app.financiallocationapp.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;
import java.util.ArrayList;



public class SettingsFragment extends PreferenceFragmentCompat {
    User user;
    ArrayList<Preference> preferences = new ArrayList<>();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        Field[] fields = R.string.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().startsWith("pref_key")) {
                try {
                    preferences.add(findPreference(getString((int) fields[i].get(null))));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Preference preference : preferences) {
            preference.setEnabled(true);
        }





        Preference logoutPreference = findPreference(getString(R.string.pref_key_logout));
        logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                return true;
            }
        });

        Preference customCategoriesPreference = findPreference(getString(R.string.pref_key_custom_categories));
        customCategoriesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                getActivity().startActivity(new Intent(getActivity(), AdditionalCategoriesActivity.class));
                return true;
            }
        });

    }


















    private void saveUser(User user) {
        ProfileModel.saveModel(getUid(), user);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}



