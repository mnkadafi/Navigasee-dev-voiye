package ac.id.unikom.codelabs.navigasee.mvvm.video_call

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.model.dashboard.Location
import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingBody
import ac.id.unikom.codelabs.navigasee.data.source.DashboardRepository
import ac.id.unikom.codelabs.navigasee.data.source.WaitingRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivityVideoCallBinding
import ac.id.unikom.codelabs.navigasee.mvvm.confirm_masuk_kendaraan.ConfirmMasukKendaraanActivitiy
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseActivity
import ac.id.unikom.codelabs.navigasee.utilities.extensions.showToast
import ac.id.unikom.codelabs.navigasee.utilities.helper.EventObserver
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_video_call.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.appspot.apprtc.*
import org.appspot.apprtc.AppRTCClient.RoomConnectionParameters
import org.appspot.apprtc.PeerConnectionClient.DataChannelParameters
import org.appspot.apprtc.PeerConnectionClient.PeerConnectionParameters
import org.webrtc.*
import org.webrtc.RendererCommon.ScalingType
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException

class VideoCallActivity : BaseActivity<VideoCallViewModel,
        ActivityVideoCallBinding>(R.layout.activity_video_call),
        AppRTCClient.SignalingEvents,
        PeerConnectionClient.PeerConnectionEvents, VideoCallFragment.OnCallEvents,
        EasyPermissions.PermissionCallbacks {


    private class ProxyVideoSink : VideoSink {
        private var target: VideoSink? = null

        @Synchronized
        override fun onFrame(frame: VideoFrame) {
            target?.onFrame(frame)
        }

        @Synchronized
        fun setTarget(target: VideoSink?) {
            this.target = target
        }
    }

    companion object {
        private const val RC_PERMISSION_MANDATORY_PERMISSIONS = 103
        private const val CAPTURE_PERMISSION_REQUEST_CODE = 102
        private const val STAT_CALLBACK_PERIOD = 1000

        const val EXTRA_EMAIL_TUNANETRA = "ac.id.unikom.codelabs.navigasee.EMAIL_TUNANETRA"
        const val EXTRA_TUJUAN_TUNANETRA = "ac.id.unikom.codelabs.navigasee.TUJUAN_TUNANETRA"
        const val EXTRA_TRAYEK = "ac.id.unikom.codelabs.navigasee.TRAYEK"
        const val EXTRA_PENOLAK = "ac.id.unikom.codelabs.navigasee.PENOLAK"
        const val EXTRA_FCM_PENERIMA = "ac.id.unikom.codelabs.navigasee.FCM_PENERIMA"
        const val EXTRA_POSISI_TUNANETRA = "ac.id.unikom.codelabs.navigasee.POSISI_TUNANETRA"
        const val EXTRA_ROOMID = "ac.id.unikom.codelabs.navigasee.ROOMID"
        const val EXTRA_URLPARAMETERS = "ac.id.unikom.codelabs.URLPARAMETERS"
        const val EXTRA_VIDEO_FILE_AS_CAMERA = "ac.id.unikom.codelabs.EXTRA_VIDEO_FILE_AS_CAMERA"
        const val EXTRA_LOOPBACK = "ac.id.unikom.codelabs.navigasee.LOOPBACK"
        const val EXTRA_VIDEO_CALL = "ac.id.unikom.codelabs.navigasee.VIDEO_CALL"
        const val EXTRA_SCREENCAPTURE = "ac.id.unikom.codelabs.navigasee.SCREENCAPTURE"
        const val EXTRA_CAMERA2 = "ac.id.unikom.codelabs.navigasee.CAMERA2"
        const val EXTRA_VIDEO_WIDTH = "ac.id.unikom.codelabs.navigasee.VIDEO_WIDTH"
        const val EXTRA_VIDEO_HEIGHT = "ac.id.unikom.codelabs.navigasee.VIDEO_HEIGHT"
        const val EXTRA_VIDEO_FPS = "ac.id.unikom.codelabs.navigasee.VIDEO_FPS"
        const val EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED = "org.appsopt.apprtc.VIDEO_CAPTUREQUALITYSLIDER"
        const val EXTRA_VIDEO_BITRATE = "ac.id.unikom.codelabs.navigasee.VIDEO_BITRATE"
        const val EXTRA_VIDEOCODEC = "ac.id.unikom.codelabs.navigasee.VIDEOCODEC"
        const val EXTRA_HWCODEC_ENABLED = "ac.id.unikom.codelabs.navigasee.HWCODEC"
        const val EXTRA_CAPTURETOTEXTURE_ENABLED = "ac.id.unikom.codelabs.navigasee.CAPTURETOTEXTURE"
        const val EXTRA_FLEXFEC_ENABLED = "ac.id.unikom.codelabs.navigasee.FLEXFEC"
        const val EXTRA_AUDIO_BITRATE = "ac.id.unikom.codelabs.navigasee.AUDIO_BITRATE"
        const val EXTRA_AUDIOCODEC = "ac.id.unikom.codelabs.navigasee.AUDIOCODEC"
        const val EXTRA_NOAUDIOPROCESSING_ENABLED = "ac.id.unikom.codelabs.navigasee.NOAUDIOPROCESSING"
        const val EXTRA_AECDUMP_ENABLED = "ac.id.unikom.codelabs.navigasee.AECDUMP"
        const val EXTRA_SAVE_INPUT_AUDIO_TO_FILE_ENABLED = "ac.id.unikom.codelabs.navigasee.SAVE_INPUT_AUDIO_TO_FILE"
        const val EXTRA_OPENSLES_ENABLED = "ac.id.unikom.codelabs.navigasee.OPENSLES"
        const val EXTRA_DISABLE_BUILT_IN_AEC = "ac.id.unikom.codelabs.navigasee.DISABLE_BUILT_IN_AEC"
        const val EXTRA_DISABLE_BUILT_IN_AGC = "ac.id.unikom.codelabs.navigasee.DISABLE_BUILT_IN_AGC"
        const val EXTRA_DISABLE_BUILT_IN_NS = "ac.id.unikom.codelabs.navigasee.DISABLE_BUILT_IN_NS"
        const val EXTRA_DISABLE_WEBRTC_AGC_AND_HPF = "ac.id.unikom.codelabs.navigasee.DISABLE_WEBRTC_GAIN_CONTROL"
        const val EXTRA_DISPLAY_HUD = "ac.id.unikom.codelabs.navigasee.DISPLAY_HUD"
        const val EXTRA_TRACING = "ac.id.unikom.codelabs.navigasee.TRACING"
        const val EXTRA_CMDLINE = "ac.id.unikom.codelabs.navigasee.CMDLINE"
        const val EXTRA_RUNTIME = "ac.id.unikom.codelabs.navigasee.RUNTIME"
        const val EXTRA_DATA_CHANNEL_ENABLED = "ac.id.unikom.codelabs.navigasee.DATA_CHANNEL_ENABLED"
        const val EXTRA_ORDERED = "ac.id.unikom.codelabs.navigasee.ORDERED"
        const val EXTRA_MAX_RETRANSMITS_MS = "ac.id.unikom.codelabs.navigasee.MAX_RETRANSMITS_MS"
        const val EXTRA_MAX_RETRANSMITS = "ac.id.unikom.codelabs.navigasee.MAX_RETRANSMITS"
        const val EXTRA_PROTOCOL = "ac.id.unikom.codelabs.navigasee.PROTOCOL"
        const val EXTRA_NEGOTIATED = "ac.id.unikom.codelabs.navigasee.NEGOTIATED"
        const val EXTRA_ID = "ac.id.unikom.codelabs.navigasee.ID"
        const val EXTRA_ENABLE_RTCEVENTLOG = "ac.id.unikom.codelabs.navigasee.ENABLE_RTCEVENTLOG"
        const val EXTRA_USE_LEGACY_AUDIO_DEVICE = "ac.id.unikom.codelabs.navigasee.USE_LEGACY_AUDIO_DEVICE"
        const val EXTRA_TUNANETRA = "EXTRA_TUNANETRA"

        private val PERMISSIONS_MANDATORY = arrayOf(
                "android.permission.MODIFY_AUDIO_SETTINGS",
                "android.permission.RECORD_AUDIO",
                "android.permission.INTERNET"
        )
    }

    private lateinit var preferences: Preferences

    private var mediaProjectionPermissionResultCode = 0
    private var callStartedTimeMs = 0L
    private var isSwappedFeeds = false
    private var isError = false
    private var iceConnected = false
    private var screencaptureEnabled = false
    private var activityRunning = false
    private var commandLineRun = false
    private var hasRequest = false
    private var isCancelled = true
    private var micEnabled = true
    private var callControlFragmentVisible = true

    private val remoteProxyRenderer = ProxyVideoSink()
    private val localProxyVideoSink = ProxyVideoSink()

    private var signalingParameters: AppRTCClient.SignalingParameters? = null
    private var peerConnectionClient: PeerConnectionClient? = null
    private var peerConnectionParameters: PeerConnectionParameters? = null
    private var appRtcClient: AppRTCClient? = null
    private var roomConnectionParameters: RoomConnectionParameters? = null
    private var cpuMonitor: CpuMonitor? = null
    private var mediaProjectionPermissionResultData: Intent? = null
    private var audioManager: AppRTCAudioManager? = null

    private val remoteSinks = mutableListOf<VideoSink>()
    private val videoCallFragment = VideoCallFragment()

    var tunanetra: String? = null

    private val viewModel: VideoCallViewModel by viewModels {
        VideoCallViewModelFactory(DashboardRepository.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(UnhandledExceptionHandler(this))
        mParentVM = viewModel
        preferences = Preferences.getInstance()

        pipVideoView.setOnClickListener { setSwappedFeeds(!isSwappedFeeds) }
        fullScreenVideoView.setOnClickListener { toggleCallControlFragmentVisibility() }
        remoteSinks.add(remoteProxyRenderer)

        if (!EasyPermissions.hasPermissions(this, *PERMISSIONS_MANDATORY)) {
            EasyPermissions.requestPermissions(
                    this,
                    "Untuk melakukan videl call, dibutuhkan izin untuk mengakses pengaturan",
                    RC_PERMISSION_MANDATORY_PERMISSIONS,
                    *PERMISSIONS_MANDATORY
            )
        }

        tunanetra = intent.getStringExtra(EXTRA_TUNANETRA)

        startingCall()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isCancelled) {
            val email_tunanetra: String? = intent.getStringExtra(EXTRA_EMAIL_TUNANETRA)
            val tujuan_tunanetra: String? = intent.getStringExtra(EXTRA_TUJUAN_TUNANETRA)
            val trayek: String? = intent.getStringExtra(EXTRA_TRAYEK)
            val posisiTunanetraJson: String? = intent.getStringExtra(EXTRA_POSISI_TUNANETRA)
            val posisiTunanetra: Location? = Gson().fromJson(posisiTunanetraJson, Location::class.java)
            var penolak: ArrayList<String>? = null
            intent.getStringExtra(EXTRA_PENOLAK)?.let {
                penolak = Gson().fromJson(it, object : TypeToken<ArrayList<String>>() {}.type)
            }
            preferences.getEmail()?.let { penolak?.add(it) }

            if (email_tunanetra != null && tujuan_tunanetra != null && trayek != null && posisiTunanetraJson != null && posisiTunanetra != null) {
                val body = WaitingBody(
                        email_tunanetra,
                        tujuan_tunanetra,
                        trayek,
                        posisiTunanetra.latitude,
                        posisiTunanetra.longitude,
                        penolak
                )

                GlobalScope.launch {
                    val response = WaitingRepository.getInstance().waiting(body)
                    if (response?.status == 200) {
                        Log.d(this.javaClass.simpleName, "Batalkan sukses dikirim")
                    } else {
                        Log.d(this.javaClass.simpleName, "Batalkan gagal terkirim")
                    }
                }
            }
        }

        tunanetra?.let {
            val intent = Intent(this@VideoCallActivity, ConfirmMasukKendaraanActivitiy::class.java)
            startActivity(intent)
        }

        disconnect()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onCreateObserver(viewModel: VideoCallViewModel) {
        viewModel.apply {
            tokenSent.observe(this@VideoCallActivity, EventObserver {
                if (hasRequest)
                    if (it) {
                        if (screencaptureEnabled) startScreenCapture()
                        else startCall()
                    } else {
                        isCancelled = true
                        finish()
                    }

            })
        }
    }

    override fun setContentData() {
    }

    override fun setMessageType() = MESSAGE_TYPE_TOAST

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == RC_PERMISSION_MANDATORY_PERMISSIONS) {
            cancelActivity("Tidak ada izin untuk melakukan panggilan")
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onConnectedToRoom(params: AppRTCClient.SignalingParameters?) {
        runOnUiThread {
            signalingParameters = params
            var videoCapturer: VideoCapturer? = null
            if (peerConnectionParameters?.videoCallEnabled!!) {
                videoCapturer = createVideoCapturer()
            }
            peerConnectionClient?.createPeerConnection(localProxyVideoSink, remoteSinks,
                    videoCapturer, signalingParameters)

            if (signalingParameters?.initiator!!) {
                peerConnectionClient?.createOffer()
            } else {
                params?.offerSdp?.let {
                    peerConnectionClient?.setRemoteDescription(it)
                    peerConnectionClient?.createAnswer()
                }

                params?.iceCandidates?.let {
                    for (iceCandidate in it) {
                        peerConnectionClient?.addRemoteIceCandidate(iceCandidate)
                    }
                }
            }
        }
    }

    override fun onRemoteDescription(sdp: SessionDescription?) {
        runOnUiThread {
            if (peerConnectionClient == null) return@runOnUiThread
            peerConnectionClient?.setRemoteDescription(sdp)
            if (!signalingParameters?.initiator!!) peerConnectionClient?.createAnswer()
        }
    }

    override fun onRemoteIceCandidate(candidate: IceCandidate?) {
        runOnUiThread {
            if (peerConnectionClient == null) return@runOnUiThread
            peerConnectionClient?.addRemoteIceCandidate(candidate)
        }
    }

    override fun onRemoteIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {
        runOnUiThread {
            if (peerConnectionClient == null) return@runOnUiThread
            peerConnectionClient?.removeRemoteIceCandidates(candidates)
        }
    }

    override fun onChannelClose() {
        runOnUiThread { disconnect() }
    }

    override fun onChannelError(description: String?) {
    }

    override fun onLocalDescription(sdp: SessionDescription?) {
        runOnUiThread {
            appRtcClient?.let {
                if (signalingParameters?.initiator!!) it.sendOfferSdp(sdp)
                else it.sendAnswerSdp(sdp)
            }
            if (peerConnectionParameters?.videoMaxBitrate!! > 0) {
                peerConnectionClient?.setVideoMaxBitrate(peerConnectionParameters?.videoMaxBitrate)
            }
        }
    }

    override fun onIceCandidate(candidate: IceCandidate?) {
        runOnUiThread { appRtcClient?.sendLocalIceCandidate(candidate) }
    }

    override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {
        runOnUiThread { appRtcClient?.sendLocalIceCandidateRemovals(candidates) }
    }

    override fun onConnected() {
    }

    override fun onIceConnected() {
        runOnUiThread {
            iceConnected = true
            callConnected()
        }
    }

    override fun onIceDisconnected() {
        runOnUiThread {
            iceConnected = false
            disconnect()
        }
    }

    override fun onPeerConnectionClosed() {
    }

    override fun onPeerConnectionStatsReady(reports: Array<out StatsReport>?) {
    }

    override fun onDisconnected() {
    }

    override fun onPeerConnectionError(description: String?) {
    }

    override fun onCallHangUp() {
        disconnect()
    }

    override fun onCameraSwitch() {
        peerConnectionClient?.switchCamera()
    }

    override fun onToggleMic(): Boolean {
        peerConnectionClient?.let {
            micEnabled = !micEnabled
            it.setAudioEnabled(micEnabled)
        }
        return micEnabled
    }

    @TargetApi(17)
    private fun getDisplayMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics
    }

    @TargetApi(21)
    private fun startScreenCapture() {
        val mediaProjectionManager = application.getSystemService(
                Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(), CAPTURE_PERMISSION_REQUEST_CODE)
    }

    private fun callConnected() {
        if (peerConnectionClient == null || isError) return
        peerConnectionClient?.enableStatsEvents(true, STAT_CALLBACK_PERIOD)
        setSwappedFeeds(false)
    }

    private fun disconnect() {
        activityRunning = false
        remoteProxyRenderer.setTarget(null)
        localProxyVideoSink.setTarget(null)
        if (appRtcClient != null) {
            appRtcClient?.disconnectFromRoom()
            appRtcClient = null
        }

        if (peerConnectionClient != null) {
            peerConnectionClient?.close()
            peerConnectionClient = null
        }

        if (audioManager != null) {
            audioManager?.stop()
            audioManager = null
        }

        if (pipVideoView != null) pipVideoView.release()
        if (fullScreenVideoView != null) fullScreenVideoView.release()
        if (iceConnected && !isError) setResult(Activity.RESULT_OK)
        else setResult(Activity.RESULT_CANCELED)

        finish()
    }

    private fun createVideoCapturer(): VideoCapturer? {
        val videoCapturer: VideoCapturer?
        val videoFileAsCamera = intent.getStringExtra(EXTRA_VIDEO_FILE_AS_CAMERA)
        when {
            videoFileAsCamera != null -> try {
                videoCapturer = FileVideoCapturer(videoFileAsCamera)
            } catch (e: IOException) {
                return null
            }
            screencaptureEnabled -> return createScreenCapturer()
            useCamera2() -> {
                if (!captureToTexture()) {
                    return null
                }

                videoCapturer = createCameraCapturer(Camera2Enumerator(this))
            }
            else -> videoCapturer = createCameraCapturer(Camera1Enumerator(captureToTexture()))
        }

        return videoCapturer
    }

    @TargetApi(21)
    private fun createScreenCapturer(): VideoCapturer? {
        if (mediaProjectionPermissionResultCode != Activity.RESULT_OK) {
            return null
        }
        return ScreenCapturerAndroid(mediaProjectionPermissionResultData, null)
    }

    private fun createCameraCapturer(enumerator: CameraEnumerator): VideoCapturer? {
        val deviceNames = enumerator.deviceNames

        for (deviceName in deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                val videoCapturer = enumerator.createCapturer(deviceName, null)
                if (videoCapturer != null) return videoCapturer
            }

            if (enumerator.isBackFacing(deviceName)) {
                val videoCapturer = enumerator.createCapturer(deviceName, null)
                if (videoCapturer != null) return videoCapturer
            }
        }

        return null
    }

    private fun useCamera2() =
            Camera2Enumerator.isSupported(this) && intent.getBooleanExtra(EXTRA_CAMERA2, true)

    private fun captureToTexture() =
            intent.getBooleanExtra(EXTRA_CAPTURETOTEXTURE_ENABLED, false)

    private fun startCall() {
        callStartedTimeMs = System.currentTimeMillis()
        appRtcClient?.connectToRoom(roomConnectionParameters)
        audioManager = AppRTCAudioManager.create(applicationContext)
        audioManager?.start { _, _ -> }
    }

    private fun setSwappedFeeds(isSwappedFeeds: Boolean) {
        this.isSwappedFeeds = isSwappedFeeds
        localProxyVideoSink.setTarget(if (isSwappedFeeds) fullScreenVideoView else pipVideoView)
        remoteProxyRenderer.setTarget(if (isSwappedFeeds) pipVideoView else fullScreenVideoView)
        fullScreenVideoView.setMirror(false)
        pipVideoView.setMirror(!isSwappedFeeds)
    }

    private fun toggleCallControlFragmentVisibility() {
        if (!iceConnected || !videoCallFragment.isAdded) return

        callControlFragmentVisible = !callControlFragmentVisible
        val ft = supportFragmentManager.beginTransaction()
        if (callControlFragmentVisible) {
            ft.show(videoCallFragment)
        } else {
            ft.hide(videoCallFragment)
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.commit()
    }

    private fun cancelActivity(message: String) {
        showToast(message)
        Log.i(javaClass.simpleName, message)
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    @AfterPermissionGranted(RC_PERMISSION_MANDATORY_PERMISSIONS)
    private fun startingCall() {
        val eglBase = EglBase.create()

        pipVideoView.init(eglBase.eglBaseContext, null)
        pipVideoView.setScalingType(ScalingType.SCALE_ASPECT_FIT)
        fullScreenVideoView.init(eglBase.eglBaseContext, null)
        fullScreenVideoView.setScalingType(ScalingType.SCALE_ASPECT_FILL)

        pipVideoView.setZOrderMediaOverlay(true)
        pipVideoView.setEnableHardwareScaler(true)
        fullScreenVideoView.setEnableHardwareScaler(false)
        setSwappedFeeds(true)

        val roomUri = intent.data
        if (roomUri == null) {
            cancelActivity("Uri dari intent bernilai null")
            return
        }

        val roomId = intent.getStringExtra(EXTRA_ROOMID)
        if (roomId == null || roomId.isEmpty()) {
            cancelActivity("Room id tidak ditemukan")
            return
        }

        val tracing = intent.getBooleanExtra(EXTRA_TRACING, false)
        screencaptureEnabled = intent.getBooleanExtra(EXTRA_SCREENCAPTURE, false)
        var videoWidth = intent.getIntExtra(EXTRA_VIDEO_WIDTH, 0)
        var videoHeight = intent.getIntExtra(EXTRA_VIDEO_HEIGHT, 0)
        if (screencaptureEnabled && videoWidth == 0 && videoHeight == 0) {
            val displayMetrics = getDisplayMetrics()
            videoWidth = displayMetrics.widthPixels
            videoHeight = displayMetrics.heightPixels
        }
        var dataChannelParameters: DataChannelParameters? = null
        if (intent.getBooleanExtra(EXTRA_DATA_CHANNEL_ENABLED, false)) {
            dataChannelParameters = DataChannelParameters(intent.getBooleanExtra(EXTRA_ORDERED, true),
                    intent.getIntExtra(EXTRA_MAX_RETRANSMITS_MS, -1),
                    intent.getIntExtra(EXTRA_MAX_RETRANSMITS, -1), intent.getStringExtra(EXTRA_PROTOCOL),
                    intent.getBooleanExtra(EXTRA_NEGOTIATED, false), intent.getIntExtra(EXTRA_ID, -1))
        }
        peerConnectionParameters = PeerConnectionParameters(
                intent.getBooleanExtra(EXTRA_VIDEO_CALL, true),
                false, tracing, videoWidth, videoHeight,
                intent.getIntExtra(EXTRA_VIDEO_FPS, 0),
                intent.getIntExtra(EXTRA_VIDEO_BITRATE, 0),
                intent.getStringExtra(EXTRA_VIDEOCODEC),
                intent.getBooleanExtra(EXTRA_HWCODEC_ENABLED, true),
                intent.getBooleanExtra(EXTRA_FLEXFEC_ENABLED, false),
                intent.getIntExtra(EXTRA_AUDIO_BITRATE, 0),
                intent.getStringExtra(EXTRA_AUDIOCODEC),
                intent.getBooleanExtra(EXTRA_NOAUDIOPROCESSING_ENABLED, false),
                intent.getBooleanExtra(EXTRA_AECDUMP_ENABLED, false),
                intent.getBooleanExtra(EXTRA_SAVE_INPUT_AUDIO_TO_FILE_ENABLED, false),
                intent.getBooleanExtra(EXTRA_OPENSLES_ENABLED, false),
                intent.getBooleanExtra(EXTRA_DISABLE_BUILT_IN_AEC, false),
                intent.getBooleanExtra(EXTRA_DISABLE_BUILT_IN_AGC, false),
                intent.getBooleanExtra(EXTRA_DISABLE_BUILT_IN_NS, false),
                intent.getBooleanExtra(EXTRA_DISABLE_WEBRTC_AGC_AND_HPF, false),
                intent.getBooleanExtra(EXTRA_ENABLE_RTCEVENTLOG, false),
                dataChannelParameters)
        commandLineRun = intent.getBooleanExtra(EXTRA_CMDLINE, false)
        val runTimeMs = intent.getIntExtra(EXTRA_RUNTIME, 0)
        if (!DirectRTCClient.IP_PATTERN.matcher(roomId).matches()) appRtcClient = WebSocketRTCClient(this)
        else appRtcClient = DirectRTCClient(this)

        val urlParameters = intent.getStringExtra(EXTRA_URLPARAMETERS)
        roomConnectionParameters = RoomConnectionParameters(roomUri.toString(), roomId, false, urlParameters)

        if (CpuMonitor.isSupported()) cpuMonitor = CpuMonitor(this)

        videoCallFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction()
                .add(R.id.callFragmentContainer, videoCallFragment)
                .commit()

        if (commandLineRun && runTimeMs > 0) Handler().postDelayed({ disconnect() }, runTimeMs.toLong())

        peerConnectionClient = PeerConnectionClient(
                applicationContext, eglBase, peerConnectionParameters, this@VideoCallActivity)
        val options = PeerConnectionFactory.Options()
        peerConnectionClient?.createPeerConnectionFactory(options)

        val fcmPenerima = intent.getStringExtra(EXTRA_FCM_PENERIMA)
        fcmPenerima?.let {
            viewModel.sendToken(it, roomId)
            hasRequest = true
        }

        tunanetra?.let {
            hasRequest = true
            viewModel.updateSendToken()
        }
    }
}
