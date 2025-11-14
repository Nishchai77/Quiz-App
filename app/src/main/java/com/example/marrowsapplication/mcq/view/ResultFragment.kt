package com.example.marrowsapplication.mcq.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.marrowsapplication.R
import com.example.marrowsapplication.databinding.FragmentResultBinding
import com.example.marrowsapplication.mcq.model.network.RetrofitBuilder
import com.example.marrowsapplication.mcq.repository.QuizRepository
import com.example.marrowsapplication.mcq.viewModel.QuizViewModel
import com.example.marrowsapplication.mcq.viewModel.QuizViewModelFactory


class ResultFragment : Fragment() {

    private lateinit var mBinding: FragmentResultBinding

    private  val quizViewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(QuizRepository(RetrofitBuilder.api))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentResultBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        mBinding.restakeButton.setOnClickListener{
            val intent = Intent(requireActivity(), MainScreenActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    @SuppressLint("SetTextI18n")
    fun bindData() {
        mBinding.txtViewCorrectScore.text =
            "${quizViewModel.questionsAnsweredCorrectly.value.toString()}/10"
        mBinding.txtViewSkippedScore.text = quizViewModel.questionsSkipped.value.toString()
        mBinding.txtViewStreakScore.text = quizViewModel.streakScore.value.toString()
    }


}