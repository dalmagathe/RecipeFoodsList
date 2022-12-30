package com.example.recipefoodslist;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class WriteDataJson {

    public static void main(String path, String ingredient) throws IOException, JSONException {
        JSONObject obj = new JSONObject();

        obj.put("Name", ingredient);

        try{
            File f = new File(path + "/newTestFile.json");
            FileWriter file = new FileWriter(f.getAbsoluteFile());
            file.write(obj.toString());
            file.flush();
            file.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        System.out.println("File created");

    }
}
