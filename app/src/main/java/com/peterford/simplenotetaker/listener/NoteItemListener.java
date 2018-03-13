package com.peterford.simplenotetaker.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.peterford.simplenotetaker.activity.NoteActivity;
import com.peterford.simplenotetaker.model.Note;

import java.util.ArrayList;

public class NoteItemListener implements RecyclerViewTouchListener.OnItemClickListener {

    private final String TAG = NoteItemListener.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ArrayList<Note> mNotes;
    public NoteItemListener(RecyclerView recyclerView, ArrayList<Note> notes) {
        mRecyclerView = recyclerView;
        mNotes = notes;
    }

    @Override
    public void onItemClick(View view, int position, Object object) {
        Note note = (Note) object;

        // Go to Note Activity
        Intent intent = new Intent(view.getContext(), NoteActivity.class);
        intent.putExtra("note", note);
        view.getContext().startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position, Object object) {
        Note note = (Note) object;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage("Are you sure you want to Delete?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Context context = dialogBuilder.getContext();

                if( context.deleteFile(note.getCreatedDate() + ".preferences") ) {
                    Log.v(TAG, "DELETED THIS NOTE");
                    mNotes.remove(position);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Log.v(TAG, "CANT FIND NOTE");
                }
            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
