package com.chistia007.instagramclone;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.chistia007.instagramclone.databinding.ActivityPostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {
    ActivityPostBinding binding;
    private Uri selectedImageUri;
    StorageReference storageRef;
    ProgressDialog p;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageChooser();

        FirebaseStorage storage=FirebaseStorage.getInstance("gs://instagram-clone-a151d.appspot.com");
        storageRef = storage.getReference();

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });
        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p=new ProgressDialog(PostActivity.this);
                p.setTitle("Posting...");
                p.show();
                upload();
            }
        });
    }

    private void upload() {
        if (selectedImageUri!=null){
            StorageReference mountainsRef = storageRef.child("Posts/"+UUID.randomUUID().toString()); //will give random name to the uploaded files
            mountainsRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    p.dismiss();
                    Toast.makeText(PostActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                    imageUrl=selectedImageUri.toString();

                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
                    String postId= ref.push().getKey(); // creates unique id

                    HashMap<String,Object> map=new HashMap<>();
                    map.put("postId",postId);
                    map.put("imageUrl",imageUrl);
                    map.put("description",binding.description.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid()); // id of the current user

                    ref.child(postId).setValue(map);


                }
            });
        }
    }


    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        binding.imageAdded.setImageBitmap(selectedImageBitmap);
                    }
                }
            });
}