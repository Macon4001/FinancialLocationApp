package com.app.financiallocationapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.financiallocationapp.EditAdditionalCategoryActivity;
import com.app.financiallocationapp.R;
import com.app.financiallocationapp.models.Category;

import java.util.List;


public class AdditionalCategoriesAdapter extends ArrayAdapter<Category> implements View.OnClickListener {

    final Activity activity;


    Context context;



    public AdditionalCategoriesAdapter(Activity activity, List<Category> data, Context context) {
        super(context, R.layout.highest_item, data);
        this.context = context;
        this.activity = activity;

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.additional_categories_item, parent, false);


        Category category = getItem(position);

        TextView categoryNameTextView = listItem.findViewById(R.id.category_textview);
        ImageView iconImageView = listItem.findViewById(R.id.icon_imageview);

        iconImageView.setImageResource(category.getIconResourceID());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getIconColor()));

        categoryNameTextView.setText(category.getCategoryVisibleName(getContext()));


        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditAdditionalCategoryActivity.class);
                intent.putExtra("category-id", category.getCategoryID());
                intent.putExtra("category-name", category.getCategoryVisibleName(getContext()));
                intent.putExtra("category-color", category.getIconColor());
                activity.startActivity(intent);
            }
        });



        return listItem;
    }


}
