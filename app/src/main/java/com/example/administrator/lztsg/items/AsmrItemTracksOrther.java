package com.example.administrator.lztsg.items;

public class AsmrItemTracksOrther {
    private String title;
    private String type;
    private String mediaStreamUrl;
    private String duration;

    public AsmrItemTracksOrther(String title, String type, String mediaStreamUrl){
        this.title = title;
        this.type = type;
        this.mediaStreamUrl = mediaStreamUrl;
    }

    public AsmrItemTracksOrther(String title, String type,String mediaStreamUrl,String duration){
        this.title = title;
        this.type = type;
        this.mediaStreamUrl = mediaStreamUrl;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaStreamUrl() {
        return mediaStreamUrl;
    }

    public void setMediaStreamUrl(String mediaStreamUrl) {
        this.mediaStreamUrl = mediaStreamUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
