package com.bangkit.mountainapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.mountainapp.data.local.UserPreference
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    fun logout(pref: UserPreference) {
        viewModelScope.launch {
            pref.logout()
        }
    }
}