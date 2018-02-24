package com.peterford.simplenotetaker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peterford.simplenotetaker.model.Note;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private Note[] mNotes;
    private Context mContext;

    public NoteAdapter(Context context, Note[] notes) {
        mContext = context;
        mNotes = notes;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.notes_listview, null, false);
        NoteViewHolder holder = new NoteViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {

        holder.bindNote(mNotes[position]);
    }

    @Override
    public int getItemCount() {
        return mNotes.length;
    }



    public class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_title) TextView mNoteTitle;
        @BindView(R.id.note_content) TextView mNoteContent;

        public NoteViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindNote(Note note) {
            mNoteTitle.setText( note.getTitle() );
            mNoteContent.setText( note.getContent() );
        }
    }
}
