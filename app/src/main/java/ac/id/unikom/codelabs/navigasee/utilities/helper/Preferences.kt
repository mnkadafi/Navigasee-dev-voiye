package ac.id.unikom.codelabs.navigasee.utilities.helper

import ac.id.unikom.codelabs.navigasee.MyApplication
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup.bantuan_datang.PopUpBantuanDatang
import ac.id.unikom.codelabs.navigasee.utilities.*
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class Preferences {

    private val context = MyApplication.getContext()
    private var sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = sharedPreferences.edit()

    fun getEmail(): String? {
        return sharedPreferences.getString(EMAIL, "")
    }

    fun getRole(): String? {
        return sharedPreferences.getString(ROLE, "")
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, "")
    }

    fun getLatitude(): String? {
        return sharedPreferences.getString(LATITUDE, "")
    }

    fun getLongitude(): String? {
        return sharedPreferences.getString(LONGITUDE, "")
    }

    fun getCurrentPlace(): String? {
        return sharedPreferences.getString(CURRENT_PLACE, "")
    }

    fun getTokenVc(): String? {
        return sharedPreferences.getString(TOKEN_VC, "")
    }

    fun setTokenVc(token_vc: String?) {
        editor.putString(TOKEN_VC, token_vc)
        editor.apply()
    }

    fun getJumlahPoinYangDiberikan(): Int {
        return sharedPreferences.getInt(JUMLAH_POIN_YANG_DIBERIKAN, 0)
    }

    fun getTempatTujuan(): String? {
        return sharedPreferences.getString(TEMPAT_TUJUAN, "")
    }

    fun setTempatTujuan(tempat_tujuan: String?) {
        editor.putString(TEMPAT_TUJUAN, tempat_tujuan)
        editor.apply()
    }


    fun getMenitPerjalanan(): String? {
        return sharedPreferences.getString(MENIT_PERJALANAN, "")
    }

    fun setMenitPerjalanan(menit_perjalanan: String?) {
        editor.putString(MENIT_PERJALANAN, menit_perjalanan)
        editor.apply()
    }

    fun setJumlahPoinYangDiberikan(poin: Int) {
        editor.putInt(JUMLAH_POIN_YANG_DIBERIKAN, poin)
        editor.apply()
    }

    fun setStatusTunanetra(posisi: String?) {
        editor.putString(STATUS_TUNANETRA, posisi)
        editor.apply()
    }

    fun getLangkahMenujuDestinasi(): String? {
        return sharedPreferences.getString(LANGKAH_MENUJU_DESTINASI, "")
    }

    fun setLangkahMenujuDestinasi(langkah_json: String) {
        editor.putString(LANGKAH_MENUJU_DESTINASI, langkah_json)
        editor.apply()
    }

    fun getStatusTunanetra(): String? {
        return sharedPreferences.getString(STATUS_TUNANETRA, "")
    }

//    fun getRoomUrl() = sharedPreferences.getString(PopUpBantuanDatang.KEY_PREF_ROOM_SERVER_URL,
//            context.getString(org.appspot.apprtc.R.string.pref_room_server_url_default))

