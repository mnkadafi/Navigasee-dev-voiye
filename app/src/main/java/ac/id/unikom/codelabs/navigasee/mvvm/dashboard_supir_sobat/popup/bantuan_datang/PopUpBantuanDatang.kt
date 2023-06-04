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
import ac.id.unikom.codelabs.navigasee.webrtcnew.SocketRepository
import ac.id.unikom.codelabs.navigasee.webrtcnew.kelas.CallNewActivity
import ac.id.unikom.codelabs.navigasee.webrtcnew.model.MessageModel
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

    private var penolak: ArrayList<String>? = null

    private var fcmPenerima: String? = null
    private var email_tunanetra: String? = null

    //private var fcmBody: String? = null
    private var fcmTunanetra: String? = null

    private var tujuan_tunanetra: String? = null
    private var trayek: String? = null
    private var onstart_finish = false

    companion object {
        const val EXTRA_FCM_PENERIMA = "extra_fcm_penerima"
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

    private var socketRepository: SocketRepository? = null

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
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }

    override fun onStart() {
        super.onStart()
        val extras = intent.extras
        if (extras != null) {
            trayek = extras.getString(TRAYEK)

            fcmPenerima = extras.getString(EXTRA_FCM_PENERIMA)

            tujuan_tunanetra = extras.getString(TEMPAT_TUJUAN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mBinding.textView7.text = Html.fromHtml(
                    getString(
                        R.string.popup_bantuan_datang,
                        trayek,
                        tujuan_tunanetra
                    ), Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                mBinding.textView7.text = Html.fromHtml(
                    getString(
                        R.string.popup_bantuan_datang,
                        trayek,
                        tujuan_tunanetra
                    )
                )
            }

            if (!extras.getString(PENOLAK)!!.equals("[]")) {
                penolak = Gson().fromJson(
                    extras.getString(PENOLAK),
                    object : TypeToken<ArrayList<String>>() {}.type
                )
            } else {
                penolak = ArrayList()
            }

            onstart_finish = true
            posisi_tunanetra = extras.getString(POSISI_TUNANETRA)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun connectToRoom() {
        val intent = Intent(this, CallNewActivity::class.java)

        socketRepository?.sendMessageToSocket(
            MessageModel(
                "store_user", preferences.getEmail(), "", null
            )
        )

        intent
            .putExtra(CallNewActivity.EXTRA_EMAIL_SAHABAT, preferences.getEmail())
            .putExtra(CallNewActivity.EXTRA_FCM_PENERIMA, fcmPenerima)

            .putExtra(CallNewActivity.EXTRA_POSISI_TUNANETRA, posisi_tunanetra)
            .putExtra(CallNewActivity.EXTRA_PENOLAK, penolak)
            .putExtra(CallNewActivity.EXTRA_EMAIL_TUNANETRA, email_tunanetra)
            .putExtra(CallNewActivity.EXTRA_TUJUAN_TUNANETRA, tujuan_tunanetra)
            .putExtra(CallNewActivity.EXTRA_TRAYEK, trayek)

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
            val response = PopUpRepository.getInstance()
                .saya_akan_datangi_langsung(fcmPenerima!!, preferences.getEmail()!!)
            if (response?.status == 200) {
                batalkan = false
                handler.removeCallbacks(runnable)
                Log.d(this.javaClass.simpleName, "Saya akan bantu sukses dikirim")
                val posisiTunanetra = Gson().fromJson(posisi_tunanetra, Location::class.java)

                //intent to google maps direction
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=${posisiTunanetra.latitude},${posisiTunanetra.longitude}")
                )
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
                fcmPenerima!!,
                preferences.getEmail()!!,
                preferences.getLongitude()!!.toDouble(),
                preferences.getLatitude()!!.toDouble()
            )
            if (response?.status == 200) {
                batalkan = false
                handler.removeCallbacks(runnable)
                Log.d(this.javaClass.simpleName, "Saya akan bantu sukses dikirim")
                val posisiTunanetra = Gson().fromJson(posisi_tunanetra, Location::class.java)

                //intent to google maps direction
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=${posisiTunanetra.latitude},${posisiTunanetra.longitude}")
                )
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
