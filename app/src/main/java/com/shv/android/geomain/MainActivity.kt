package com.shv.android.geomain

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var textViewQuestion: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

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
            if (currentIndex + 1 == questionBank.size) {
                summary(count)
                disableAll()
            } else {
                currentIndex = (currentIndex + 1) % questionBank.size
                updateQuestion()
            }
        }

        backButton.setOnClickListener { view: View ->
            currentIndex = (currentIndex - 1) % questionBank.size
            if (currentIndex < 0) currentIndex = questionBank.size - 1
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

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        textViewQuestion.setText(questionTextResId)

        trueButton.isEnabled = true
        falseButton.isEnabled = true
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        if (userAnswer == correctAnswer) count++
        Log.d("count", "count = $count")
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
        currentIndex = (currentIndex + 1) % questionBank.size
        updateQuestion()
    }

    private fun summary(count: Int) {
        val resultText: String = resources.getString(R.string.txt_correct) + "${ count * 100 / questionBank.size}%"
        Toast.makeText(this, resultText, Toast.LENGTH_LONG).show()
    }

    private fun disableAll() {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
        backButton.isEnabled = false
        nextButton.isEnabled = false
    }
}