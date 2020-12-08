package com.example.android.my_trip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelnortherntaiwan.R;

import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter<ExpenseItem> {

    public ExpenseAdapter(Context context, ArrayList<ExpenseItem> expenseList){
        super(context, 0,expenseList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.expense_spinner_layout, parent, false
            );
        }

        ImageView imageViewExpense = convertView.findViewById(R.id.image_view_expense_category);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        ExpenseItem currentItem = getItem(position);

        if(currentItem != null){
            imageViewExpense.setImageResource(currentItem.getmCategoryImage());
            textViewName.setText(currentItem.getmCategoryName());
        }

        return convertView;
    }
}
