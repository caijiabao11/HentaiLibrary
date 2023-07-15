package com.example.administrator.lztsg.items;


import android.content.Context;

public class Item implements MultipleItem {

    private String mId;
    private String mTitle;
    private String mIntroduction;
    private int mPreferences;
    private String mImgurl;
    private int mMassid;
    private String mMasstit;
    private String mTag;
    Context context;

    public Item(String id, String title) {
        mId = id;
        mTitle = title;
    }

    public Item(String id, String title,String imgurl) {
        mId = id;
        mTitle = title;
        mImgurl = imgurl;
    }

    public Item(String id, String title, String introduction, int preferences, String imgurl,int massid,String tag) {
        mId = id;
        mTitle = title;
        mIntroduction = introduction;
        mPreferences = preferences;
        mImgurl = imgurl;
        mMassid = massid;
        mTag = tag;
    }

    public Item(String id, String title, String introduction, int preferences, String imgurl,String masstit,String tag) {
        mId = id;
        mTitle = title;
        mIntroduction = introduction;
        mPreferences = preferences;
        mImgurl = imgurl;
        mMasstit = masstit;
        mTag = tag;
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String introduction) {
        mIntroduction = introduction;
    }

    public int getPreferences() {
        return mPreferences;
    }

    public void setPreferences(int preferences) {
        mPreferences = preferences;
    }

    public String getImgurl() {
        return mImgurl;
    }

    public void setImgurl(String imgurl) {
        mImgurl = imgurl;
    }

    public int getmMassid() {
        return mMassid;
    }

    public void setmMassid(int massid) {
        mMassid = massid;
    }

    public String getmTag() {
        return mTag;
    }

    public void setmTag(String tag) {
        mTag = tag;
    }

    public String getmMassTit() {
        return mMasstit;
    }

    public void setmMassTit(String masstit) {
        mMasstit = masstit;
    }

    public ItemType getItemType() {
        return ItemType.HENTAI;
    }
}

