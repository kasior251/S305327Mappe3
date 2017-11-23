package com.example.kasia.s305327mappe3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(this);
        setContentView(R.layout.activity_pet_data);
        icon = (ImageView) findViewById(R.id.pet_icon);
        type = getIntent().getIntExtra("TYPE" ,0);
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

    }

}
