package com.example.recipefoodslist;

import android.content.Context;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class RecipesList extends AppCompatActivity {
    private ListView lvAllRecipes;
    private List<String> recipe = new Vector<String>();
    private JSONObject jsonObject;
    private List<String> ingredientQuantityVector = new Vector<String>();
    private Button OpenNbSelectionActivity;
    private List<String> selectedRecipeList = new Vector<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialise the json object
        try {jsonObject = ReadDataJson.getRecipesInput(getExternalFilesDir(null).toString());} catch (JSONException e) {throw new RuntimeException(e);}

        //Remove the windows' title
        requestWindowFeature(Window.FEATURE_NO_TITLE); this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);getSupportActionBar().hide();

        //Set the view with the activity
        setContentView(R.layout.activity_start_shopping);

        //Upload the recipes
        try {updateRecipesList();} catch (JSONException e) {e.printStackTrace();}

        //Display the recipes from the JSON
        lvAllRecipes = findViewById(R.id.allRecipeListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, recipe);
        lvAllRecipes.setAdapter(adapter);

        //Check the selected recipes from the JSON in the listVIew
        try {checkRecipesSelectedFromJson();} catch (JSONException e) {e.printStackTrace();}

        //Update the previously recipes list selected
        getItemSelected();

        //Get recipes selected from the user
        lvAllRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedRecipeList.contains(String.valueOf((lvAllRecipes.getItemAtPosition(i))))) { //.substring(0, String.valueOf((lvAllRecipes.getItemAtPosition(i))).indexOf("\n"))
                    selectedRecipeList.remove(lvAllRecipes.getItemAtPosition(i));
                    ShoppingIngredientList.ingredientNameQty.clear();
                    ingredientQuantityVector.clear();
                    ShoppingIngredientList.eraseIngredientsSelectedMemory();
                } else {
                    selectedRecipeList.add(String.valueOf(lvAllRecipes.getItemAtPosition(i)));
                }
            }
        });

        lvAllRecipes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONObject recipeObj = jsonObject.getJSONObject(String.valueOf((lvAllRecipes.getItemAtPosition(i))));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeObj.getString("Link")));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        OpenNbSelectionActivity = (Button) findViewById(R.id.openNbSelection);
        OpenNbSelectionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WriteDataJson.saveRecipesSelectedJSON(selectedRecipeList, getExternalFilesDir(null).toString()); //Save the selected recipes into the JSON
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                SelectNb.getRecipesSelected(selectedRecipeList); //Update the recipes selected byt the user
                openRecipeListFct();
            }
        });
    }

    private void updateRecipesList() throws JSONException {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){recipe.add(keys.next());}
    }

    public void openRecipeListFct(){
        Intent intent = new Intent(this, SelectNb.class);
        startActivity(intent);
    }

    public void checkRecipesSelectedFromJson() throws JSONException {
        //Get the recipes selected from the JSON
        List<String> recipesSelectedList = ReadDataJson.getRecipesSelected(String.valueOf(getExternalFilesDir(null)));
        //Check the recipes selected from the JSON on the listView
        for (int i = 0; i < recipesSelectedList.size(); ++i) {
            for (int j = 0; j < lvAllRecipes.getAdapter().getCount(); ++j) {
                if ((String.valueOf((lvAllRecipes.getItemAtPosition(j)))).equals(recipesSelectedList.get(i))) {
                    lvAllRecipes.setItemChecked(j, true);
                }
            }
        }
    }

    public void getItemSelected(){
        for (int i = 0; i < recipe.size(); i++){
            boolean isRecipeSelected = lvAllRecipes.isItemChecked(i);
            if (isRecipeSelected == true){
                selectedRecipeList.add(String.valueOf(lvAllRecipes.getItemAtPosition(i)));
            }
        }
    }

}
