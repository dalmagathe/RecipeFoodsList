package com.example.recipefoodslist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ReadDataJson {

    static String nameFile = "/recipesData.json";

    public static JSONObject getAllJsonObj(String path){
        String data ="";
        JSONObject jsonObject = new JSONObject();

        try{
            File f = new File(path + nameFile);

            if(!(f.exists())){

            }
            else{
                InputStream inputStream = new FileInputStream(path + nameFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine())!= null){
                    data = data + line;
                }

                if(!data.isEmpty()){
                    jsonObject = new JSONObject(data);
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject getRecipesInput(String path) throws JSONException {
        JSONObject jsonObject = getAllJsonObj(path);
        JSONObject allRecipesInput = jsonObject.getJSONObject("Recipes input");
        return allRecipesInput;
    }

    public static List<String> getRecipesSelected(String path) throws JSONException {
        List<String> recipesSelected = new Vector<>();
        JSONObject jsonObject = getAllJsonObj(path);
        if(jsonObject.has("Recipes selected")){
            JSONArray jsonArray = jsonObject.getJSONArray("Recipes selected");
            for (int i=0; i < jsonArray.length(); i++) {
                recipesSelected.add((String) jsonArray.get(i));
            }
        }
        return recipesSelected;
    }

    public static JSONArray getElementsAdded(String path) throws JSONException {
        JSONObject jsonObject = getAllJsonObj(path);
        JSONArray elementsObj = new JSONArray();
        if(jsonObject.has("Elements added")){
            elementsObj = jsonObject.getJSONArray("Elements added");
            }
        return elementsObj;
        }

    public static List<String> getIngredientsSelected(String path) throws JSONException {
        List<String> ingredientsSelected = new Vector<>();
        JSONObject jsonObject = getAllJsonObj(path);
        if(jsonObject.has("Ingredients selected")){
            JSONArray jsonArray = jsonObject.getJSONArray("Ingredients selected");
            for (int i=0; i < jsonArray.length(); i++) {
                ingredientsSelected.add((String) jsonArray.get(i));
            }
        }
        return ingredientsSelected;
    }

    public static List<String> getElementsSelected(String path) throws JSONException {
        List<String> elementsSelected = new Vector<>();
        JSONObject jsonObject = getAllJsonObj(path);
        if(jsonObject.has("Elements selected")){
            JSONArray jsonArray = jsonObject.getJSONArray("Elements selected");
            for (int i=0; i < jsonArray.length(); i++) {
                elementsSelected.add((String) jsonArray.get(i));
            }
        }
        return elementsSelected;
    }

    public static Map<String, String> getRecipeNb(String path) throws JSONException {
        Map<String, String> elementsSelected = new HashMap<>();
        JSONObject jsonObject = getAllJsonObj(path);
        //Look for the recipes selected
        if(jsonObject.has("Recipes selected")){
            JSONObject allRecipesInput = jsonObject.getJSONObject("Recipes input");
            JSONArray jsonArray = jsonObject.getJSONArray("Recipes selected");
            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject recipe = allRecipesInput.getJSONObject((String) jsonArray.get(i));
                elementsSelected.put((String) jsonArray.get(i), (String) recipe.get("Nb"));
            }
        }
        //Look for previous nb selected and change it into the MAP
        if(jsonObject.has("Nb selected")){
            JSONArray jsonArray = jsonObject.getJSONArray("Nb selected");
            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String name = jsonObj.getString("Name recipe");
                String nb = jsonObj.getString("Nb");

                if(elementsSelected.containsKey(name)){
                    elementsSelected.put(name, nb);
                }
            }
        }
        return elementsSelected;
    }

    public static Map<String, String> getRecipeNbSelected(String path) throws JSONException {
        Map<String, String> elementsSelected = new HashMap<>();
        JSONArray jsonArray = getAllJsonObj(path).getJSONArray("Nb selected");

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            String name = jsonObj.getString("Name recipe");
            String nb = jsonObj.getString("Nb");

            elementsSelected.put(name, nb);
        }

        return elementsSelected;
    }

}
