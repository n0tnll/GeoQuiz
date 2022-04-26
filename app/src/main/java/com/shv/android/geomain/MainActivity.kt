package com.shv.android.geomain

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var textViewQuestion: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.btn_true)
        falseButton = findViewById(R.id.btn_false)
        nextButton = findViewById(R.id.btn_next)
        backButton = findViewById(R.id.btn_back)
        cheatButton = findViewById(R.id.cheat_button)
        textViewQuestion = findViewById(R.id.txt_view_question)

        updateQuestion()

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.isCheater = false
            if (quizViewModel.currentIndex + 1 == quizViewModel.questionBankSize) {
                printResult()
                printCheatingCount()
                disableAll()
            } else {
                quizViewModel.moveToNext()
                updateQuestion()
            }
        }

        backButton.setOnClickListener {
            quizViewModel.moveToBack()
            if (quizViewModel.currentIndex < 0) quizViewModel.currentIndex =
                quizViewModel.questionBankSize - 1
            updateQuestion()
        }

        textViewQuestion.setOnClickListener {
            nextQuestion()
        }

        val getCheat = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOW, false) ?: false
                quizViewModel.hintCount = result.data?.getIntExtra(KEY_HINT, -1) ?: 0
            }
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val hintCount = quizViewModel.hintCount
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue, hintCount)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              val options = ActivityOptionsCompat.makeClipRevealAnimation(it, 0,0, it.width, it.height)
              getCheat.launch(intent, options)
            } else {
                getCheat.launch(intent)
            }
        }

    }



    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSavedInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        textViewQuestion.setText(questionTextResId)

        trueButton.isEnabled = true
        falseButton.isEnabled = true
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        if (userAnswer == correctAnswer) quizViewModel.count++
        if (quizViewModel.isCheater) quizViewModel.cheatingCount++
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        trueButton.isEnabled = false
        falseButton.isEnabled = false
        backButton.isEnabled = false
    }

    private fun nextQuestion() {
        quizViewModel.moveToNext()
        updateQuestion()
    }

    private fun printResult() {
        val resultText = quizViewModel.summary(quizViewModel.count)
        Toast.makeText(this, resultText, Toast.LENGTH_LONG).show()
    }

    private fun printCheatingCount() {
        val cheatingText = quizViewModel.summaryCheating(quizViewModel.cheatingCount)
        Toast.makeText(this, cheatingText, Toast.LENGTH_SHORT).show()
    }

    private fun disableAll() {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
        backButton.isEnabled = false
        nextButton.isEnabled = false
    }
}