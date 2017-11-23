package com.example.kasia.s305327mappe3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    static int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "PetDB";

    //konstruktør
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE = "CREATE TABLE " + TABLE_PETS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " +
                KEY_BIRTH + " TEXT, " + KEY_TYPE + " INTEGER)";
        Log.d("SQL", CREATE);
        sqLiteDatabase.execSQL(CREATE);
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
        }
        return pet;
    }
}
