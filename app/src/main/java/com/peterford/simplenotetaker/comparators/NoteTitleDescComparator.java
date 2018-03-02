package com.peterford.simplenotetaker.comparators;

import android.media.audiofx.AudioEffect;

import com.peterford.simplenotetaker.model.Note;

import java.util.Comparator;

public class NoteTitleDescComparator implements Comparator<Note> {

    @Override
    public int compare(Note note, Note t1) {
        return note.getTitle().compareTo(t1.getTitle());
    }
}
