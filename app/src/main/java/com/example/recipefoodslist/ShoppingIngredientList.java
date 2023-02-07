package com.example.recipefoodslist;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.microedition.khronos.egl.EGLDisplay;

public class ShoppingIngredientList extends AppCompatActivity {

    static Map<String, Integer> ingredientQty = new HashMap<>();
    static List<String> ingredientQtyVector = new Vector<>();
    static ListView lvAllIngredient;
    static ListView lvElementAdded;
    private static Map<String, Pair<Float, String>> ingredientQuantityUnit = new HashMap<>();
    static List<String> ingredientNameQty = new Vector<>();
    static List<String> ingredientSelected = new Vector<>();
    static List<String> elementSelected = new Vector<>();
    private Button btn;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterElementAdded;
    private List<String> elementAddedList = new Vector<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_shopping_ingredient_list);

        btn = (Button) findViewById(R.id.addElementsBtn);
        lvAllIngredient = findViewById(R.id.allIngredientView);
        lvElementAdded = findViewById(R.id.elementAddedView);

        //Get the elements added before display them
        try {addPreviousElementAddedToList();} catch (JSONException e) {e.printStackTrace();}
        //Get the ingredients from recipes before display them
        try {getIngredientQuantity();} catch (JSONException e) {throw new RuntimeException(e);}
        transformIntoList();

        // List view adapters
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ingredientQtyVector);
        lvAllIngredient.setAdapter(adapter);
        adapterElementAdded = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, elementAddedList);
        lvElementAdded.setAdapter(adapterElementAdded);

        //Check the selected recipes from the JSON in the listVIew
        try {checkRecipesSelectedFromJson();checkElementsSelectedFromJson();} catch (JSONException e) {e.printStackTrace();}

        lvAllIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray arrayItemChecked = lvAllIngredient.getCheckedItemPositions();
                if (arrayItemChecked.get(i)) {
                    Pattern p = Pattern.compile("(\\D*)\\s\\d.*");
                    Matcher m = p.matcher(String.valueOf((lvAllIngredient.getItemAtPosition(i))));
                    if (m.matches()) {
                        ingredientSelected.add(m.group(1));
                    }
                    else {
                        ingredientSelected.add("");
                    }
                }
                else {
                    Pattern p = Pattern.compile("(\\D*)\\s\\d.*");
                    Matcher m = p.matcher(String.valueOf((lvAllIngredient.getItemAtPosition(i))));
                    if (m.matches()) {
                        ingredientSelected.remove(m.group(1));
                    }
                }
            }
        });

        lvElementAdded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray arrayItemChecked = lvElementAdded.getCheckedItemPositions();
                if (arrayItemChecked.get(i) && !(elementSelected.contains(String.valueOf((lvElementAdded.getItemAtPosition(i)))))) {
                    elementSelected.add(String.valueOf((lvElementAdded.getItemAtPosition(i))));
                }
                else {
                    elementSelected.remove(lvElementAdded.getItemAtPosition(i));
                }
            }
        });

        lvElementAdded.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog dialog = new Dialog(ShoppingIngredientList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.popup);
                dialog.show();

                Button btnYes = dialog.findViewById(R.id.yestn);
                Button btnNo = dialog.findViewById(R.id.noBtn);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int j = 0; j < elementAddedList.size(); ++j){
                            String name;
                            String test = lvElementAdded.getItemAtPosition(i).toString();
                            if (elementAddedList.get(j) == lvElementAdded.getItemAtPosition(i)){
                                elementAddedList.remove(j);
                                adapterElementAdded = new ArrayAdapter<String>(ShoppingIngredientList.this, android.R.layout.simple_list_item_multiple_choice, elementAddedList);
                                lvElementAdded.setAdapter(adapterElementAdded);
                                Pattern p = Pattern.compile("(\\D*)\\s\\d.*");
                                Matcher m = p.matcher(test);
                                if (m.matches()) {
                                    name = m.group(1);
                                }
                                else {
                                    name ="";
                                }
                                WriteDataJson.removeElementJSON(getExternalFilesDir(null).toString(), name);
                                dialog.cancel();
                                break;
                            }
                        }
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                return false;
            }
        });

        onBtnClick();
    }

    private void onBtnClick(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ShoppingIngredientList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.popup_add_element);
                dialog.show();

                Button btnAdd = dialog.findViewById(R.id.btnAddElement);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Get information
                        EditText elementNameEt = (EditText) dialog.findViewById(R.id.elementName2);
                        EditText elementQtyEt = (EditText) dialog.findViewById(R.id.elementQty2);
                        EditText elementUnitEt = (EditText) dialog.findViewById(R.id.elementUnit2);

                        //Save information into a list
                        elementAddedList.add(elementNameEt.getText().toString() + " " + elementQtyEt.getText().toString() + " " + elementUnitEt.getText().toString());

                        //Display the information
                        adapterElementAdded.notifyDataSetChanged();
                        lvElementAdded.setAdapter(adapterElementAdded);

                        //Save into JSON
                        try {
                            WriteDataJson.saveNewElementAddedJSON(getExternalFilesDir(null).toString(), elementNameEt.getText().toString(), elementQtyEt.getText().toString(), elementUnitEt.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        dialog.cancel();
                    }
                });

            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            WriteDataJson.saveIngredientSelectedJSON(ingredientSelected, getExternalFilesDir(null).toString());
            WriteDataJson.saveElementSelectedJSON(elementSelected, getExternalFilesDir(null).toString());
            ingredientQty.clear();
            ingredientQtyVector.clear();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPreviousElementAddedToList() throws JSONException {
        JSONArray elementsObj = ReadDataJson.getElementsAdded(getExternalFilesDir(null).toString());
        int nbElements = elementsObj.length();
        while (nbElements != 0){
            JSONObject elementsIndI = elementsObj.getJSONObject(nbElements-1);
            String name = elementsIndI.getString("Name");
            String qty = elementsIndI.getString("Quantity");
            String unit = elementsIndI.getString("Unit");
            elementAddedList.add(name + " " + qty + " " + unit);
            --nbElements;
        }
    }

    public void checkRecipesSelectedFromJson() throws JSONException {
        //Get the recipes selected from the JSON
        List<String> ingredientsSelectedList = null;
        String name;
        ingredientsSelectedList = ReadDataJson.getIngredientsSelected(String.valueOf(getExternalFilesDir(null)));
        //Check the recipes selected from the JSON on the listView
        for (int i = 0; i < ingredientsSelectedList.size(); ++i) {
            for (int j = 0; j < lvAllIngredient.getAdapter().getCount(); ++j) {
                Pattern p = Pattern.compile("(\\D*)\\s\\d.*");
                Matcher m = p.matcher(String.valueOf((lvAllIngredient.getItemAtPosition(j))));
                if (m.matches()) {
                    name = m.group(1);
                }
                else {
                    name ="";
                }
                if (name.equals(ingredientsSelectedList.get(i))) {
                    lvAllIngredient.setItemChecked(j, true);
                }
            }
        }
    }

    public void checkElementsSelectedFromJson() throws JSONException {
        //Get the recipes selected from the JSON
        List<String> elementsSelectedList = null;
        elementsSelectedList = ReadDataJson.getElementsSelected(String.valueOf(getExternalFilesDir(null)));
        //Check the recipes selected from the JSON on the listView
        for (int i = 0; i < elementsSelectedList.size(); ++i) {
            for (int j = 0; j < lvElementAdded.getAdapter().getCount(); ++j) {
                if ((String.valueOf((lvElementAdded.getItemAtPosition(j)))).equals(elementsSelectedList.get(i))) {
                    lvElementAdded.setItemChecked(j, true);
                }
            }
        }
    }

    private void getIngredientQuantity() throws JSONException {
        JSONObject jsonObject = ReadDataJson.getRecipesInput(getExternalFilesDir(null).toString()); //Get "Recipes input" from JSON
        Map<String, String> recipesNbSelected = SelectNb.getNbSelected();                                //Get the recipes + nb selected by the user
        for (Map.Entry<String, String> pair : recipesNbSelected.entrySet()) {
            String recipe = pair.getKey();                            //Get the recipe selected by the user
            JSONObject recipeObj = jsonObject.getJSONObject(recipe);  //Search for the recipe in JSON file
            int nbSelected = Integer.parseInt(pair.getValue());                //Get the selected nb of people for the recipe
            int nbInitial = Integer.parseInt(recipeObj.getString("Nb"));  //Get the initial nb of people for the recipe

            // Get the correct quantity + ingredients' name + unit
            JSONArray ingredientObj = recipeObj.getJSONArray("Ingredients");           //Get the ingredients
            int size = ingredientObj.length();
            while (size != 0){
                JSONObject getFirstIngredient = ingredientObj.getJSONObject(size-1);   //Get the i ingredient
                String getNameIngredient = getFirstIngredient.getString("Name");
                String ing = getFirstIngredient.getString("Quantity");
                float quantityOperation = ((Integer.valueOf(ing)) * 1.0f / nbInitial) * nbSelected;
                String getUnitIngredient = getFirstIngredient.getString("Unit");
                ingredientQuantityUnit.put(getNameIngredient, new Pair<>(quantityOperation, getUnitIngredient));
                --size;
            }
        }
    }

    private void transformIntoList(){
        for (Map.Entry<String, Pair<Float, String>> pair : ingredientQuantityUnit.entrySet()) {
            ingredientQtyVector.add(pair.getKey() + " " + pair.getValue().first + " " + pair.getValue().second);
        }
    }

    static public void eraseIngredientsSelectedMemory(){
        ingredientSelected.clear();
        elementSelected.clear();
    }
}
