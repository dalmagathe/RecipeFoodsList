package com.example.recipefoodslist;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class GroceryList extends AppCompatActivity {
    ListView lvAllRecipe;
    List<String> recipe = new Vector<String>();
    JSONObject jsonObject = new JSONObject();
    Map<String, Integer> ingredientQuantity = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_shopping);

        try {
            updateRecipeList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lvAllRecipe = findViewById(R.id.allRecipeView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, recipe);
        lvAllRecipe.setAdapter(adapter);

        lvAllRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (String) (lvAllRecipe.getItemAtPosition(i));
                try {
                    getIngredientQuantity(selectedFromList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateRecipeList() throws JSONException {
        jsonObject = ReadDataJson.main(getExternalFilesDir(null).toString());
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){
            String key = keys.next();
            recipe.add(key);
        }
    }

    private void getIngredientQuantity(String recipe) throws JSONException {
        jsonObject = ReadDataJson.main(getExternalFilesDir(null).toString());
        JSONObject recipeObj = jsonObject.getJSONObject(recipe);         //Get a recipe
        JSONArray ingredientObj = recipeObj.getJSONArray("Ingredients");      //Get the ingredients
        int size = ingredientObj.length();
        while (size != 0){
            JSONObject getFirstIngredient = ingredientObj.getJSONObject(size-1);
            String getNameIngredient = getFirstIngredient.getString("Name");
            int getQuantityIngredient = Integer.valueOf(getFirstIngredient.getString("Quantity"));
            ingredientQuantity.put(getNameIngredient, getQuantityIngredient);
            --size;
        }
    }
}
