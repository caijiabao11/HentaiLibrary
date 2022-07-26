package com.example.administrator.lztsg.items;


public class TestholeItem implements MultipleItem {

    private String mTitle; //标题
    private String mVideoUrl; //视频链接
    private String mImageUrl; //图片链接
    private String mDuration; //时间戳

    public TestholeItem(String name, String contentUrl, String thumbnailUrl, String duration) {
        mTitle = name;
        mVideoUrl = contentUrl;
        mImageUrl = thumbnailUrl;
        mDuration = duration;
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

    public String getmDuration(){
        return mDuration;
    }

    public void setmDuration(String duration){
        this.mDuration = duration;
    }

    public ItemType getItemType() {
        return ItemType.TESTHOLE;
    }
}

