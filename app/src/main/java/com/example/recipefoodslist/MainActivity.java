package com.example.recipefoodslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private Button OpenRecipeActivity;

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
    }

    public void OpenRecipeActivityFct(){
        Intent intent = new Intent(this, AddRecipe.class);
        startActivity(intent);
    }
}