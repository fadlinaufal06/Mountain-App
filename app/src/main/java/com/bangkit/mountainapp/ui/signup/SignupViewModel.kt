package com.bangkit.mountainapp.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.mountainapp.data.remote.api.ApiConfig
import com.bangkit.mountainapp.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {

    private val _responseRegister = MutableLiveData<Boolean>()
    val responseRegister: LiveData<Boolean> = _responseRegister

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _onFailure = MutableLiveData<String>()
    val onFailure: LiveData<String> = _onFailure

    companion object {
        private const val TAG = "SignupViewModel"
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.postValue(false)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _responseRegister.postValue(true)
                    } else {
                        _responseRegister.postValue(false)
                        _onFailure.postValue("Is Not Success: ${response.message()}")
                        Log.e(TAG, "isNotSuccess: ${response.message()}")
                    }
                } else {
                    _responseRegister.postValue(false)
                    Log.e(TAG, "isNotSuccess: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _onFailure.postValue("On Failure: ${t.message.toString()}")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}