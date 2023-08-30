package rs.raf.rmaprojekat.presentation.view.activities

import androidx.fragment.app.Fragment

interface FragmentContainerListener {
    fun replaceFragment(fragment: Fragment)
    fun backPressed(fragment: Fragment)
}
