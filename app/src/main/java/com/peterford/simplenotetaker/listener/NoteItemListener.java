package com.peterford.simplenotetaker.listener;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.peterford.simplenotetaker.NoteActivity;
import com.peterford.simplenotetaker.model.Note;

/**
 * Created by Peter on 2/23/2018.
 */

public class NoteItemListener implements RecyclerViewClickListener.OnItemClickListener {
    @Override
    public void onItemClick(View view, int position, Object object) {
        Note note = (Note) object;
        Log.i("MY RECYCLERVIEW", "PRESSED : " + note.getDateTimeString());

        // Go to Note Activity
        Intent intent = new Intent(view.getContext(), NoteActivity.class);
        intent.putExtra("note", note);
        view.getContext().startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view) {
    }

}
