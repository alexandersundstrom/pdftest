package com.pdf.test.model;

import java.util.List;

public class Person {

    private String firstName;
    private String lastName;
    private int age;
    private boolean married;
    private String imagePath;
    private List<String> interests;

    public Person() {
    }

    public Person(String firstName, String lastName, int age, boolean married) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.married = married;
    }

    public Person(String firstName, String lastName, int age, boolean married, String imagePath, List<String> interests) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.married = married;
        this.imagePath = imagePath;
        this.interests = interests;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
