package com.example.recipefoodslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.view.View;
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

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<String> Ingredient = new Vector<String>();
    private List<String> Qty = new Vector<String>();
    private List<String> Unit = new Vector<String>();
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>(); //arrayList
    private ListView listView; //lv
    private IngredientAdapter ingredientAdapter;
    private int ingredientNB = 0;

    EditText etRecipeName;
    EditText etIngredientName;
    EditText etIngredientQuantity;
    Spinner etIngredientUnit;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        Spinner spinner = findViewById(R.id.spinnerUnit);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.unit, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(this);

        etRecipeName = (EditText) findViewById(R.id.recipeName);
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

                    writeIntoJSON(etRecipeName.getText().toString(), Ingredient.get(listSize), Qty.get(listSize), Unit.get(listSize));
                    ++ingredientNB;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void writeIntoJSON(String Recipe, String Ingredient, String Qty, String Unit){
        try {
            WriteDataJson.main(getExternalFilesDir(null).toString(), Recipe, Ingredient, Qty, Unit);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}