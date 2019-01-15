package p.testtimer


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationParams


class MyLocationService : Service() {

    private val binder = Binder()

    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // show notification to keep service working if screen is off
            // https://hackernoon.com/android-location-tracking-with-a-service-80940218f561
            startForeground(123456789, getNotification())
        }
    }

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
            .start {
                val broadcast = Intent(receiverIntentName)
                broadcast.putExtra(locationParamName, it)
                LocalBroadcastManager.getInstance(application).sendBroadcast(broadcast)
            }

        return Service.START_NOT_STICKY
    }

    companion object {
        private const val receiverIntentName = "GPSLocationUpdates"
        private const val locationParamName = "location"

        fun registerReceiver(context: Context, receiver: (Location) -> Unit) {
            LocalBroadcastManager
                .getInstance(context)
                .registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        receiver(intent.getParcelableExtra(locationParamName))
                    }
                }, IntentFilter(receiverIntentName))
        }
    }
}
