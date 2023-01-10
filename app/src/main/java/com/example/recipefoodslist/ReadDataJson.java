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

    private static JSONObject parsingJSON(File f, String data) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(data);                           //Get JSON file
        JSONObject allRecipesInput = jsonObject.getJSONObject("Recipes input");
        return allRecipesInput;
    }

    public static JSONObject getRecipesInput(String path) throws JSONException {
        JSONObject jsonObject = getAllJsonObj(path);
        JSONObject allRecipesInput = jsonObject.getJSONObject("Recipes input");
        return allRecipesInput;
    }

    public static List<String> getRecipesSelected(String path) throws JSONException {
        JSONObject jsonObject = getAllJsonObj(path);
        JSONArray jsonArray = jsonObject.getJSONArray("Recipes selected");
        List<String> recipesSelected = new Vector<>();
        for (int i=0; i < jsonArray.length(); i++) {
            recipesSelected.add((String) jsonArray.get(i));
        }

        return recipesSelected;
    }

}
