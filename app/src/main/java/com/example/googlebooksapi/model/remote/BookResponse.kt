package com.example.googlebooksapi.model.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class BookResponse (
    val items: List<BookItem>
)
@Parcelize
data class BookItem (
    val volumeInfo: BookVolumeInfo
):Parcelable

@Parcelize
data class BookVolumeInfo(
    val title: String,
    val authors: List<String>?,
    val imageLinks: BookImageLinks?,
    val description: String?,
    val publishedDate: String?
):Parcelable

@Parcelize
data class BookImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
    ):Parcelable