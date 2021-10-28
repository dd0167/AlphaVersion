package com.example.alphaversion;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBref {

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static FirebaseStorage FBCS = FirebaseStorage.getInstance();

    public static DatabaseReference refUsers=FBDB.getReference("Users");

    public static DatabaseReference root=FBDB.getReference("Images");
    public static StorageReference reference=FBCS.getReference();
}