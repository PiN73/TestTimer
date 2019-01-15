package p.testtimer

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NOTE: delegate the permission handling to generated function
        startListeningLocationWithPermissionCheck()
    }

    private var maxSpeed: Float = 0f
    private var prevLoc: Location? = null
    private var totalDistance: Float = 0f

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun startListeningLocation() {
        val intent = Intent(application, MyLocationService::class.java)
        application.startService(intent)
        application.bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {}
            override fun onServiceDisconnected(className: ComponentName) {}
        }, Context.BIND_AUTO_CREATE)

        MyLocationService.registerReceiver(this) { location ->
            if (maxSpeed < location.speed) {
                maxSpeed = location.speed
            }
            prevLoc?.let {
                totalDistance += it.distanceTo(location)
            }
            prevLoc = location
            textView.text = """
                Точность: ${location.accuracy} м
                Скорость: ${location.speed * 3.6} км/ч
                Высота: ${location.altitude} м
                Максимальная скорость: ${maxSpeed * 3.6} км/ч
                Расстояние: $totalDistance м
            """.trimIndent()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
