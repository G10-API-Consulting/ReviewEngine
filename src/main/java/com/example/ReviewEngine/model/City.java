package com.example.ReviewEngine.model;


public class City {
    public long id;
    public String name;
    public String state;
    public String country;
    public Coord coord;

    public static class Coord {
        public double lon;
        public double lat;
    }
}
