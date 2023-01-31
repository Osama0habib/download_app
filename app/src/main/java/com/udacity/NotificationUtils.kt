/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat


// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
@SuppressLint("WrongConstant")
fun NotificationManager.sendNotification(messageBody: String,status :String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext,MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT)

    val detailIntent = Intent(applicationContext,DetailActivity::class.java)
    println(messageBody)
    println(status)
    detailIntent.putExtra("url",messageBody)
    detailIntent.putExtra("status",status)
    val detailPendingIntent = PendingIntent.getActivity(applicationContext, REQUEST_CODE,detailIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext,"channelId")
    // TODO: Step 1.8 use the new 'breakfast' notification channel

    // TODO: Step 1.3 set title, text and icon to builder
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
    // TODO: Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // TODO: Step 2.1 add style to builder
//        .setStyle(bigPicStyle)
//        .setLargeIcon(eggImage)

        // TODO: Step 2.3 add snooze action
        .addAction(R.drawable.ic_assistant_black_24dp,applicationContext.getString(R.string.notification_button),detailPendingIntent)
        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH  )
    // TODO: Step 1.4 call notify
        notify(NOTIFICATION_ID,builder.build())

}

// TODO: Step 1.14 Cancel all notifications
fun NotificationManager.cancelNotifications(){
    cancelAll()
}