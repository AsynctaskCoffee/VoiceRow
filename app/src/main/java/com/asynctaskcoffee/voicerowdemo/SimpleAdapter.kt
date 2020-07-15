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

class SimpleAdapter(private var list: List<VoiceObject>, private var activity: Activity) :
    BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v: View
        val holder: ViewHolder
        var voiceObject = list[position]
        if (convertView == null) {
            v = LayoutInflater.from(activity).inflate(R.layout.simple_row_layout, parent, false)
            holder = ViewHolder()
            holder.voiceView = v.findViewById(R.id.voiceView)
            v.tag = holder
        } else {
            v = convertView
            holder = convertView.tag as ViewHolder
        }
        voiceObject.bind(holder.voiceView, activity, true)
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

    private class ViewHolder {
        lateinit var voiceView: VoiceView
    }

}