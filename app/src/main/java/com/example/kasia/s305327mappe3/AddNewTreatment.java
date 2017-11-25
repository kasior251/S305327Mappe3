package com.example.kasia.s305327mappe3;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kasia on 24.11.2017.
 */

public class AddNewTreatment extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    Spinner nextTreatment;
    Button treatmentDate;
    EditText name;
    EditText nextTreatmentNr;
    int day, month, year, day1, month1, year1;

    @Override
    protected void onCreate(Bundle savenInstanceState) {
        super.onCreate(savenInstanceState);
        setContentView(R.layout.activity_add_new_treatment);
        treatmentDate = (Button) findViewById(R.id.treatment_date);
        nextTreatment = (Spinner) findViewById(R.id.next_treatment_unit);
        // lag ArrayAdapter ved bruk av string array og default layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.next_treatment_period, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nextTreatment.setAdapter(adapter);
        //koble til listener
        nextTreatment.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int days;
        switch (i) {
            case 1:
                days = 1;
                break;
            case 2:
                days = 7;
                break;
            case 3:
                days = 30;
                break;
            case 4:
                days = 365;
                break;
            default:
                days = 0;
        }


        Toast.makeText(this, "Days " + days , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //gjør ingenting, som default er det undefined
    }

    public void saveTreatment(View view) {

    }

    public void chooseDate(View view) {
        final Calendar c = Calendar.getInstance();

        year = year1;
        month = month1-1;
        day = day1;



        //vis datepicker for å velge behandlingsdato
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
            treatmentDate.setText(day1 + "/" + month1 + "/" + year1);
        } else {
            //dato er ikke valid - vis feilmld
            treatmentDate.setText(getString(R.string.choose_date));
            Toast.makeText(getApplicationContext(), "You can't choose a future date", Toast.LENGTH_SHORT).show();
        }
    }
}
