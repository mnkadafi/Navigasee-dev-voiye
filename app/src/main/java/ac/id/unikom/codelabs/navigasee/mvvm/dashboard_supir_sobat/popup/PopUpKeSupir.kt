package ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.model.dashboard.Location
import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingBody
import ac.id.unikom.codelabs.navigasee.data.source.PopUpRepository
import ac.id.unikom.codelabs.navigasee.data.source.WaitingRepository
import ac.id.unikom.codelabs.navigasee.utilities.*
import ac.id.unikom.codelabs.navigasee.utilities.extensions.showToast
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_pop_up_ke_supir.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PopUpKeSupir : AppCompatActivity() {
    private var batalkan = true
    private var tempat_tujuan: String? = null
    private var email_tunanetra: String? = null
    private var tujuan_tunanetra: String? = null
    private var posisi_tunanetra: String? = null
    private var penolak: ArrayList<String>? = null
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_ke_supir)

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

    override fun onResume() {
        super.onResume()
        val extras = intent.extras
        if (extras != null) {
            tempat_tujuan = extras.getString(TEMPAT_TUJUAN)
            email_tunanetra = extras.getString(EMAIL_TUNANETRA)
            tujuan_tunanetra = extras.getString(TEMPAT_TUJUAN)
            val fcmPenerima = extras.getString(FCM_TUNANETRA)

            if (!extras.getString(PENOLAK)!!.equals("[]")) {
                penolak = Gson().fromJson(extras.getString(PENOLAK), object : TypeToken<ArrayList<String>>() {}.type)
            } else {
                penolak = ArrayList()
            }

            posisi_tunanetra = extras.getString(POSISI_TUNANETRA)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textViewTrayek.text = Html.fromHtml(getString(R.string.popup_ke_supir, tempat_tujuan), Html.FROM_HTML_MODE_COMPACT)
            } else {
                textViewTrayek.text = Html.fromHtml(getString(R.string.popup_ke_supir, tempat_tujuan))
            }

            btn_close.setOnClickListener {
                finish()
            }
            btn_saya_akan_bantu.setOnClickListener {
                val preferences = Preferences.getInstance()
                val posisiTunanetra = Gson().fromJson(posisi_tunanetra, Location::class.java)
                val email_penerima = preferences.getEmail()

                //tell the backend i will help
                batalkan = false

                GlobalScope.launch {
                    val response = PopUpRepository.getInstance().saya_akan_datangi_langsung(fcmPenerima!!, email_penerima!!)
                    if (response?.status == 200) {
                        Log.d(this.javaClass.simpleName, "Saya akan bantu sukses dikirim")
                    } else {
                        Log.d(this.javaClass.simpleName, "Saya akan bantu gagal terkirim")
                    }
                }

                //intent to google maps direction
                val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=${posisiTunanetra.latitude},${posisiTunanetra.longitude}"))
                startActivity(intent)

                finish()
            }

            runnable = Runnable {
                showToast(R.string.anda_tidak_memberikan_jawaban_selama_15_detik)
                finish()
            }

            handler = Handler().apply {
                postDelayed(runnable, 15000)
            }


        } else {
            Log.e("PopUpKeSupir", "extras is null")
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (batalkan) {
            val preferences = Preferences.getInstance()
            val posisiTunanetra = Gson().fromJson(posisi_tunanetra, Location::class.java)

            preferences.getEmail()?.let { penolak?.add(it) }

            val body = WaitingBody(
                    email_tunanetra!!,
                    tujuan_tunanetra!!,
                    tempat_tujuan!!,
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
        handler.removeCallbacks(runnable)

    }
}
