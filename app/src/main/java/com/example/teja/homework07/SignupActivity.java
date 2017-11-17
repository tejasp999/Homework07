package com.example.teja.homework07;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    public DatePickerDialog fromDatePickerDialog;
    EditText firstName, lastName, emailID, birthdayText, passwordData, conPass;
    private FirebaseAuth mAuth;
    public int ageCalculation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstName = (EditText) findViewById(R.id.fName);
        lastName = (EditText) findViewById(R.id.lastname);
        emailID = (EditText) findViewById(R.id.email);
        passwordData = (EditText) findViewById(R.id.pWord);
        conPass = (EditText) findViewById(R.id.confirmpWord);
        birthdayText = (EditText) findViewById(R.id.date);
        birthdayText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    setDateFields();
                } else {

                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDateFields(){
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                ageCalculation = Calendar.getInstance().get(Calendar.YEAR) - year;
                birthdayText.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
                birthdayText.clearFocus();
                passwordData.requestFocus();

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }
    public void registerUser(View view) {
        mAuth = FirebaseAuth.getInstance();
        final String email = emailID.getText().toString().trim();
        final String passwordText = passwordData.getText().toString().trim();
        final String conpasswordText = conPass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (passwordText.matches(emailPattern) && passwordText.equals(conpasswordText) && ageCalculation > 13) {
            mAuth.createUserWithEmailAndPassword(email, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("The exception", "is" + task.getException());
                        Toast.makeText(SignupActivity.this, "Failed to register the user", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(SignupActivity.this, "Make sure Email ID is valid, password texts are same and age is above 13", Toast.LENGTH_LONG).show();
        }
    }
}
