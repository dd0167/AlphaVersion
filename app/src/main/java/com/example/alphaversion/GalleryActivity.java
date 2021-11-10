package com.example.alphaversion;

import static com.example.alphaversion.FBref.reference;
import static com.example.alphaversion.FBref.root;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    ImageView image_view;
    Uri imageUri;
    AlertDialog.Builder adb;
    EditText et_GetFileName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        image_view=(ImageView) findViewById(R.id.image_view);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        String st=item.getTitle().toString();
        if (st.equals("Auth"))
        {
            Intent in=new Intent(this,MainActivity.class);
            startActivity(in);
            finish();
        }
        if (st.equals("Map"))
        {
            Intent in=new Intent(this,MapActivity.class);
            startActivity(in);
            finish();
        }
        if (st.equals("Gallery"))
        {
            Intent in=new Intent(this,GalleryActivity.class);
            startActivity(in);
            finish();
        }
        return true;
    }

    public void select_image(View view) {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==2 && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            image_view.setImageURI(imageUri);
        }
    }

    public void upload_image(View view) {
        if (imageUri!=null)
        {
            adb = new AlertDialog.Builder(this);
            adb.setCancelable(false);
            adb.setTitle("Save As");
            et_GetFileName=new EditText(this);
            et_GetFileName.setHint("Type Text Here: Name");
            et_GetFileName.setGravity(Gravity.CENTER);
            adb.setView(et_GetFileName);
            adb.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String file_name=et_GetFileName.getText().toString();
                    if (!file_name.equals(""))
                    {

                        progressDialog = new ProgressDialog(GalleryActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("Uploading file...");
                        progressDialog.setProgress(0);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

//                        StorageReference fileRef=reference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
                        StorageReference fileRef=reference.child(file_name+".png");
                        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Model model = new Model(uri.toString());
//                                        String modelId = root.push().getKey();
                                        root.child(file_name).setValue(model);
                                        Toast.makeText(GalleryActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                int currentProgress = (int) (100*(snapshot.getBytesTransferred()/snapshot.getTotalByteCount()));
                                progressDialog.setProgress(currentProgress);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GalleryActivity.this, "Uploading Failed!", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(GalleryActivity.this, "Insert Name!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog ad=adb.create();
            ad.show();
        }
        else
        {
            Toast.makeText(this, "Select Image!", Toast.LENGTH_SHORT).show();
        }
    }

//    public String getFileExtension(Uri mUri)
//    {
//        ContentResolver cr=getContentResolver();
//        MimeTypeMap mime=MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cr.getType(mUri));
//    }
}