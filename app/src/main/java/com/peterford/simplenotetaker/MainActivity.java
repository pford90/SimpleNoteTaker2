package com.peterford.simplenotetaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.peterford.simplenotetaker.comparators.NoteCreatedDateDescComparator;
import com.peterford.simplenotetaker.comparators.NoteTitleDescComparator;
import com.peterford.simplenotetaker.decoration.VerticalSpacingDecoration;
import com.peterford.simplenotetaker.listener.*;
import com.peterford.simplenotetaker.listener.RecyclerViewTouchListener;
import com.peterford.simplenotetaker.model.Note;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_toolbar) Toolbar mToolbar;
    @BindView(R.id.main_recyclerView_notes) RecyclerView mRecyclerView;

    @BindView(R.id.main_slidingPanel) SlidingUpPanelLayout mSlidingUpPanelLayout;
    private ArrayList<Note> mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_nav);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mSlidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED)
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                else {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            }
        });



        loadNotes();

        mRecyclerView.addOnItemTouchListener( new RecyclerViewTouchListener(this, mRecyclerView, mSlidingUpPanelLayout, new RecyclerViewTouchListener.OnItemClickListener() {
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
                                deleteNote(position);
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
                }, mNotes ) );


        NoteAdapter adapter = new NoteAdapter(this, mNotes);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration( new com.peterford.simplenotetaker.decoration.DividerItemDecoration(this));
        mRecyclerView.addItemDecoration( new VerticalSpacingDecoration(24));

        mSlidingUpPanelLayout.setEnabled(true);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mSlidingUpPanelLayout.addPanelSlideListener( new MainSlidePanelListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        String result = "";
        switch (id) {
            case R.id.title_sort:
                result = "Title Sort";
                toggleMenuItems(item);
                mNotes.sort(new NoteTitleDescComparator());
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.modified_date_asc:
                result = "Modified Date";
                toggleMenuItems(item);
                mNotes.sort(new NoteCreatedDateDescComparator(NoteCreatedDateDescComparator.SortType.ASC));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.modified_date_desc:
                result = "Modified Date";
                toggleMenuItems(item);
                mNotes.sort(new NoteCreatedDateDescComparator(NoteCreatedDateDescComparator.SortType.DESC));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.menu_add:
                result = "Add";
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                break;
            default:
                result = "Found no item";
                result = item.getItemId() + " : " + item.toString();
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void toggleMenuItems(MenuItem item) {
        item.setChecked(!item.isChecked());
    }

    private void loadNotes() {
        ArrayList<Note> notes_l = new ArrayList<>();
        for(String fileName : fileList()) {
            if( fileName.endsWith(getString(R.string.preferences)) ) {
                Note note = readNote(fileName);
                if( note != null ) {
                    notes_l.add(note);
                }
            }
        }
        mNotes = notes_l;
    }

    private Note readNote(String fileName) {
        FileInputStream fis = null;
        Note note = null;
        try {
            fis = this.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            note = (Note)is.readObject();
            is.close();
            fis.close();

        } catch ( IOException|ClassNotFoundException e ) {
            note = null;
        }
        return note;
    }

    private void deleteNote(int position) {

        if( deleteFile(mNotes.get(position).getDateTime() + getResources().getString(R.string.preferences)) ) {
            Log.v(TAG, "DELETED THIS NOTE");
            mNotes.remove(position);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } else {
            Log.v(TAG, "CANT FIND NOTE");
        }

    }

}
