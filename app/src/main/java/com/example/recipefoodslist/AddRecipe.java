package com.example.recipefoodslist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity {

    private String[] Ingredient = new String[]{"Tomate", ""};
    private int[] Qty = new int[]{500, 0};
    private String[] Unit = new String[]{"gr", ""};
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>(); //arrayList
    private ListView listView; //lv
    private IngredientAdapter ingredientAdapter;
    private int ingredientNB = 0;

    EditText et;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        et = (EditText) findViewById(R.id.ingredientName);
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
                Ingredient[1] = et.getText().toString();
                Qty[1] = 3;
                Unit[1] = "l";
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
}