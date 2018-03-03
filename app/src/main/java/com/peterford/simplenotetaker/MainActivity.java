package com.peterford.simplenotetaker;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.peterford.simplenotetaker.comparators.NoteCreatedDateDescComparator;
import com.peterford.simplenotetaker.comparators.NoteTitleDescComparator;
import com.peterford.simplenotetaker.decoration.VerticalSpacingDecoration;
import com.peterford.simplenotetaker.listener.*;
import com.peterford.simplenotetaker.listener.RecyclerViewClickListener;
import com.peterford.simplenotetaker.model.Note;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar) Toolbar mToolbar;
//    @BindView(R.id.main_drawer_layout) DrawerLayout mDrawerLayout;
//    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.main_recyclerView_notes) RecyclerView mRecyclerView;

    @BindView(R.id.main_slidingPanel) SlidingUpPanelLayout mSlidingUpPanelLayout;
    private ArrayList<Note> mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.navigation_drawer);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        loadNotes();

        mRecyclerView.addOnItemTouchListener( new RecyclerViewClickListener(this, mSlidingUpPanelLayout,
                        new NoteItemListener(), mNotes) );

        NoteAdapter adapter = new NoteAdapter(this, mNotes);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration( new com.peterford.simplenotetaker.decoration.DividerItemDecoration(this));
        mRecyclerView.addItemDecoration( new VerticalSpacingDecoration(24));

        mSlidingUpPanelLayout.setEnabled(true);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mSlidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(previousState == SlidingUpPanelLayout.PanelState.EXPANDED)
                {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            }

        });
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
            case R.id.menu_search:
                result = "Search";
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
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
                break;
        }

        if( !result.equalsIgnoreCase("search"))
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

        if(notes_l.size() > 0) {
//            mNotes = notes_l.toArray(new Note[notes_l.size()]);
            mNotes = notes_l;
        }
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

}
