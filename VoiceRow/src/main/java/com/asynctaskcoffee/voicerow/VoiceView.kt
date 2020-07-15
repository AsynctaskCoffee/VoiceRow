package com.asynctaskcoffee.voicerow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("InflateParams")
class VoiceView : FrameLayout, MediaPlayListener {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(LayoutInflater.from(context).inflate(R.layout.voice_row_their, null))
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        addView(LayoutInflater.from(context).inflate(R.layout.voice_row_their, null))
    }

    constructor(context: Context) : super(
        context
    ) {
        addView(LayoutInflater.from(context).inflate(R.layout.voice_row_their, null))
    }

    lateinit var player: Player
    private lateinit var activity: Activity
    private lateinit var voiceObject: VoiceObject
    var audioIsPlaying = false
    private var isSender: Boolean = false

    private lateinit var cardAudioPlay: CardView
    private lateinit var imgAudioPlay: ImageView
    private lateinit var seekBarAudio: SeekBar
    private lateinit var durationAudio: TextView
    private lateinit var userImage: ImageView

    private var audioProgressHandler: Handler? = null
    private var updateAudioPlayerProgressThread: Thread? = null
    private val UPDATE_AUDIO_PROGRESS_BAR = 3

    fun init(
        activity: Activity,
        voiceObject: VoiceObject,
        isSender: Boolean
    ) {
        this.voiceObject = voiceObject
        this.isSender = isSender
        this.activity = activity
        player = Player(this)
        setupView()
        fillViews()
        setListeners()
    }

    @SuppressLint("InflateParams")
    private fun setupView() {

        removeAllViews()

        val v =
            if (isSender) LayoutInflater.from(activity)
                .inflate(R.layout.voice_row_their, null) else LayoutInflater.from(activity)
                .inflate(R.layout.voice_row_sender, null)

        addView(v)

        cardAudioPlay = v.findViewById(R.id.cardAudioPlay)
        imgAudioPlay = v.findViewById(R.id.imgAudioPlay)
        seekBarAudio = v.findViewById(R.id.seekBarAudio)
        durationAudio = v.findViewById(R.id.durationAudio)
        userImage = v.findViewById(R.id.userImage)
    }

    private fun fillViews() {
        if (!TextUtils.isEmpty(voiceObject.uri))
            player.injectMedia(voiceObject.uri)
        if (!TextUtils.isEmpty(voiceObject.senderUrl)) {
            Glide.with(activity).load(voiceObject.senderUrl).into(userImage)
            userImage.visibility = VISIBLE
        } else userImage.visibility = GONE

        try {
            durationAudio.text = getMMSSFromMillis(player.player!!.duration.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartMedia() {
        imgAudioPlay.setOnClickListener(stopMediaListener)
    }

    override fun onStopMedia() {
        imgAudioPlay.setOnClickListener(playMediaListener)
    }

    var playMediaListener: View.OnClickListener = View.OnClickListener {
        player.stopPlaying()
        player.injectMedia(voiceObject.uri)
        player.startPlaying()
        audioIsPlaying = true
        imgAudioPlay.setImageDrawable(
            activity.resources.getDrawable(
                if (isSender) R.drawable.sender_audio_pause_icon
                else R.drawable.me_audio_pause_icon
            )
        )

        if (updateAudioPlayerProgressThread == null) {
            updateAudioPlayerProgressThread = Thread {
                try {
                    while (audioIsPlaying) {
                        if (audioProgressHandler != null) {
                            var msg = Message()
                            msg.what = UPDATE_AUDIO_PROGRESS_BAR
                            audioProgressHandler!!.sendMessage(msg)
                            sleep(1000)
                        }
                    }
                } catch (ex: Exception) {

                }
            }
            updateAudioPlayerProgressThread!!.start();
        }
    }

    var stopMediaListener: View.OnClickListener = View.OnClickListener {
        player.player!!.pause()
        audioIsPlaying = false
        imgAudioPlay.setOnClickListener(playMediaListener)
        updateAudioPlayerProgressThread = null
        imgAudioPlay.setImageDrawable(
            activity.resources
                .getDrawable(if (isSender) R.drawable.sender_audio_play_icon else R.drawable.me_audio_play_icon)
        )
    }

    @SuppressLint("HandlerLeak")
    private fun setListeners() {
        imgAudioPlay.setOnClickListener(playMediaListener)
        if (audioProgressHandler == null) {
            audioProgressHandler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {
                        if (player.player != null) {
                            val currPlayPosition: Int = player.player!!.currentPosition
                            val totalTime: Int = player.player!!.duration
                            val currProgress = currPlayPosition * 1000 / totalTime
                            seekBarAudio.progress = currProgress / 10
                            durationAudio.text = getMMSSFromMillis(currPlayPosition.toLong())
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getMMSSFromMillis(millis: Long): String {
        return try {
            val date = Date(millis)
            val formatter = SimpleDateFormat("mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            formatter.format(date)
        } catch (ignored: Exception) {
            ignored.printStackTrace();
            "00:00"
        }
    }
}