package com.example.recipefoodslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectNbAdapter extends BaseAdapter {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> mSpinnerItems;
    private List<String> mData;
    private Context mContext;
    private Spinner spinner;

    public static Map<String, String> keyValue = new HashMap<>();

    public SelectNbAdapter(List<String> data, ArrayList<String> spinnerItems, Context context) {
        mData = data;
        mContext = context;
        mSpinnerItems = spinnerItems;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    public String getItemEditText(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_adapter_selectnb_lv, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.row_item_textview);
        spinner = (Spinner) view.findViewById(R.id.row_item_spinner);

        textView.setText(mData.get(position));
        adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mSpinnerItems);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                keyValue.put(mData.get(position), adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    public static Map<String, String> getMapRecipeNb(){
        Map<String, String> recipesNbPeopleInput;
        recipesNbPeopleInput = keyValue;
        return recipesNbPeopleInput;
    }
}