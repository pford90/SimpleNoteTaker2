package com.peterford.simplenotetaker.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.peterford.simplenotetaker.NoteActivity;
import com.peterford.simplenotetaker.model.Note;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Peter on 2/23/2018.
 */

public class NoteItemListener implements RecyclerViewClickListener.OnItemClickListener {

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
        Log.i("MY RECYCLERVIEW", "PRESSED : " + note.getDateTimeString());

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

                if( context.deleteFile(note.getDateTime() + ".preferences") ) {
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
