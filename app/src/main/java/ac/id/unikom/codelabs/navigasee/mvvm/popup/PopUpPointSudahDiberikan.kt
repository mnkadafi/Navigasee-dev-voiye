package ac.id.unikom.codelabs.navigasee.mvvm.popup

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.mvvm.rute_yang_dilalui.RuteYangDilaluiActivity
import ac.id.unikom.codelabs.navigasee.utilities.TUNANETRA_STEP_LIHAT_RUTE_SEKARANG
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.popup_poin_sudah_diberikan.*

class PopUpPointSudahDiberikan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_poin_sudah_diberikan)
        val preferences = Preferences.getInstance()

        btn_lanjutkan_perjalanan.setOnClickListener {
            preferences.setStatusTunanetra(TUNANETRA_STEP_LIHAT_RUTE_SEKARANG)
            val intent = Intent(this, RuteYangDilaluiActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
}