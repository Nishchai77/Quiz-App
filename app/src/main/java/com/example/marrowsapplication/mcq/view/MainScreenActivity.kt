package com.example.marrowsapplication.mcq.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.marrowsapplication.R
import com.example.marrowsapplication.databinding.ActivityMainScreenBinding
import com.example.marrowsapplication.mcq.model.network.RetrofitBuilder
import com.example.marrowsapplication.mcq.repository.QuizRepository
import com.example.marrowsapplication.mcq.viewModel.QuizViewModel
import com.example.marrowsapplication.mcq.viewModel.QuizViewModelFactory
import kotlinx.coroutines.launch

class MainScreenActivity : AppCompatActivity() {

    private val viewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(QuizRepository(RetrofitBuilder.api))
    }

    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.fetchQuestions()

        lifecycleScope.launch {
            viewModel.isLoading.observe(this@MainScreenActivity){
                if(it){
                    binding.txtViewLoadQuestions.visibility = View.VISIBLE
                    binding.startButton.visibility = View.GONE
                }else{
                    binding.txtViewLoadQuestions.visibility = View.GONE
                    binding.startButton.visibility = View.VISIBLE
                }
            }
        }

        binding.startButton.setOnClickListener {
            binding.startButton.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, QuizFragment())
                .commit()
        }
    }
}