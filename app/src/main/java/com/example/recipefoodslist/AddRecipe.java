package com.example.recipefoodslist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] Ingredient = new String[]{"Tomate", ""};
    private String[] Qty = new String[]{"500", "0"};
    private String[] Unit = new String[]{"gr", ""};
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>(); //arrayList
    private ListView listView; //lv
    private IngredientAdapter ingredientAdapter;
    private int ingredientNB = 0;

    EditText etIngredientName;
    EditText etIngredientQuantity;
    Spinner etIngredientUnit;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        Spinner spinner = findViewById(R.id.spinnerUnit);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.unit, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(this);

        etIngredientName = (EditText) findViewById(R.id.ingredientName);
        etIngredientQuantity = (EditText) findViewById(R.id.ingredientQuantity);
        etIngredientUnit = (Spinner) findViewById(R.id.spinnerUnit);
        bt = (Button) findViewById(R.id.addIngredientBtn);
        listView = findViewById(R.id.listView);

        //arrayList
        populateList(ingredientArrayList);
        //adapter
        ingredientAdapter = new IngredientAdapter(AddRecipe.this,ingredientArrayList);
        listView.setAdapter(ingredientAdapter);

        onBtnClick();

    }

    public void onBtnClick(){
        ++ingredientNB;
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient[1] = etIngredientName.getText().toString();
                Qty[1] = etIngredientQuantity.getText().toString();
                Unit[1] = etIngredientUnit.getSelectedItem().toString();
                populateList(ingredientArrayList);
                ingredientAdapter.notifyDataSetChanged();
            }
        });
    }

    private void populateList(ArrayList<Ingredient> ingredientArrayList){
        Ingredient ingredientModel = new Ingredient();
        ingredientModel.setIngredient(Ingredient[ingredientNB]);
        ingredientModel.setQty(Qty[ingredientNB]);
        ingredientModel.setUnit(Unit[ingredientNB]);
        ingredientArrayList.add(ingredientModel);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}