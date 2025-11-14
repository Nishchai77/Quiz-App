package com.example.marrowsapplication.mcq.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marrowsapplication.mcq.model.network.data.Quiz
import com.example.marrowsapplication.mcq.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(private val quizRepository: QuizRepository): ViewModel() {

    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _questionList: MutableLiveData<List<Quiz>> = MutableLiveData<List<Quiz>>()
    val questionList: LiveData<List<Quiz>> = _questionList

    private var _questionsSkipped: MutableLiveData<Int> = MutableLiveData<Int>(0)

    val questionsSkipped: LiveData<Int> = _questionsSkipped

    private var _questionsAnsweredCorrectly: MutableLiveData<Int> = MutableLiveData<Int>(0)

    val questionsAnsweredCorrectly: LiveData<Int> = _questionsAnsweredCorrectly

    private var _streakScore: MutableLiveData<Int> = MutableLiveData<Int>(0)

    val streakScore: LiveData<Int> = _streakScore


    //Fetch question list from API
    fun fetchQuestions(){
            _isLoading.value = true
            viewModelScope.launch{
                    val response = quizRepository.getQuestionList()
                response.onSuccess {
                    _isLoading.value = false
                    _questionList.postValue(it)
                    Log.d("ViewModel","Question list--->${it.toString()}")
                }.onFailure {
                    Log.d("ViewModel","Some error occurred")
                }
            }
    }

    //Update number of correctly answered questions
    fun setCorrectAnswers(value: Int){
        viewModelScope.launch {
            _questionsAnsweredCorrectly.value = value
        }
    }

    //Update number of skipped questions
    fun setSkippedQuestions(value: Int){
        viewModelScope.launch {
            _questionsSkipped.value = value
        }
    }

    //Update number of streak score
    fun setHighestStreakScore(value: Int){
        viewModelScope.launch {
            _streakScore.value = value
        }
    }



}