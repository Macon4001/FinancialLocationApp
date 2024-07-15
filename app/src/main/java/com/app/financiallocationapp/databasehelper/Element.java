package com.app.financiallocationapp.databasehelper;

import com.google.firebase.database.DatabaseError;

public class Element<T> {
    private T element;
    private DatabaseError databaseError;

    public Element(T element) {
        this.element = element;
    }
    public Element(DatabaseError databaseError) {
        this.databaseError = databaseError;
    }

    public T getElement() {
        return element;
    }

    public boolean hasNoError() {
        return element != null;
    }
}
