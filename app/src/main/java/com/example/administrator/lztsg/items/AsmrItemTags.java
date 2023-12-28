package com.example.administrator.lztsg.items;

import android.os.Parcel;
import android.os.Parcelable;

public class AsmrItemTags implements Parcelable {
    private String id;
    private String name;

    public AsmrItemTags(String id, String name){
        this.id = id;
        this.name = name;
    }

    public AsmrItemTags(Parcel source) {
        id = source.readString();
        name = source.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
    // 添加CREATOR字段
    public static final Parcelable.Creator<AsmrItemTags> CREATOR = new Parcelable.Creator<AsmrItemTags>() {
        @Override
        public AsmrItemTags createFromParcel(Parcel source) {
            return new AsmrItemTags(source);
        }

        @Override
        public AsmrItemTags[] newArray(int size) {
            return new AsmrItemTags[size];
        }
    };
}
