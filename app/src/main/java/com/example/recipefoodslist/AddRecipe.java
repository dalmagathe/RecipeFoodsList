package com.example.recipefoodslist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

public class AddRecipe extends AppCompatActivity {

    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>();
    private Map<String, Pair<String, String>> ingredientMap= new HashMap<>();
    private ListView listView;
    private IngredientAdapter ingredientAdapter;

    EditText etRecipeName, etRecipeNb, etLink, etIngredientName, etIngredientQuantity;
    Spinner etIngredientUnit;
    Button bt;
    CheckBox check;

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
        etRecipeNb = (EditText) findViewById(R.id.recipeNb);
        etLink = (EditText) findViewById(R.id.recipeLink);
        etIngredientName = (EditText) findViewById(R.id.ingredientName);
        etIngredientQuantity = (EditText) findViewById(R.id.ingredientQuantity);
        etIngredientUnit = (Spinner) findViewById(R.id.spinnerUnit);
        bt = (Button) findViewById(R.id.addIngredientBtn);
        listView = findViewById(R.id.listView);
        check = findViewById(R.id.checkLink);

        ingredientAdapter = new IngredientAdapter(AddRecipe.this,ingredientMap);

        onBtnClick();
        onCheck();

    }

    public void onCheck(){
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check.isChecked())
                {
                    etLink.setFocusableInTouchMode(true);
                    etLink.setBackgroundTintList(ColorStateList.valueOf(0xFFFF5722));
                }
                else{
                    etLink.setFocusable(false);
                    etLink.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
                }
            }
        });

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
                ingredientMap.clear();
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((etIngredientName.getText().toString()).equals("") || (etIngredientQuantity.getText().toString().equals("")) || (etIngredientUnit.getSelectedItem().toString().equals(""))){
                    AlertDialog dialogIngredientNull = new AlertDialog.Builder(AddRecipe.this).create();
                    dialogIngredientNull.setTitle("Warning!");
                    dialogIngredientNull.setMessage("You have to fill all information");
                    dialogIngredientNull.show();
                }
                else if(ingredientMap.isEmpty() || ingredientMap.containsKey(etIngredientName.getText().toString())==false){
                    ingredientMap.put(etIngredientName.getText().toString(),
                                    new Pair<>(etIngredientQuantity.getText().toString(), etIngredientUnit.getSelectedItem().toString()));

                    ingredientAdapter.notifyDataSetChanged();
                    listView.setAdapter(ingredientAdapter);

                    writeIntoJSON(etRecipeName.getText().toString(),
                                  etRecipeNb.getText().toString(),
                                  etLink.getText().toString(),
                                  etIngredientName.getText().toString(),
                                  etIngredientQuantity.getText().toString(),
                                  etIngredientUnit.getSelectedItem().toString());
                }
            }
        });
    }

    public void writeIntoJSON(String Recipe, String Nb, String Link, String Ingredient, String Qty, String Unit){
        try {
            WriteDataJson.main(getExternalFilesDir(null).toString(), Recipe, Nb, Link, Ingredient, Qty, Unit);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}