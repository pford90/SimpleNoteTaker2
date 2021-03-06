package com.peterford.simplenotetaker.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.peterford.simplenotetaker.R;
import com.peterford.simplenotetaker.model.Note;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private static final String PREF_FILE = ".preferences";
    private static final String TAG = NoteActivity.class.getSimpleName();

    @BindView(R.id.note_note_title)     EditText mNoteTitle;
    @BindView(R.id.note_note_content)   EditText mNoteContent;
    @BindView(R.id.note_toolbar)        Toolbar mToolbar;

    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if( getIntent().getSerializableExtra(("note")) != null ) {
            mNote =  (Note) getIntent().getSerializableExtra("note");
            mNoteTitle.setText(mNote.getTitle());
            mNoteContent.setText(mNote.getContent());
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "INSIDE ON MENU ITEM CLICK", Toast.LENGTH_SHORT).show();
        return false;
    }

    @OnClick(R.id.note_save)
    public void saveNote(View view) {

        Note note = null;
        if( mNote == null) {
           note = new Note(mNoteTitle.getText().toString(), mNoteContent.getText().toString(), new Date().getTime());
        } else {
            mNote.setTitle(mNoteTitle.getText().toString());
            mNote.setContent(mNoteContent.getText().toString());
            mNote.setModifiedDate( new Date().getTime());
            note = mNote;
        }

        Log.v("NOTEACTIVITY", note.getDateTimeString(note.getModifiedDate()));


        if( note.getContent() != null && note.getContent().length() > 0) {
            FileOutputStream fos = null;
            try {
                String fileName = String.valueOf(note.getCreatedDate()) + PREF_FILE;
                fos = this.openFileOutput(fileName, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(note);
                os.flush();
                os.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
