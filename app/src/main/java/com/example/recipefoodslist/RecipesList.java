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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class RecipesList extends AppCompatActivity {

    List<String> recipesList = new Vector<String>();
    List<String> selectedRecipesList = new Vector<String>();
    JSONObject jsonObject;
    Button OpenNbSelectionActivity;
    ListView lvRecipes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialise the json object
        try {jsonObject = ReadDataJson.getRecipesInput(getExternalFilesDir(null).toString());} catch (JSONException e) {throw new RuntimeException(e);}

        //Remove the windows' title
        requestWindowFeature(Window.FEATURE_NO_TITLE); this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);getSupportActionBar().hide();

        //Set the view with the activity
        setContentView(R.layout.activity_recipes_list);

        //Get the recipes from JSON and display them
        try {updateRecipesList();} catch (JSONException e) {e.printStackTrace();}
        lvRecipes = findViewById(R.id.allRecipeListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, recipesList);
        lvRecipes.setAdapter(adapter);

        //Select in the lv the previous recipes selected
        try {selectRecipesSelectedFromJson();} catch (JSONException e) {e.printStackTrace();}

        //Get recipes selected from the user
        lvRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedRecipesList.contains(String.valueOf((lvRecipes.getItemAtPosition(i))))) {
                    selectedRecipesList.remove(lvRecipes.getItemAtPosition(i));
                    //ShoppingIngredientList.ingredientNameQty.clear();
                    //ShoppingIngredientList.eraseIngredientsSelectedMemory();
                } else {
                    selectedRecipesList.add(String.valueOf(lvRecipes.getItemAtPosition(i)));
                }
            }
        });

        lvRecipes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONObject recipeObj = jsonObject.getJSONObject(String.valueOf((lvRecipes.getItemAtPosition(i))));
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
                    WriteDataJson.saveRecipesSelectedJSON(selectedRecipesList, getExternalFilesDir(null).toString()); //Save the selected recipes into the JSON
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                openSelectNbActivity();
            }
        });
    }

    private void updateRecipesList() throws JSONException {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){recipesList.add(keys.next());}
    }

    private void selectRecipesSelectedFromJson() throws JSONException {
        //Get the recipes selected from the JSON
        selectedRecipesList = ReadDataJson.getRecipesSelected(String.valueOf(getExternalFilesDir(null)));
        //Check the recipes selected from the JSON on the listView
        for (int i = 0; i < selectedRecipesList.size(); ++i) {
            for (int j = 0; j < lvRecipes.getAdapter().getCount(); ++j) {
                if ((String.valueOf((lvRecipes.getItemAtPosition(j)))).equals(selectedRecipesList.get(i))) {
                    lvRecipes.setItemChecked(j, true);
                }
            }
        }
    }

    private void openSelectNbActivity(){
        Intent intent = new Intent(this, SelectNb.class);
        startActivity(intent);
    }


}
