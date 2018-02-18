package com.peterford.simplenotetaker;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar) Toolbar mToolbar;
    @BindView(R.id.main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.setDrawerListener(drawerToggle);

        drawerToggle.syncState();

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
}
