package com.asynctaskcoffee.voicerowdemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.asynctaskcoffee.audiorecorder.worker.AudioRecordListener
import com.asynctaskcoffee.vorow.VoiceObject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AudioRecordListener {

    private var simpleAdapter: SimpleAdapter? = null
    private var list: ArrayList<VoiceObject> = ArrayList()
    private var permissionsRequired = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private var permissionToRecordAccepted = false
    private var permissionCode = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()
    }

    private fun setViews() {
        simpleAdapter = SimpleAdapter(list, this)
        voiceListView.adapter = simpleAdapter
        recordButton.audioRecordListener = this
        recordButton.beepEnabled = true
        if (letsCheckPermissions()) {
            recordButton.setRecordListener()
        } else {
            ActivityCompat.requestPermissions(this, permissionsRequired, permissionCode)
        }
    }

    override fun onAudioReady(audioUri: String?) {
        Toast.makeText(this, audioUri, Toast.LENGTH_SHORT).show()
        list.add(
            VoiceObject(
                audioUri.toString(),
                "",
                "https://miro.medium.com/fit/c/336/336/1*ls6LIlDfs0C_DFcuAZ7zRw.png",
                ""
            )
        )
        simpleAdapter?.notifyDataSetChanged()
    }

    override fun onRecordFailed(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun letsCheckPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            permissionToRecordAccepted =
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) && ((grantResults[1] == PackageManager.PERMISSION_GRANTED))
            if (permissionToRecordAccepted) recordButton.setRecordListener()
        }
        if (!permissionToRecordAccepted) Toast.makeText(
            this,
            "You have to accept permissions to send voice",
            Toast.LENGTH_SHORT
        ).show()
    }

}