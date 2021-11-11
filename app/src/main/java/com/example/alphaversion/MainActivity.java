package com.example.alphaversion;

import static com.example.alphaversion.FBref.mAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity {

    EditText et_mail;
    EditText et_password;
    ProgressBar progressBar_ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_mail=(EditText) findViewById(R.id.et_email);
        et_password=(EditText) findViewById(R.id.et_password);
        progressBar_ma=(ProgressBar) findViewById(R.id.progressBar_ma);

        progressBar_ma.setVisibility(View.INVISIBLE);
    }

    public void insert(View view) {
        progressBar_ma.setVisibility(View.VISIBLE);
        String email=et_mail.getText().toString();
        String password=et_password.getText().toString();
        if (!email.isEmpty() && !password.isEmpty())
        {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar_ma.setVisibility(View.INVISIBLE);
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(MainActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressBar_ma.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Registration Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            et_mail.setText("");
            et_password.setText("");
        }
        else
        {
            progressBar_ma.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Enter all the required information!", Toast.LENGTH_SHORT).show();
        }
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
        if (st.equals("Credits"))
        {
            Intent in=new Intent(this,CreditsActivity.class);
            startActivity(in);
            finish();
        }
        return true;
    }
}