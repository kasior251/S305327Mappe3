package com.example.kasia.s305327mappe3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasia on 22.11.2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    static String TABLE_PETS  = "Pets";
    static String KEY_ID = "_ID";
    static String KEY_NAME = "Name";
    static String KEY_BIRTH = "BirthDate";
    static String KEY_TYPE = "Type";
    static String KEY_WEIGHT = "Weight";
    static int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "PetDB";
    static String TABLE_TREATMENTS = "Treatments";
    static String KEY_PET = "Pet";
    static String KEY_TREATMENT_NAME = "Name";
    static String KEY_TREATMENT_DATE = "TreatmentDate";
    static String KEY_NEXT_TREATMENT = "NextTreatmentDate";

    //konstruktør
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //streng for å lage pet Table
        String CREATE = "CREATE TABLE " + TABLE_PETS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " +
                KEY_BIRTH + " TEXT, " + KEY_TYPE + " INTEGER, " + KEY_WEIGHT + " DOUBLE)";
        //Streng for å lage Treatments table - for å registrere behandlinger
        String CREATE2 = "CREATE TABLE " + TABLE_TREATMENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_PET + " INTEGER, " + KEY_TREATMENT_NAME + " TEXT, " + KEY_TREATMENT_DATE + " TEXT, " +
                KEY_NEXT_TREATMENT + " TEXT, " +
                "FOREIGN KEY(" + KEY_PET + ") REFERENCES " + TABLE_PETS +  "(" + KEY_ID +
                ")) ";
        Log.d("SQL", CREATE);
        sqLiteDatabase.execSQL(CREATE);
        sqLiteDatabase.execSQL(CREATE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PETS);
        onCreate(sqLiteDatabase);
    }

    //legger til et kjæledyr
    public void addPet(Pet pet) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pet.getName());
        values.put(KEY_BIRTH, pet.getBirthDate());
        values.put(KEY_TYPE, pet.getType());
        values.put(KEY_WEIGHT, pet.getWeight());
        sqLiteDatabase.insert(TABLE_PETS, null, values);
        sqLiteDatabase.close();
    }

    //finner alle registrerte kjæledyr
    public List<Pet> findAllPets() {
        List<Pet> allPets = new ArrayList<Pet>();
        String selectQuery = "SELECT * FROM " + TABLE_PETS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Pet pet = new Pet();
                pet.setId(cursor.getInt(0));
                pet.setName(cursor.getString(1));
                pet.setBirthDate(cursor.getString(2));
                pet.setType(cursor.getInt(3));
                pet.setWeight(cursor.getLong(4));
                allPets.add(pet);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return allPets;
    }

    public Pet getPet(int id) {
        Pet pet = new Pet();
        String selectQuery = "SELECT * FROM " + TABLE_PETS + " WHERE " + KEY_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            pet.setId(id);
            pet.setName(cursor.getString(1));
            pet.setBirthDate(cursor.getString(2));
            pet.setType(cursor.getInt(3));
            pet.setWeight(cursor.getDouble(4));
        }
        return pet;
    }

    public int updateWeight(int id, double weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WEIGHT, weight);
        int updated = db.update(TABLE_PETS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        return updated;
    }

    public void addTreatment(Treatment t) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TREATMENT_NAME, t.getName());
        values.put(KEY_PET, t.getPet().getId());
        values.put(KEY_TREATMENT_DATE, t.getTreatmentDate());
        values.put(KEY_NEXT_TREATMENT, t.getNextTreatment());
        sqLiteDatabase.insert(TABLE_TREATMENTS, null, values);
        sqLiteDatabase.close();
    }

    //finner 3 sist utførte behandlinger
    public List<Treatment> findGivenTreatments(int petId) {
        String selectQuery = "SELECT * FROM " + TABLE_TREATMENTS + " WHERE " + KEY_PET  +" = " + petId + " ORDER BY " + KEY_TREATMENT_DATE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        return getTreatments(selectQuery, db);
    }

    public List<Treatment> findDueTreatments(int petId) {
        String selectQuery = "SELECT * FROM " + TABLE_TREATMENTS + " WHERE " + KEY_PET  +" = " + petId + " AND " + KEY_NEXT_TREATMENT  + " NOT NULL " + " ORDER BY " + KEY_NEXT_TREATMENT + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        return getTreatments(selectQuery, db);
    }

    //privat medofe for å hente behandlinger
    private List<Treatment> getTreatments(String sql, SQLiteDatabase db) {
        List<Treatment> treatments = new ArrayList<Treatment>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Treatment treatment = new Treatment();
                treatment.setId(cursor.getInt(0));
                treatment.setPet(new Pet(cursor.getInt(1)));
                treatment.setName(cursor.getString(2));
                treatment.setTreatmentDate(cursor.getString(3));
                treatment.setNextTreatment(cursor.getString(4));
                treatments.add(treatment);
                } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        Log.d("SQL", "Found treatments " + treatments.size());
        return treatments;
    }

    public void deleteTreatment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TREATMENTS, KEY_ID + " =? ",
                new String[]{Integer.toString(id)});
    }

    public void deleteTreatmentNextDate(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_TREATMENTS + " SET " + KEY_NEXT_TREATMENT + " = NULL WHERE " + KEY_ID + " = " + id;
        db.execSQL(query);
        db.close();
    }

    public void deletePet(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PETS, KEY_ID + " =? ",
                new String[]{Integer.toString(id)});
        db.delete(TABLE_TREATMENTS, KEY_PET + " =? ",
                new String[]{Integer.toString(id)});
    }


}
