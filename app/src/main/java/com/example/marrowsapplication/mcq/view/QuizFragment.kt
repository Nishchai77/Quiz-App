package com.example.marrowsapplication.mcq.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.marrowsapplication.databinding.FragmentQuizBinding
import com.example.marrowsapplication.mcq.model.network.data.Quiz
import com.example.marrowsapplication.mcq.model.network.RetrofitBuilder
import com.example.marrowsapplication.mcq.repository.QuizRepository
import com.example.marrowsapplication.mcq.viewModel.QuizViewModel
import com.example.marrowsapplication.mcq.viewModel.QuizViewModelFactory
import com.example.marrowsapplication.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


class QuizFragment : Fragment() {


    private lateinit var  mBinding: FragmentQuizBinding

    private var questionCount = 0

    private var questionsSkipped = 0

    private var questionsAnsweredCorrectly = 0

    private var highestStreak = 0

    private val quizViewModel: QuizViewModel by activityViewModels{
        QuizViewModelFactory(QuizRepository(RetrofitBuilder.api))

    }

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       mBinding = FragmentQuizBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val questionsList = quizViewModel.questionList.value


        //Fade/Slide Animations
        val slideOut = ObjectAnimator.ofFloat(mBinding.root, "translationX", 0f, -100f)
        val fadeOut = ObjectAnimator.ofFloat(mBinding.root, "alpha", 1f, 0f)
        val slideIn = ObjectAnimator.ofFloat(mBinding.root, "translationX", 100f, 0f)
        val fadeIn = ObjectAnimator.ofFloat(mBinding.root, "alpha", 0f, 1f)

        val outSet = AnimatorSet().apply {
            playTogether(slideOut, fadeOut)
            duration = 200
        }
        val inSet = AnimatorSet().apply {
            playTogether(slideIn, fadeIn)
            duration = 200
        }

        //Binding questions with views
        bindQuestionAndOptions(
            mBinding.txtViewQuestion,
            mBinding.optionBtnOne,
            mBinding.optionBtnTwo,
            mBinding.optionBtnThree,
            mBinding.optionBtnFour,
            questionsList?.get(0)
        )

        //skip button listener
        mBinding.skipBtn.setOnClickListener {
            onSkipped(questionsList,inSet,outSet)
        }


        //option button listener
        mBinding.optionBtnOne.setOnClickListener {
            val result = onOptionSelected(0,
                questionsList?.get(questionCount))
            upDateUiOptions(
                mBinding.optionBtnOne,
                result,
                questionsList?.get(questionCount)?.correctOptionIndex ?: 0
            )
            coroutineScope.launch {
                delay(2.seconds)
                displayNextQuestionOnOptionSelected(questionsList,questionCount,inSet,outSet)
            }
        }

