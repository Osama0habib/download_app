package com.udacity

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var URL : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setOnClickListener {
            chooseFileToDownload()
            if (URL.isEmpty()) {
                custom_button.downloadComplete()
            } else {
                download()
            }

        }


    }

    fun chooseFileToDownload(){
        when (RadioGroup.checkedRadioButtonId){
            R.id.glide_radioBtn -> {
                URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
            }
            R.id.project_radioBtn -> {
                URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
            }
            R.id.retrofit_radioBtn -> {
                URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"
            }
            else ->{
                Toast.makeText(this,"Select File to Download",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            println("on recive")
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val action = intent!!.action
            if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val query = DownloadManager.Query()
                query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
                val manager = context!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor: Cursor = manager.query(query)
                if (cursor.moveToFirst()) {
                    if (cursor.count > 0) {
                        val status: Int =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            val file: String =
                                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))

                            val notificationManager = ContextCompat.getSystemService(context,NotificationManager::class.java) as NotificationManager
                            notificationManager.sendNotification(URL ,"success",context)
                            // So something here on success
                            custom_button.downloadComplete()

                            println(file)
                            Toast.makeText(context,"Downloaded",Toast.LENGTH_SHORT).show()
                        } else {
                            val message: Int =
                                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                            val notificationManager = ContextCompat.getSystemService(context,NotificationManager::class.java) as NotificationManager
                            notificationManager.sendNotification(URL ,"Failed",context)
                            // So something here on failed.
                            custom_button.downloadComplete()

                        }
                    }
                }
            }

        }
    }

    private fun download() {
        println("downloading")

            val request =
                DownloadManager.Request(Uri.parse(URL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// en

            // queue puts the download request in the queue.


    }

//    companion object {
//        private const val URL =
//            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
//        private const val CHANNEL_ID = "channelId"
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH)

        notificationChannel.enableLights(true)

        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = applicationContext.getString(R.string.notification_description)
        notificationChannel.apply {
            setShowBadge(false)
        }
        // TODO: Step 1.6 END create a channel
        val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)

        notificationManager.createNotificationChannel(notificationChannel)
    }
}
