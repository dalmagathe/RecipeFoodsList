package com.example.recipefoodslist;

import static java.lang.System.in;
import static java.lang.System.out;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class WriteDataJson {

    public static void main(String path, String Recipe, String Link, String Ingredient, String Qty, String Unit) throws IOException, JSONException {

        String data ="";

        try{
            File f = new File(path + "/newTestFile.json");

            if(!(f.exists())){
                FileWriter file = new FileWriter(f.getAbsoluteFile(), true);
                JSONObject objAllRecipe = createJSON(Recipe, Link, Ingredient, Qty, Unit);
                file.write(objAllRecipe.toString(2));
                file.flush();
                file.close();
            }
            else{
                InputStream inputStream = new FileInputStream(path + "/newTestFile.json");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine())!= null){
                    data = data + line;
                }

                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);                           //Get JSON file
                    writeNewJSONData(f, jsonObject, Recipe, Link, Ingredient, Qty, Unit);
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static JSONObject createJSON(String Recipe, String Link, String Ingredient, String Qty, String Unit) throws JSONException {
        JSONArray testArray = new JSONArray();

        JSONObject objNewIngredient = new JSONObject();
        objNewIngredient.put("Name", Ingredient);
        objNewIngredient.put("Quantity", Qty);
        objNewIngredient.put("Unit", Unit);
        testArray.put(objNewIngredient);

        JSONObject objAllIngredient = new JSONObject();
        objAllIngredient.put("Link", Link);
        objAllIngredient.put("Ingredients", testArray);

        JSONObject objAllRecipe = new JSONObject();
        objAllRecipe.put(Recipe, objAllIngredient);

        JSONObject objRecipe = new JSONObject();
        objRecipe.put("Recipes input", objAllRecipe);

        return objRecipe;
    }

    private static void writeNewJSONData(File f, JSONObject jsonObject, String Recipe, String Link, String Ingredient, String Qty, String Unit) throws JSONException, IOException {
        JSONObject allRecipeObj = jsonObject.getJSONObject("Recipes input");
        if(!(allRecipeObj.has(Recipe))){

            JSONObject objNewIngredient = new JSONObject();
            objNewIngredient.put("Name", Ingredient);
            objNewIngredient.put("Quantity", Qty);
            objNewIngredient.put("Unit", Unit);

            JSONArray testArray = new JSONArray();
            testArray.put(objNewIngredient);

            JSONObject objAllIngredient = new JSONObject();
            objAllIngredient.put("Link", Link);
            objAllIngredient.put("Ingredients", testArray);

            allRecipeObj.put(Recipe, objAllIngredient);
        }
        else{
            JSONObject objNewIngredient = new JSONObject();
            objNewIngredient.put("Name", Ingredient);
            objNewIngredient.put("Quantity", Qty);
            objNewIngredient.put("Unit", Unit);

            allRecipeObj.getJSONObject(Recipe).getJSONArray("Ingredients").put(objNewIngredient);
        }

        FileWriter file = new FileWriter(f.getAbsoluteFile(), false);
        file.write(jsonObject.toString(2));
        file.flush();
        file.close();
    }

    static public void saveRecipesSelectedJSON(List<String> selectedRecipeList, String path) throws JSONException, IOException {

        String data = "";

        try{
            File f = new File(path + "/newTestFile.json");

            InputStream inputStream = new FileInputStream(path + "/newTestFile.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine())!= null){
                data = data + line;
            }

            if(!data.isEmpty()){
                JSONObject jsonObject = new JSONObject(data);                           //Get JSON file

                JSONArray jsonArray = new JSONArray();
                for (int i=0; i < selectedRecipeList.size(); i++) {
                    jsonArray.put(selectedRecipeList.get(i));
                }

                jsonObject.put("Recipes selected", jsonArray);

                f = new File(path + "/newTestFile.json");
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

    static public void saveIngredientSelectedJSON(List<String> selectedIngredientsList, String path) throws JSONException, IOException {

        String data = "";

        try{
            File f = new File(path + "/newTestFile.json");

            InputStream inputStream = new FileInputStream(path + "/newTestFile.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine())!= null){
                data = data + line;
            }

            if(!data.isEmpty()){
                JSONObject jsonObject = new JSONObject(data);                           //Get JSON file

                JSONArray jsonArray = new JSONArray();
                for (int i=0; i < selectedIngredientsList.size(); i++) {
                    jsonArray.put(selectedIngredientsList.get(i));
                }

                jsonObject.put("Ingredients selected", jsonArray);

                f = new File(path + "/newTestFile.json");
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

    static public void saveNewElementAddedJSON(String path, String ElementName, String ElementQty, String ElementUnit) throws JSONException, IOException {

        String data = "";

        try{
            File f = new File(path + "/newTestFile.json");

            InputStream inputStream = new FileInputStream(path + "/newTestFile.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine())!= null){
                data = data + line;
            }

            if(!data.isEmpty()){
                JSONObject jsonObject = new JSONObject(data);                           //Get JSON file

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

                f = new File(path + "/newTestFile.json");
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

    static public void removeElementJSON (String path, String name){
        String data = "";

        try{
            File f = new File(path + "/newTestFile.json");

            InputStream inputStream = new FileInputStream(path + "/newTestFile.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine())!= null){
                data = data + line;
            }

            if(!data.isEmpty()){
                JSONObject jsonObject = new JSONObject(data);                           //Get JSON file
                JSONArray elementAddedObj = jsonObject.getJSONArray("Elements added");

                for (int i=0; i < elementAddedObj.length(); i++) {
                    JSONObject nameElement = elementAddedObj.getJSONObject(i);
                    String test = nameElement.get("Name").toString();
                    if (test.equals(name)) {
                        elementAddedObj.remove(i);
                    }
                }

                jsonObject.put("Elements added", elementAddedObj);

                f = new File(path + "/newTestFile.json");
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
