package com.example.data.di

object TokenCache {
    @Volatile
    var accessToken: String? = null
}