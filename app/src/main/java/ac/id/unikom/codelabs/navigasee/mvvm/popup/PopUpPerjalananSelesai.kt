package ac.id.unikom.codelabs.navigasee.mvvm.popup

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.source.PopUpRepository
import ac.id.unikom.codelabs.navigasee.utilities.TUNANETRA_STEP_SUDAH_SAMPAI_TUJUAN
import ac.id.unikom.codelabs.navigasee.utilities.extensions.showToast
import ac.id.unikom.codelabs.navigasee.utilities.helper.CloseActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.popup_perjalanan_end.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PopUpPerjalananSelesai : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_perjalanan_end)

        preferences = Preferences.getInstance()
        preferences.setStatusTunanetra(TUNANETRA_STEP_SUDAH_SAMPAI_TUJUAN)
        progressDialog = ProgressDialog(this)

        btn_akhiri_sesi.setOnClickListener {
            progressDialog.setTitle("")
            progressDialog.setMessage(getString(R.string.wait_label))
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.setCancelable(false)

            progressDialog.show()
            GlobalScope.launch(Dispatchers.Main) {
                val repo = PopUpRepository.getInstance()

                val emailPemberiBantuan = preferences.getEmailPemberiBantuan()
                val poin = preferences.getJumlahPoinYangDiberikan()

                val response = repo.akhiri_sesi(emailPemberiBantuan!!, poin)
                println(response)
                if (response!!.status == 200) {
                    //bersih bersih
                    preferences.setEmailPemberiBantuan("")
                    preferences.setStatusTunanetra("")
                    preferences.setLangkahMenujuDestinasi("")
                    preferences.setJumlahPoinYangDiberikan(0)
                    preferences.setMenitPerjalanan("")
                    preferences.setTempatTujuan("")
                    preferences.setTokenVc("")
                    preferences.setWaiting("")
                    preferences.setLatTujuan("")
                    preferences.setLongTujuan("")
                    preferences.setTujuan("")
                    preferences.setAngkutanPertama("")
                    preferences.setJenisBantuan("")
                    progressDialog.hide()

                    showToast(R.string.selamat_berkegiatan)

                    delay(700)

                } else {
                    showToast(R.string.gagal_mengakhiri_sesi)
                    progressDialog.hide()
                }

                val intent = Intent(this@PopUpPerjalananSelesai, CloseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

        }
    }
}