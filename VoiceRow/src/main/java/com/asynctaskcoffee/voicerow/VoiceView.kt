package com.asynctaskcoffee.voicerow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Chronometer
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import androidx.cardview.widget.CardView

class VoiceView : MediaPlayListener {

    private lateinit var player: Player
    private lateinit var activity: Activity
    private lateinit var mediaPlayListener: MediaPlayListener
    private lateinit var voiceObject: VoiceObject
    private var isSender: Boolean = false

    private lateinit var cardAudioPlay: CardView
    private lateinit var imgAudioPlay: ImageView
    private lateinit var seekBarAudio: SeekBar
    private lateinit var durationAudio: Chronometer
    private lateinit var userImage: ImageView

    fun init(
        activity: Activity,
        mediaPlayListener: MediaPlayListener,
        voiceObject: VoiceObject,
        isSender: Boolean
    ) {
        this.voiceObject = voiceObject
        this.isSender = isSender
        this.activity = activity
        this.mediaPlayListener = mediaPlayListener
        player = Player(this)
    }

    @SuppressLint("InflateParams")
    private fun setupView() {
        val v =
            if (isSender) LayoutInflater.from(activity)
                .inflate(R.layout.voice_row_their, null) else LayoutInflater.from(activity)
                .inflate(R.layout.voice_row_sender, null)

        cardAudioPlay = v.findViewById(R.id.cardAudioPlay)
        imgAudioPlay = v.findViewById(R.id.imgAudioPlay)
        seekBarAudio = v.findViewById(R.id.seekBarAudio)
        durationAudio = v.findViewById(R.id.durationAudio)
        userImage = v.findViewById(R.id.userImage)
    }

    private fun fillViews() {

    }

    override fun onStartMedia() {
        mediaPlayListener.onStartMedia()
    }

    override fun onStopMedia() {
        mediaPlayListener.onStopMedia()
    }
}