        mBinding.optionBtnTwo.setOnClickListener {
            val result = onOptionSelected(1,
                questionsList?.get(questionCount))
            upDateUiOptions(
                mBinding.optionBtnTwo,
                result,
                questionsList?.get(questionCount)?.correctOptionIndex ?: 0
            )
            coroutineScope.launch {
                delay(2.seconds)
                displayNextQuestionOnOptionSelected(questionsList,questionCount,inSet,outSet)
            }
        }
        mBinding.optionBtnThree.setOnClickListener {
            val result = onOptionSelected(2,
                questionsList?.get(questionCount))
            upDateUiOptions(
                mBinding.optionBtnThree,
                result,
                questionsList?.get(questionCount)?.correctOptionIndex ?: 0
            )
            coroutineScope.launch {
                delay(2.seconds)
                displayNextQuestionOnOptionSelected(questionsList,questionCount,inSet, outSet)
            }
        }
        mBinding.optionBtnFour.setOnClickListener {
            val result = onOptionSelected(3,
                questionsList?.get(questionCount))
            upDateUiOptions(
                mBinding.optionBtnFour,
                result,
                questionsList?.get(questionCount)?.correctOptionIndex ?: 0
            )
            coroutineScope.launch {
                delay(2.seconds)
                displayNextQuestionOnOptionSelected(questionsList,questionCount,inSet,outSet)
            }
        }
    }


    //Handling on skip button pressed logic
    private fun onSkipped(questionsList: List<Quiz>?,inSet: AnimatorSet,outSet: AnimatorSet){
        questionCount++
        questionsSkipped++
        quizViewModel.setSkippedQuestions(questionsSkipped)
        if(questionCount<questionsList?.size!!) {
            val question = questionsList[questionCount]
            Log.d("QuizFragment", "Receiving quiz count as $questionCount")
            outSet.addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    bindQuestionAndOptions(
                        mBinding.txtViewQuestion,
                        mBinding.optionBtnOne,
                        mBinding.optionBtnTwo,
                        mBinding.optionBtnThree,
                        mBinding.optionBtnFour,
                        question
                    )
                    inSet.start()
                }
            })
            outSet.start()
        }else{
           loadResultFragment()
        }
    }

    //UI functionality binding questions and ptions with views
    private fun bindQuestionAndOptions(
        questionView: TextView, optionViewOne: MaterialButton, optionViewTwo: MaterialButton,
        optionViewThree: MaterialButton, optionViewFour: MaterialButton,
        question: Quiz?
    ) {
        questionView.text = question?.question
        optionViewOne.text = question?.options[0]
        optionViewTwo.text = question?.options[1]
        optionViewThree.text = question?.options[2]
        optionViewFour.text = question?.options[3]
        val context = context ?:return
        val color = ContextCompat.getColor(
            context,
            R.color.white
        )
        optionViewFour.strokeColor = ColorStateList.valueOf(color)
        optionViewThree.strokeColor = ColorStateList.valueOf(color)
        optionViewTwo.strokeColor = ColorStateList.valueOf(color)
        optionViewOne.strokeColor = ColorStateList.valueOf(color)
    }


    //Handling correct option logic
    private fun onOptionSelected(selectedOptionIndex:Int,question: Quiz?): Boolean{
        return selectedOptionIndex==question?.correctOptionIndex
    }

    //Ui functionality updating UI on user selecting options with correct and wrong answer
    private fun upDateUiOptions(optionButton: MaterialButton, answerCorrect: Boolean,correctAnswer:Int) {
        mBinding.skipBtn.visibility = View.GONE
        if (answerCorrect) {
            questionsAnsweredCorrectly++
            highestStreak++
            quizViewModel.setCorrectAnswers(questionsAnsweredCorrectly)
            if(highestStreak>=3){
                mBinding.linearLytStreak.visibility = View.VISIBLE
                mBinding.txtViewStreak.visibility = View.VISIBLE
            }
            quizViewModel.streakScore.value?.let {
                if(it<highestStreak) {
                    quizViewModel.setHighestStreakScore(highestStreak)
                }
            }
            val color = ContextCompat.getColor(
                requireContext(),
                R.color.teal_700
            )
            optionButton.strokeColor = ColorStateList.valueOf(color)
        } else {
            mBinding.linearLytStreak.visibility = View.GONE
            mBinding.txtViewStreak.visibility = View.GONE
            highestStreak = 0
            val color = ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
            optionButton.strokeColor = ColorStateList.valueOf(color)
        }
        mBinding.optionBtnOne.isClickable = false
        mBinding.optionBtnThree.isClickable = false
        mBinding.optionBtnTwo.isClickable = false
        mBinding.optionBtnFour.isClickable = false
        highlightCorrectAnswer(correctAnswer)
    }

    //Ui functionality biding next questions and answers with views.
    private fun displayNextQuestionOnOptionSelected(
        questionsList: List<Quiz>?,
        index: Int,
        inSet: AnimatorSet,
        outSet: AnimatorSet
    ) {
        questionCount++
        if (questionCount < questionsList?.size!!) {
            mBinding.optionBtnOne.isClickable = true
            mBinding.optionBtnThree.isClickable = true
            mBinding.optionBtnTwo.isClickable = true
            mBinding.optionBtnFour.isClickable = true
            mBinding.skipBtn.visibility = View.VISIBLE

            outSet.addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    bindQuestionAndOptions(
                        mBinding.txtViewQuestion,
                        mBinding.optionBtnOne,
                        mBinding.optionBtnTwo,
                        mBinding.optionBtnThree,
                        mBinding.optionBtnFour,
                        questionsList[index]
                    )
                    inSet.start()
                }
            })
            outSet.start()

        } else {
            loadResultFragment()
        }
    }

    fun loadResultFragment(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ResultFragment())
            .commit()
    }

    //Highlighting stroke color to diff bw correct nd wrong answer when user selects options
    fun highlightCorrectAnswer(optionIndex: Int) {
        val color = ContextCompat.getColor(
            requireContext(),
            R.color.teal_700
        )
        val strokeColor = ColorStateList.valueOf(color)
        when (optionIndex) {
            0 -> {
                mBinding.optionBtnOne.strokeColor = strokeColor
            }

            1 -> {
                mBinding.optionBtnTwo.strokeColor = strokeColor
            }

            2->{
                mBinding.optionBtnThree.strokeColor = strokeColor
            }
            3 ->{
                mBinding.optionBtnFour.strokeColor = strokeColor
            }
        }
    }



}