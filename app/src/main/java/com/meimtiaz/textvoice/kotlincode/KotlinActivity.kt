package com.meimtiaz.textvoice.kotlincode

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.meimtiaz.textvoice.R

class KotlinActivity : AppCompatActivity(){
    private var voiceResultTv: TextView? = null
    private lateinit var textToSpeech : TextToSpeech
    private lateinit var textEt : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        voiceResultTv = findViewById(R.id.voiceResultTv)
        textEt = findViewById(R.id.textEt)
        val takeVoiceBtn = findViewById<TextView>(R.id.takeVoiceBtn)
        val listenVoiceBtn = findViewById<TextView>(R.id.listenVoiceBtn)

        textToSpeech = TextToSpeech(this) {}

        // voice to text
        takeVoiceBtn.setOnClickListener { view: View? ->
            getSpeechInput(
                view
            )
        }

        // text to voice
        listenVoiceBtn.setOnClickListener {
            textToSpeech.stop()
            textToSpeech.speak( textEt.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    override fun onPause() {
        super.onPause()
        textToSpeech.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
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