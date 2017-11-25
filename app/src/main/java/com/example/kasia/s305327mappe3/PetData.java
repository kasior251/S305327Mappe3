package com.example.kasia.s305327mappe3;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Toast.makeText(this, "weight " + petsWeight, Toast.LENGTH_SHORT).show();
        //sett bare dersom vekt ikke er 0
        if (petsWeight != 0) {
            String weightToShow = Double.toString(petsWeight) + " kg";
            weight.setText(weightToShow);
        }

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

    public void saveNewWeight(View view) {
        newWeight = ((EditText) fragmentView.findViewById(R.id.new_weight)).getText().toString();
        double weight = 0;
        try {
            weight = Double.parseDouble(newWeight);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Wrong weight format. Use dot as a separator ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.updateWeight(id, weight) == 0) {
            Toast.makeText(this, "Something went wront. Please try again", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Weight updated", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            //put den oppdaterte vekten i intenten
            Pet pet = db.getPet(id);
            intent.putExtra("WEIGHT", pet.getWeight());
            finish();
            startActivity(intent);
        }
    }

    public void cancel(View view) {
        dialog.cancel();
    }

}
