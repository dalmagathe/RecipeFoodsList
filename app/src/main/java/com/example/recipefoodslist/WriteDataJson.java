package com.example.recipefoodslist;

import static java.lang.System.out;

import android.content.Context;
import android.util.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WriteDataJson {

    public static void main(String path, String ingredient) throws IOException, JSONException {

        JSONArray testArray = new JSONArray();

        JSONObject objNewIngredient = new JSONObject();
        objNewIngredient.put("Name", "Tomate");
        objNewIngredient.put("Ingredient", "2");
        objNewIngredient.put("Unit", "unit");
        testArray.put(objNewIngredient);

        JSONObject objAllIngredient = new JSONObject();
        objAllIngredient.put("Ingredients", testArray);

        JSONObject objAllRecipe = new JSONObject();
        objAllRecipe.put("PateCarbo", objAllIngredient);

        try{
            File f = new File(path + "/newTestFile.json");
            FileWriter file = new FileWriter(f.getAbsoluteFile(), true);
            file.write(objAllRecipe.toString(2));
            file.flush();
            file.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        out.println("File created");

    }
}
