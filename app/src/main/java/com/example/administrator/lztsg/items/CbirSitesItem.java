package com.example.administrator.lztsg.items;


public class CbirSitesItem implements MultipleItem {
    private String mImageUrl; //图片链接
    private String mSize; //尺寸
    private String mTitle; //标题
    private String mTitleUrl; //跳转链接
    private String mDomain; //网址信息
    private String mDescriptionl; //其他信息

    public CbirSitesItem(String imageurl){
        mImageUrl = imageurl;
    }
    public CbirSitesItem(String imageurl,String size,String title,String titleurl,String domain,String description) {
        mImageUrl = imageurl;
        mSize = size;
        mTitle = title;
        mTitleUrl = titleurl;
        mDomain = domain;
        mDescriptionl = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageurl) {
        mImageUrl = imageurl;
    }

    public String getImgSize() {
        return mSize;
    }

    public void setImgSize(String size) {
        mSize = size;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitleUrl() {
        return mTitleUrl;
    }

    public void setTitleUrl(String titleurl) {
        mTitleUrl = titleurl;
    }

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String domain) {
        mDomain = domain;
    }

    public String getDescriptionl() {
        return mDescriptionl;
    }

    public void setDescriptionl(String description) {
        mDescriptionl = description;
    }

    public ItemType getItemType() {
        return ItemType.SEARCH_IMG;
    }
}

