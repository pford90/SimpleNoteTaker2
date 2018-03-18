package com.peterford.simplenotetaker.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.peterford.simplenotetaker.adapter.NoteAdapter;
import com.peterford.simplenotetaker.R;
import com.peterford.simplenotetaker.comparators.NoteDateComparator;
import com.peterford.simplenotetaker.comparators.NoteTitleDescComparator;
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

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private enum Selected { YES, NO };

    @BindView(R.id.main_toolbar) Toolbar mToolbar;
    @BindView(R.id.main_recyclerView_notes) RecyclerView mRecyclerView;
    @BindView(R.id.main_slidingPanel) SlidingUpPanelLayout mSlidingUpPanelLayout;
    @BindView(R.id.delete_counter) TextView mDeleteCounter;

    MenuItem mSearchMenuItem;
    MenuItem mDeleteMenuItem;

    private ArrayList<Note> mNotes;
    private ArrayList<Note> mDeleteNotes;
    private boolean mDeleteItemsFlag = false;

    NoteAdapter mNoteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        setUpActionBar();

        loadNotes();

        mDeleteNotes = new ArrayList<>();

        setupRecyclerView();

        setupSlidingUpPanel();

    }

    private void setupSlidingUpPanel() {
        mSlidingUpPanelLayout.setEnabled(true);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mSlidingUpPanelLayout.addPanelSlideListener( new MainSlidePanelListener());
    }

    private void setupRecyclerView() {
        mNoteAdapter = new NoteAdapter(this, mNotes);

        mRecyclerView.addOnItemTouchListener( new RecyclerViewTouchListener(this, mRecyclerView, mSlidingUpPanelLayout, new RecyclerViewTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object object) {
                Note note = (Note) object;
                if(mDeleteItemsFlag) {
                    if( view.getTag() == null || view.getTag() != Selected.YES ) {
                        view.setBackgroundColor(getColor(R.color.colorAccent));
                        view.setTag(Selected.YES);
                        mDeleteNotes.add(note);
                        mDeleteCounter.setText(String.valueOf(mDeleteNotes.size()));
                    } else {
                        view.setTag(null);
                        mDeleteNotes.remove(note);
                        view.setBackgroundColor(Color.WHITE);
                        if( mDeleteNotes.size() == 0 ) {
                            mDeleteItemsFlag = false;
                            invalidateOptionsMenu();
                        }
                    }
                } else {
                    // Go to Note Activity
                    Intent intent = new Intent(view.getContext(), NoteActivity.class);
                    intent.putExtra("note", note);
                    view.getContext().startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, Object object) {
                Note note = (Note) object;
                view.setBackgroundColor(getColor(R.color.colorAccent));
                view.setTag(Selected.YES);
                mDeleteNotes.add(note);
                mDeleteItemsFlag = true;
                invalidateOptionsMenu();
            }
        }, mNotes ) );

        mRecyclerView.setAdapter(mNoteAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Drawable dividerDrawable = getDrawable(R.drawable.divider);
        mRecyclerView.addItemDecoration( new com.peterford.simplenotetaker.decoration.DividerItemDecoration(dividerDrawable));
    }

    private void setUpActionBar() {

        if( mDeleteItemsFlag ) {

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeleteNotes.clear();

                    for( int i = 0; i < mNoteAdapter.getItemCount(); i++ ) {
                        if( mRecyclerView.getChildAt(i).getTag() == Selected.YES ) {
                            mRecyclerView.getChildAt(i).setBackgroundColor(Color.WHITE);
                            mRecyclerView.getChildAt(i).setTag(null);
                        }
                    }
                    mDeleteItemsFlag = false;
                    invalidateOptionsMenu();
                }
            });

            mDeleteCounter.setText( String.valueOf(mDeleteNotes.size() ) );
            mDeleteCounter.setVisibility(View.VISIBLE);
        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            mToolbar.setTitle(getString(R.string.all_notes));
            mDeleteCounter.setVisibility(View.INVISIBLE);
            mToolbar.setNavigationIcon(R.drawable.ic_nav);

            mToolbar.setNavigationOnClickListener(view -> {
                if (mSlidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED)
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                else {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        mDeleteMenuItem = menu.findItem(R.id.menu_delete);
        mSearchMenuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.title_sort:
                mNotes.sort(new NoteTitleDescComparator());
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.modified_date_asc:
                mNotes.sort(new NoteDateComparator(NoteDateComparator.SortType.MODIFIED_ASC));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.modified_date_desc:
                mNotes.sort(new NoteDateComparator(NoteDateComparator.SortType.MODIFIED_DESC));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.created_date_asc:
                mNotes.sort(new NoteDateComparator(NoteDateComparator.SortType.CREATED_ASC));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.created_date_desc:
                mNotes.sort(new NoteDateComparator(NoteDateComparator.SortType.CREATED_DESC));
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.menu_add:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_delete:
                deleteNotes();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        setUpActionBar();
        toggleDeleteActionBar();

        return true;
    }

    private void toggleDeleteActionBar() {
        if(mDeleteItemsFlag) {
            mSearchMenuItem.setVisible(false);
            mDeleteMenuItem.setVisible(true);
        } else {
            mSearchMenuItem.setVisible(true);
            mDeleteMenuItem.setVisible(false);
            mDeleteCounter.setVisibility(View.GONE);
        }
    }


    private void loadNotes() {
        if( mNotes == null ) {
            mNotes = new ArrayList<>();
        } else {
            mNotes.clear();
        }

        for(String fileName : fileList()) {
            if( fileName.endsWith(getString(R.string.preferences)) ) {
                Note note = readNote(fileName);
                if( note != null ) {
                    mNotes.add(note);
                }
            }
        }
    }

    private Note readNote(String fileName) {
        FileInputStream fis;
        Note note;
        try {
            fis = this.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            note = (Note)is.readObject();
            is.close();
            fis.close();

        } catch ( IOException|ClassNotFoundException e ) {
            deleteFile(fileName);
            note = null;
        }
        return note;
    }


    private void deleteNotes() {
        for( Note note : mDeleteNotes ) {
            deleteFile(note.getCreatedDate() + getResources().getString(R.string.preferences));
            mNoteAdapter.deleteNote(note);
        }

        mDeleteNotes.clear();
        mDeleteItemsFlag = false;
        invalidateOptionsMenu();

    }

    //START SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String query) {
        if(TextUtils.isEmpty(query)) {
        } else {
            mNoteAdapter.filterNotes(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mNoteAdapter.filterNotes(newText);
        return true;
    }
    // END SearchView.OnQueryTextListener
}