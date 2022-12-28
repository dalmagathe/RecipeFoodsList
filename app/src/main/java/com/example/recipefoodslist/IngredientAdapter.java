package com.example.recipefoodslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Ingredient> ingredientArrayList;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredientArrayList) {

        this.context = context;
        this.ingredientArrayList = ingredientArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return ingredientArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredientArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null, true);

            holder.tvProduct = (TextView) convertView.findViewById(R.id.tvIngredient);
            holder.tvQty = (TextView) convertView.findViewById(R.id.tvQty);
            holder.tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tvProduct.setText(ingredientArrayList.get(position).getIngredient());
        holder.tvQty.setText(String.valueOf(ingredientArrayList.get(position).getQty()));
        holder.tvUnit.setText(String.valueOf(ingredientArrayList.get(position).getUnit()));

        return convertView;
    }

    private class ViewHolder {

        protected TextView tvProduct, tvQty, tvUnit;

    }

}
