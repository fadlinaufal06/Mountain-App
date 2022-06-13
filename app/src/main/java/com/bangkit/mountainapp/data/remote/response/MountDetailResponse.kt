package com.bangkit.mountainapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class MountDetailResponse(
	@field:SerializedName("listMountDetail")
	val listMountDetail: List<MountDetailItem>
)

data class MountDetailItem(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("mountain_name")
	val mountainName: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("history")
	val history: String,

	@field:SerializedName("iconic_site")
	val iconic_site: String,

	@field:SerializedName("elevation")
	val elevation: String,

	@field:SerializedName("stars")
	val stars: Int
)
