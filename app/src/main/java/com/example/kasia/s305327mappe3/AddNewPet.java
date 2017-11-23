package com.example.kasia.s305327mappe3;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kasia on 09.11.2017.
 */

public class AddNewPet extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    int day, month, year, day1, month1, year1;
    DBHandler db;
    EditText petsName;
    Button birthDate;
    RadioGroup radioButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);
        petsName = (EditText) findViewById(R.id.name);
        birthDate = (Button) findViewById(R.id.born);
        db = new DBHandler(this);
        radioButtons = (RadioGroup)findViewById(R.id.radioGroup);

    }

    public void chooseDate(View view) {
        final Calendar c = Calendar.getInstance();
        //Dersom ingen dato var valgt før, sett dato til dags dato
        if (year1 == 0) {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        //set dato til den sist valgte ellers
        else {
            year = year1;
            month = month1-1;
            day = day1;
        }


        //vis datepicker for å velge fødslsdato
        DatePickerDialog dialog = new DatePickerDialog(this, this, year, month, day);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        boolean dateValid;
        year1 = i;
        month1 = i1 + 1;
        day1 = i2;
        Date chosenDate = new Date();
        //sjekke hvis valgt dato ikke ligger i fremtiden, hvis ja gi feilmld
        if (i > year) {
            dateValid = false;
        } else if (i == year) {
            if (i1 > month) {
                dateValid = false;
            }
            else if (i1 == month) {
                if (i2 > day) {
                    dateValid = false;
                } else {
                    dateValid = true;
                }
            } else {
                dateValid = true;
            }
        } else {
            dateValid = true;
        }

        if (dateValid) {
            //dato er valid - OK
            birthDate.setText(day1 + "/" + month1 + "/" + year1);
        } else {
            //dato er ikke valid - vis feilmld
            birthDate.setText("SET DATE");
            Toast.makeText(getApplicationContext(), "You can't choose a future date", Toast.LENGTH_SHORT).show();
        }

    }

    public void savePet(View view) {
        //0 = katt, 1 = hund
        int radioButtonID = radioButtons.getCheckedRadioButtonId();
        View radioButton = radioButtons.findViewById(radioButtonID);
        int idx = radioButtons.indexOfChild(radioButton);
        //sjekk om type av kjæledyr ble valgt
        if (idx < 0) {
            Toast.makeText(getApplicationContext(), "Choose type", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = petsName.getText().toString();
        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String bDate = birthDate.getText().toString();
        if (bDate.toLowerCase().equals("set date")) {
            Toast.makeText(getApplicationContext(), "Choose date", Toast.LENGTH_SHORT).show();
            return;
        }

        Pet pet = new Pet(name, bDate, idx);
        db.addPet(pet);
        Log.d("SQL", "Pet added");
        petsName.setText("");
        birthDate.setText("SET DATE");
        radioButtons.clearCheck();

    }

}

