package com.example.mohamed.capstone;

public class User {
    String name;
    String status;
    String image;

    public User(String mname, String mstatus, String mimage) {
        name = mname;
        status = mstatus;
        image = mimage;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }
}
