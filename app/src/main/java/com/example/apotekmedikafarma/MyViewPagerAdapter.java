package com.example.apotekmedikafarma;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.apotekmedikafarma.fragment.AboutFragment;
import com.example.apotekmedikafarma.fragment.HomeFragment;
import com.example.apotekmedikafarma.fragment.TransaksiFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case  0:
                return new HomeFragment();
            case 1:
                return new TransaksiFragment();
            case 2:
                return new AboutFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
