package com.example.administrator.lztsg.items;

import android.os.Parcel;
import android.os.Parcelable;

public class AsmrItemVas implements Parcelable {
    private String id;
    private String name;

    public AsmrItemVas(String id, String name){
        this.id = id;
        this.name = name;
    }

    public AsmrItemVas(Parcel source) {
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
    public static final Parcelable.Creator<AsmrItemVas> CREATOR = new Parcelable.Creator<AsmrItemVas>() {
        @Override
        public AsmrItemVas createFromParcel(Parcel source) {
            return new AsmrItemVas(source);
        }

        @Override
        public AsmrItemVas[] newArray(int size) {
            return new AsmrItemVas[size];
        }
    };

}
