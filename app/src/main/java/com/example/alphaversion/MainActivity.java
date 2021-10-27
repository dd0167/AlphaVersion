package com.example.alphaversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText et_mail;
    EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_mail=(EditText) findViewById(R.id.et_email);
        et_phone=(EditText) findViewById(R.id.et_phone);
    }

    // Java program to check if an email address
    // is valid using Regex.
    // import java.util.regex.Matcher;
    // import java.util.regex.Pattern;
    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public void insert(View view) {
        String email=et_mail.getText().toString();
        String phone=et_phone.getText().toString();
        if (isValidEmail(email))
        {
            Toast.makeText(this, "The Email is valid", Toast.LENGTH_SHORT).show();
            // insert to firebase
        }
        else
        {
            Toast.makeText(this, "The Email is invalid", Toast.LENGTH_SHORT).show();
        }
        if (isValidPhone(phone))
        {
            Toast.makeText(this, "The Phone is valid", Toast.LENGTH_SHORT).show();
            // insert to firebase
        }
        else
        {
            Toast.makeText(this, "The Phone is invalid", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean validateNumber(String str)
    {
        //(0/91): number starts with (0/91)
        //[7-9]: starting of the number may contain a digit between 0 to 9
        //[0-9]: then contains digits 0 to 9
        Pattern ptrn = Pattern.compile("(0/91)?[0-9]{9}");
        //the matcher() method creates a matcher that will match the given input against this pattern
        Matcher match = ptrn.matcher(str);
        //returns a boolean value
        return (match.find() && match.group().equals(str));
    }

    public static boolean isValidPhone(String mobNumber)
    {
        //validates phone numbers having 10 digits (9998887776)
        if (mobNumber.matches("\\d{10}"))
            return true;
        //validates phone numbers having digits, -, . or spaces
        else if (mobNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;
        else if (mobNumber.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}"))
            return true;
        //validates phone numbers having digits and extension (length 3 to 5)
        else if (mobNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
            return true;
        //validates phone numbers having digits and area code in braces
        else if (mobNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;
        else if (mobNumber.matches("\\(\\d{5}\\)-\\d{3}-\\d{3}"))
            return true;
        else if (mobNumber.matches("\\(\\d{4}\\)-\\d{3}-\\d{3}"))
            return true;
        //return false if any of the input matches is not found
        else
            return false;
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
}