package com.app.financiallocationapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.app.financiallocationapp.models.User;


public class Currency {



    public static String formatCurrency(com.app.financiallocationapp.models.Currency currency, long money) {
        long absMoney = Math.abs(money);
        return (currency.left ? (currency.symbol + (currency.space ? " " : "")): "") +
                (money < 0 ? "-" : "") +
                (absMoney / 100) + "." +
                (absMoney % 100 < 10 ? "0" : "") +
                (absMoney % 100)  +
                (currency.left ? "" : ((currency.space ? " " : "") + currency.symbol));
    }
    public static void setupAmountEditText(EditText editText, User user) {



        editText.setText(Currency.formatCurrency(user.currency,0), TextView.BufferType.EDITABLE);



        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!charSequence.toString().equals(current)) {


                    editText.removeTextChangedListener(this);


                    current = Currency.formatCurrency(user.currency,convertAmountStringToLong(charSequence));
                    editText.setText(current);

                    editText.setSelection(current.length() -
                            (user.currency.left ? 0 : (user.currency.symbol.length() + (user.currency.space ? 1 : 0))));


                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public static long convertAmountStringToLong(CharSequence s) {
        String cleanString = s.toString().replaceAll("[^0-9]", "");
        return Long.valueOf(cleanString);
    }
}
