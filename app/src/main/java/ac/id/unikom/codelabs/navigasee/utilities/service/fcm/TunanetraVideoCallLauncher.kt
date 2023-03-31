package ac.id.unikom.codelabs.navigasee.utilities.service.fcm

import ac.id.unikom.codelabs.navigasee.mvvm.video_call.VideoCallActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Context
import android.content.Intent
import android.net.Uri

class TunanetraVideoCallLauncher {
    private val preferences = Preferences.getInstance()

    fun buildIntent(context: Context, data: Map<String, String>): Intent {
        val roomId = data.get("tokenVC")
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
        val intent = Intent(context, VideoCallActivity::class.java)
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
                .putExtra(VideoCallActivity.EXTRA_FCM_PENERIMA, data.get("fcmPenerima"))
                .putExtra(VideoCallActivity.EXTRA_TUNANETRA, "EXTRA_TUNANETRA")

        if (dataChannelEnabled) {
            intent.putExtra(VideoCallActivity.EXTRA_ORDERED, ordered)
                    .putExtra(VideoCallActivity.EXTRA_MAX_RETRANSMITS_MS, maxRetrMs)
                    .putExtra(VideoCallActivity.EXTRA_MAX_RETRANSMITS, maxRetr)
                    .putExtra(VideoCallActivity.EXTRA_PROTOCOL, protocol)
                    .putExtra(VideoCallActivity.EXTRA_NEGOTIATED, negotiated)
                    .putExtra(VideoCallActivity.EXTRA_ID, id)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        //TODO : GANTI METODE CARA PEROLEH POIN
        preferences.setJumlahPoinYangDiberikan(50)
        preferences.setJenisBantuan(Preferences.JENIS_BANTUAN_VIDEO_CALL)

        return intent
    }
}