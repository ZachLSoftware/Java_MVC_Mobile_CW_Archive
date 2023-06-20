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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnListClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    final ShoppingListAdapter adapter = new ShoppingListAdapter(new ShoppingListAdapter.ShoppingListDiff(), this);
    int listPos;
    private ShoppingListViewModel shoppingListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView empty = findViewById(R.id.textNoList);
        RecyclerView rv = findViewById(R.id.recyclerView_shop_lists);


        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //Set RecyclerView data
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListViewModel.getAllLists().observe(this, lists -> {
            List<ShoppingList> list = new ArrayList<>();
            for(ShoppingListWithProducts sl : lists){
                list.add(sl.shoppingList);
            }
            adapter.submitList(list);
        });

        //Set Fab action
        final FloatingActionButton button = findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listResultLauncher.launch(new Intent(MainActivity.this, CreateListActivity.class));
            }
        });
    }

    //Activity Result for creating a new list
    ActivityResultLauncher<Intent> listResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            //Check result code
                if (result.getResultCode() == RESULT_OK) {

                    //Get Shopping List data
                    Intent data = result.getData();
                    ShoppingList list = (ShoppingList) data.getSerializableExtra("List");

                    //Try to add shopping list to the database, returns false if duplicate
                    if (!shoppingListViewModel.insertList(list)) {
                        Toast.makeText(getApplicationContext(), "Cannot add items with the same name", Toast.LENGTH_SHORT).show();
                    }
                }
                //Check if user cancelled operation
                else if(result.getResultCode()==RESULT_CANCELED){
                    Toast.makeText(getApplicationContext(), "Creating List Cancelled", Toast.LENGTH_SHORT).show();
                }
                //Any unforeseen issues
                else{
                    Toast.makeText(getApplicationContext(), R.string.Error, Toast.LENGTH_SHORT).show();
                }
            }
        });



    //Gets position of item clicked and launches list activity
    @Override
    public void onListClick(int pos) {
        Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);

        //Places selected list as an extra
        intent.putExtra("List", shoppingListViewModel.getAllLists().getValue().get(pos));

        //Stores the current List Position
        listPos=pos;

        //Launch
        startActivity(intent);
    }

    //Detect Long Click
    @Override
    public void onListLongClick(int pos){

        //Add Dialog box
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(MainActivity.this);
        confirmDelete.setTitle("Delete Confirmation");
        confirmDelete.setMessage("Are you sure you want to delete " + shoppingListViewModel.getAllLists().getValue().get(pos).shoppingList.getName());

        //Confirm Delete
        confirmDelete.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            //Delete selected list
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                shoppingListViewModel.deleteList(shoppingListViewModel.getAllLists().getValue().get(pos).shoppingList);
            }
        });

        //Cancel delete
        confirmDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        //Show Dialog
        confirmDelete.create();
        confirmDelete.show();
    }
}
