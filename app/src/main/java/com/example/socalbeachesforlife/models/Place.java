package com.example.socalbeachesforlife.models;

public class Place {
    public String name;
    public String formatted_address;
    public String formatted_phone_number;
    public String opening_hours;
    public String place_id;
    public double rating;
    public String latitude;
    public String longitude;

    public Place() {

    }

    public Place(String name, String formatted_address, String formatted_phone_number) {
        this.name = name;
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_phone_number;
    }
}
