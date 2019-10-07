package com.hppk.sw.hppkcommuterbus.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hppk.sw.hppkcommuterbus.R
import com.hppk.sw.hppkcommuterbus.data.model.BusLine
import com.hppk.sw.hppkcommuterbus.ui.KEY_BUS_LINES
import com.hppk.sw.hppkcommuterbus.ui.MainActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


private const val PERMISSION_REQUEST_CODE = 1200

class SplashActivity : AppCompatActivity(), SplashContract.View {

    private val presenter : SplashContract.Presenter by lazy { SplashPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_spalsh)

        if (hasPermission()) {
            presenter.getBusLines()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
//        printHashKey() // TODO: for debug
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    private fun hasPermission() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.getBusLines()
                } else {
                    onError(R.string.permission_required, R.string.permission_required_message)
                }
            }
        }
    }

    override fun onError(errTitle: Int, errMsg: Int) {
        AlertDialog.Builder(this)
            .setTitle(errTitle)
            .setMessage(errMsg)
            .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
            .setOnDismissListener { finish() }
            .show()
    }

    override fun onBusLinesLoaded(busLines: List<BusLine>) {
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(KEY_BUS_LINES, busLines.toTypedArray())
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun printHashKey() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(
                "com.hppk.sw.hppkcommuterbus",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

}
