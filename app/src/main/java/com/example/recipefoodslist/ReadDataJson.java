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
import java.util.List;
import java.util.Vector;

public class ReadDataJson {

    public static JSONObject getAllJsonObj(String path){
        String data ="";
        JSONObject jsonObject = new JSONObject();

        try{
            File f = new File(path + "/newTestFile.json");

            if(!(f.exists())){

            }
            else{
                InputStream inputStream = new FileInputStream(path + "/newTestFile.json");
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

    public static List<String> getLink(String path) throws JSONException {
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

}
