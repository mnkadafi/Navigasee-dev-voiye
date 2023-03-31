package ac.id.unikom.codelabs.navigasee.utilities.service.fcm

import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup.PopUpTerimaPoin
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup.bantuan_datang.PopUpBantuanDatang
import ac.id.unikom.codelabs.navigasee.utilities.*
import android.content.Context
import android.content.Intent

class SahabatLauncher {

    fun buildIntent(context: Context, data: Map<String, String>): Intent {
        val poin = data.get("poin")?.toInt()
        if (poin == null) {
            val intent = Intent(context, PopUpBantuanDatang::class.java)
            intent.putExtra(TRAYEK, data.get("angkot"))
            intent.putExtra(POSISI_TUNANETRA, data.get("posisiPemohon"))
            intent.putExtra(EMAIL_TUNANETRA, data.get("emailPemohon"))
            intent.putExtra(TEMPAT_TUJUAN, data.get("tujuan"))
            intent.putExtra(PENOLAK, data.get("penolak").toString())
            intent.putExtra(PopUpBantuanDatang.EXTRA_FCM_PENERIMA, data.get(FCM_TUNANETRA).toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        } else {
            val intent = Intent(context, PopUpTerimaPoin::class.java)
            intent.putExtra(PopUpTerimaPoin.JUMLAH_POIN, poin)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }
}