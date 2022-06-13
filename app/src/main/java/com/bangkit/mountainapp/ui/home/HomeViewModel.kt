package com.bangkit.mountainapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.data.remote.api.ApiConfig
import com.bangkit.mountainapp.data.remote.response.MountDetailItem
import com.bangkit.mountainapp.data.remote.response.MountDetailResponse
import com.bangkit.mountainapp.data.remote.response.RegisterResponse
import com.bangkit.mountainapp.model.UserModel
import com.bangkit.mountainapp.ui.detailmount.DetailMountViewModel
import com.bangkit.mountainapp.ui.signup.SignupViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _responseGet2Fav = MutableLiveData<MountDetailResponse>()
    val responseGet2Fav: LiveData<MountDetailResponse> = _responseGet2Fav

    private val _responseMount = MutableLiveData<MountDetailItem>()
    val responseMount: LiveData<MountDetailItem> = _responseMount

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getMount(id: Int) {
        val client = ApiConfig.getApiService().getMountId(id)
        client.enqueue(object : Callback<MountDetailItem> {
            override fun onResponse(
                call: Call<MountDetailItem>,
                response: Response<MountDetailItem>
            ) {
                _responseMount.postValue(response.body())
            }

            override fun onFailure(call: Call<MountDetailItem>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUser(pref: UserPreference): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getTop2FavMount() {
//        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().getTwoTopFavMount()
        client.enqueue(object : Callback<MountDetailResponse> {
            override fun onResponse(
                call: Call<MountDetailResponse>,
                response: Response<MountDetailResponse>
            ) {
//                _isLoading.postValue(false)

                _responseGet2Fav.postValue(response.body())

//                if (response.equals("success")) {
//                    _responseRegister.postValue(true)
//                } else {
//                    _responseRegister.postValue(false)
//                    Log.e(TAG, "isNotSuccess: ${response.message()}")
//                }
            }

            override fun onFailure(call: Call<MountDetailResponse>, t: Throwable) {
//                _isLoading.postValue(false)
//                _onFailure.postValue("On Failure: ${t.message.toString()}")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}