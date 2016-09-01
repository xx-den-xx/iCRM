package ru.bda.icrm.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import ru.bda.icrm.R;
import ru.bda.icrm.enums.NavMode;
import ru.bda.icrm.fragment.ContragentFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ContragentFragment contragentFragment;
    private FrameLayout fragmentContent;
    private DrawerLayout drawer;
    private NavMode mNavMode;
    private Toolbar toolbar;
    private MenuItem mMenuAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavMode = NavMode.CLIENTS;
        setFragment(mNavMode);
        setMenuToolbar();

        fragmentContent = (FrameLayout) findViewById(R.id.fragment_content);
        contragentFragment = new ContragentFragment();
        addFragment(contragentFragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFragment(NavMode mode) {
        if (mode == NavMode.CLIENTS) {
            toolbar.setTitle(R.string.nav_clients);
        } else if (mode == NavMode.MAP) {
            toolbar.setTitle(R.string.nav_map);
        } else if (mode == NavMode.SCORES) {
            toolbar.setTitle(R.string.nav_scores);
        } else if (mode == NavMode.EVENTS) {
            toolbar.setTitle(R.string.nav_events);
        } else if (mode == NavMode.PRICE) {
            toolbar.setTitle(R.string.nav_price);
        } else if (mode == NavMode.TASKS) {
            toolbar.setTitle(R.string.nav_tasks);
        } else if (mode == NavMode.MAILS) {
            toolbar.setTitle(R.string.nav_mails);
        }
    }

    private void setMenuToolbar() {
        toolbar.inflateMenu(R.menu.menu_contragent);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(fragmentContent.getId(), fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_clients) {
            mNavMode = NavMode.CLIENTS;
        } else if (id == R.id.nav_scores) {
            mNavMode = NavMode.SCORES;
        } else if (id == R.id.nav_map) {
            mNavMode = NavMode.MAP;
        } else if (id == R.id.nav_events) {
            mNavMode = NavMode.EVENTS;
        } else if (id == R.id.nav_price) {
            mNavMode = NavMode.PRICE;
        } else if (id == R.id.nav_tasks) {
            mNavMode = NavMode.TASKS;
        } else if (id == R.id.nav_mails) {
            mNavMode = NavMode.MAILS;
        } else if (id == R.id.nav_swap) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_exit) {

        }

        setFragment(mNavMode);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
