package ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup.bantuan_datang

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.model.dashboard.Location
import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingBody
import ac.id.unikom.codelabs.navigasee.data.source.PopUpRepository
import ac.id.unikom.codelabs.navigasee.data.source.WaitingRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivityPopUpBantuanDatangBinding
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup.PopUpPilihJenisBantuan
import ac.id.unikom.codelabs.navigasee.mvvm.video_call.VideoCallActivity
import ac.id.unikom.codelabs.navigasee.utilities.*
import ac.id.unikom.codelabs.navigasee.utilities.extensions.showToast
import ac.id.unikom.codelabs.navigasee.utilities.helper.CloseActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.Manifest
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_pop_up_bantuan_datang.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class PopUpBantuanDatang : BantuanDatangListener, AppCompatActivity() {
    override fun datangiLangsung() {
        val intent = Intent(this, PopUpPilihJenisBantuan::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var preferences: Preferences
    private lateinit var mBinding: ActivityPopUpBantuanDatangBinding
    private var videoCallIntent: Intent? = null
    private var posisi_tunanetra: String? = null
    private var batalkan: Boolean = true
    private var fcmPenerima: String? = null
    private var penolak: ArrayList<String>? = null
    private var email_tunanetra: String? = null
    private var tujuan_tunanetra: String? = null
    private var trayek: String? = null
    private var onstart_finish = false

    companion object {
        const val EXTRA_FCM_PENERIMA = "extra_fcm_penerima"

        const val KEY_PREF_ROOM_SERVER_URL = "key_pref_room_server_url"
        const val KEY_PREF_RESOLUTION = "key_pref_resolution"
        const val KEY_PREF_FPS = "key_pref_fps"
        const val KEY_PREF_VIDEO_BITRATE_TYPE = "key_pref_video_bitrate_type"
        const val KEY_PREF_VIDEO_BITRATE_VALUE = "key_pref_video_bitrate_value"
        const val KEY_PREF_AUDIO_BITRATE_TYPE = "key_pref_audio_bitrate_type"
        const val KEY_PREF_AUDIO_BITRATE_VALUE = "key_pref_audio_bitrate_value"
        const val RC_PERMISSIONS_START_CALL = 101

        const val REQUEST_CODE = 191

        const val RESULT_BANTUAN_SAMPAI_TUJUAN = "RESULT_BANTUAN_SAMPAI_TUJUAN"
        const val RESULT_BANTUAN_SAMPAI_KENDARAAN = "RESULT_BANTUAN_SAMPAI_KENDARAAN"
        const val RESULT_BANTUAN = "RESULT_BANTUAN"
        val PERMISSIONS_START_CALL = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pop_up_bantuan_datang)
        mBinding.mListener = this
        preferences = Preferences.getInstance()

        //set flags for turn on user's phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onStart() {
        super.onStart()
        val extras = intent.extras
        if (extras != null) {
            trayek = extras.getString(TRAYEK)
            email_tunanetra = extras.getString(EMAIL_TUNANETRA)
            tujuan_tunanetra = extras.getString(TEMPAT_TUJUAN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mBinding.textView7.text = Html.fromHtml(getString(R.string.popup_bantuan_datang, trayek, tujuan_tunanetra), Html.FROM_HTML_MODE_COMPACT)
            } else {
                mBinding.textView7.text = Html.fromHtml(getString(R.string.popup_bantuan_datang, trayek, tujuan_tunanetra))
            }

            if (!extras.getString(PENOLAK)!!.equals("[]")) {
                penolak = Gson().fromJson(extras.getString(PENOLAK), object : TypeToken<ArrayList<String>>() {}.type)
            } else {
                penolak = ArrayList()
            }

            onstart_finish = true
            posisi_tunanetra = extras.getString(POSISI_TUNANETRA)
            fcmPenerima = intent.getStringExtra(EXTRA_FCM_PENERIMA)
            runnable = Runnable {
                showToast(R.string.anda_tidak_memberikan_jawaban_selama_15_detik)
                finish()
            }

            handler = Handler().apply {
                postDelayed(runnable, 15000)
            }
            onstart_finish = true

            btn_close.setOnClickListener {
                finish()
            }

        } else {
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun connectToRoom() {
        val roomId = System.currentTimeMillis().toString()
        val roomUrl = preferences.getRoomUrl()
        val videoCallEnabled = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_videocall_key,
                org.appspot.apprtc.R.string.pref_videocall_default)
        val useScreencapture = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_screencapture_key,
                org.appspot.apprtc.R.string.pref_screencapture_default)
        val useCamera2 = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_camera2_key,
                org.appspot.apprtc.R.string.pref_camera2_default)
        val hwCodec = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_hwcodec_key,
                org.appspot.apprtc.R.string.pref_hwcodec_default)
        val captureToTexture = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_capturetotexture_key,
                org.appspot.apprtc.R.string.pref_capturetotexture_default)
        val flexfecEnabled = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_flexfec_key,
                org.appspot.apprtc.R.string.pref_flexfec_default)
        val noAudioProcessing = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_noaudioprocessing_key,
                org.appspot.apprtc.R.string.pref_noaudioprocessing_default)
        val aecDump = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_aecdump_key,
                org.appspot.apprtc.R.string.pref_aecdump_default)
        val saveInputAudioToFile = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_enable_save_input_audio_to_file_key,
                org.appspot.apprtc.R.string.pref_enable_save_input_audio_to_file_default)
        val useOpenSLES = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_opensles_key,
                org.appspot.apprtc.R.string.pref_opensles_default)
        val disableBuiltInAEC = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_disable_built_in_aec_key,
                org.appspot.apprtc.R.string.pref_disable_built_in_aec_default)
        val disableBuiltInAGC = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_disable_built_in_agc_key,
                org.appspot.apprtc.R.string.pref_disable_built_in_agc_default)
        val disableBuiltInNS = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_disable_built_in_ns_key,
                org.appspot.apprtc.R.string.pref_disable_built_in_ns_default)
        val disableWebRtcAGCAndHPF = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_disable_webrtc_agc_and_hpf_key,
                org.appspot.apprtc.R.string.pref_disable_webrtc_agc_and_hpf_key)
        val displayHud = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_displayhud_key,
                org.appspot.apprtc.R.string.pref_displayhud_default)
        val tracing = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_tracing_key,
                org.appspot.apprtc.R.string.pref_tracing_default)
        val rtcEventLogEnabled = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_enable_rtceventlog_key,
                org.appspot.apprtc.R.string.pref_enable_rtceventlog_default)
        val useLegacyAudioDevice = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_use_legacy_audio_device_key,
                org.appspot.apprtc.R.string.pref_use_legacy_audio_device_default)
        val dataChannelEnabled = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_enable_datachannel_key,
                org.appspot.apprtc.R.string.pref_enable_datachannel_default)
        val ordered = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_ordered_key,
                org.appspot.apprtc.R.string.pref_ordered_default)
        val negotiated = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_negotiated_key,
                org.appspot.apprtc.R.string.pref_negotiated_default)

        val videoCodec = preferences.getStringWithId(org.appspot.apprtc.R.string.pref_videocodec_key,
                org.appspot.apprtc.R.string.pref_videocodec_default)
        val audioCodec = preferences.getStringWithId(org.appspot.apprtc.R.string.pref_audiocodec_key,
                org.appspot.apprtc.R.string.pref_audiocodec_default)
        val captureQualitySlider = preferences.getBooleanWithId(org.appspot.apprtc.R.string.pref_capturequalityslider_key,
                org.appspot.apprtc.R.string.pref_capturequalityslider_default)
        val protocol = preferences.getStringWithId(org.appspot.apprtc.R.string.pref_data_protocol_key,
                org.appspot.apprtc.R.string.pref_data_protocol_default)

        var videoWidth = 0
        var videoHeight = 0
        val resolution = preferences.getResolution()
        val dimensions = resolution
                ?.split("[ x]+".toRegex())
                ?.dropLastWhile { it.isEmpty() }
                ?.toTypedArray()
        if (dimensions?.size == 2) {
            try {
                videoWidth = dimensions[0].toInt()
                videoHeight = dimensions[1].toInt()
            } catch (e: NumberFormatException) {
                videoWidth = 0
                videoHeight = 0
            }
        }

        var cameraFps = 0
        val fps = preferences.getFps()
        val fpsValues = fps
                ?.split("[ x]+".toRegex())
                ?.dropLastWhile { it.isEmpty() }
                ?.toTypedArray()
        if (fpsValues?.size == 2) {
            cameraFps = try {
                Integer.parseInt(fpsValues[0])
            } catch (e: NumberFormatException) {
                0
            }
        }

        var videoStartBitrate = 0
        var bitrateType = preferences.getVideoBitrateType()
        if (bitrateType != "Default") {
            val bitrateValue = preferences.getVideoBitrateValue()
            videoStartBitrate = bitrateValue!!.toInt()
        }

        var audioStartBitrate = 0
        bitrateType = preferences.getAudioBitrateType()
        if (bitrateType != "Default") {
            val bitrateValue = preferences.getAudioBitrateValue()
            audioStartBitrate = bitrateValue!!.toInt()
        }

        val maxRetrMs = preferences.getIntegerWithId(org.appspot.apprtc.R.string.pref_max_retransmit_time_ms_key,
                org.appspot.apprtc.R.string.pref_max_retransmit_time_ms_default)
        val maxRetr = preferences.getIntegerWithId(org.appspot.apprtc.R.string.pref_max_retransmits_key,
                org.appspot.apprtc.R.string.pref_max_retransmits_default)
        val id = preferences.getIntegerWithId(org.appspot.apprtc.R.string.pref_data_id_key,
                org.appspot.apprtc.R.string.pref_data_id_default)

        val uri = Uri.parse(roomUrl)
        val intent = Intent(this, VideoCallActivity::class.java)
        intent.data = uri
        intent.putExtra(VideoCallActivity.EXTRA_ROOMID, roomId)
                .putExtra(VideoCallActivity.EXTRA_LOOPBACK, false)
                .putExtra(VideoCallActivity.EXTRA_VIDEO_CALL, videoCallEnabled)
                .putExtra(VideoCallActivity.EXTRA_SCREENCAPTURE, useScreencapture)
                .putExtra(VideoCallActivity.EXTRA_CAMERA2, useCamera2)
                .putExtra(VideoCallActivity.EXTRA_VIDEO_WIDTH, videoWidth)
                .putExtra(VideoCallActivity.EXTRA_VIDEO_HEIGHT, videoHeight)
                .putExtra(VideoCallActivity.EXTRA_VIDEO_FPS, cameraFps)
                .putExtra(VideoCallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, captureQualitySlider)
                .putExtra(VideoCallActivity.EXTRA_VIDEO_BITRATE, videoStartBitrate)
                .putExtra(VideoCallActivity.EXTRA_VIDEOCODEC, videoCodec)
                .putExtra(VideoCallActivity.EXTRA_HWCODEC_ENABLED, hwCodec)
                .putExtra(VideoCallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED, captureToTexture)
                .putExtra(VideoCallActivity.EXTRA_FLEXFEC_ENABLED, flexfecEnabled)
                .putExtra(VideoCallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED, noAudioProcessing)
                .putExtra(VideoCallActivity.EXTRA_AECDUMP_ENABLED, aecDump)
                .putExtra(VideoCallActivity.EXTRA_SAVE_INPUT_AUDIO_TO_FILE_ENABLED, saveInputAudioToFile)
                .putExtra(VideoCallActivity.EXTRA_OPENSLES_ENABLED, useOpenSLES)
                .putExtra(VideoCallActivity.EXTRA_DISABLE_BUILT_IN_AEC, disableBuiltInAEC)
                .putExtra(VideoCallActivity.EXTRA_DISABLE_BUILT_IN_AGC, disableBuiltInAGC)
                .putExtra(VideoCallActivity.EXTRA_DISABLE_BUILT_IN_NS, disableBuiltInNS)
                .putExtra(VideoCallActivity.EXTRA_DISABLE_WEBRTC_AGC_AND_HPF, disableWebRtcAGCAndHPF)
                .putExtra(VideoCallActivity.EXTRA_AUDIO_BITRATE, audioStartBitrate)
                .putExtra(VideoCallActivity.EXTRA_AUDIOCODEC, audioCodec)
                .putExtra(VideoCallActivity.EXTRA_DISPLAY_HUD, displayHud)
                .putExtra(VideoCallActivity.EXTRA_TRACING, tracing)
                .putExtra(VideoCallActivity.EXTRA_ENABLE_RTCEVENTLOG, rtcEventLogEnabled)
                .putExtra(VideoCallActivity.EXTRA_CMDLINE, false)
                .putExtra(VideoCallActivity.EXTRA_RUNTIME, 0)
                .putExtra(VideoCallActivity.EXTRA_USE_LEGACY_AUDIO_DEVICE, useLegacyAudioDevice)
                .putExtra(VideoCallActivity.EXTRA_DATA_CHANNEL_ENABLED, dataChannelEnabled)
                .putExtra(VideoCallActivity.EXTRA_FCM_PENERIMA, fcmPenerima)
                .putExtra(VideoCallActivity.EXTRA_POSISI_TUNANETRA, posisi_tunanetra)
                .putExtra(VideoCallActivity.EXTRA_PENOLAK, penolak)
                .putExtra(VideoCallActivity.EXTRA_EMAIL_TUNANETRA, email_tunanetra)
                .putExtra(VideoCallActivity.EXTRA_TUJUAN_TUNANETRA, tujuan_tunanetra)
                .putExtra(VideoCallActivity.EXTRA_TRAYEK, trayek)

        if (dataChannelEnabled) {
            intent.putExtra(VideoCallActivity.EXTRA_ORDERED, ordered)
                    .putExtra(VideoCallActivity.EXTRA_MAX_RETRANSMITS_MS, maxRetrMs)
                    .putExtra(VideoCallActivity.EXTRA_MAX_RETRANSMITS, maxRetr)
                    .putExtra(VideoCallActivity.EXTRA_PROTOCOL, protocol)
                    .putExtra(VideoCallActivity.EXTRA_NEGOTIATED, negotiated)
                    .putExtra(VideoCallActivity.EXTRA_ID, id)
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        videoCallIntent = intent
        startVideoCallActivity()
    }

    @AfterPermissionGranted(RC_PERMISSIONS_START_CALL)
    private fun startVideoCallActivity() {
        if (!EasyPermissions.hasPermissions(this, *PERMISSIONS_START_CALL)) {
            EasyPermissions.requestPermissions(
                    this,
                    "Untuk melakukan video call, dibutuhkan izin untuk mengakses kamera serta mikrofon",
                    RC_PERMISSIONS_START_CALL,
                    *PERMISSIONS_START_CALL
            )
            return
        }

        batalkan = false
        handler.removeCallbacks(runnable)
        startActivity(videoCallIntent)
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (batalkan && onstart_finish) {
            val posisiTunanetra = Gson().fromJson(posisi_tunanetra, Location::class.java)

            preferences.getEmail()?.let { penolak?.add(it) }

            val body = WaitingBody(
                    email_tunanetra!!,
                    tujuan_tunanetra!!,
                    trayek!!,
                    posisiTunanetra.latitude,
                    posisiTunanetra.longitude,
                    penolak
            )

            GlobalScope.launch {
                val response = WaitingRepository.getInstance().waiting(body)
                if (response != null) {
                    Log.d(this.javaClass.simpleName, "Batalkan sukses dikirim")
                } else {
                    Log.d(this.javaClass.simpleName, "Batalkan gagal terkirim")
                }

                val intent = Intent(this@PopUpBantuanDatang, CloseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            handler.removeCallbacks(runnable)

        }
    }

    private fun bantu_sampai_kendaraan() {
        GlobalScope.launch(Dispatchers.Main) {
            val response = PopUpRepository.getInstance().saya_akan_datangi_langsung(fcmPenerima!!, preferences.getEmail()!!)
            if (response?.status == 200) {
                batalkan = false
                handler.removeCallbacks(runnable)
                Log.d(this.javaClass.simpleName, "Saya akan bantu sukses dikirim")
                val posisiTunanetra = Gson().fromJson(posisi_tunanetra, Location::class.java)

                //intent to google maps direction
                val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=${posisiTunanetra.latitude},${posisiTunanetra.longitude}"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finishAffinity()
            } else {

                Log.d(this.javaClass.simpleName, "Saya akan bantu gagal terkirim")
            }
        }

    }

    private fun bantu_sampai_tujuan() {
        GlobalScope.launch(Dispatchers.Main) {
            val response = PopUpRepository.getInstance().saya_akan_bantu_sampai_tujuan(
                    fcmPenerima!!, preferences.getEmail()!!, preferences.getLongitude()!!.toDouble(), preferences.getLatitude()!!.toDouble()
            )
            if (response?.status == 200) {
                batalkan = false
                handler.removeCallbacks(runnable)
                Log.d(this.javaClass.simpleName, "Saya akan bantu sukses dikirim")
                val posisiTunanetra = Gson().fromJson(posisi_tunanetra, Location::class.java)

                //intent to google maps direction
                val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=${posisiTunanetra.latitude},${posisiTunanetra.longitude}"))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finishAffinity()

            } else {

                Log.d(this.javaClass.simpleName, "Saya akan bantu gagal terkirim")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val extras = data?.extras
                if (extras != null) {
                    val bantuan = extras.getString(RESULT_BANTUAN)
                    if (bantuan != null) {
                        if (bantuan.equals(RESULT_BANTUAN_SAMPAI_KENDARAAN))
                            bantu_sampai_kendaraan()
                        else if (bantuan.equals(RESULT_BANTUAN_SAMPAI_TUJUAN))
                            bantu_sampai_tujuan()
                    }
                }
            }
        }
    }

}
