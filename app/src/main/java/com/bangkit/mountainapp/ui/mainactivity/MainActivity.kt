package com.bangkit.mountainapp.ui.mainactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.databinding.ActivityMainBinding
import com.bangkit.mountainapp.model.ViewModelFactory
import com.bangkit.mountainapp.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var isPasswordShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        checkIsLogin()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]
    }

    private fun checkIsLogin() {
        viewModel.getUser(UserPreference.getInstance(dataStore)).observe(this) { user ->
            if (user.isLogin) {
                val navView: BottomNavigationView = binding.navView

                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_home,
                        R.id.navigation_feeds,
                        R.id.navigation_favorite,
                        R.id.navigation_profile
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}
