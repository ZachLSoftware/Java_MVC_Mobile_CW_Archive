package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class addProductActivity extends AppCompatActivity implements OnItemSelectedListener {
    //Create field and button variables to be used throughout activity.
    EditText productName;
    EditText productQuantity;
    Button addBtn;
    String unit;
    long listId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize buttons and get shopping list foreign key
        setContentView(R.layout.activity_add_product);
        productName=findViewById(R.id.editTextName);
        productQuantity = findViewById(R.id.editTextQuantity);
        addBtn = findViewById(R.id.addProductBtn);
        addBtn.setEnabled(false);
        Intent i = getIntent();
        listId=i.getLongExtra("listId",-1);

        //Create a text changed listener to enable the submit button.
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                setBtnEnable();
            }
        });

        //Create a text changed listener to enable the submit button.
        productQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                setBtnEnable();
            }
        });

        //Create spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.labels_unit, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);
        spinner.setOnItemSelectedListener(this);

        //Add Product Button
        addBtn.setOnClickListener(view -> {
            Intent reply = new Intent();
            Product newp = new Product();
            newp.setListId(listId);
            newp.setName(productName.getText().toString());

            //Validate Quantity
            if(!newp.setQuantity(Integer.parseInt(productQuantity.getText().toString()))){
                Toast.makeText(getApplicationContext(), "Quantity must be greater then 0", Toast.LENGTH_LONG).show();
                return;
            }
            newp.setUnit(unit);
            reply.putExtra("Product", newp);
            setResult(RESULT_OK, reply);
            finish();

        });


    }

    //Enable or disable add btn
    private void setBtnEnable() {
        if (TextUtils.isEmpty(productName.getText()) || TextUtils.isEmpty(productQuantity.getText())) {
            addBtn.setEnabled(false);
        }
        else{
            addBtn.setEnabled(true);
        }

    }

    //Spinner selection
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        unit = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}