package com.example.myapplication;

public class Photo {
    private String  resource;

    public Photo(String resource) {
        this.resource = resource;
    }

    public Photo() {
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
