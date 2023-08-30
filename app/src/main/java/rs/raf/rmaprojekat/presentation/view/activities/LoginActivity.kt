package rs.raf.rmaprojekat.presentation.view.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.data.models.user.User
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.states.UserState
import rs.raf.rmaprojekat.presentation.viewmodel.UserViewModel
import timber.log.Timber

class LoginActivity: AppCompatActivity() {

    private val userViewModel: MainContract.UserViewModel by viewModel<UserViewModel>()


    var username: EditText? = null
    var password: EditText? = null
    var login: Button? = null
    var emptyUsername: TextView? = null
    var emptyPassword: TextView? = null
    var valid = false
    var messageWritten = false


    companion object {
        const val IS_LOGGED_IN = "prefMessageKey"
        const val USERNAME = "username"
        const val USER_ID = "1"



        fun isValidPassword(password: String): Boolean {
            if (password.length < 5) {
                return false
            }
//            var hasDigit = false
//            var hasUppercase = false
//            for (c in password.toCharArray()) {
//                if (Character.isDigit(c)) {
//                    hasDigit = true
//                } else if (Character.isUpperCase(c)) {
//                    hasUppercase = true
//                }
//            }
//            if (!hasDigit || !hasUppercase) {
//                return false
//            }
//            val forbiddenChars = "~#^|$%&*!"
//            for (c in password.toCharArray()) {
//                if (forbiddenChars.indexOf(c) != -1) {
//                    return false
//                }
//            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    fun init() {
        initView()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        userViewModel.mealsState.observe(this) {
            Timber.e(it.toString())
            renderState(it)
        }
    }

    private fun initView() {
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.button)
        emptyUsername = findViewById(R.id.empty_username)
        emptyPassword = findViewById(R.id.empty_password)
    }

    private fun initListeners() {
        login!!.setOnClickListener { l: View? ->
            emptyUsername!!.visibility = View.INVISIBLE
            emptyPassword!!.visibility = View.INVISIBLE
            if (password!!.text.toString() == "") {
                emptyPassword!!.visibility = View.VISIBLE
                valid = false
            }
            if (username!!.text.toString() == "") {
                emptyUsername!!.visibility = View.VISIBLE
                valid = false
            }

            if (isValidPassword(password!!.text.toString().trim { it <= ' ' })
            ) {
                if (username!!.text.toString() != "") {
                    valid = true
                }
                Toast.makeText(this, "Strong password", Toast.LENGTH_SHORT).show()
            } else {
                valid = false
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            }

            if (valid) {
                var id = userViewModel.add(User(0, username!!.text.toString(), password!!.text.toString()))

                val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
                sharedPreferences
                    .edit()
                    .putString(IS_LOGGED_IN, "yes")
                    .putLong(USER_ID, id)
                    .putString(
                        USERNAME,
                        username!!.text.toString().trim { it <= ' ' })
                    .apply()
                messageWritten = true
                Toast.makeText(this, "Message written to preferences", Toast.LENGTH_SHORT).show()
                val result: Int = if (messageWritten) RESULT_OK else RESULT_CANCELED
                setResult(result)
                finish()
            }
        }
    }

    private fun initUi(data: List<User>) {
        data.filter { it.username.equals(username) && it.password.equals(password) }
    }


    private fun renderState(state: UserState) {
        when (state) {
            is UserState.Success -> {
                initUi(state.categories)
            }
            is UserState.Error -> {
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
            is UserState.DataFetched -> {
                Toast.makeText(this, "Fresh data fetched from the server", Toast.LENGTH_LONG).show()
            }
            is UserState.Loading -> {
            }
        }
    }


}