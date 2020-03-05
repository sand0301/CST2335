package com.example.cst2335.guardian;

import java.io.Serializable;

/**
 * This class holds the details of an article
 *
 * To pass it through activity using intents it is implementing
 * Serializable interface
 * */
class Article implements Serializable {

    private String webTitle;
    private String webUrl;
    private String sectionName;
    private String webPublicationDate;
    private String id;

    String getWebTitle() {
        return webTitle;
    }

    void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    String getWebUrl() {
        return webUrl;
    }

    void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    String getSectionName() {
        return sectionName;
    }

    void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    String getWebPublicationDate() {
        return webPublicationDate;
    }

    void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }
}
