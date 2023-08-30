package rs.raf.rmaprojekat.presentation.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.presentation.view.fragments.ListMealsFragment

class MealsActivity : AppCompatActivity(), FragmentContainerListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meals)
        initUi()
    }

    private fun initUi() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.addFragmentFcv, ListMealsFragment())
        transaction.commit()
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.addFragmentFcv, fragment)
            .addToBackStack(null)
            .commit()
    }

     override fun backPressed(fragment: Fragment) {
         val intent = Intent(this, MainActivity::class.java)
         this.startActivity(intent)
    }

}
