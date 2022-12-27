package com.example.recipefoodslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button OpenRecipeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenRecipeActivity = (Button) findViewById(R.id.RecipeBtn);
        OpenRecipeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenRecipeActivityFct();
            }
        });
    }

    public void OpenRecipeActivityFct(){
        Intent intent = new Intent(this, AddRecipe.class);
        startActivity(intent);
    }
}