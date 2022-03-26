package com.example.administrator.lztsg.items;


public class TestholeItem implements MultipleItem {

    private int mImageResId;
    private String mTitle;

    public TestholeItem(int imageResId, String title) {
        mImageResId = imageResId;
        mTitle = title;
    }

    public int getImageResId() {
        return mImageResId;
    }

    public void setImageResId(int imageResId) {
        mImageResId = imageResId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ItemType getItemType() {
        return ItemType.TESTHOLE;
    }
}

