package com.barrycallebaut.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.barrycallebaut.app.ui.fragment.InspeksiFragment
import com.barrycallebaut.app.ui.fragment.SensusFragment

class TabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, num_tab: Int) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    val num_tabs = num_tab
    override fun getItemCount(): Int {
        return num_tabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return SensusFragment()
        }
        return InspeksiFragment()
    }
}