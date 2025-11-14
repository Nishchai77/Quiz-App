package com.example.marrowsapplication.mcq.model.network.data

import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int
)