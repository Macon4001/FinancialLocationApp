package com.app.financiallocationapp.budget;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.app.financiallocationapp.models.ProfileBaseModel;
import com.app.financiallocationapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



public class ProfileModel implements ViewModelProvider.Factory {
    private String uid;//making changes in the database

    private ProfileModel(String uid) {
        this.uid = uid;

    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ProfileBaseModel(uid);
    }


    //for delete
    public static void saveModel(String uid, User user) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user);
    }


    public static ProfileBaseModel getModel(String uid, FragmentActivity activity) {

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return ViewModelProviders.of(activity, new ProfileModel(uid)).get(ProfileBaseModel.class);
    }


}