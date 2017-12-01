package com.example.kasia.s305327mappe3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kasia on 09.11.2017.
 */

public class AddNewPet extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ErrorDialog.DialogClickListener {

    int day, month, year, day1, month1, year1;
    DBHandler db;
    EditText petsName;
    Button birthDate;
    EditText weight;
    RadioGroup radioButtons;
    ErrorDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);
        petsName = (EditText) findViewById(R.id.name);
        birthDate = (Button) findViewById(R.id.born);
        weight = (EditText) findViewById(R.id.weight);
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
            birthDate.setText(year1 + "/" + month1 + "/" + day1);
        } else {
            //dato er ikke valid - vis feilmld
            birthDate.setText("SET DATE");
            showError("DATE ERROR!", "You can't choose a future date");
        }

    }

    public void savePet(View view) {
        //0 = katt, 1 = hund
        int radioButtonID = radioButtons.getCheckedRadioButtonId();
        View radioButton = radioButtons.findViewById(radioButtonID);
        int idx = radioButtons.indexOfChild(radioButton);
        //sjekk om type av kjæledyr ble valgt
        if (idx < 0) {
            showError("ERROR!", "Choose kind of animal");
            return;
        }
        String name = petsName.getText().toString();
        if (name.equals("")) {
            showError("NAME MISSING!", "Name can't be empty");
            return;
        }

        String bDate = birthDate.getText().toString();
        if (bDate.toLowerCase().equals("set date")) {
            showError("DATE ERROR!", "Choose date");
            return;
        }

        //hent vekt
        double petsWeight;
        try {
            petsWeight = Double.parseDouble(weight.getText().toString().trim());
        }
        catch (NumberFormatException e) {
            //dersom ingenting er skrevet inn - sett vkt til null
            if (weight.getText().toString().length() == 0) {
                petsWeight = 0;
            } else {
                showError("WEIGHT ERROR!", "Use dot as a separator");
                weight.setText("");
                return;
            }
        }

        Pet pet = new Pet(name, bDate, idx, petsWeight);
        db.addPet(pet);
        Toast.makeText(this, "Pet successfully added", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showError(String title, String message ) {
        dialog = ErrorDialog.newInstance(title, message, (int) R.layout.activity_add_new_pet);
        dialog.show(getSupportFragmentManager(), "");
    }
    @Override
    public void exit() {
        dialog.dismiss();
    }
}


