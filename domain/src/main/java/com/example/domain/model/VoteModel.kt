package com.example.domain.model

data class VoteModel (
    val id: Int,
    val title: String,
    var selectedIcon: Int = -1
)