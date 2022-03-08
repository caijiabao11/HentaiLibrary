package com.example.administrator.lztsg.items;


public class More1Item implements MultipleItem {

    private int mImageResId;
    private String mTitle;

    public More1Item(int imageResId, String title) {
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
        return ItemType.MORE1;
    }
}

