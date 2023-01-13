package com.example.recipefoodslist;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ShoppingIngredientList extends AppCompatActivity {

    static Map<String, Integer> ingredientQuantity = new HashMap<>();
    static ListView lvAllIngredient;
    Map<String, Integer> ingredientQuantityList = new HashMap<>();
    static List<String> ingredientNameQty = new Vector<>();
    List<String> ingredientSelected = new Vector<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_ingredient_list);

        lvAllIngredient = findViewById(R.id.allIngredientView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ingredientNameQty);
        lvAllIngredient.setAdapter(adapter);

        lvAllIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray arrayItemChecked = lvAllIngredient.getCheckedItemPositions();
                for (int j = 0; j < adapter.getCount(); j++) {
                    if (arrayItemChecked.get(i) && !(ingredientSelected.contains(String.valueOf((lvAllIngredient.getItemAtPosition(i)))))) {
                        ingredientSelected.add(String.valueOf((lvAllIngredient.getItemAtPosition(i))));
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            WriteDataJson.saveIngredientSelectedJSON(ingredientSelected, getExternalFilesDir(null).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
