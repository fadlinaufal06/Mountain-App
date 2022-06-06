package com.bangkit.mountainapp.ui.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.model.UserModel

class MainViewModel(private val pref: UserPreference): ViewModel() {

    fun getUser(pref: UserPreference): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}