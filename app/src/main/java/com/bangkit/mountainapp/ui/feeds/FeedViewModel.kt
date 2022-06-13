package com.bangkit.mountainapp.ui.feeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.model.UserModel

class FeedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun getUser(pref: UserPreference): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}