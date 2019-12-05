package com.king.holymary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.king.holymary.ClassUpdates;
import com.king.holymary.Home_Tab;
import com.king.holymary.Notify;
import com.king.holymary.Post;
import com.king.holymary.Profile;

/**
 * Created by Arvindo on 22-02-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class Tabs_Adapter extends FragmentStatePagerAdapter{

    private int num_of_tabs;
    private final String userType;

    public Tabs_Adapter(FragmentManager fm, int num_of_tabs, String userType) {
        super(fm);
        this.num_of_tabs = num_of_tabs;
        this.userType = userType;
    }

    @Override
    public Fragment getItem(int position) {
        switch (userType) {
            case "1":
                switch (position) {
                    case 0:
                        return new Home_Tab();
                    case 1:
                        return new Notify();
                    case 2:
                        return new ClassUpdates();
                    case 3:
                        return new Profile();
                    default:
                        return null;
                }
            case "2":
                switch (position) {
                    case 0:
                        return new Home_Tab();
                    case 1:
                        return new Notify();
                    case 2:
                        return new ClassUpdates();
                    case 3:
                        return new Post();
                    case 4:
                        return new Profile();
                    default:
                        return null;
                }
            case "3":
            case "4":
                switch (position) {
                    case 0:
                        return new Home_Tab();
                    case 1:
                        return new Notify();
                    case 2:
                        return new Profile();
                    default:
                        return null;
                }
            default:
                switch (position) {
                    case 0:
                        return new Home_Tab();
                    case 1:
                        return new Profile();
                    default:
                        return null;
                }
        }

    }

    @Override
    public int getCount() {
        return num_of_tabs;
    }
}
