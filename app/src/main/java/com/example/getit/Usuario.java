package com.example.getit;

public class Usuario {
    private int UserId;
    private String Name;
    private String LastName;
    private String Age;
    private String Dni;
    private String Email;
    private String Password;
    private String Cellphone;
    private String Latitude;
    private String Longitude;
    private String Stars;
    private String RegisterCompleted;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }
}
