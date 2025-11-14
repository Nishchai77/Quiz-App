package com.example.marrowsapplication.mcq.model.network

import com.example.marrowsapplication.mcq.model.network.data.Quiz
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw")
    suspend fun getQuestionList(): Response<List<Quiz>>
}