package com.app.financiallocationapp.databasehelper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QueryLiveDataElement<T> extends LiveData<Element<T>> {
    private Query query;
    private ValueEventListener listener;


    public QueryLiveDataElement(Class<T> genericTypeClass, Query query) {
        setValue(null);
        listener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                T item = dataSnapshot.getValue(genericTypeClass);
                setValue(new Element<>(item));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setValue(new Element<>(databaseError));
                removeListener();
                setListener();
            }
        };
        this.query = query;
    }

    private void removeListener() {
        query.removeEventListener(listener);
    }

    private void setListener() {
        query.addValueEventListener(listener);
    }

    @Override
    protected void onActive() {
        setListener();
    }


    @Override
    protected void onInactive() {
        removeListener();
    }

}