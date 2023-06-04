package ac.id.unikom.codelabs.navigasee.mvvm.confirm_masuk_kendaraan

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.mvvm.popup.PopUpPointSudahDiberikan
import ac.id.unikom.codelabs.navigasee.mvvm.sobat_waiting.SobatWaitingActivitiy
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_confirm_masuk_kendaraan.*

class ConfirmMasukKendaraanActivitiy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_masuk_kendaraan)

        btn_sudah.setOnClickListener {
            val preferences = Preferences.getInstance()
            val intent = Intent(this, PopUpPointSudahDiberikan::class.java)
            startActivity(intent)
            // TODO: Bedakan ketika yang memberi bantuan diantar tujuan maka poin diberikan di akhir
//            if (preferences.getJenisBantuan() == Preferences.JENIS_BANTUAN_VIDEO_CALL || preferences.getJenisBantuan() == Preferences.JENIS_BANTUAN_DIDATANGI_LANGSUNG) {
//            }
//            else if (preferences.getJenisBantuan() == Preferences.JENIS_BANTUAN_DIANTAR_TUJUAN) {
//                val intent = Intent(this, RuteYangDilaluiActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//                finish()
//            }
        }

        btn_belum.setOnClickListener {
            startActivity(Intent(this, SobatWaitingActivitiy::class.java))
            finish()
        }
    }
}
