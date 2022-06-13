package com.bangkit.mountainapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.model.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    fun getUser(pref: UserPreference): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout(pref: UserPreference) {
        viewModelScope.launch {
            pref.logout()
        }
    }
}