package com.bangkit.mountainapp.ui.detailmount

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.data.remote.api.ApiConfig
import com.bangkit.mountainapp.data.remote.response.MountDetailItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMountViewModel(private val pref: UserPreference) : ViewModel() {
    private val _responseMount = MutableLiveData<MountDetailItem>()
    val responseMount: LiveData<MountDetailItem> = _responseMount

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

    companion object {
        private const val TAG = "DetailMountViewModel"
    }
}