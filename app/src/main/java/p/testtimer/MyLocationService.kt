package p.testtimer


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationParams


class MyLocationService : Service() {

    private val binder = LocationServiceBinder()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotification(): Notification {
        val channelId = "channel_01"

        val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_DEFAULT)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        return Notification
            .Builder(applicationContext, channelId)
            .setAutoCancel(true)
            .build()
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        SmartLocation.with(this)
            .location()
            .continuous()
            .config(LocationParams.NAVIGATION)
            .start { Log.d("QWE", "$it") }

        return Service.START_NOT_STICKY
    }

    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // show notification to keep service working if screen is off
            // https://hackernoon.com/android-location-tracking-with-a-service-80940218f561
            startForeground(123456789, getNotification())
        }
    }

    inner class LocationServiceBinder : Binder()
}
