package com.peterford.simplenotetaker.comparators;

import com.peterford.simplenotetaker.model.Note;

import java.util.Comparator;

/**
 * Created by Peter on 3/1/2018.
 */

public class NoteCreatedDateDescComparator implements Comparator<Note> {

    public enum SortType { DESC, ASC }

    private SortType mSortType;

    public NoteCreatedDateDescComparator(SortType sortType) {
        mSortType = sortType;
    }


    @Override
    public int compare(Note note, Note t1) {
        if( mSortType == SortType.DESC)
            return new Long(note.getDateTime()).compareTo(t1.getDateTime());
        else if( mSortType == SortType.ASC)
            return new Long(t1.getDateTime()).compareTo(note.getDateTime());

        return 0;
    }
}
