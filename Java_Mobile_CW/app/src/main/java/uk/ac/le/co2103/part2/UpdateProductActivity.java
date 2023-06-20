package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class UpdateProductActivity extends AppCompatActivity implements OnItemSelectedListener {
    EditText productName;
    EditText productQuantity;
    Button updateBtn;
    String unit;
    Product currentProduct;
    Button addBtn;
    Button subBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        productName=findViewById(R.id.updateEditTextName);
        productQuantity = findViewById(R.id.updateEditTextQuantity);
        updateBtn = findViewById(R.id.updateProductBtn);
        addBtn = findViewById(R.id.addBtn);
        subBtn = findViewById(R.id.subtractBtn);
        updateBtn.setEnabled(true);
        Intent i = getIntent();
        currentProduct = (Product) i.getSerializableExtra("product");
        productName.setText(currentProduct.getName());
        productQuantity.setText(String.valueOf(currentProduct.getQuantity()));

        //Enable disable submit button based on if text boxes are empty
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
        Spinner updateSpinner = findViewById(R.id.updateSpinner);
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.labels_unit, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateSpinner.setAdapter(spinAdapter);
        updateSpinner.setOnItemSelectedListener(this);
        updateSpinner.setSelection(spinAdapter.getPosition(currentProduct.getUnit()));

        updateBtn.setOnClickListener(view -> {
            Intent reply = new Intent();
            Product newp = currentProduct;
            newp.setName(productName.getText().toString());
            if(!newp.setQuantity(Integer.parseInt(productQuantity.getText().toString()))){
                Toast.makeText(getApplicationContext(), "Quantity must be greater then 0", Toast.LENGTH_LONG).show();
                return;
            }
            //Return result
            newp.setUnit(unit);
            reply.putExtra("Product", newp);
            setResult(RESULT_OK, reply);
            finish();

        });

        //Add or Subtract quantity
        addBtn.setOnClickListener(view -> {
            productQuantity.setText(String.valueOf(Integer.parseInt(productQuantity.getText().toString())+1));
        });

        subBtn.setOnClickListener(view -> {
            productQuantity.setText(String.valueOf(Integer.parseInt(productQuantity.getText().toString())-1));
        });


    }

    //Enables or disables the submit button
    private void setBtnEnable() {
        updateBtn.setEnabled(!TextUtils.isEmpty(productName.getText()) && !TextUtils.isEmpty(productQuantity.getText()));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        unit = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}