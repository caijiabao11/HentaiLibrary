package com.example.administrator.lztsg.items;

import org.json.JSONArray;

public class AsmrItemTracks {
    private String title;
    private String type;
    private JSONArray folder;
    private AsmrItemTracksOrther orthers;


    public AsmrItemTracks(String title,String type,JSONArray folder){
        this.title = title;
        this.type = type;
        this.folder = folder;
    }

    public AsmrItemTracks(AsmrItemTracksOrther orthers){
        this.orthers = orthers;
    }

    public AsmrItemTracks(JSONArray folder){
        this.folder = folder;
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

    public JSONArray getFolder() {
        return folder;
    }

    public void setFolder(JSONArray folder) {
        this.folder = folder;
    }

    public AsmrItemTracksOrther getOrthers() {
        return orthers;
    }

    public void setOrthers(AsmrItemTracksOrther orthers) {
        this.orthers = orthers;
    }
}
