package com.bangkit.mountainapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.data.remote.api.ApiConfig
import com.bangkit.mountainapp.data.remote.response.LoginResponse
import com.bangkit.mountainapp.model.UserModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _responseLogin = MutableLiveData<Boolean>()
    val responseLogin: LiveData<Boolean> = _responseLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _onFailure = MutableLiveData<String>()
    val onFailure: LiveData<String> = _onFailure

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun login(email: String, password: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.result == "success") {
                        _responseLogin.postValue(true)
                        val username = responseBody.loginResult.username
                        val emailUser = responseBody.loginResult.email
                        val passwordUser = responseBody.loginResult.password
                        saveUserPreference(UserModel(username, emailUser, passwordUser, true))
                    } else {
                        _responseLogin.postValue(false)
                        _onFailure.postValue("Is Not Success: ${responseBody?.message}")
                        Log.e(TAG, "isNotSuccess: ${response.message()}")
                    }
                } else {
                    _responseLogin.postValue(false)
                    Log.e(TAG, "isNotSuccess: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _onFailure.postValue("On Failure: ${t.message.toString()}")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun saveUserPreference(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}