package com.noam.timebin.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.noam.timebin.MainActivity

class TabsFragmentAdapter(private val mainActivity: MainActivity, private val itemsCount: Int) :
    FragmentStateAdapter(mainActivity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return TabsFragment.getInstance(mainActivity, position)
    }
}