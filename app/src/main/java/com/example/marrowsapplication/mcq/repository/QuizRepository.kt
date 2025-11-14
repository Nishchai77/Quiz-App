package com.example.marrowsapplication.mcq.repository

import com.example.marrowsapplication.mcq.model.network.data.Quiz
import com.example.marrowsapplication.mcq.model.network.ApiService

class QuizRepository(private val apiService: ApiService) {

    suspend fun getQuestionList(): Result<List<Quiz>>{
        return try {
            val response = apiService.getQuestionList()
            if(response.isSuccessful){
                Result.success(response.body()?:emptyList())
            }else{
                Result.failure(Exception("Api error:${response.code()}"))
            }
        }catch (ex: Exception){
            Result.failure(ex)
        }
    }
}