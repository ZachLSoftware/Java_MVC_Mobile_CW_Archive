package uk.ac.le.co2103.part2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ShoppingListActivity extends AppCompatActivity implements OnListClickListener {

    //Initialize variables
    final ProductAdapter adapter = new ProductAdapter(new ProductAdapter.ProductDiff(), this);
    ShoppingListWithProducts sl;
    TextView emptyText;
    RecyclerView rv;
    int updatePOS;
    private ShoppingListViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get fields
        setContentView(R.layout.activity_shopping_list);
        emptyText=findViewById(R.id.noProductsText);
        rv = findViewById(R.id.recyclerView_Products);
        Intent i = getIntent();
        sl = (ShoppingListWithProducts) i.getSerializableExtra("List");
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //Find shopping list in database based on provided list passed by main activity
        productViewModel=new ViewModelProvider(this).get(ShoppingListViewModel.class);
        productViewModel.getAllLists().observe(this, lists -> {
            for(ShoppingListWithProducts s : lists){
                if (s.shoppingList.getListId()==sl.shoppingList.getListId()) {
                    sl = s;
                    break;
                }
            }
            adapter.submitList(sl.products);
        });

        //set activity for adding a product
        final FloatingActionButton fab = findViewById(R.id.fabAddProduct);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShoppingListActivity.this, addProductActivity.class);
                i.putExtra("listId", sl.shoppingList.listId);
                productResultLauncher.launch(i);
            }
        });

    }


    ActivityResultLauncher<Intent> productResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            //Check Results
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Product product = (Product) data.getSerializableExtra("Product");

                //If product already exists in the database, returns false
                if(!productViewModel.insertProduct(product, sl.shoppingList.getListId())){
                    Toast.makeText(getApplicationContext(), R.string.productError, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //User Cancelled
            else if(result.getResultCode()==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Adding Product Cancelled", Toast.LENGTH_SHORT).show();
            }
            //Other issues
            else {
                Toast.makeText(getApplicationContext(), "Issue adding Product", Toast.LENGTH_SHORT).show();
            }
        }
    });

    ActivityResultLauncher<Intent> productUpdateResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            //If result ok then update product
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Product product =(Product) data.getSerializableExtra("Product");

                productViewModel.updateProduct(product);
            }
            else if(result.getResultCode()==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Updating Product Cancelled", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Issue Updating Product", Toast.LENGTH_SHORT).show();
            }
        }
    });


    @Override
    public void onListClick(int pos) {

        //Provide a random description when a product is tapped.
        updatePOS = pos;

        //Get a random adjective
        List<String> adjectives = new ArrayList<String>(Arrays.asList("Heavenly ", "Wonderful ", "Beautiful ", "Cheerful ",
                "Colorful ", "Delightful ", "Elegant ", "Fantastic ", "Glamorous ", "Happy ", "Mysterious ", "Old-Fashioned ",
                "Perfect ", "Splendid ", "Super ", "Unusual ", "Wicked ", "Zealous "));

        //Get a random quantity description based on size
        List<String> one = new ArrayList<String>(Arrays.asList("A solitary ", "A single ", "A sole "));
        List<String> small = new ArrayList<String>(Arrays.asList("A few of ", "Not many of ", "A handful of ", "A couple of ",
                "A sprinkling of ", "A small quantity of "));
        List<String> large = new ArrayList<String>(Arrays.asList("Plentiful amount of ", "A Legion of ", "Countless ",
                "Numberless amount of ", "No end of ", "Bountiful amount of ", "Copious amounts of "));

        //Create msg variable and random integer
        String msg;
        Random rand = new Random();
        int qty = sl.products.get(pos).getQuantity();

        //If quantity is one, use one list
        if(qty==1 && sl.products.get(pos).getUnit().equals("Unit")){
            msg = one.get(rand.nextInt(one.size())) + adjectives.get(rand.nextInt(adjectives.size())) + sl.products.get(pos).getName();
        }
        //If quantity is 1-10 get small list
        else if (qty>=1 && qty<10){
            msg = small.get(rand.nextInt(small.size())) + adjectives.get(rand.nextInt(adjectives.size())) + sl.products.get(pos).getName();

            //Adds plural suffix
            if (!msg.substring(msg.length()-1).equals("s")) {
                msg=msg+"s";
           }
        }
        //Else quantity is large, use large list
        else{
            msg = large.get(rand.nextInt(large.size())) + adjectives.get(rand.nextInt(adjectives.size())) + sl.products.get(pos).getName();

            //adds plural suffix if does not exist
            if (!msg.substring(msg.length()-1).equals("s")) {
                msg=msg+"s";
            }
        }

        //Show description message
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();

        //Create dialog box for options
        AlertDialog.Builder productOptions = new AlertDialog.Builder(ShoppingListActivity.this);
        productOptions.setTitle("Select option");
        productOptions.setMessage("Delete or Edit " + sl.products.get(pos).getName());

        //Delete Product
        productOptions.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                productViewModel.deleteProduct(sl.products.get(pos));

            }
        });

        //Edit Product
        productOptions.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent update = new Intent(ShoppingListActivity.this, UpdateProductActivity.class);

                //Add product to extra and launch
                update.putExtra("product",sl.products.get(updatePOS));
                productUpdateResultLauncher.launch(update);
            }
        });

        productOptions.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        //Show dialog
        productOptions.create().show();

    }

    @Override
    public void onListLongClick(int pos) {

    }

}