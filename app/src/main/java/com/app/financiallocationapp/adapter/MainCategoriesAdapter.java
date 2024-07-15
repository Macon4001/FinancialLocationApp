package com.app.financiallocationapp.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.app.financiallocationapp.Currency;
import com.app.financiallocationapp.R;
import com.app.financiallocationapp.models.Category;

import java.util.ArrayList;



public class MainCategoriesAdapter extends ArrayAdapter<MainCategoryListModel> implements View.OnClickListener {

    private ArrayList<MainCategoryListModel> dataSet;
    Context context;


    public MainCategoriesAdapter(ArrayList<MainCategoryListModel> data, Context context) {
        super(context, R.layout.highest_item, data);
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.highest_item, parent, false);

        MainCategoryListModel dataModel = getItem(position);

        Category category = dataModel.getCategory();


        Log.e("dddd", "getView: "+ position);

        TextView categoryNameTextView = listItem.findViewById(R.id.item_category);
        TextView sumTextView = listItem.findViewById(R.id.item_sum);
        ImageView iconImageView = listItem.findViewById(R.id.icon_imageview);

        iconImageView.setImageResource(category.getIconResourceID());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getIconColor()));

        categoryNameTextView.setText(dataModel.getCategoryName());
        sumTextView.setText(Currency.formatCurrency(dataModel.getCurrency(), dataModel.getMoney()));
        if (dataModel.getMoney() < 0)
            sumTextView.setTextColor(ContextCompat.getColor(context, R.color.gauge_expense));
        else
            sumTextView.setTextColor(ContextCompat.getColor(context, R.color.gauge_income));


        listItem.setClickable(false);
        listItem.setOnClickListener(null);



        return listItem;
    }
}
