package com.example.recipefoodslist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button OpenRecipeActivity;
    private Button OpenGroceryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        OpenRecipeActivity = (Button) findViewById(R.id.RecipeBtn);
        OpenRecipeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenRecipeActivityFct();
            }
        });

        OpenGroceryActivity = (Button) findViewById(R.id.ShoppingBtn);
        OpenGroceryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGroceryActivityFct();
            }
        });
    }

    public void OpenRecipeActivityFct(){
        Intent intent = new Intent(this, AddRecipe.class);
        startActivity(intent);
    }

    public void OpenGroceryActivityFct(){
        File f = new File(getExternalFilesDir(null).toString() + "/recipesData.json");
        if(!(f.exists())){
            AlertDialog dialogIngredientNull = new AlertDialog.Builder(this).create();
            dialogIngredientNull.setTitle("Warning!");
            dialogIngredientNull.setMessage("You must add at least one recipe.");
            dialogIngredientNull.show();
        }
        else{
            Intent intent = new Intent(this, RecipesList.class);
            startActivity(intent);
        }
    }
}