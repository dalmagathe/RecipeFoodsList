package com.example.recipefoodslist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import androidx.appcompat.app.AlertDialog;
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

public class ShoppingIngredientList extends AppCompatActivity {

    ListView lvAllIngredient, lvElementAdded;
    Button btnAddingElm;
    List<String> adapterIngredientsList = new Vector<>();
    List<String> elementAddedList = new Vector<>();
    List<String> elementSelected = new Vector<>();
    List<String> ingredientSelected = new Vector<>();
    Map<String, Pair<Float, String>> ingredientQtyMap = new HashMap<>();
    ArrayAdapter<String> adapter, adapterElementAdded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_shopping_ingredient_list);

        btnAddingElm = findViewById(R.id.addElementsBtn);
        lvAllIngredient = findViewById(R.id.allIngredientView);
        lvElementAdded = findViewById(R.id.elementAddedView);

        //Get the ingredients from recipes before display them
        try {getRecipesIngredients();} catch (JSONException e) {throw new RuntimeException(e);}
        //Get the elements added
        try {getElementAdded();} catch (JSONException e) {e.printStackTrace();}

        mapToList();

        //List view adapters
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, adapterIngredientsList);
        lvAllIngredient.setAdapter(adapter);
        adapterElementAdded = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, elementAddedList);
        lvElementAdded.setAdapter(adapterElementAdded);

        //Check the selected recipes from the JSON in the listVIew
        try {selectIngredients();selectElements();} catch (JSONException e) {e.printStackTrace();}

        lvAllIngredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray arrayItemChecked = lvAllIngredient.getCheckedItemPositions();
                Pattern p = Pattern.compile("(\\D*)\\s\\d.*");
                Matcher m = p.matcher(String.valueOf((lvAllIngredient.getItemAtPosition(i))));
                if (arrayItemChecked.get(i)) {
                    if (m.matches()) {
                        ingredientSelected.add(m.group(1));
                    }
                    else {
                        ingredientSelected.add("");
                    }
                }
                else {
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
                //Create dialog to remove item
                AlertDialog.Builder dialogIngredientNull = new AlertDialog.Builder(ShoppingIngredientList.this);
                dialogIngredientNull.setTitle("Information");
                dialogIngredientNull.setMessage("Do you want to delete this item?");
                AlertDialog dialogRemoveElement = dialogIngredientNull.create();

                dialogIngredientNull.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k) {
                        for (int j = 0; j < elementAddedList.size(); ++j) {
                            String nameElement;
                            if (elementAddedList.get(j) == lvElementAdded.getItemAtPosition(i)) {
                                Pattern p = Pattern.compile("(\\D*)\\s\\d.*");
                                Matcher m = p.matcher(lvElementAdded.getItemAtPosition(i).toString());
                                if (m.matches()) {
                                    nameElement = m.group(1);
                                } else {
                                    nameElement = "";
                                }
                                WriteDataJson.removeElementJSON(getExternalFilesDir(null).toString(), nameElement);
                                elementAddedList.remove(j);
                                adapterElementAdded = new ArrayAdapter<String>(ShoppingIngredientList.this, android.R.layout.simple_list_item_multiple_choice, elementAddedList);
                                lvElementAdded.setAdapter(adapterElementAdded);

                                dialogRemoveElement.cancel();
                                break;
                            }
                        }
                    }
                });
                dialogIngredientNull.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogRemoveElement.cancel();
                    }
                });

                dialogIngredientNull.create().show();
                return false;
            }
        });

        onBtnAddElmClick();
    }

    private void getRecipesIngredients() throws JSONException {
        JSONObject allRecipes = ReadDataJson.getRecipesInput(getExternalFilesDir(null).toString()); //Get "Recipes input" from JSON
        Map<String, String> recipesNbSelected = ReadDataJson.getRecipeNbSelected(getExternalFilesDir(null).toString()); //Get the recipes + number selected by the user
        for (Map.Entry<String, String> pair : recipesNbSelected.entrySet()) {
            String recipeName = pair.getKey();                            //Get the recipe selected by the user
            JSONObject recipeObj = allRecipes.getJSONObject(recipeName);  //Search for the recipe in JSON file
            int nbSelected = Integer.parseInt(pair.getValue());                //Get the selected nb of people for the recipe
            int nbInitial = Integer.parseInt(recipeObj.getString("Nb"));  //Get the initial nb of people for the recipe

            // Get the correct quantity + ingredients' name + unit
            JSONArray recipeIngredientsObj = recipeObj.getJSONArray("Ingredients");           //Get the ingredients
            int ingredientNbr = recipeIngredientsObj.length();
            while (ingredientNbr != 0){
                JSONObject getIngredient = recipeIngredientsObj.getJSONObject(ingredientNbr-1);   //Get the i ingredient
                String ingredientQuantity = getIngredient.getString("Quantity");
                float ingredientQuantityOp = ((Integer.valueOf(ingredientQuantity)) * 1.0f / nbInitial) * nbSelected;
                if (ingredientQtyMap.containsKey(getIngredient.getString("Name"))){
                    float quantity = ingredientQtyMap.get(getIngredient.getString("Name")).first;
                    String unit = ingredientQtyMap.get(getIngredient.getString("Name")).second;
                    ingredientQtyMap.put(getIngredient.getString("Name"), new Pair<>(quantity + ingredientQuantityOp, unit));
                }
                else{
                    ingredientQtyMap.put(getIngredient.getString("Name"), new Pair<>(ingredientQuantityOp, getIngredient.getString("Unit")));
                }
                --ingredientNbr;
            }
        }
    }

    private void getElementAdded() throws JSONException {
        JSONArray elementsObj = ReadDataJson.getElementsAdded(getExternalFilesDir(null).toString());
        int nbElements = elementsObj.length();
        while (nbElements != 0){
            JSONObject elementsIndI = elementsObj.getJSONObject(nbElements-1);
            elementAddedList.add(elementsIndI.getString("Name") + " " + elementsIndI.getString("Quantity") + " " + elementsIndI.getString("Unit"));
            --nbElements;
        }
    }

    private void mapToList(){
        for (Map.Entry<String, Pair<Float, String>> pair : ingredientQtyMap.entrySet()) {
            adapterIngredientsList.add(pair.getKey() + " " + pair.getValue().first + " " + pair.getValue().second);
        }
    }

    private void onBtnAddElmClick(){
        btnAddingElm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ShoppingIngredientList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.popup_add_element);
                dialog.show();

                Spinner spinner = dialog.findViewById(R.id.spinElement);
                ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(dialog.getContext(), R.array.unit, android.R.layout.simple_spinner_item);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterSpinner);

                Button btnAdd = dialog.findViewById(R.id.btnAddElement);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Get information
                        EditText elementNameEt = (EditText) dialog.findViewById(R.id.elementName2);
                        EditText elementQtyEt = (EditText) dialog.findViewById(R.id.elementQty2);
                        Spinner elementUnitEt = (Spinner) dialog.findViewById(R.id.spinElement);

                        //Save information into a list
                        elementAddedList.add(elementNameEt.getText().toString() + " " + elementQtyEt.getText().toString() + " " + elementUnitEt.getSelectedItem().toString());

                        //Display the information
                        adapterElementAdded.notifyDataSetChanged();
                        lvElementAdded.setAdapter(adapterElementAdded);

                        //Save into JSON
                        try {
                            WriteDataJson.saveNewElementJSON(getExternalFilesDir(null).toString(), elementNameEt.getText().toString(), elementQtyEt.getText().toString(), elementUnitEt.getSelectedItem().toString());
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
            WriteDataJson.saveSelectedItemsJSON(ingredientSelected, "Ingredients selected", getExternalFilesDir(null).toString());
            WriteDataJson.saveSelectedItemsJSON(elementSelected, "Elements selected", getExternalFilesDir(null).toString());
            if(!adapterIngredientsList.isEmpty()){adapterIngredientsList.clear();}
            if(!ingredientSelected.isEmpty()){ingredientSelected.clear();}
            if(!elementSelected.isEmpty()){elementSelected.clear();}
            if(!elementAddedList.isEmpty()){elementAddedList.clear();}
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectIngredients() throws JSONException {
        //Get the recipes selected from the JSON
        String name;
        ingredientSelected = ReadDataJson.getIngredientsSelected(String.valueOf(getExternalFilesDir(null)));

        for (int i = 0; i < ingredientSelected.size(); ++i) {
            for (int j = 0; j < lvAllIngredient.getAdapter().getCount(); ++j) {
                Pattern p = Pattern.compile("(\\D*)\\s\\d.*");
                Matcher m = p.matcher(String.valueOf((lvAllIngredient.getItemAtPosition(j))));
                if (m.matches()) {
                    name = m.group(1);
                }
                else {
                    name ="";
                }
                if (name.equals(ingredientSelected.get(i))) {
                    lvAllIngredient.setItemChecked(j, true);
                }
            }
        }
    }

    public void selectElements() throws JSONException {
        //Get the recipes selected from the JSON
        elementSelected = ReadDataJson.getElementsSelected(String.valueOf(getExternalFilesDir(null)));
        //Check the recipes selected from the JSON on the listView
        for (int i = 0; i < elementSelected.size(); ++i) {
            for (int j = 0; j < lvElementAdded.getAdapter().getCount(); ++j) {
                if ((String.valueOf((lvElementAdded.getItemAtPosition(j)))).equals(elementSelected.get(i))) {
                    lvElementAdded.setItemChecked(j, true);
                }
            }
        }
    }

}
