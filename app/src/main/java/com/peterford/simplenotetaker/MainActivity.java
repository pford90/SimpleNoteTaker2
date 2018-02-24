package com.peterford.simplenotetaker;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.peterford.simplenotetaker.decoration.VerticalSpacingDecoration;
import com.peterford.simplenotetaker.listener.*;
import com.peterford.simplenotetaker.listener.RecyclerViewClickListener;
import com.peterford.simplenotetaker.model.Note;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

import static android.widget.LinearLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar) Toolbar mToolbar;
    @BindView(R.id.main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.main_recyclerView_notes) RecyclerView mRecyclerView;

    private Note[] mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(drawerToggle);

        drawerToggle.syncState();
        loadNotes();

        Log.i("MAINACTIVITY", String.valueOf(mNotes.length));

        mRecyclerView.addOnItemTouchListener( new RecyclerViewClickListener(this, mRecyclerView,
                        new NoteItemListener(), Arrays.asList(mNotes)) );

        NoteAdapter adapter = new NoteAdapter(this, mNotes);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration( new com.peterford.simplenotetaker.decoration.DividerItemDecoration(this));
        mRecyclerView.addItemDecoration( new VerticalSpacingDecoration(24));

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
                break;
            case R.id.title_sort:
                result = "Title Sort";
                toggleMenuItems(item);
                break;
            case R.id.modified_date_asc:
                result = "Modified Date";
                toggleMenuItems(item);
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
            mNotes = notes_l.toArray(new Note[notes_l.size()]);
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
