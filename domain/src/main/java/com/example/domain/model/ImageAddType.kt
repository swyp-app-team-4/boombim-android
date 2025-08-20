package com.example.domain.model

import android.net.Uri

/**
 * 이미지 타입
 */
sealed class ImageAddType {
    data object Default : ImageAddType()
    data class Content(val uri: Uri) : ImageAddType()
    data class Contents(val uris: List<Uri>) : ImageAddType()
}