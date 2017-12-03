package com.example.kasia.s305327mappe3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kasia on 24.11.2017.
 */

public class AddNewTreatment extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, ErrorDialog.DialogClickListener {

    Spinner nextTreatment;
    Button treatmentDate;
    EditText name;
    EditText nextTreatmentNr;
    int day, month, year, day1, month1, year1;
    Date chosenDate;
    Date nextTreatmentDate;
    int timeUnit;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    int petId;
    DBHandler db;
    ErrorDialog dialog;
    boolean dateValid;


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
        db = new DBHandler(this);
        petId = getIntent().getIntExtra("ID", 0);
        nextTreatment.setOnItemSelectedListener(this);
        name = (EditText) findViewById(R.id.treatment_name);
        nextTreatmentNr = (EditText) findViewById(R.id.next_treatment_number);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //holde tidsenhet valgt i variablelen timeUnit;
        switch (i) {
            case 1:
                //enhet = dag
                timeUnit = 1;
                break;
            case 2:
                //enhet = uke
                timeUnit = 7;
                break;
            case 3:
                //enhet = måned, blir noe unøyaktig dersom antal måneder << 1
                timeUnit = 30;
                break;
            case 4:
                //enhet = år
                timeUnit = 365;
                break;
            default:
                timeUnit = 0;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        timeUnit = 0;
    }

    public void saveTreatment(View view) {
        String treatmentName = name.getText().toString();

        //sjekk om det er skrevet noe behandlingsnavn
        if (treatmentName.length() == 0) {
            showError("MISSING TREATMENT NAME", "Name can't be empty");
            return;
        }

        //sjekk om datoen ble valgt
        if (chosenDate == null) {
            showError("DATE ERROR!", "Choose date");
            return;
        }

        //sjekk om next treatment dato skal settes
        String offset = nextTreatmentNr.getText().toString();
        int dateOffset;
        try {
            dateOffset = Integer.parseInt(offset);
        }
        catch (NumberFormatException e) {
            //sett til 0 dersom ugyldig input
            dateOffset = 0;
        }

        //valgt tid enhet
        if (timeUnit != 0) {
            //skrevet inn gyldig tall i next treatment felt
            if (dateOffset != 0) {
                nextTreatmentDate = createNextTreatmentDate(chosenDate, dateOffset * timeUnit);
                Log.d("DATA", "next treatment date" + nextTreatmentDate);
            }
            else {
                //ugyldig input i next treatment felt
                showError("NOT A NUMBER!", "Next treatment field containts not allowed characters");
                return;
            }
        } else if (dateOffset != 0) {  //valid tall skrevet i nextTreatment felt men ingen tid enhet valgt
            showError("ERROR", "Choose time unit from the drop-down list");
            return;
        }

        //alt gikk bra, kan lagre behandlignen til databasen
        String tDate = sdf.format(chosenDate);
        String nDate;
        if (nextTreatmentDate != null) {
            nDate = sdf.format(nextTreatmentDate);
        } else {
            nDate = null;
        }
        Pet pet = new Pet(petId);
        Treatment treatment;
        //registreres behandling i fortiden
        if (dateValid) {
            treatment = new Treatment(pet, treatmentName, tDate, nDate);
        } else {
            //her "treatment date" blir til "next treatment date" og treatment date blir til null
            //må legge til datePicker her
            treatment = new Treatment(pet, treatmentName, null, tDate);
        }

        db.addTreatment(treatment);
        Toast.makeText(getApplicationContext(), "Treatment sccessfully added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private Date createNextTreatmentDate(Date date, int dayOffset) {
        Log.d("DATA", "day offset " + dayOffset);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);
        Date newDate = calendar.getTime();
        return newDate;
    }

    public void chooseDate(View view) {
        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //vis datepicker for å velge behandlingsdato
        DatePickerDialog dialog = new DatePickerDialog(this, this, year, month, day);
        dialog.show();
    }

    //privat metode for å få ut dato format fra datePicker
    private static Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        year1 = i;
        month1 = i1 + 1;
        day1 = i2;
        chosenDate = getDateFromDatePicker(datePicker);
        Log.d("DATA", "Chosen date " + chosenDate);
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

        treatmentDate.setText(day1 + "/" + month1 + "/" + year1);

        //date ligger i framtiden - kan ikke velges dato for neste behandling, for man registrerer "neste" behandling
        if (!dateValid) {
            nextTreatmentNr.setBackground(getDrawable(R.drawable.input_disabled));
            nextTreatment.setBackground(getDrawable(R.drawable.input_disabled));
            nextTreatmentNr.setEnabled(false);
            nextTreatment.setEnabled(false);
        } else {
            nextTreatmentNr.setBackground(getDrawable(R.drawable.input));
            nextTreatment.setBackground(getDrawable(R.drawable.input));
            nextTreatmentNr.setEnabled(true);
            nextTreatment.setEnabled(true);
        }

    }

    private void showError(String title, String message ) {
        dialog = ErrorDialog.newInstance(title, message, (int) R.layout.activity_add_new_treatment);
        dialog.show(getSupportFragmentManager(), "");
    }


    @Override
    public void exit() {
        dialog.dismiss();
    }
}
