package com.nightcafeadmin.app.fooditems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nightcafeadmin.app.R;


import java.io.IOException;

public class AddItemsActivity extends AppCompatActivity {

    StorageReference storageReference;
    DatabaseReference databaseReference;
    Uri FilePathUri;
    int Image_Request_Code = 7;
    ProgressBar probar;
    ImageView image;
   String item_name,category_name,regular_price,large_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);


         image = findViewById(R.id.item_image);
        Button upload = findViewById(R.id.addItemBtn);
        ImageView back = findViewById(R.id.arrow);


        probar = findViewById(R.id.progressbar);

        //on start progress bar invisible
        probar.setVisibility(View.GONE);

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(android.R.id.content, new FoodFragment()).commit();
                finish();
            }
        });

        //select image from phone
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name =  validateItemName();
                category_name = validateRadioButtonValue();
                regular_price = validateRegularPrice();
                large_price = validateLargePrice();

                if(item_name.isEmpty() && category_name.isEmpty() && regular_price.isEmpty() && large_price.isEmpty())
                {
                    return;
                }
                else {
                    UploadImage();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                image.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            probar.setVisibility(View.VISIBLE);
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                    new OnCompleteListener<Uri>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String imageURL = task.getResult().toString();
                                            String ImageUploadId = databaseReference.push().getKey();
                                            UploadItemModel itemInfo = new UploadItemModel(imageURL,item_name,category_name,regular_price,large_price,"Available");
                                            databaseReference.child(ImageUploadId).setValue(itemInfo);
                                            Toast.makeText(getApplicationContext(), "Item Added Successfully ", Toast.LENGTH_LONG).show();
                                            probar.setVisibility(View.GONE);
                                            getSupportFragmentManager().beginTransaction().add(android.R.id.content, new FoodFragment()).commit();
                                            finish();

                                        }
                                    });

                        }
                    });
        }
        else {

            Toast.makeText(AddItemsActivity.this, "Please Select Image", Toast.LENGTH_LONG).show();

        }
    }

    private void checkId(){



    }

    private String validateItemName() {
        TextInputLayout name = findViewById(R.id.name);
        String i_name = name.getEditText().getText().toString();


        if (i_name.isEmpty()) {
            name.setError("Field can not be empty");
            return null;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return i_name;
        }
    }

    private String validateRadioButtonValue()
    {
        RadioGroup radioGroup = findViewById(R.id.category);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String radioButtonValue = radioButton.getText().toString();

        if (radioButtonValue.isEmpty()) {
            Toast.makeText(AddItemsActivity.this, "Please Select Category", Toast.LENGTH_LONG).show();
        }

       else{
            return radioButtonValue;
        }

        return null;

    }

    private String validateRegularPrice() {
        TextInputLayout r_price = findViewById(R.id.regularPrice);
        String reg_price = r_price.getEditText().getText().toString().trim();

        if (reg_price.isEmpty() ) {
            r_price.setError("Field can not be empty");
            return null;
        }

        else {
            r_price.setError(null);
            r_price.setErrorEnabled(false);

            return reg_price;
        }
    }

    private String validateLargePrice() {

        TextInputLayout l_price = findViewById(R.id.largePrice);
        String lar_price = l_price.getEditText().getText().toString().trim();

        if (lar_price.isEmpty()) {
            l_price.setError("Field can not be empty");
            return null;
        }

        else {
            l_price.setError(null);
            l_price.setErrorEnabled(false);
            return lar_price;
        }
    }
}