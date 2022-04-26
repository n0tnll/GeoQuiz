package com.shv.android.geomain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView

const val EXTRA_ANSWER_SHOW = "com.shv.android.geomain.answer_show"
private const val EXTRA_ANSWER_IS_TRUE = "com.shv.android.geomain.answer_is_true"
const val KEY_HINT = "hint"


class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiVersionTextView: TextView
    private lateinit var hintCountTextView: TextView

    private var answerIsTrue = false
    private var hintCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        hintCount = intent.getIntExtra(KEY_HINT, -1)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.button_show_answer)
        apiVersionTextView = findViewById(R.id.text_view_api_version)
        hintCountTextView = findViewById(R.id.text_view_count_hints)

        apiVersionTextView.text = resources.getString(R.string.txt_api_level) + " ${Build.VERSION.SDK_INT}"
        updateHint()
        checkHint()
        showAnswerButton.setOnClickListener {
            hintCount--
            showAnswerButton.isEnabled = false
            updateHint()
            val answerText = when {
                answerIsTrue -> R.string.btn_true
                else -> R.string.btn_false
            }
            answerTextView.setText(answerText)
            setAnswerShowResult(true)
        }
    }

    private fun checkHint() {
        if (hintCount <= 0) showAnswerButton.isEnabled = false
    }

    private fun updateHint() {
        hintCountTextView.text = resources.getString(R.string.txt_hint_count) + ": $hintCount"
    }

    private fun setAnswerShowResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOW, isAnswerShown)
            putExtra(KEY_HINT, hintCount)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, hintCount: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(KEY_HINT, hintCount)
            }
        }
    }
}