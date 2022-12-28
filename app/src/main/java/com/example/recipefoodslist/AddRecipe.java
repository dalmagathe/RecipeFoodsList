package com.example.recipefoodslist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity {

    private String[] Ingredient = new String[]{"Pizza", "Burger", "Pasta", "blablabla", "Rice","Sandwich","Chips","bla", "blabla", "test", "Pizza", "Burger", "Pasta", "blablabla"};
    private int[] Qty = new int[]{3, 4, 2, 1, 5,8,20,50,78,20,20,50,78,20};
    private String[] Unit = new String[]{"Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test", "Test"};
    private ArrayList<Ingredient> ingredientArrayList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        listView = findViewById(R.id.listView);

        ingredientArrayList = populateList();

        IngredientAdapter ingredientAdapter = new IngredientAdapter(this,ingredientArrayList);
        listView.setAdapter(ingredientAdapter);

    }

    private ArrayList<Ingredient> populateList(){

        ArrayList<Ingredient> list = new ArrayList<>();

        for(int i = 0; i < 14; i++){
            Ingredient ingredientModel = new Ingredient();
            ingredientModel.setIngredient(Ingredient[i]);
            ingredientModel.setQty(Qty[i]);
            ingredientModel.setUnit(Unit[i]);
            list.add(ingredientModel);
        }

        return list;
    }
}