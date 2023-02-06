package com.example.recipefoodslist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
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
    private static Map<String, String> recipesNbPeople = new HashMap();
    private List<String> nbSelected = new Vector<>();
    private List<String> name = new ArrayList<>();
    private List<String> spinnerNb = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_nb);
        lvSpinner = (ListView) findViewById(R.id.listview_spinner);
        bt = (Button) findViewById(R.id.ingredientListBtn);

        try {setMapRecipeSpinner();} catch (JSONException e) {throw new RuntimeException(e);}

        adapter = new SelectNbAdapter(this, name, spinnerNb);
        lvSpinner.setAdapter(adapter);

        onBtnClick();
    }

    private void onBtnClick() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNbSelected();
                WriteDataJson.saveNbJSON(recipesNbPeople, getExternalFilesDir(null).toString());
                openIngredientsListFct(); //Open the Ingredients list activity
            }
        });
    }

    private void openIngredientsListFct() {
        Intent intent = new Intent(this, ShoppingIngredientList.class);
        startActivity(intent);
    }

    private void saveNbSelected() {
        recipesNbPeople = SelectNbAdapter.getMapRecipeNb();
    }

    public static Map<String, String> getNbSelected() {
        Map<String, String> recipesNbPeopleOutput = recipesNbPeople;
        return recipesNbPeopleOutput;
    }

    public static void getRecipesSelected(List<String> selectedRecipeListInput) {
        selectedRecipeList = selectedRecipeListInput;
    }

    private void setMapRecipeSpinner() throws JSONException {
        ArrayList<String> mSpinnerData = new ArrayList<>();
        Map<String,String> recipeNb = ReadDataJson.getNbSelected(String.valueOf(getExternalFilesDir(null)));

        if(recipeNb.isEmpty()){
            name = selectedRecipeList;
            for (int j = 1; j < 5; ++j){{spinnerNb.add(String.valueOf(j));}}
        }
        else{
            for (Map.Entry<String, String> pair : recipeNb.entrySet()) {
                name.add(pair.getKey());

                int nbSelect = Integer.parseInt(pair.getValue());   //Previous nb selected by the user
                spinnerNb.add(String.valueOf(nbSelect));
                for (int j = 1; j < 5; ++j){if (j != nbSelect) {spinnerNb.add(String.valueOf(j));}}
            }
        }
    }
}