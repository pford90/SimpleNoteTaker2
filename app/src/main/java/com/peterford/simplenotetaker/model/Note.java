package com.peterford.simplenotetaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {
    private String mTitle;
    private String mContent;
    private long mDateTime;

    public Note(String title, String content, long dateTime) {
        mTitle = title;
        mContent = content;
        mDateTime = dateTime;
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

    public long getDateTime() {
        return mDateTime;
    }

    public void setDateTime(long dateTime) {
        mDateTime = dateTime;
    }

    public String getDateTimeString() {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM YYYY hh:mm");


        return format.format(new Date(mDateTime));
    }


}
