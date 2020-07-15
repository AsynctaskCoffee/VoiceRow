package com.asynctaskcoffee.voicerowdemo

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.asynctaskcoffee.voicerow.VoiceObject
import com.asynctaskcoffee.voicerow.VoiceView
import kotlin.random.Random

class SimpleAdapter(var list: List<VoiceObject>, var activity: Activity) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = LayoutInflater.from(activity).inflate(R.layout.simple_row_layout, null)
        val voiceView = v.findViewById<VoiceView>(R.id.voiceView)
        voiceView.init(activity, list[position], true)
        return v
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}