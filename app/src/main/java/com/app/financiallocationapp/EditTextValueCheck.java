package com.app.financiallocationapp;

import android.widget.EditText;

public class EditTextValueCheck {
    public boolean hasValue(EditText editText)
    {
        try {
            if (editText.getText().toString().isEmpty())
            {
                editText.setError("Empty !");
                return false;
            }else
            {
                return true;
            }
        }catch (Exception e)
        {
            editText.setError("Empty !");
            return false;
        }
    }
}
