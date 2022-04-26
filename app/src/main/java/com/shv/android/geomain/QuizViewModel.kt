package com.shv.android.geomain

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    var currentIndex = 0
    var count = 0
    var isCheater = false
    var cheatingCount = 0
    var hintCount = 3

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val questionBankSize: Int
        get() = questionBank.size

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToBack() {
        currentIndex = (currentIndex - 1) % questionBank.size
    }

    fun summary(count: Int) =
        "Correct: ${count * 100 / questionBankSize}%"

    fun summaryCheating(count: Int) =
        "Cheating times: $cheatingCount"
}