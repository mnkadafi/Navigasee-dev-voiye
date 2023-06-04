package ac.id.unikom.codelabs.navigasee.utilities.service.fcm

import ac.id.unikom.codelabs.navigasee.mvvm.confirm_masuk_kendaraan.ConfirmMasukKendaraanActivitiy
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Context
import android.content.Intent
import android.location.Location

class TunanetraDidatangiLangsungLauncher {
    private val preferences = Preferences.getInstance()

    fun buildIntent(context: Context, data: Map<String, String>): Intent {
        val intent = Intent(context, ConfirmMasukKendaraanActivitiy::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val locationTunanetra = Location("")
        val locationYangBantu = Location("")

        locationTunanetra.latitude = preferences.getLatitude()!!.toDouble()
        locationTunanetra.longitude = preferences.getLongitude()!!.toDouble()

        locationYangBantu.longitude = data.get("long")!!.toDouble()
        locationYangBantu.latitude = data.get("lat")!!.toDouble()

        val poin = getPoin(locationTunanetra, locationYangBantu)
        preferences.setJumlahPoinYangDiberikan(poin)
        preferences.setJenisBantuan(Preferences.JENIS_BANTUAN_DIDATANGI_LANGSUNG)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return intent
    }

    //TODO : GANTI METODE CARA PEROLEH POIN
    private fun getPoin(locationTunanetra: Location, locationYangBantu: Location): Int {
        val jarak = locationTunanetra.distanceTo(locationYangBantu)
        val poin = jarak * 25 / 100

        return poin.toInt()
    }
}