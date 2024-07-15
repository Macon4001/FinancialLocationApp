package com.app.financiallocationapp.models;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;


import com.app.financiallocationapp.databasehelper.Element;
import com.app.financiallocationapp.databasehelper.FirebaseObserver;
import com.app.financiallocationapp.databasehelper.QueryLiveDataElement;
import com.google.firebase.database.FirebaseDatabase;



public class ProfileBaseModel extends ViewModel {
    private final QueryLiveDataElement<User> liveData;

    public ProfileBaseModel(String uid) {

        liveData = new QueryLiveDataElement<>(User.class, FirebaseDatabase.getInstance().getReference().child("users").child(uid));

    }

    public void observe(LifecycleOwner owner, FirebaseObserver<Element<User>> observer) {
        if(liveData.getValue() != null) observer.onChanged(liveData.getValue());
        liveData.observe(owner, new Observer<Element<User>>() {
            @Override
            public void onChanged(@Nullable Element<User> firebaseElement) {
                if(firebaseElement != null) observer.onChanged(firebaseElement);

            }
        });
    }

}