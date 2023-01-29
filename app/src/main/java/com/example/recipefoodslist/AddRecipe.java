package com.example.recipefoodslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.example.recipefoodslist.WriteDataJson;

import org.json.JSONException;

public class AddRecipe extends AppCompatActivity {

    private List<String> Ingredient = new Vector<String>();
    private List<String> Qty = new Vector<String>();
    private List<String> Unit = new Vector<String>();
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>(); //arrayList
    private ListView listView; //lv
    private IngredientAdapter ingredientAdapter;
    private int ingredientNB = 0;

    String previousRecipe = "";

    EditText etRecipeName;
    EditText etLink;
    EditText etIngredientName;
    EditText etIngredientQuantity;
    Spinner etIngredientUnit;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_recipe);

        Spinner spinner = findViewById(R.id.spinnerUnit);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.unit, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        etRecipeName = (EditText) findViewById(R.id.recipeName);
        etLink = (EditText) findViewById(R.id.recipeLink);
        etIngredientName = (EditText) findViewById(R.id.ingredientName);
        etIngredientQuantity = (EditText) findViewById(R.id.ingredientQuantity);
        etIngredientUnit = (Spinner) findViewById(R.id.spinnerUnit);
        bt = (Button) findViewById(R.id.addIngredientBtn);
        listView = findViewById(R.id.listView);

        //populateList(ingredientArrayList);
        //adapter
        ingredientAdapter = new IngredientAdapter(AddRecipe.this,ingredientArrayList);
        //listView.setAdapter(ingredientAdapter);

        onBtnClick();

    }

    public void onBtnClick(){
        etRecipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listView.setAdapter(null);
                Ingredient.clear();
                Qty.clear();
                Unit.clear();
                ingredientArrayList.clear();
                ingredientNB=0;
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int listSize = Ingredient.size();
                if((listSize == 0) || ((listSize > 0) && (Ingredient.contains(etIngredientName.getText().toString()) == false))){
                    Ingredient.add(etIngredientName.getText().toString());
                    Qty.add(etIngredientQuantity.getText().toString());
                    Unit.add(etIngredientUnit.getSelectedItem().toString());
                    populateList(ingredientArrayList);
                    ingredientAdapter.notifyDataSetChanged();
                    listView.setAdapter(ingredientAdapter);

                    writeIntoJSON(etRecipeName.getText().toString(), etLink.getText().toString(), Ingredient.get(listSize), Qty.get(listSize), Unit.get(listSize));
                    ++ingredientNB;
                    previousRecipe = etRecipeName.getText().toString();
                }
            }
        });
    }

    private void populateList(ArrayList<Ingredient> ingredientArrayList){
        Ingredient ingredientModel = new Ingredient();
        ingredientModel.setIngredient(Ingredient.get(ingredientNB));
        ingredientModel.setQty(Qty.get(ingredientNB));
        ingredientModel.setUnit(Unit.get(ingredientNB));
        ingredientArrayList.add(ingredientModel);
    }

    public void writeIntoJSON(String Recipe, String Link, String Ingredient, String Qty, String Unit){
        try {
            WriteDataJson.main(getExternalFilesDir(null).toString(), Recipe, Link, Ingredient, Qty, Unit);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}