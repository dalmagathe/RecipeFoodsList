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

public class WriteDataJson {

    public static void main(String path, String Recipe, String Ingredient, String Qty, String Unit) throws IOException, JSONException {

        String data ="";

        try{
            File f = new File(path + "/newTestFile.json");

            if(!(f.exists())){
                FileWriter file = new FileWriter(f.getAbsoluteFile(), true);
                JSONObject objAllRecipe = createJSON(Recipe, Ingredient, Qty, Unit);
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
                    JSONObject jsonObject = parsingJSON(f, data);
                    writeNewJSONData(f, jsonObject, Recipe, Ingredient, Qty, Unit);
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static JSONObject parsingJSON(File f, String data) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(data);                           //Get JSON file
        //JSONObject recipeObj = jsonObject.getJSONObject("PateCarbo");         Get a recipe
        //JSONArray ingredientObj = recipeObj.getJSONArray("Ingredients");      Get the ingredients
        return jsonObject;
    }

    private static JSONObject createJSON(String Recipe, String Ingredient, String Qty, String Unit) throws JSONException {
        JSONArray testArray = new JSONArray();

        JSONObject objNewIngredient = new JSONObject();
        objNewIngredient.put("Name", Recipe);
        objNewIngredient.put("Quantity", Qty);
        objNewIngredient.put("Unit", Unit);
        testArray.put(objNewIngredient);

        JSONObject objAllIngredient = new JSONObject();
        objAllIngredient.put("Ingredients", testArray);

        JSONObject objAllRecipe = new JSONObject();
        objAllRecipe.put(Recipe, objAllIngredient);

        return objAllRecipe;
    }

    private static void writeNewJSONData(File f, JSONObject jsonObject, String Recipe, String Ingredient, String Qty, String Unit) throws JSONException, IOException {
        if(!(jsonObject.has(Recipe))){
            JSONArray testArray = new JSONArray();

            JSONObject objNewIngredient = new JSONObject();
            objNewIngredient.put("Name", Recipe);
            objNewIngredient.put("Quantity", Qty);
            objNewIngredient.put("Unit", Unit);
            testArray.put(objNewIngredient);

            JSONObject objAllIngredient = new JSONObject();
            objAllIngredient.put("Ingredients", testArray);

            jsonObject.put(Recipe, objAllIngredient);
        }
        else{
            JSONObject recipeObj = jsonObject.getJSONObject(Recipe);

            JSONObject objNewIngredient = new JSONObject();
            objNewIngredient.put("Name", Ingredient);
            objNewIngredient.put("Quantity", Qty);
            objNewIngredient.put("Unit", Unit);

            JSONArray newIngredientInArray = recipeObj.getJSONArray("Ingredients");
            newIngredientInArray.put(objNewIngredient);
        }

        FileWriter file = new FileWriter(f.getAbsoluteFile(), false);
        file.write(jsonObject.toString(2));
        file.flush();
        file.close();
    }
}
