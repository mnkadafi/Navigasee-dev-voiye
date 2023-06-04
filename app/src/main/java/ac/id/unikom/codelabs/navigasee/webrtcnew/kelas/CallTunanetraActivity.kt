package ac.id.unikom.codelabs.navigasee.webrtcnew.kelas

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.model.dashboard.Location
import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingBody
import ac.id.unikom.codelabs.navigasee.data.source.DashboardRepository
import ac.id.unikom.codelabs.navigasee.data.source.WaitingRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivityCallTunanetraBinding
import ac.id.unikom.codelabs.navigasee.mvvm.confirm_masuk_kendaraan.ConfirmMasukKendaraanActivitiy
import ac.id.unikom.codelabs.navigasee.mvvm.my_location.MyLocationActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import ac.id.unikom.codelabs.navigasee.webrtcnew.RTCClient
import ac.id.unikom.codelabs.navigasee.webrtcnew.SocketRepository
import ac.id.unikom.codelabs.navigasee.webrtcnew.model.IceCandidateModel
import ac.id.unikom.codelabs.navigasee.webrtcnew.model.MessageModel
import ac.id.unikom.codelabs.navigasee.webrtcnew.utils.NewMessageInterface
import ac.id.unikom.codelabs.navigasee.webrtcnew.utils.PeerConnectionObserver
import ac.id.unikom.codelabs.navigasee.webrtcnew.utils.RTCAudioManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription

class CallTunanetraActivity : AppCompatActivity(), NewMessageInterface {

    companion object {
        const val EXTRA_FCM_PENERIMA = "ac.id.unikom.codelabs.navigasee.FCM_PENERIMA"
        const val EXTRA_EMAIL_TUNANETRA = "ac.id.unikom.codelabs.navigasee.EMAIL_TUNANETRA"
        const val EXTRA_TUNANETRA = "ac.id.unikom.codelabs.navigasee.EXTRA_TUNANETRA"

    }

    private var email: String? = null
    private var target: String = ""

    // var fcmPenerima:String? = null

    var tunanetra: String? = null
    private var hasRequest = false

    private var socketRepository: SocketRepository? = null
    private var rtcClient: RTCClient? = null
    private val TAG = "CallActivity"
    private val gson = Gson()
    private var isMute = false
    private val rtcAudioManager by lazy { RTCAudioManager.create(this) }

    private lateinit var mBinding: ActivityCallTunanetraBinding
    private var isCancelled = false
    private val preferences = Preferences.getInstance()

    private val viewModel: CallViewModel by viewModels {
        CallViewModelFactory(DashboardRepository.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCallTunanetraBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        tunanetra = intent.getStringExtra(EXTRA_TUNANETRA)
        init()
    }

    private fun init() {
        email = intent.getStringExtra(EXTRA_EMAIL_TUNANETRA)

        socketRepository?.sendMessageToSocket(
            MessageModel(
                "store_user", email, "", null
            )
        )

        socketRepository = SocketRepository(this)
        email?.let { socketRepository?.initSocket(it) }
        rtcClient =
            RTCClient(application, email!!, socketRepository!!, object : PeerConnectionObserver() {
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    rtcClient?.addIceCandidate(p0)
                    val candidate = hashMapOf(
                        "sdpMid" to p0?.sdpMid,
                        "sdpMLineIndex" to p0?.sdpMLineIndex,
                        "sdpCandidate" to p0?.sdp
                    )

                    socketRepository?.sendMessageToSocket(
                        MessageModel("ice_candidate", email, target, candidate)
                    )

                }

                override fun onAddStream(p0: MediaStream?) {
                    super.onAddStream(p0)
                    p0?.videoTracks?.get(0)?.addSink(mBinding.remoteView)
                    Log.d(TAG, "onAddStream: $p0")

                }
            })
        rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)


        mBinding.apply {
            target = preferences.getEmailPemberiBantuan().toString()
            GlobalScope.launch(Dispatchers.Main) {
                delay(3000)
                socketRepository?.sendMessageToSocket(
                    MessageModel(
                        "start_call", email, target, null
                    )
                )
            }


            switchCameraButton.setOnClickListener {
                rtcClient?.switchCamera()
            }

            micButton.setOnClickListener {
                if (isMute) {
                    isMute = false
                    micButton.setImageResource(R.drawable.ic_mic_off)
                } else {
                    isMute = true
                    micButton.setImageResource(R.drawable.ic_mic)
                }
                rtcClient?.toggleAudio(isMute)
            }

            endCallButton.setOnClickListener {
                rtcClient?.endCall()

                    val intent = Intent(this@CallTunanetraActivity, ConfirmMasukKendaraanActivitiy::class.java)
                    startActivity(intent)

            }
        }
        val fcmPenerima = intent.getStringExtra(CallNewActivity.EXTRA_FCM_PENERIMA)
        fcmPenerima?.let {
            viewModel.sendToken(it, fcmPenerima)
            hasRequest = true
        }

        tunanetra?.let {
            hasRequest = true
            viewModel.updateSendToken()
        }

    }

    override fun onNewMessage(message: MessageModel) {
        Log.d(TAG, "onNewMessage: $message")
        when (message.type) {
            "call_response" -> {
                if (message.data == "user is not online") {
                    //user is not reachable
                    runOnUiThread {
                        Toast.makeText(this, "user is not reachable", Toast.LENGTH_LONG).show()

                    }
                } else {
                    //we are ready for call, we started a call
                    runOnUiThread {
                        mBinding.apply {
                            rtcClient?.initializeSurfaceView(localView)
                            rtcClient?.initializeSurfaceView(remoteView)
                            rtcClient?.startLocalVideo(localView)
                            rtcClient?.call(target)
                        }
                    }

                }
            }
            "answer_received" -> {
                val session = SessionDescription(
                    SessionDescription.Type.ANSWER,
                    message.data.toString()
                )
                rtcClient?.onRemoteSessionReceived(session)
                runOnUiThread {
                    mBinding.remoteViewLoading.visibility = View.GONE
                }
            }
            "offer_received" -> {
                runOnUiThread {
                    mBinding.apply {
                        rtcClient?.initializeSurfaceView(localView)
                        rtcClient?.initializeSurfaceView(remoteView)
                        rtcClient?.startLocalVideo(localView)
                    }
                    val session = SessionDescription(
                        SessionDescription.Type.OFFER,
                        message.data.toString()
                    )
                    rtcClient?.onRemoteSessionReceived(session)
                    rtcClient?.answer(message.name!!)
                    target = message.name!!
                    mBinding.remoteViewLoading.visibility = View.GONE

                }

            }
            "ice_candidate" -> {
                try {
                    val receivingCandidate = gson.fromJson(
                        gson.toJson(message.data),
                        IceCandidateModel::class.java
                    )
                    rtcClient?.addIceCandidate(
                        IceCandidate(
                            receivingCandidate.sdpMid,
                            Math.toIntExact(receivingCandidate.sdpMLineIndex.toLong()),
                            receivingCandidate.sdpCandidate
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}