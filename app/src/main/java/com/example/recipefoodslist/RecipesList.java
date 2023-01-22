package com.example.recipefoodslist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class RecipesList extends AppCompatActivity {
    ListView lvAllRecipe;
    List<String> recipe = new Vector<String>();
    JSONObject jsonObject = new JSONObject();
    Map<String, Integer> ingredientQuantity = new HashMap<>();
    private Button OpenIngredientsListActivity;
    List<String> selectedRecipeList = new Vector<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start_shopping);

        //Upload the recipes
        try {
            updateRecipeList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Display the recipes from the JSON
        lvAllRecipe = findViewById(R.id.allRecipeView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, recipe);
        lvAllRecipe.setAdapter(adapter);

        //Check the selected recipes from the JSON in the listVIew
        try {
            checkRecipesSelectedFromJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Update the previously recipes list selected
        getItemSelected();

        //Get recipes selected from the user
        lvAllRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedRecipeList.contains(String.valueOf((lvAllRecipe.getItemAtPosition(i))))) { //.substring(0, String.valueOf((lvAllRecipe.getItemAtPosition(i))).indexOf("\n"))
                    selectedRecipeList.remove(lvAllRecipe.getItemAtPosition(i));
                    ShoppingIngredientList.ingredientNameQty.clear();
                    ingredientQuantity.clear();
                    ShoppingIngredientList.eraseIngredientsSelectedMemory();
                } else {
                    selectedRecipeList.add(String.valueOf(lvAllRecipe.getItemAtPosition(i)));
                }
            }
        });

        lvAllRecipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    jsonObject = ReadDataJson.getRecipesInput(getExternalFilesDir(null).toString());
                    JSONObject recipeObj = jsonObject.getJSONObject(String.valueOf((lvAllRecipe.getItemAtPosition(i))));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeObj.getString("Link")));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        //Open an activity with the list of ingredients
        OpenIngredientsListActivity = (Button) findViewById(R.id.openIngredientListActivity);
        OpenIngredientsListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getIngredientQuantity(selectedRecipeList); //Update the recipes selected byt the user
                    WriteDataJson.saveRecipesSelectedJSON(selectedRecipeList, getExternalFilesDir(null).toString()); //Save the selected recipes into the JSON
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                openIngredientsListFct(); //Open the Ingredients list activity
            }
        });
    }

    private void updateRecipeList() throws JSONException {
        jsonObject = ReadDataJson.getRecipesInput(getExternalFilesDir(null).toString());
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){
            String key = keys.next();
            JSONObject recipeObj = jsonObject.getJSONObject(key);
            String link = recipeObj.getString("Link");
            //recipe.add(key + "\n" + link);
            recipe.add(key);
        }
    }

    private void getIngredientQuantity(List<String> recipe) throws JSONException {
        jsonObject = ReadDataJson.getRecipesInput(getExternalFilesDir(null).toString());        //Get "Recipes input"
        int nbRecipesSelected = recipe.size();
        while(nbRecipesSelected != 0){
            JSONObject recipeObj = jsonObject.getJSONObject(recipe.get(nbRecipesSelected-1));         //Get a recipe
            JSONArray ingredientObj = recipeObj.getJSONArray("Ingredients");                    //Get the ingredients
            int size = ingredientObj.length();
            while (size != 0){
                JSONObject getFirstIngredient = ingredientObj.getJSONObject(size-1);            //Get the i ingredient
                String getNameIngredient = getFirstIngredient.getString("Name");
                int getQuantityIngredient = Integer.valueOf(getFirstIngredient.getString("Quantity"));
                ingredientQuantity.put(getNameIngredient, getQuantityIngredient);
                --size;
            }
            if(size == 0){
                ShoppingIngredientList.sumSaveIngredients(ingredientQuantity);
                ingredientQuantity.clear();
            }
            --nbRecipesSelected;
        }
    }

    public void openIngredientsListFct(){
        Intent intent = new Intent(this, ShoppingIngredientList.class);
        startActivity(intent);
    }

    public void checkRecipesSelectedFromJson() throws JSONException {
        //Get the recipes selected from the JSON
        List<String> recipesSelectedList = null;
        recipesSelectedList = ReadDataJson.getRecipesSelected(String.valueOf(getExternalFilesDir(null)));
        //Check the recipes selected from the JSON on the listView
        for (int i = 0; i < recipesSelectedList.size(); ++i) {
            for (int j = 0; j < lvAllRecipe.getAdapter().getCount(); ++j) {
                if ((String.valueOf((lvAllRecipe.getItemAtPosition(j)))).equals(recipesSelectedList.get(i))) {
                    lvAllRecipe.setItemChecked(j, true);
                }
            }
        }
    }

    public void getItemSelected(){
        for (int i = 0; i < recipe.size(); i++){
            boolean isRecipeSelected = lvAllRecipe.isItemChecked(i);
            if (isRecipeSelected == true){
                selectedRecipeList.add(String.valueOf(lvAllRecipe.getItemAtPosition(i)));
            }
        }
    }

}
