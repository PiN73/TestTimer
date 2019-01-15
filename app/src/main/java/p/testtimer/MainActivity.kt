package p.testtimer

import android.Manifest
import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*


@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NOTE: delegate the permission handling to generated function
        startListeningLocationWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun startListeningLocation() {
        val intent = Intent(application, MyLocationService::class.java)
        application.startService(intent)
        application.bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {

            }

            override fun onServiceDisconnected(className: ComponentName) {

            }
        }, Context.BIND_AUTO_CREATE)

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val location = intent.getParcelableExtra<Location>("location")
                    textView.text = location.toString()
                }
            }, IntentFilter("GPSLocationUpdates"))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
