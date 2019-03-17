package com.example.sujit.utkal_hacks;

public class Files {

    String pushId;
    String type;
    String url;
    String name;
    String subject;
    String unit;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Files(String pushId, String type, String url, String name, String subject, String unit) {
        this.pushId = pushId;
        this.type = type;
        this.url = url;
        this.name = name;
        this.subject = subject;
        this.unit = unit;
    }




    Files(){

    }


    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

