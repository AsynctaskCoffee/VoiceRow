package com.asynctaskcoffee.voicerow

import android.app.Activity

class VoiceObject constructor(
    val uri: String,
    val name: String,
    val senderUrl: String,
    val senderName: String
) {
    private var voiceView: VoiceView? = null
    fun bind(voiceView: VoiceView, activity: Activity, isSender: Boolean) {
        if (this.voiceView == null) {
            this.voiceView = voiceView
            this.voiceView!!.init(activity, this, isSender)
        }
    }
}