//    fun getResolution() = sharedPreferences.getString(PopUpBantuanDatang.KEY_PREF_RESOLUTION,
//            context.getString(org.appspot.apprtc.R.string.pref_resolution_default))
//
//    fun getFps() = sharedPreferences.getString(PopUpBantuanDatang.KEY_PREF_FPS,
//            context.getString(org.appspot.apprtc.R.string.pref_fps_default))
//
//    fun getVideoBitrateType() = sharedPreferences.getString(PopUpBantuanDatang.KEY_PREF_VIDEO_BITRATE_TYPE,
//            context.getString(org.appspot.apprtc.R.string.pref_maxvideobitrate_default))
//
//    fun getVideoBitrateValue() = sharedPreferences.getString(PopUpBantuanDatang.KEY_PREF_VIDEO_BITRATE_VALUE,
//            context.getString(org.appspot.apprtc.R.string.pref_maxvideobitratevalue_default))
//
//    fun getAudioBitrateType() = sharedPreferences.getString(PopUpBantuanDatang.KEY_PREF_AUDIO_BITRATE_TYPE,
//            context.getString(org.appspot.apprtc.R.string.pref_startaudiobitrate_default))
//
//    fun getAudioBitrateValue() = sharedPreferences.getString(PopUpBantuanDatang.KEY_PREF_AUDIO_BITRATE_VALUE,
//            context.getString(org.appspot.apprtc.R.string.pref_startaudiobitratevalue_default))

    fun setWaiting(body: String) {
        editor.putString(WAITING_BODY, body)
        editor.apply()
    }

    fun getWaiting(): String? {
        return sharedPreferences.getString(WAITING_BODY, "")
    }

    fun saveLocation(longitude: String?, latitude: String?, placeName: String?) {
        editor.putString(LATITUDE, latitude)
        editor.putString(LONGITUDE, longitude)
        editor.putString(CURRENT_PLACE, placeName)
        editor.apply()
    }

    fun setToken(token: String) {
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun saveLogin(
            name: String,
            foto_profil: String,
            token: String,
            email: String,
            role: String
    ) {
        editor.putString(NAME, name)
        editor.putString(TOKEN, token)
        editor.putString(FOTO_PROFIL, foto_profil)
        editor.putString(EMAIL, email)
        editor.putString(ROLE, role)

        editor.apply()
    }

    fun getEmailPemberiBantuan(): String? = sharedPreferences.getString(EMAIL_PEMBERI_BANTUAN, "")

    fun setEmailPemberiBantuan(email: String?) {
        editor.putString(EMAIL_PEMBERI_BANTUAN, email)
        editor.apply()
    }

    fun setLatTujuan(lat: String?) {
        editor.putString(LATITUDE_TUJUAN, lat)
        editor.apply()
    }

    fun setLongTujuan(long: String?) {
        editor.putString(LONGITUDE_TUJUAN, long)
        editor.apply()
    }

    fun getLatTujuan(): String? = sharedPreferences.getString(LATITUDE_TUJUAN, "")

    fun getLongTujuan(): String? = sharedPreferences.getString(LONGITUDE_TUJUAN, "")

    fun getStringWithId(attributeId: Int, defaultId: Int): String? {
        val defaultValue = context.getString(defaultId)
        val attributeName = context.getString(attributeId)
        return sharedPreferences.getString(attributeName, defaultValue)
    }

    fun getBooleanWithId(attributeId: Int, defaultId: Int): Boolean {
        val defaultValue = context.getString(defaultId)
        val attributeName = context.getString(attributeId)
        return sharedPreferences.getBoolean(attributeName, defaultValue.toBoolean())
    }

    fun getIntegerWithId(attributeId: Int, defaultId: Int): Int {
        val defaultString = context.getString(defaultId)
        val defaultValue = defaultString.toInt()
        val attributeName = context.getString(attributeId)
        val value = sharedPreferences.getString(attributeName, defaultString)
        return try {
            value!!.toInt()
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getJarakKeDestinasi(): String? = sharedPreferences.getString(JARAK_KE_DESTINASI, "")
    fun setJarakKeDestinasi(jarak: String) {
        editor.putString(JARAK_KE_DESTINASI, jarak)
        editor.apply()
    }

    fun getAngkutanPertama(): String? = sharedPreferences.getString(ANGKUTAN_PERTAMA, "")
    fun setAngkutanPertama(angkutan_pertama: String) {
        editor.putString(ANGKUTAN_PERTAMA, angkutan_pertama)
        editor.apply()
    }

    fun getTujuan(): String? = sharedPreferences.getString(TUJUAN, "")
    fun setTujuan(tujuan: String) {
        editor.putString(TUJUAN, tujuan)
        editor.apply()
    }

    fun getJenisBantuan(): String? = sharedPreferences.getString(JENIS_BANTUAN, "")
    fun setJenisBantuan(bantuan: String) {
        editor.putString(JENIS_BANTUAN, bantuan)
        editor.apply()
    }

    companion object {
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val ROLE = "role"
        private const val FOTO_PROFIL = "foto"
        private const val TOKEN = "token"
        private const val WAITING_BODY = "waiting_body"
        private const val STATUS_TUNANETRA = "status_tunanetra" //posisi_pemohon
        private const val TOKEN_VC = "token_vc"
        private const val JUMLAH_POIN_YANG_DIBERIKAN = "JumlahPoinYangDiberikan"
        private const val LANGKAH_MENUJU_DESTINASI = "LANGKAH_MENUJU_DESTINASI"
        private const val EMAIL_PEMBERI_BANTUAN = "EMAIL_PEMBERI_BANTUAN"
        private const val LONGITUDE_TUJUAN = "LONGITUDE_TUJUAN"
        private const val LATITUDE_TUJUAN = "LATITUDE_TUJUAN"
        private const val JARAK_KE_DESTINASI = "JARAK_KE_DESTINASI"
        private const val ANGKUTAN_PERTAMA = "ANGKUTAN_PERTAMA"
        private const val TUJUAN = "TUJUAN"
        private const val JENIS_BANTUAN = "JENIS_BANTUAN"

        const val JENIS_BANTUAN_DIDATANGI_LANGSUNG = "JENIS_BANTUAN_DIDATANGI_LANGSUNG"
        const val JENIS_BANTUAN_DIANTAR_TUJUAN = "JENIS_BANTUAN_DIANTAR_TUJUAN"
        const val JENIS_BANTUAN_VIDEO_CALL = "JENIS_BANTUAN_VIDEO_CALL"


        // For Singleton instantiation
        private var instance: Preferences? = null

        fun getInstance() = instance
            ?: Preferences().also { instance = it }
    }

}