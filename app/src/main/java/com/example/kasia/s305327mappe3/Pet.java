package com.example.kasia.s305327mappe3;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kasia on 22.11.2017.
 */

public class Pet {

    private int id;
    private String name;
    private String birthDate;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public Pet() {

    }

    public Pet(String name, String birthDate, int type) {

        this.name = name;
        this.birthDate = birthDate;
        this.type = type;
    }

    public Pet(int id, String name, String birthDate, int type) {

        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.type = type;
    }
}
