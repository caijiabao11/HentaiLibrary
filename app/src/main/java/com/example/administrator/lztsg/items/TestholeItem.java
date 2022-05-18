package com.example.administrator.lztsg.items;


public class TestholeItem implements MultipleItem {

    private String mTitle; //标题
    private String mVideoUrl; //视频链接
    private String mImageUrl; //图片链接

    public TestholeItem(String name, String contentUrl, String thumbnailUrl) {
        mTitle = name;
        mVideoUrl = contentUrl;
        mImageUrl = thumbnailUrl;
    }
//
//    public TestholeItem() {
//
//    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String name) {
        mTitle = name;
    }

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public void setmVideoUrl(String contentUrl) {
        mTitle = contentUrl;
    }

    public String getmImageUrl(){
        return mImageUrl;
    }

    public void setmImageUrl(String thumbnailUrl){
        this.mImageUrl = thumbnailUrl;
    }

    public ItemType getItemType() {
        return ItemType.TESTHOLE;
    }
}

