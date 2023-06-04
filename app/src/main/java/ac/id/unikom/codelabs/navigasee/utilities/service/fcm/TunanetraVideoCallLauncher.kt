package ac.id.unikom.codelabs.navigasee.utilities.service.fcm

import ac.id.unikom.codelabs.navigasee.mvvm.video_call.VideoCallActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import ac.id.unikom.codelabs.navigasee.webrtcnew.SocketRepository
import ac.id.unikom.codelabs.navigasee.webrtcnew.kelas.CallNewActivity
import ac.id.unikom.codelabs.navigasee.webrtcnew.kelas.CallTunanetraActivity
import ac.id.unikom.codelabs.navigasee.webrtcnew.model.MessageModel
import android.content.Context
import android.content.Intent

class TunanetraVideoCallLauncher {
    private val preferences = Preferences.getInstance()
    private var socketRepository: SocketRepository? = null

    fun buildIntent(context: Context, data: Map<String, String>): Intent {

        val intent = Intent(context, CallTunanetraActivity::class.java)
        socketRepository?.sendMessageToSocket(
            MessageModel(
                "store_user", preferences.getEmail(), "", null
            )
        )
        intent
            .putExtra(CallTunanetraActivity.EXTRA_EMAIL_TUNANETRA, preferences.getEmail())
            .putExtra(CallTunanetraActivity.EXTRA_TUNANETRA, "EXTRA_TUNANETRA")

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        //TODO : GANTI METODE CARA PEROLEH POIN
        preferences.setJumlahPoinYangDiberikan(50)
        preferences.setJenisBantuan(Preferences.JENIS_BANTUAN_VIDEO_CALL)

        return intent
    }
}