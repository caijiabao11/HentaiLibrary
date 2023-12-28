package com.example.administrator.lztsg.items;

public class Hpoiitem implements MultipleItem {

    private String mTitle;
    private String mImgurl;
    private String mUrl;

    public Hpoiitem(String title,String imgurl,String url) {
        mTitle = title;
        mImgurl = imgurl;
        mUrl = url;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImgurl() {
        return mImgurl;
    }

    public void setmImgurl(String mImgurl) {
        this.mImgurl = mImgurl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public ItemType getItemType() {
        return ItemType.HPOI;
    }
}
