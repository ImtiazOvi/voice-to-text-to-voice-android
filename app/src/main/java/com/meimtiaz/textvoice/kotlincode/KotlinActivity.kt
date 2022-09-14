package com.meimtiaz.textvoice.kotlincode

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.meimtiaz.textvoice.R

class KotlinActivity : AppCompatActivity() {
    private var voiceResultTv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        voiceResultTv = findViewById(R.id.voiceResultTv)
        val takeVoiceBtn = findViewById<TextView>(R.id.takeVoiceBtn)

        takeVoiceBtn.setOnClickListener { view: View? ->
            getSpeechInput(
                view
            )
        }
    }

    private fun getSpeechInput(view: View?) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        try {
            someActivityResultLauncher.launch(intent)
            voiceResultTv!!.text = ""
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                "Your device doesn't support Speech to Text",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private var someActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data!!
            val text =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            voiceResultTv!!.text = text!![0]
        }
    }

}