package com.example.administrator.lztsg.items;

public class Hpoidetailpageitem implements MultipleItem {

    private String mImgurl;

    public Hpoidetailpageitem(String imgurl) {
        mImgurl = imgurl;
    }

    public String getmImgurl() {
        return mImgurl;
    }

    public void setmImgurl(String mImgurl) {
        this.mImgurl = mImgurl;
    }

    @Override
    public ItemType getItemType() {
        return ItemType.HPOIDETAILPAGE;
    }
}
