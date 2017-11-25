package com.example.kasia.s305327mappe3;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private Toolbar toolbar;
    LinearLayout list;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DBHandler(this);
        showAllPets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllPets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu, legg til elementer til action bar hvis det finnes noen
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle actionbar klikk her
        switch (item.getItemId()) {
            case R.id.action_settings:
                //code for settings
                break;
            case R.id.action_add:
                Intent intent = new Intent(this, AddNewPet.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void showAllPets() {
        list = (LinearLayout)findViewById(R.id.list);
        list.removeAllViews();
        List<Pet> pets = db.findAllPets();
        List<Button> buttons = new ArrayList<>();
        for (Pet p : pets) {
            Button button = new Button(this);
            if (p.getType() == 0) {
                button.setBackground(getDrawable(R.drawable.cat_button));
            }
            else {
                button.setBackground(getDrawable(R.drawable.dog_button));
            }
            button.setText(p.getName());
            button.setId(p.getId());
            button.setOnClickListener(this);
            buttons.add(button);


        }
        LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        checkParams.setMargins(10, 10, 10, 10);
        checkParams.gravity = Gravity.CENTER;
        for (Button b : buttons) {
            list.addView(b, checkParams);
        }
    }

    @Override
    public void onClick(View view) {
        //Få id til knappen for å vite hvilket dyr ble valgt
        int id = view.getId();
        Pet pet = db.getPet(id);
        Intent i = new Intent(this, PetData.class);
        //legg till alle data om dyret i intenten
        i.putExtra("ID", id);
        i.putExtra("NAME", pet.getName());
        i.putExtra("BIRTH", pet.getBirthDate());
        i.putExtra("TYPE", pet.getType());
        i.putExtra("WEIGHT", pet.getWeight());
        startActivity(i);
    }
}
