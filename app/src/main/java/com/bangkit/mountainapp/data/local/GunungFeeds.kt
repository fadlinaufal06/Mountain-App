package com.bangkit.mountainapp.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GunungFeeds(
    var userNameFeeds: String,
    var gunungPhotoFeeds: Int,
    var feedsUploadedAt: String,
    var feedsDescription: String,
) : Parcelable

