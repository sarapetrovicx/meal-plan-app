package rs.raf.rmaprojekat.presentation.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.databinding.ActivityMainBinding
import rs.raf.rmaprojekat.presentation.view.adapters.MainPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val CAT_KEY = "category"
        const val AREA_KEY = "area"
        const val INGRD_KEY = "ingr"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen: SplashScreen = this.installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            false
        }

        val sharedPreferencesActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val sharedPreferences = getSharedPreferences(
                        packageName,
                        MODE_PRIVATE
                    )
                    var message =
                        sharedPreferences.getString(LoginActivity.IS_LOGGED_IN, "")
                    if (message == null) message = "Error"
                    Toast.makeText(
                        this,
                        "We have written to pref: $message",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this, "Nothing was written", Toast.LENGTH_SHORT).show()
                }
            }

        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getString(LoginActivity.IS_LOGGED_IN, null)

        if (isLoggedIn == null) {
            val intent = Intent(this, LoginActivity::class.java)
            sharedPreferencesActivityResultLauncher.launch(intent)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            init()
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            init()
        }
    }


    private fun init() {
        initUi()
    }

    private fun initUi() {
        binding.viewPager.adapter =
            MainPagerAdapter(
                supportFragmentManager,
                this
            )

//        binding.bottomNavigation.setupWithViewPager(binding.viewPager)
        binding.bottomNavigation.setOnItemSelectedListener {
                item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_1 -> binding.viewPager.setCurrentItem(MainPagerAdapter.FRAGMENT_1, false)
                R.id.navigation_2 -> binding.viewPager.setCurrentItem(MainPagerAdapter.FRAGMENT_2, false)
                R.id.navigation_3 -> binding.viewPager.setCurrentItem(MainPagerAdapter.FRAGMENT_3, false)
                R.id.navigation_4 -> binding.viewPager.setCurrentItem(MainPagerAdapter.FRAGMENT_4, false)
                R.id.navigation_5 -> binding.viewPager.setCurrentItem(MainPagerAdapter.FRAGMENT_5, false)

            }
            true
        }
    }

}
