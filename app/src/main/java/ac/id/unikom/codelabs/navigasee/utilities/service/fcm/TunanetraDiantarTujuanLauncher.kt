package ac.id.unikom.codelabs.navigasee.utilities.service.fcm

import ac.id.unikom.codelabs.navigasee.mvvm.confirm_masuk_kendaraan.ConfirmMasukKendaraanActivitiy
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Context
import android.content.Intent

class TunanetraDiantarTujuanLauncher {
    private val preferences = Preferences.getInstance()

    fun buildIntent(context: Context): Intent {
        val intent = Intent(context, ConfirmMasukKendaraanActivitiy::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val poin = getPoin()
        preferences.setJumlahPoinYangDiberikan(poin)
        preferences.setJenisBantuan(Preferences.JENIS_BANTUAN_DIDATANGI_LANGSUNG)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return intent
    }

    //TODO : GANTI METODE CARA PEROLEH POIN
    private fun getPoin(): Int {
        val jarak = preferences.getJarakKeDestinasi()
        val poin = jarak!!.toFloat() * 1000 * 25 / 100

        return poin.toInt()
    }
}