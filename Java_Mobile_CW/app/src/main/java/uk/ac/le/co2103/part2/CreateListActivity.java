package uk.ac.le.co2103.part2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateListActivity extends AppCompatActivity {

    //Intialize variables
    EditText editListName;
    Button imageSelect;
    ImageView imagePreview;
    Button saveList;
    Uri imageUri;
    String REPLY = "REPLY";
    int RESULT = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        //Get view items
        editListName = findViewById(R.id.editTextListName);
        imageSelect = findViewById(R.id.imgSelectBtn);
        imagePreview = findViewById(R.id.imagePreview);
        saveList = findViewById(R.id.saveListBtn);

        //Disable the save button
        saveList.setEnabled(false);
        ShoppingList newList = new ShoppingList();

        //Get an image from the users gallery
        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);

                //Launch activity
                selectImageResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
            }
        });

        //Validate Name is not empty before enabling submit button
        editListName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableBtn();
            }
        });

        //Return list as extra
        saveList.setOnClickListener(view ->{
            Intent reply = new Intent();
            newList.setName(editListName.getText().toString());
            if(imageUri!=null && !imageUri.equals(Uri.EMPTY)){
                newList.setImage(imageUri.toString());
            }
            reply.putExtra("List", newList);
            setResult(RESULT_OK,reply);

            finish();
        });
    }

    //Enables or disables submit button
    private void enableBtn() {
        if (TextUtils.isEmpty(editListName.getText())) {
            saveList.setEnabled(false);
        } else {
            saveList.setEnabled(true);
        }
    }

    //Gets image data from activity
    ActivityResultLauncher<Intent> selectImageResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                imageUri = result.getData().getData();
                if (null != imageUri) {
                    getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    imagePreview.setImageURI(imageUri);
                }
            }
        }
    });

}