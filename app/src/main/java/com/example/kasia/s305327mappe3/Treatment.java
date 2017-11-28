package com.example.kasia.s305327mappe3;

/**
 * Created by Kasia on 24.11.2017.
 */

public class Treatment {

    private int id;
    private Pet pet;
    private String name;
    private String treatmentDate;
    private String nextTreatment;

    //konstruktører
    public Treatment(int id, Pet pet, String name, String treatmentDate, String nextTreatment) {
        this.id = id;
        this.pet = pet;
        this.name = name;
        this.treatmentDate = treatmentDate;
        this.nextTreatment = nextTreatment;
    }

    public Treatment(String name, String treatmentDate, String nextTreatment) {

        this.name = name;
        this.treatmentDate = treatmentDate;
        this.nextTreatment = nextTreatment;
    }

    public Treatment(Pet pet, String name, String treatmentDate, String nextTreatment) {

        this.pet = pet;
        this.name = name;
        this.treatmentDate = treatmentDate;
        this.nextTreatment = nextTreatment;
    }

    public Treatment() {

    }
    //konstruktører slutt


    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(String treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getNextTreatment() {
        return nextTreatment;
    }

    public void setNextTreatment(String nextTreatment) {
        this.nextTreatment = nextTreatment;
    }
}
