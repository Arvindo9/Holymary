package com.king.holymary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.king.holymary.adapter.Tabs_Adapter;
import com.king.holymary.database_sqlite.DataBaseAwake_1;

import java.sql.SQLException;

/**
 * Created by Arvindo on 22-02-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class Tabs_Activity extends DrawerActivity implements TabLayout.OnTabSelectedListener{
    private TabLayout tabLayout;
    private ViewPager view;
    private Tabs_Adapter adpter;
    private String userType;
    private DataBaseAwake_1 db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        view = (ViewPager)findViewById(R.id.viewPager);

        db = new DataBaseAwake_1(this);
        try {
            userType = db.userType();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        tabLayout = (TabLayout)findViewById(R.id.tabs_layout);

        switch (userType) {
            case "1":
                //student
                tabLayout.addTab(tabLayout.newTab().setText("HOME"));
                tabLayout.addTab(tabLayout.newTab().setText("NOTIFY"));
                tabLayout.addTab(tabLayout.newTab().setText("CLASS"));
                tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
                break;
            case "2":
                //admin
                tabLayout.addTab(tabLayout.newTab().setText("HOME"));
                tabLayout.addTab(tabLayout.newTab().setText("NOTIFY"));
                tabLayout.addTab(tabLayout.newTab().setText("CLASS"));
                tabLayout.addTab(tabLayout.newTab().setText("POST"));
                tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
                break;
            case "3":
                //staff
                tabLayout.addTab(tabLayout.newTab().setText("HOME"));
                tabLayout.addTab(tabLayout.newTab().setText("NOTIFY"));
                tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
                break;
            case "4":
                //parent
                tabLayout.addTab(tabLayout.newTab().setText("HOME"));
                tabLayout.addTab(tabLayout.newTab().setText("NOTIFY"));
                tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
                break;
            default:
                //others
                tabLayout.addTab(tabLayout.newTab().setText("HOME"));
                tabLayout.addTab(tabLayout.newTab().setText("PROFILE"));
                break;
        }

        tabLayout.setOnTabSelectedListener(this);
        adpter = new Tabs_Adapter(getSupportFragmentManager(),tabLayout.getTabCount(), userType);
        view.setAdapter(adpter);
        view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        view.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
}
