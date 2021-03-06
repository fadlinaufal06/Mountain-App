package com.bangkit.mountainapp.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.ui.detailmount.DetailMountViewModel
import com.bangkit.mountainapp.ui.login.LoginViewModel
import com.bangkit.mountainapp.ui.mainactivity.MainViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory {
    private var pref: UserPreference? = null
    private var context: Context? = null

    constructor(pref: UserPreference) {
        this.pref = pref
    }

    constructor(context: Context) {
        this.context = context
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                pref?.let { LoginViewModel(it) } as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                pref?.let { MainViewModel(it) } as T
            }
            modelClass.isAssignableFrom(DetailMountViewModel::class.java) -> {
                pref?.let { DetailMountViewModel(it) } as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}