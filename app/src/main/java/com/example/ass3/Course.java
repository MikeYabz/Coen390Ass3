package com.example.ass3;

public class Course {

    private long id;
    private String title;
    private String code;

    public Course(int id, String title, String code) {
        this.id = id;
        this.title = title;
        this.code = code;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
