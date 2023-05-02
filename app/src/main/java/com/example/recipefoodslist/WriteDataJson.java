package com.example.recipefoodslist;

import android.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class WriteDataJson {
    
    static String nameFile = "/recipesData.json";

    //Method used to check the existence of a file and return the file contents
    private static String returnFile(String path) throws IOException {
        String data = "";

        InputStream inputStream = new FileInputStream(path + nameFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = bufferedReader.readLine())!= null){
            data = data + line;
        }
        return data;
    }

    //Function to add a recipe with ingredients and url to the JSON
    public static void addDataJson(String path, String Recipe, String Nb, String Link, Map<String, Pair<String, String>> ingredient) throws IOException, JSONException {
        try{
            File f = new File(path + nameFile);

            if(!(f.exists())){
                FileWriter file = new FileWriter(f.getAbsoluteFile(), true);
                JSONObject objAllRecipe = createJSON(Recipe, Link, Nb, ingredient);
                file.write(objAllRecipe.toString(2));
                file.flush();
                file.close();
            }
            else{
                if(!returnFile(path).isEmpty()){
                    JSONObject jsonObject = new JSONObject(returnFile(path));                           //Get JSON file
                    writeNewJSONData(f, jsonObject, Recipe, Link, Nb, ingredient);
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Function to create a JSON file
    private static JSONObject createJSON(String Recipe, String Link, String Nb, Map<String, Pair<String, String>> ingredient) throws JSONException {
        JSONArray testArray = new JSONArray();

        for (Map.Entry<String, Pair<String, String>> pair : ingredient.entrySet()) {
            JSONObject objNewIngredient = new JSONObject();
            objNewIngredient.put("Name", pair.getKey());
            objNewIngredient.put("Quantity", pair.getValue().first);
            objNewIngredient.put("Unit", pair.getValue().second);
            testArray.put(objNewIngredient);
        }

        JSONObject objAllIngredient = new JSONObject();
        objAllIngredient.put("Nb", Nb);
        objAllIngredient.put("Link", Link);
        objAllIngredient.put("Ingredients", testArray);

        JSONObject objAllRecipe = new JSONObject();
        objAllRecipe.put(Recipe, objAllIngredient);

        JSONObject objRecipe = new JSONObject();
        objRecipe.put("Recipes input", objAllRecipe);

        return objRecipe;
    }

    //Function to create a JSON object for a recipe
    private static void writeNewJSONData(File f, JSONObject jsonObject, String Recipe, String Link, String Nb, Map<String, Pair<String, String>> ingredient) throws JSONException, IOException {
        JSONObject allRecipeObj = jsonObject.getJSONObject("Recipes input");
        if(!(allRecipeObj.has(Recipe))){
            JSONArray testArray = new JSONArray();

            for (Map.Entry<String, Pair<String, String>> pair : ingredient.entrySet()) {
                JSONObject objNewIngredient = new JSONObject();
                objNewIngredient.put("Name", pair.getKey());
                objNewIngredient.put("Quantity", pair.getValue().first);
                objNewIngredient.put("Unit", pair.getValue().second);
                testArray.put(objNewIngredient);
            }

            JSONObject objAllIngredient = new JSONObject();
            objAllIngredient.put("Nb", Nb);
            objAllIngredient.put("Link", Link);
            objAllIngredient.put("Ingredients", testArray);

            allRecipeObj.put(Recipe, objAllIngredient);
        }
        else{

        }

        FileWriter file = new FileWriter(f.getAbsoluteFile(), false);
        file.write(jsonObject.toString(2));
        file.flush();
        file.close();
    }

    //Function to save a new element added by the user in the shopping list
    static public void saveNewElementJSON(String path, String ElementName, String ElementQty, String ElementUnit) throws JSONException, IOException {
        try{
            if(!returnFile(path).isEmpty()){
                JSONObject jsonObject = new JSONObject(returnFile(path));                           //Get JSON file

                if(jsonObject.has("Elements added")){
                    JSONObject objNewElement = new JSONObject();
                    objNewElement.put("Name", ElementName);
                    objNewElement.put("Quantity", ElementQty);
                    objNewElement.put("Unit", ElementUnit);

                    jsonObject.getJSONArray("Elements added").put(objNewElement);
                }
                else{
                    JSONObject objNewElement = new JSONObject();
                    objNewElement.put("Name", ElementName);
                    objNewElement.put("Quantity", ElementQty);
                    objNewElement.put("Unit", ElementUnit);

                    JSONArray testArray = new JSONArray();
                    testArray.put(objNewElement);

                    jsonObject.put("Elements added", testArray);
                }

                File f = new File(path + nameFile);
                FileWriter file = new FileWriter(f.getAbsoluteFile(), false);
                file.write(jsonObject.toString(2));
                file.flush();
                file.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    //Function to remove an element from the grocery list
    static public void removeElementJSON (String path, String name){

        try{
            if(!returnFile(path).isEmpty()){
                JSONObject jsonObject = new JSONObject(returnFile(path));                           //Get JSON file
                JSONArray elementAddedObj = jsonObject.getJSONArray("Elements added");

                for (int i=0; i < elementAddedObj.length(); i++) {
                    JSONObject nameElement = elementAddedObj.getJSONObject(i);
                    String test = nameElement.get("Name").toString();
                    if (test.equals(name)) {
                        elementAddedObj.remove(i);
                    }
                }

                jsonObject.put("Elements added", elementAddedObj);

                File f = new File(path + nameFile);
                FileWriter file = new FileWriter(f.getAbsoluteFile(), false);
                file.write(jsonObject.toString(2));
                file.flush();
                file.close();

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Function to save recipes, ingredients and elements selected
    static public void saveSelectedItemsJSON(List<String> selectedItemsList, String keyName, String path) throws JSONException, IOException {
        try{
            if(!returnFile(path).isEmpty()){
                JSONObject jsonObject = new JSONObject(returnFile(path));                           //Get JSON file

                JSONArray jsonArray = new JSONArray();
                for (int i=0; i < selectedItemsList.size(); i++) {
                    jsonArray.put(selectedItemsList.get(i));
                }

                jsonObject.put(keyName, jsonArray);

                File f = new File(path + nameFile);
                FileWriter file = new FileWriter(f.getAbsoluteFile(), false);
                file.write(jsonObject.toString(2));
                file.flush();
                file.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Function to save the number of people on each recipe selected by the user
    static public void saveNbPeopleSelectedJSON(Map<String, String> recipeNbMap, String path){
        try{
            if(!returnFile(path).isEmpty()){
                JSONObject objAllRecipes= new JSONObject();
                JSONObject jsonObject = new JSONObject(returnFile(path));                           //Get JSON file
                if(jsonObject.has("Nb selected")){
                    jsonObject.remove("Nb selected");
                }
                for (Map.Entry<String, String> pair : recipeNbMap.entrySet()) {
                    if(jsonObject.has("Nb selected")){
                        JSONObject objNewElement = new JSONObject();
                        objNewElement.put("Name recipe", pair.getKey());
                        objNewElement.put("Nb", pair.getValue());

                        jsonObject.getJSONArray("Nb selected").put(objNewElement);
                    }
                    else{
                        JSONObject objNewElement = new JSONObject();
                        objNewElement.put("Name recipe", pair.getKey());
                        objNewElement.put("Nb", pair.getValue());

                        JSONArray testArray = new JSONArray();
                        testArray.put(objNewElement);

                        jsonObject.put("Nb selected", testArray);
                    }
                }

                File f = new File(path + nameFile);
                FileWriter file = new FileWriter(f.getAbsoluteFile(), false);
                file.write(jsonObject.toString(2));
                file.flush();
                file.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
