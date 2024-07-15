package com.app.financiallocationapp.databasehelper;

public interface FirebaseObserver<T> {
    void onChanged(T t);
}
