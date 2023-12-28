package com.example.administrator.lztsg.items;

import android.os.Parcel;

public class Asmr_chip {
    private String id;
    private String name;
    private String count;



    public Asmr_chip(String id, String name, String count){
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public Asmr_chip(Parcel source) {
        id = source.readString();
        name = source.readString();
        count = source.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
