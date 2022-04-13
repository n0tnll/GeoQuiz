package com.shv.android.geomain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var textViewQuestion: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

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
        textViewQuestion = findViewById(R.id.txt_view_question)

        updateQuestion()


        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener { view: View ->
            if (quizViewModel.currentIndex + 1 == quizViewModel.questionBankSize) {
                printResult()
                disableAll()
            } else {
                quizViewModel.moveToNext()
                updateQuestion()
            }
        }

        backButton.setOnClickListener { view: View ->
            quizViewModel.moveToBack()
            if (quizViewModel.currentIndex < 0) quizViewModel.currentIndex = quizViewModel.questionBankSize - 1
            updateQuestion()
        }

        textViewQuestion.setOnClickListener { view: View ->
            nextQuestion()
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
        val messageResId = if (userAnswer == correctAnswer)
            R.string.correct_toast
        else
            R.string.incorrect_toast
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

    private fun disableAll() {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
        backButton.isEnabled = false
        nextButton.isEnabled = false
    }
}