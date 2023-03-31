package ac.id.unikom.codelabs.navigasee.utilities.service.fcm

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.source.LoginRepository
import ac.id.unikom.codelabs.navigasee.mvvm.my_location.MyLocationActivity
import ac.id.unikom.codelabs.navigasee.utilities.ROLE_SAHABAT
import ac.id.unikom.codelabs.navigasee.utilities.ROLE_SOPIR
import ac.id.unikom.codelabs.navigasee.utilities.extensions.showToast
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Kotlin sample functional, you must remove the following from the Java messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 *   <action android:name="com.firebase.jobdispatcher.ACTION_EXECUTE" />
 * </intent-filter>
 */
class FcmService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param device_id The new token.
     */
    private fun sendRegistrationToServer(device_id: String?) {
        Log.d(TAG, "sendRegistrationToServer")
        val preferences = Preferences.getInstance()
        val email = preferences.getEmail()
        if (!device_id.isNullOrEmpty() && !email.isNullOrEmpty()) {
            val repo = LoginRepository.getInstance()
            GlobalScope.launch {
                val response = repo.refreshFcm(email, device_id)
                if (response?.status != 200) {
                    Log.e(TAG, "Something error when refresh token")
                }
            }
        }
    }

    /**
     * Show a notification that distrub user activity
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(data: Map<String, String>) {
        val preferences = Preferences.getInstance()
        val role = preferences.getRole()
        if (role.equals(ROLE_SOPIR)) {
            val sopirLauncher = SopirLauncher()
            val intent = sopirLauncher.buildIntent(this, data)
            startActivity(intent)

        } else if (role.equals(ROLE_SAHABAT)) {
            val sahabatLauncher = SahabatLauncher()
            val intent = sahabatLauncher.buildIntent(this, data)
            startActivity(intent)
        } else if (role.equals("Tunanetra")) {
            val status_user_aktif = data.get("status")

            if (status_user_aktif.equals("Tidak ada pengguna aktif yang dapat diminta bantuan")) {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        this@FcmService,
                        getString(R.string.tidak_ada_pengguna_aktif_yang_dapat_diminta_bantuan),
                        Toast.LENGTH_LONG
                    ).show()
                    preferences.setWaiting("")
                    val intent = Intent(this@FcmService, MyLocationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    delay(2000)
                    startActivity(intent)
                }
            } else {
                preferences.setEmailPemberiBantuan(data.get("emailPenerima"))
                if (data.get("jenisBantuan").equals("Datangi Langsung")) {
                    GlobalScope.launch(Dispatchers.Main) {
                        val tunanetraLauncher = TunanetraDidatangiLangsungLauncher()
                        val intent = tunanetraLauncher.buildIntent(this@FcmService, data)
                        showToast(R.string.mohon_tunggu_di_tempat_ada_yang_akan_datang_untuk_membantu_anda)

                        delay(2000)

                        startActivity(intent)
                    }
                } else if (data.get("jenisBantuan").equals("Antar Sampai Tujuan")) {
                    GlobalScope.launch(Dispatchers.Main) {
                        val tunanetraLauncher = TunanetraDiantarTujuanLauncher()
                        val intent = tunanetraLauncher.buildIntent(this@FcmService)

                        showToast(R.string.mohon_tunggu_di_tempat_ada_yang_akan_datang_untuk_membantu_anda)

                        delay(2000)

                        startActivity(intent)
                    }
                } else if (!data.get("tokenVC").isNullOrEmpty()) {
                    GlobalScope.launch(Dispatchers.Main) {
                        val tunanetraLauncher = TunanetraVideoCallLauncher()
                        val intent = tunanetraLauncher.buildIntent(this@FcmService, data)

                        Toast.makeText(
                            this@FcmService,
                            getString(R.string.voiye_buddy_membantu_anda_dengan_panggilan_video_panggilan_video_diaktifkan),
                            Toast.LENGTH_LONG
                        ).show()

                        delay(2000)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    companion object {

        private const val TAG = "FCM Service"
    }
}