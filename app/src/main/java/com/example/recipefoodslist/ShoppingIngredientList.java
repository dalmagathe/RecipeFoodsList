package com.example.recipefoodslist;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLDisplay;

public class ShoppingIngredientList extends AppCompatActivity {

    static Map<String, Integer> ingredientQty = new HashMap<>();
    static ListView lvAllIngredient;
    Map<String, Integer> ingredientQuantityList = new HashMap<>();
    static List<String> ingredientNameQty = new Vector<>();
    static List<String> ingredientSelected = new Vector<>();
    private Button btn;
    ArrayAdapter<String> adapter;
    //private List<String> elementAddedList = new Vector<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_ingredient_list);

        btn = (Button) findViewById(R.id.addElementsBtn);

        lvAllIngredient = findViewById(R.id.allIngredientView);
        convertMapToList();
        try {
            addPreviousElementAddedToList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ingredientNameQty);
        lvAllIngredient.setAdapter(adapter);

        //Check the selected recipes from the JSON in the listVIew
        try {
            checkRecipesSelectedFromJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Update the previously recipes list selected
        getItemSelected();

        lvAllIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray arrayItemChecked = lvAllIngredient.getCheckedItemPositions();
                for (int j = 0; j < adapter.getCount(); j++) {
                    if (arrayItemChecked.get(i) && !(ingredientSelected.contains(String.valueOf((lvAllIngredient.getItemAtPosition(i)))))) {
                        ingredientSelected.add(String.valueOf((lvAllIngredient.getItemAtPosition(i))));
                    }
                    else {
                        ingredientSelected.remove(lvAllIngredient.getItemAtPosition(i));
                    }
                }
            }
        });

        onBtnClick();
    }

    private void onBtnClick(){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get information
                EditText elementNameEt = (EditText) findViewById(R.id.elementName);
                EditText elementQtyEt = (EditText) findViewById(R.id.elementQty);
                Spinner elementUnitEt = (Spinner) findViewById(R.id.elementUnit);

                //Save information into a list
                ingredientNameQty.add(elementNameEt.getText().toString() + " " + elementQtyEt.getText().toString());

                //Display the information
                adapter.notifyDataSetChanged();
                lvAllIngredient.setAdapter(adapter);

                //Save into JSON
                try {
                    WriteDataJson.saveNewElementAddedJSON(getExternalFilesDir(null).toString(), elementNameEt.getText().toString(), elementQtyEt.getText().toString(), "gr");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            WriteDataJson.saveIngredientSelectedJSON(ingredientSelected, getExternalFilesDir(null).toString());
            ingredientQty.clear();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertMapToList() {
        ingredientNameQty.clear();
        for (Map.Entry<String, Integer> entry : ingredientQty.entrySet()) {
            String k = entry.getKey();
            int v = entry.getValue();
            ingredientNameQty.add(k + " " + String.valueOf(v));
        }
    }

    private void addPreviousElementAddedToList() throws JSONException {
        JSONArray elementsObj = ReadDataJson.getElementsAdded(getExternalFilesDir(null).toString());
        int nbElements = elementsObj.length();
        while (nbElements != 0){
            JSONObject elementsIndI = elementsObj.getJSONObject(nbElements-1);
            String name = elementsIndI.getString("Name");
            String qty = elementsIndI.getString("Quantity");
            //String unit = elementsIndI.getString("Unit");
            ingredientNameQty.add(name + " " + qty);
            --nbElements;
        }

    }

    static public void sumSaveIngredients(Map<String, Integer> ingredientQuantity){
        for (Map.Entry<String, Integer> entry : ingredientQuantity.entrySet()) {
            if(ingredientQty.containsKey(entry.getKey())){
                int value = ingredientQty.get(entry.getKey()) + entry.getValue();
                ingredientQty.put(entry.getKey(), value);
            }
            else{
                String k = entry.getKey();
                int v = entry.getValue();
                ingredientQty.put(k, v);
            }
        }
    }

    public void checkRecipesSelectedFromJson() throws JSONException {
        //Get the recipes selected from the JSON
        List<String> ingredientsSelectedList = null;
        ingredientsSelectedList = ReadDataJson.getIngredientsSelected(String.valueOf(getExternalFilesDir(null)));
        //Check the recipes selected from the JSON on the listView
        for (int i = 0; i < ingredientsSelectedList.size(); ++i) {
            for (int j = 0; j < lvAllIngredient.getAdapter().getCount(); ++j) {
                if ((String.valueOf((lvAllIngredient.getItemAtPosition(j)))).equals(ingredientsSelectedList.get(i))) {
                    lvAllIngredient.setItemChecked(j, true);
                }
            }
        }
    }

    public void getItemSelected(){
        for (int i = 0; i < ingredientNameQty.size(); i++){
            boolean isRecipeSelected = lvAllIngredient.isItemChecked(i);
            if (isRecipeSelected == true){
                ingredientSelected.add(String.valueOf(lvAllIngredient.getItemAtPosition(i)));
            }
        }
    }

    static public void eraseIngredientsSelectedMemory(){
        ingredientSelected.clear();
    }
}
