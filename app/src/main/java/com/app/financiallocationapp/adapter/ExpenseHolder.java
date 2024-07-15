package com.app.financiallocationapp.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.financiallocationapp.R;


public class ExpenseHolder extends RecyclerView.ViewHolder {


    final TextView nameTextView;
    final ImageView iconImageView;
    public View view;
    final TextView dateTextView;

    final TextView moneyTextView;
    final TextView categoryTextView;

    public ExpenseHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        moneyTextView = itemView.findViewById(R.id.money_textview);
        categoryTextView = itemView.findViewById(R.id.category_textview);
        nameTextView = itemView.findViewById(R.id.name_textview);
        dateTextView = itemView.findViewById(R.id.date_textview);
        iconImageView = itemView.findViewById(R.id.icon_imageview);

    }
}
