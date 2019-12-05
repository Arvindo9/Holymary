package com.king.holymary;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Arvindo on 23-02-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RelativeLayout fullLayout;
    private FrameLayout frame_layout;
    private Toolbar toolbar;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        fullLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.drawer_layout,null);
        frame_layout = (FrameLayout)fullLayout.findViewById(R.id.conetnt_frame);
        getLayoutInflater().inflate(layoutResID,frame_layout,true);
        super.setContentView(fullLayout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setCheckable(true);
        int id = item.getItemId();
        switch (id){
            case R.id.change_ph:
                startActivity(new Intent(DrawerActivity.this, ChangePhoneNum.class));
                break;

            case R.id.change_pass:
                startActivity(new Intent(DrawerActivity.this, ChangePassword.class));
                break;

            case R.id.settings:
                break;

            case R.id.credits:
                startActivity(new Intent(DrawerActivity.this, Credits.class));
                break;

            case R.id.about_us:
                startActivity(new Intent(DrawerActivity.this, AboutUs.class));
                break;
        }

        return true;
    }
}
