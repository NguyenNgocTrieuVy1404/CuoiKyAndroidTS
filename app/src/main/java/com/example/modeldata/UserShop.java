package com.example.modeldata;

public class UserShop {
    private int id;
    private String fullName;
    private String email;
    private String username;
    private String phoneNumber;
    private String gender;

    public UserShop(int id, String fullName, String email, String username, String phoneNumber, String gender) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }
}
