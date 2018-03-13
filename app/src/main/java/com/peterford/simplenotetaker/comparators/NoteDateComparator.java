package com.peterford.simplenotetaker.comparators;

import com.peterford.simplenotetaker.model.Note;

import java.util.Comparator;

/**
 * Created by Peter on 3/1/2018.
 */

public class NoteDateComparator implements Comparator<Note> {

    public enum SortType { CREATED_DESC, CREATED_ASC, MODIFIED_DESC, MODIFIED_ASC  }

    private SortType mSortType;

    public NoteDateComparator(SortType sortType) {
        mSortType = sortType;
    }


    @Override
    public int compare(Note note, Note t1) {
        if( mSortType == SortType.CREATED_DESC)
            return new Long(note.getCreatedDate()).compareTo(t1.getCreatedDate());
        else if( mSortType == SortType.CREATED_ASC)
            return new Long(t1.getCreatedDate()).compareTo(note.getCreatedDate());
        else if( mSortType == SortType.MODIFIED_DESC )
            return new Long(note.getModifiedDate()).compareTo(t1.getModifiedDate());
        else if( mSortType == SortType.MODIFIED_ASC )
            return new Long(t1.getModifiedDate()).compareTo(note.getModifiedDate());

        return 0;
    }
}
