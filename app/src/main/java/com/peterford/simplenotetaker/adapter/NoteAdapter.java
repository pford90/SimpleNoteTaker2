package com.peterford.simplenotetaker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peterford.simplenotetaker.R;
import com.peterford.simplenotetaker.model.Note;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private static final String TAG = NoteAdapter.class.getSimpleName();

    private ArrayList<Note> mNotes;
    private Context mContext;
    private boolean mCheckBoxFlag = false;


    public NoteAdapter(Context context, ArrayList<Note> notes) {
        mContext = context;
        mNotes = notes;
    }

    public void toggleCheckBoxFlag(){
        mCheckBoxFlag = !mCheckBoxFlag;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.notes_listview, null, false);
        NoteViewHolder holder = new NoteViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.bindNote( mNotes.get(position) );
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void deleteNote(Note note) {
        int position = mNotes.indexOf(note);
        mNotes.remove(position);
        notifyItemRemoved(position);
    }



    public class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_title) TextView mNoteTitle;
        @BindView(R.id.note_content) TextView mNoteContent;
        @BindView(R.id.note_dateTime_display) TextView mNoteDateTimeDisplay;

        public NoteViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindNote(Note note) {
            mNoteTitle.setText( note.getTitle() );
            mNoteContent.setText( note.getContent() );

            mNoteDateTimeDisplay.setText( note.getDateTimeString(note.getModifiedDate()) );

        }
    }
}
