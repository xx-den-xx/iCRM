package ru.bda.icrm.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;

import ru.bda.icrm.R;
import ru.bda.icrm.activity.AddContragentActivity;
import ru.bda.icrm.activity.ContragentActivity;
import ru.bda.icrm.dialog.AddEventDialog;
import ru.bda.icrm.dialog.GetContragentDialog;
import ru.bda.icrm.dialog.MyDialog;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.enums.NavMode;
import ru.bda.icrm.fragment.CallFragment;
import ru.bda.icrm.services.CallService;
import ru.bda.icrm.view.fragments.ContragentFragment;
import ru.bda.icrm.view.fragments.EventsFragment;
import ru.bda.icrm.view.fragments.MailFragment;
import ru.bda.icrm.view.fragments.PriceFragment;
import ru.bda.icrm.fragment.ScoresFragment;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddEventClickListener;
import ru.bda.icrm.listener.OnContragentClickListener;
import ru.bda.icrm.map.MyLocationManager;
import ru.bda.icrm.model.Event;
import ru.bda.icrm.view.fragments.MapFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnContragentClickListener {

    private ContragentFragment contragentFragment;
    private EventsFragment eventsFragment;
    private ScoresFragment scoresFragment;

    private FrameLayout fragmentContent;
    private DrawerLayout drawer;
    private static NavMode mNavMode;
    private Toolbar toolbar;
    private MenuItem mMenuAdd;
    private TextView mTvNameManager;
    private FloatingActionButton fab;
    private boolean isFirstStart = true;
    private MyLocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mNavMode = NavMode.CLIENTS;
        setMenuToolbar();

        mTvNameManager = (TextView) findViewById(R.id.tv_name_manager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> checkLocation());

        fragmentContent = (FrameLayout) findViewById(R.id.fragment_content);
        contragentFragment = new ContragentFragment();
        contragentFragment.setOnContragentClickListener(this);
        addFragment(contragentFragment);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFragment(mNavMode);
        Intent intent = new Intent(this, CallService.class);
        startService(intent);

    }

    private void checkLocation() {
        locationManager = new MyLocationManager(MainActivity.this);
        locationManager.startLocating();
    }

    private void setFragment(NavMode mode) {
        Fragment fragment = null;
        if (mode == NavMode.CLIENTS) {
            toolbar.setTitle(R.string.nav_clients);
            contragentFragment = new ContragentFragment();
            contragentFragment.setOnContragentClickListener(this);
            mMenuAdd.setVisible(true);
            fab.setVisibility(View.VISIBLE);
            fragment = contragentFragment;
        } else if (mode == NavMode.MAP) {
            toolbar.setTitle(R.string.nav_map);
            fragment = new MapFragment();
            mMenuAdd.setVisible(false);
            fab.setVisibility(View.GONE);
        } else if (mode == NavMode.SCORES) {
            toolbar.setTitle(R.string.nav_scores);
            scoresFragment = new ScoresFragment();
            fragment = scoresFragment;
            mMenuAdd.setVisible(true);
            fab.setVisibility(View.VISIBLE);
        } else if (mode == NavMode.EVENTS) {
            toolbar.setTitle(R.string.nav_events);
            eventsFragment = new EventsFragment();
            fragment = eventsFragment;
            mMenuAdd.setVisible(true);
            fab.setVisibility(View.VISIBLE);
        } else if (mode == NavMode.PRICE) {
            toolbar.setTitle(R.string.nav_price);
            fragment = new PriceFragment();
            mMenuAdd.setVisible(false);
            fab.setVisibility(View.VISIBLE);
        } else if (mode == NavMode.CALL) {
            toolbar.setTitle(R.string.nav_call);
            fragment = new CallFragment();
            mMenuAdd.setVisible(false);
            fab.setVisibility(View.VISIBLE);
        } else if (mode == NavMode.MAILS) {
            toolbar.setTitle(R.string.nav_mails);
            fragment = new MailFragment();
            mMenuAdd.setVisible(false);
        }
        if (!isFirstStart) addFragment(fragment);
    }

    private void setMenuToolbar() {
        toolbar.inflateMenu(R.menu.menu_contragent);
        mMenuAdd = toolbar.getMenu().findItem(R.id.action_add);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == mMenuAdd.getItemId()) {
                if (mNavMode == NavMode.CLIENTS) {
                    Intent intent = new Intent(MainActivity.this, AddContragentActivity.class);
                    startActivity(intent);
                } else if (mNavMode == NavMode.EVENTS) {
                    AddEventDialog dialog = new AddEventDialog();
                    dialog.init(new AddEventClickListener() {
                        @Override
                        public void onLeftBtnClick() {
                        }
                        @Override
                        public void onRightBtnClick(Event event) {
                            eventsFragment.setEvent(event);
                        }
                    });
                    dialog.show(MainActivity.this);
                } else if (mNavMode == NavMode.SCORES) {
                    GetContragentDialog contragentDialog  = new GetContragentDialog();
                    contragentDialog.init(c -> scoresFragment.setContragentId(c.getId()));
                    contragentDialog.show(MainActivity.this);
                }
            }
            return false;
        });
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isFirstStart) fragmentTransaction.add(fragmentContent.getId(), fragment).commit();
        else fragmentTransaction.replace(fragmentContent.getId(), fragment).commit();
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
        } else if (id == R.id.nav_call) {
            mNavMode = NavMode.CALL;
        } else if (id == R.id.nav_mails) {
            /**mNavMode = NavMode.MAILS;*/
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            startActivity(intent);
        }  else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_exit) {

            MyDialog dialog = new MyDialog();
            dialog.init(getString(R.string.user_logout_title), getString(R.string.user_logout_are_you_sure), getString(R.string.no), getString(R.string.ok), new MyDialog.OnClickListener() {
                @Override
                public void onLeftBtnClick() {

                }

                @Override
                public void onRightBtnClick() {
                    logoutProfile();
                }
            });
            dialog.show(this);
        }

        isFirstStart = false;
        setFragment(mNavMode);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutProfile() {
        AppPref.getInstance().setHexAuth("", "", "", "", this);
        AppPref.getInstance().setDateAuth(0, this);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra(Constants.INTENT_EXIT, "exit");
        startActivity(intent);
        finish();
    }

    @Override
    public void onContragentClick(String uid) {
        Intent intent = new Intent(MainActivity.this, ContragentActivity.class);
        intent.putExtra(Constants.INTENT_ID_CONTRAGENT, uid);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.stopLocating();
        }
    }
}
