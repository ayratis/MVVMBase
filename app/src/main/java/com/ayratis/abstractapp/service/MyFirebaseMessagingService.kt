package com.ayratis.abstractapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.ayratis.abstractapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "fcmService"
private const val CHANNEL_ID = "com.ayratis.abstractapp.ANDROID"
const val ACTION_PUSH = "action_push"
const val PUSH_TYPE_ID = "push_type_id"
const val PUSH_VALUE_ID = "push_value_id"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d(TAG, token)
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
//        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
//        super.onMessageReceived(p0)
        Log.d("fcm", "onMessageReceived")
        p0?.let {
            Log.d("fcmData", it.data["title"] + it.data["content"])
            createNotification(it.data)
        }
    }

    private fun createNotification(data: Map<String, String>) {
        val typeId = data["type_id"]?.toInt() ?: 0
        val valueId = data["value_id"]?.toInt() ?: 0
//        val intent = Intent(this, MainActivity::class.java)
//            .apply {
//                action = ACTION_PUSH
//                putExtra(PUSH_TYPE_ID, typeId)
//                putExtra(PUSH_VALUE_ID, valueId)
//            }

//        val pendingIntent = PendingIntent.getActivity(this, typeId, intent, PendingIntent.FLAG_ONE_SHOT)

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.notifications)
            .setDestination(R.id.notificationsFragment)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_home_black_24dp)
            .setContentTitle(data["title"])
            .setContentText(data["content"])
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        createNotificationChannel()

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}