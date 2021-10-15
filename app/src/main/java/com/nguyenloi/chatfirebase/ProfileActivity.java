package com.nguyenloi.chatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView imgProfile;
    TextInputEditText edtProfileUsername;
    Button btnProfileUpdate;

    String name,image;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    FirebaseAuth auth= FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    Uri imageUri;
    boolean imageControl = false;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setControl();
        getUserInfo();
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            imgProfile.setBackground(null);
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imgProfile);
            imageControl =  true;
        }
        else
        {
            imageControl = false;
        }
    }

    private void updateProfile(){
        String username = edtProfileUsername.getText().toString();
        reference.child("Users").child(user.getUid()).child("userName").setValue(username);


            if(imageControl){
                UUID randomId = UUID.randomUUID();
                final  String imageName = "images/"+randomId+"jpg";
                storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                        myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String filePath = uri.toString();
                                reference.child("Users").child(auth.getUid()).child("image").setValue(filePath)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ProfileActivity.this, "Write to database is successful", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this,"Write to database is unsuccessful" , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }else{
                reference.child("Users").child(auth.getUid()).child("image").setValue("null");
            }

    }

    public void imageChooser (){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    private void getUserInfo(){
        reference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 name = snapshot.child("userName").getValue().toString();
                 image = snapshot.child("image").getValue().toString();
                edtProfileUsername.setText(name);
                if(image.equals("null")){
                    imgProfile.setImageResource(R.drawable.ac);
                }else{
                    Picasso.get().load(image).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        imgProfile=findViewById(R.id.imgProfile);
        edtProfileUsername=findViewById(R.id.edtProfileUsername);
        btnProfileUpdate=findViewById(R.id.btnProfileUpdate);
    }
}