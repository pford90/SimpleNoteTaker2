package com.peterford.simplenotetaker.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {
    private String mTitle;
    private String mContent;
    private long mCreatedDate;
    private long mModifiedDate;


    public Note(String title, String content, long dateTime) {
        mTitle = title;
        mContent = content;
        mCreatedDate = dateTime;
        mModifiedDate = mCreatedDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(long createdDate) {
        mCreatedDate = createdDate;
    }

    public String getDateTimeString(long dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM YYYY hh:mm");


        return format.format(new Date(dateTime));
    }

    public long getModifiedDate() {
        return mModifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        mModifiedDate = modifiedDate;
    }
}
