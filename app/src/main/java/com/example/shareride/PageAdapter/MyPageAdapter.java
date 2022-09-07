package com.example.shareride.PageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.shareride.Fragments.AccountFragment;
import com.example.shareride.Fragments.NotificationPassengerFragment;
import com.example.shareride.Fragments.NotificationRiderFragment;
import com.example.shareride.Fragments.ProfileFragment;

public class MyPageAdapter extends FragmentPagerAdapter {

    public int itemCount;
    public String Activity;

    public MyPageAdapter(@NonNull FragmentManager fm, int itemCount, String Activity) {
        super(fm);
        this.itemCount = itemCount;
        this.Activity = Activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(Activity.equals("Account"))
        {
            switch (position)
            {
                case 0:
                    return new ProfileFragment();
                case 1:
                    return new AccountFragment();
                default:
                    return null;
            }
        }
        else if(Activity.equals("Notification"))
        {
            switch (position)
            {
                case 0:
                    return new NotificationRiderFragment();
                case 1:
                    return new NotificationPassengerFragment();
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return itemCount;
    }
}
