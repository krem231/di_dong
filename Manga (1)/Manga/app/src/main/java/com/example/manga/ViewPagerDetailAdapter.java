package com.example.manga;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerDetailAdapter extends FragmentStateAdapter {

    private String mangaId;

    public ViewPagerDetailAdapter(@NonNull FragmentActivity fragmentActivity, String mangaId) {
        super(fragmentActivity);
        this.mangaId = mangaId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Overview fragment = new Overview();
                Bundle bundle = new Bundle();
                bundle.putString("mangaId", mangaId);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                Chapter chapterFragment = new Chapter();
                Bundle args = new Bundle();
                args.putString("mangaId", mangaId);
                chapterFragment.setArguments(args);
                return chapterFragment;
            default:
                return new Overview();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
