package com.example.cst2335.bbc;

import java.io.Serializable;

/**
 * Holds the data for the single news item.
 * This is Marked as serializable to pass the data through intent.
 * */
public class BBCNewsItem implements Serializable {

    String title;
    String description;
    String link;
    String pubDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
