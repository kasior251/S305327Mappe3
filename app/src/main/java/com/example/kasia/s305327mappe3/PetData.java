package com.example.kasia.s305327mappe3;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Kasia on 23.11.2017.
 */

public class PetData extends AppCompatActivity {

    DBHandler db;
    ImageView icon;
    TextView name;
    TextView birth;
    TextView weight;
    String newWeight;
    Button changeWeight;
    View fragmentView;
    Dialog dialog;
    int id;
    int type;
    LinearLayout treatmentView;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(this);
        setContentView(R.layout.activity_pet_data);
        icon = (ImageView) findViewById(R.id.pet_icon);
        type = getIntent().getIntExtra("TYPE" ,0);
        id = getIntent().getIntExtra("ID", 0);
        //sette bilde for type av dyr
        if (type == 0) {
            icon.setImageDrawable(getDrawable(R.drawable.cat));
        } else {
            icon.setImageDrawable(getDrawable(R.drawable.dog));
        }
        name = (TextView) findViewById(R.id.pet_name);
        //sette navnet på dyret
        name.setText(getIntent().getStringExtra("NAME"));
        birth = (TextView) findViewById(R.id.pet_birth_date);
        birth.setText(getIntent().getStringExtra("BIRTH"));
        weight = (TextView)findViewById(R.id.weight);
        double petsWeight = (getIntent().getDoubleExtra("WEIGHT", 0));
        Log.d("DATA", "weight" + petsWeight);
        //sett bare dersom vekt ikke er 0
        if (petsWeight != 0) {
            String weightToShow = Double.toString(petsWeight) + " kg";
            weight.setText(weightToShow);
        }
    }

    @Override
    protected void onResume() {
        if (treatmentView != null) {
            treatmentView.removeAllViews();
        }
        super.onResume();
    }

    //metode for å registrere ny behandling på dyret
    public void addNewTreatment(View view) {
        Intent intent = new Intent(this, AddNewTreatment.class);
        //send id til valgte dyret i intenten
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    //oppdatere vekt på dyret
    public void updateWeight(View view) {
        fragmentView = (LayoutInflater.from(PetData.this).inflate(R.layout.fragment_new_weight, null));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PetData.this);
        alertBuilder.setView(fragmentView);
        dialog = alertBuilder.create();
        dialog.show();
    }


    //oppdatere dyrets vekt
    public void saveNewWeight(View view) {
        newWeight = ((EditText) fragmentView.findViewById(R.id.new_weight)).getText().toString();
        double weight = 0;
        try {
            weight = Double.parseDouble(newWeight);
        }
        catch (NumberFormatException e) {
            //feil format på vekt
            Toast.makeText(this, "Wrong weight format. Use dot as a separator ", Toast.LENGTH_SHORT).show();
            return;
        }

        //gikk ikke å oppdatere db
        if (db.updateWeight(id, weight) == 0) {
            Toast.makeText(this, "Something went wront. Please try again", Toast.LENGTH_SHORT).show();
        }
        else {
            //det gikk bra å oppdatere vekt
            Toast.makeText(this, "Weight updated", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            //put den oppdaterte vekten i intenten
            Pet pet = db.getPet(id);
            intent.putExtra("WEIGHT", pet.getWeight());
            finish();
            startActivity(intent);
        }
    }

    //lukke dialog-vinduet
    public void cancel(View view) {
        dialog.cancel();
    }

    //list opp 3 siste behandlinger
    public void showRecentTreatments(View view) {
        List<Treatment> treatments = db.findGivenTreatments(id);

        treatmentView = (LinearLayout) findViewById(R.id.list);
        treatmentView.removeAllViews();

        if (treatments.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            tv.setText(getString(R.string.no_treatments));
            treatmentView.addView(tv); }

        for (Treatment t: treatments) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);

            //name
            TextView name = new TextView(this);
            name.setTextSize(20);
            name.setWidth(350);
            name.setMaxLines(1);
            name.setText(t.getName());
            row.addView(name, params);

            //date
            TextView date = new TextView(this);
            date.setTextSize(20);
            date.setWidth(300);
            date.setMaxLines(1);
            date.setText(t.getTreatmentDate());
            row.addView(date, params);

            //button
            ImageButton btn = new ImageButton(this);
            btn.setId(t.getId());
            final int id_ = btn.getId();
            btn.setImageDrawable(getDrawable(R.drawable.remove));
            row.addView(btn, params);

            treatmentView.addView(row);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    deleteTreatment(id_);
                }
            });
        }
    }

    //metoden som fjerner behandling og oppdaterer viewet
    private void deleteTreatment(int id) {
        db.deleteTreatment(id);
        showRecentTreatments(treatmentView);
    }

        //list planlagte behandlinger
    public void showDueTreatments(View view) {
        List<Treatment> treatments = db.findDueTreatments(id);
        treatmentView = (LinearLayout) findViewById(R.id.list);
        treatmentView.removeAllViews();

        //gi info på skjerm hvis ingen behandlinger ble funnet
        if (treatments.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            tv.setText(getString(R.string.no_coming_treatments));
            treatmentView.addView(tv); }

        for (Treatment t: treatments) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);

            //name
            TextView name = new TextView(this);
            name.setTextSize(20);
            name.setWidth(350);
            name.setMaxLines(1);
            name.setText(t.getName());
            row.addView(name, params);

            //date
            TextView date = new TextView(this);
            date.setTextSize(20);
            date.setWidth(300);
            date.setMaxLines(1);
            date.setText(t.getNextTreatment());
            //sjekk om dato for neste behandling har passert (eller er i dag), hvis ja, vis dato i rødt
            if (checkDueDate(t.getNextTreatment()) > 0) {
                date.setTextColor(Color.RED);
                name.setTextColor(Color.RED);
            }
            //sjekk om det er noen behandlinger planlagt for i dag, hvis ja - vis dem i gult
            else if (checkDueDate(t.getNextTreatment())  == 0) {
                date.setTextColor(Color.YELLOW);
                name.setTextColor(Color.YELLOW);
            }
            row.addView(date, params);

            //button
            ImageButton btn = new ImageButton(this);
            btn.setId(t.getId());
            final int id_ = btn.getId();
            btn.setImageDrawable(getDrawable(R.drawable.remove));
            row.addView(btn, params);

            treatmentView.addView(row);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    deleteNextTreatmentDate(id_);
                    Toast.makeText(PetData.this, "Treatment deleted from list of due treatments", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //metoden som fjerner dato av neste behandling og oppdaterer viewet
    private void deleteNextTreatmentDate(int id) {
        db.deleteTreatmentNextDate(id);
        showDueTreatments(treatmentView);
    }

    public void deletePet(View view) {
        fragmentView = (LayoutInflater.from(PetData.this).inflate(R.layout.fragment_delete_pet, null));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PetData.this);
        alertBuilder.setView(fragmentView);
        dialog = alertBuilder.create();
        dialog.show();
    }

    public void delete(View view) {
        db.deletePet(id);
        Toast.makeText(getApplicationContext(), "Pet successfully deleted!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int checkDueDate(String date) {
        String today = sdf.format(Calendar.getInstance().getTime());
        Log.d("DATA ", date + " number " + today.compareTo(date));
        return today.compareTo(date);
    }

}
