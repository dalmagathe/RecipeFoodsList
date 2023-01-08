package com.example.recipefoodslist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ShoppingIngredientList extends AppCompatActivity {

    static Map<String, Integer> ingredientQuantity = new HashMap<>();
    static ListView lvAllIngredient;
    Map<String, Integer> ingredientQuantityList = new HashMap<>();
    static List<String> ingredientNameQty = new Vector<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_ingredient_list);

        lvAllIngredient = findViewById(R.id.allIngredientView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ingredientNameQty);
        lvAllIngredient.setAdapter(adapter);
    }

    static public void main(Map<String, Integer> ingredientQuantity) {
        ingredientNameQty.clear();
        for (Map.Entry<String, Integer> entry : ingredientQuantity.entrySet()) {
            String k = entry.getKey();
            int v = entry.getValue();
            ingredientNameQty.add(k + " " + String.valueOf(v));
        }
    }
}
