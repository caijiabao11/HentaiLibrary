package com.example.administrator.lztsg.items;


import org.json.JSONArray;

public class AsmrItem implements MultipleItem {
    String id;
    String title;
    String name;
    String release;
    JSONArray vas;
    JSONArray tags;
    String CoverUrl;

    public AsmrItem(String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.release = release;
        this.vas = vas;
        this.tags = tags;
        this.CoverUrl = CoverUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public JSONArray getVas() {
        return vas;
    }

    public void setVas(JSONArray vas) {
        this.vas = vas;
    }

    public JSONArray getTags() {
        return tags;
    }

    public void setTags(JSONArray tags) {
        this.tags = tags;
    }

    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        CoverUrl = coverUrl;
    }

    public ItemType getItemType() {
        return ItemType.ASMR;
    }
}

