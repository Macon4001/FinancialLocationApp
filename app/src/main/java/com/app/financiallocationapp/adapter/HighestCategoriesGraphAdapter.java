package com.app.financiallocationapp.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
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


public class HighestCategoriesGraphAdapter extends ArrayAdapter<HighestCategoryGraphModel> {

    private ArrayList<HighestCategoryGraphModel> dataSet;

    Context context;


    public HighestCategoriesGraphAdapter(ArrayList<HighestCategoryGraphModel> data, Context context) {
        super(context, R.layout.highest_graph_item, data);
        this.dataSet = data;
        this.context = context;

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {

            listItem = LayoutInflater.from(context).inflate(R.layout.highest_graph_item, parent, false);

        }

        HighestCategoryGraphModel dataModel = getItem(position);
        Category category = dataModel.getCategory();

        TextView categoryNameTextView = listItem.findViewById(R.id.item_category);
        TextView sumTextView = listItem.findViewById(R.id.item_sum);
        TextView percentageTextView = listItem.findViewById(R.id.item_percentage);

        ImageView iconImageView = listItem.findViewById(R.id.icon_imageview);

        iconImageView.setImageResource(category.getIconResourceID());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getIconColor()));

        categoryNameTextView.setText(dataModel.getCategoryName());
        sumTextView.setText(Currency.formatCurrency(dataModel.getCurrency(), dataModel.getMoney()));
        percentageTextView.setText((int)(dataModel.getPercentage() * 100) +"." + (int)(dataModel.getPercentage() * 10000) % 100  + "%");
        if (dataModel.getMoney() < 0) {
            sumTextView.setTextColor(ContextCompat.getColor(context, R.color.gauge_expense));
        }
        else{
            sumTextView.setTextColor(ContextCompat.getColor(context, R.color.gauge_income));
        }


        return listItem;
    }
}
