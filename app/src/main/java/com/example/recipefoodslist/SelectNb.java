package com.example.recipefoodslist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class SelectNb extends AppCompatActivity {

    private ListView lvSpinner;
    private Button bt;
    private SelectNbAdapter adapter;
    private static List<String> selectedRecipeList = new Vector<String>();
    private Map<String, String> recipesNbPeople = new HashMap();

    private List<String> ingredientQuantityVector = new Vector<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_nb);
        lvSpinner = (ListView) findViewById(R.id.listview_spinner);
        bt = (Button) findViewById(R.id.ingredientListBtn);

        ArrayList<String> mSpinnerData = new ArrayList<>();
        mSpinnerData.add("1");
        mSpinnerData.add("2");
        mSpinnerData.add("3");
        mSpinnerData.add("4");

        adapter = new SelectNbAdapter(selectedRecipeList, mSpinnerData, this);
        lvSpinner.setAdapter(adapter);

        onBtnClick();
    }

    private void onBtnClick(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNbSelected();
                try {
                    getIngredientQuantity(selectedRecipeList); //Update the recipes selected byt the user
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                openIngredientsListFct(); //Open the Ingredients list activity
            }
        });
    }

    private void getIngredientQuantity(List<String> recipe) throws JSONException {
        JSONObject jsonObject = ReadDataJson.getRecipesInput(getExternalFilesDir(null).toString());        //Get "Recipes input"
        int nbRecipesSelected = recipe.size();
        while(nbRecipesSelected != 0){
            JSONObject recipeObj = jsonObject.getJSONObject(recipe.get(nbRecipesSelected-1));         //Get a recipe
            JSONArray ingredientObj = recipeObj.getJSONArray("Ingredients");                    //Get the ingredients
            int size = ingredientObj.length();
            while (size != 0){
                JSONObject getFirstIngredient = ingredientObj.getJSONObject(size-1);            //Get the i ingredient
                String getNameIngredient = getFirstIngredient.getString("Name");
                int getQuantityIngredient = Integer.valueOf(getFirstIngredient.getString("Quantity"));
                String getUnitIngredient = getFirstIngredient.getString("Unit");
                ingredientQuantityVector.add(getNameIngredient + " " + getQuantityIngredient + " " + getUnitIngredient);
                --size;
            }
            if(size == 0){
                ShoppingIngredientList.sumSaveIngredientsList(ingredientQuantityVector);
                ingredientQuantityVector.clear();
            }
            --nbRecipesSelected;
        }
    }

    private void openIngredientsListFct(){
        Intent intent = new Intent(this, ShoppingIngredientList.class);
        startActivity(intent);
    }

    private void saveNbSelected(){
        recipesNbPeople = SelectNbAdapter.getMapRecipeNb();
    }

    public static void getRecipesSelected(List<String> selectedRecipeListInput){
        selectedRecipeList = selectedRecipeListInput;
    }

}