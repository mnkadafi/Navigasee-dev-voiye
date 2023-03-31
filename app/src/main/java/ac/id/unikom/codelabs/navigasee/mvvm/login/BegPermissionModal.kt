package ac.id.unikom.codelabs.navigasee.mvvm.login

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.utilities.extensions.showToast
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.modal_beg_permission.*

class BegPermissionModal : AppCompatActivity() {
    companion object {
        private const val RC_PERMISSION_MANDATORY_PERMISSIONS = 103

        val PERMISSIONS_MANDATORY = arrayOf(
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.INTERNET",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.WAKE_LOCK",
                "android.permission.RECEIVE_BOOT_COMPLETED",
                "android.permission.DISABLE_KEYGUARD",
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.MODIFY_AUDIO_SETTINGS"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modal_beg_permission)

        //disable cancel activity on touch outside modal
        setFinishOnTouchOutside(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView7.text = Html.fromHtml(getString(R.string.beg_permission), Html.FROM_HTML_MODE_COMPACT)
        } else {
            textView7.text = Html.fromHtml(getString(R.string.beg_permission))
        }

        textView7.requestFocus()

        btn_yes.setOnClickListener {
            ActivityCompat.requestPermissions(this,
                    PERMISSIONS_MANDATORY,
                    RC_PERMISSION_MANDATORY_PERMISSIONS)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSION_MANDATORY_PERMISSIONS) {
            if (grantResults.isNotEmpty()) {
                var isAllAllowed = true
                PERMISSIONS_MANDATORY.forEachIndexed { index, s ->
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        isAllAllowed = false
                    }
                }
                if (isAllAllowed) {
                    showToast(R.string.thanks)
                    finish()
                }
            }
        }
    }
}