package ac.id.unikom.codelabs.navigasee.mvvm.splash_screen

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.mvvm.confirm_masuk_kendaraan.ConfirmMasukKendaraanActivitiy
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.DashboardSupirSobatActivity
import ac.id.unikom.codelabs.navigasee.mvvm.login.LoginActivity
import ac.id.unikom.codelabs.navigasee.mvvm.my_location.MyLocationActivity
import ac.id.unikom.codelabs.navigasee.mvvm.popup.PopUpPerjalananSelesai
import ac.id.unikom.codelabs.navigasee.mvvm.rute_yang_dilalui.RuteYangDilaluiActivity
import ac.id.unikom.codelabs.navigasee.mvvm.sobat_waiting.SobatWaitingActivitiy
import ac.id.unikom.codelabs.navigasee.utilities.*
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val preferences = Preferences.getInstance()

        GlobalScope.launch(Dispatchers.IO) {
            delay(250)
            if (!preferences.getToken().isNullOrEmpty()) {
                if (preferences.getRole().equals(ROLE_TUNANETRA)) {
                    if (preferences.getWaiting().isNullOrEmpty()) {
                        startActivity(Intent(applicationContext, MyLocationActivity::class.java))
                    } else if (preferences.getStatusTunanetra().equals(TUNANETRA_STEP_TANYA_MASUK_KENDARAAN)) {
                        startActivity(Intent(applicationContext, ConfirmMasukKendaraanActivitiy::class.java))
                    } else if (preferences.getStatusTunanetra().equals(TUNANETRA_STEP_LIHAT_RUTE_SEKARANG)) {
                        startActivity(Intent(applicationContext, RuteYangDilaluiActivity::class.java))
                    } else if (preferences.getStatusTunanetra().equals(TUNANETRA_STEP_SUDAH_SAMPAI_TUJUAN)) {
                        startActivity(Intent(applicationContext, PopUpPerjalananSelesai::class.java))
                    } else {
                        startActivity(Intent(applicationContext, SobatWaitingActivitiy::class.java))
                    }
                } else if (preferences.getRole().equals(ROLE_SOPIR) || preferences.getRole().equals(ROLE_SAHABAT)) {
                    startActivity(Intent(applicationContext, DashboardSupirSobatActivity::class.java))
                }
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
            finish()
        }
    }
}
