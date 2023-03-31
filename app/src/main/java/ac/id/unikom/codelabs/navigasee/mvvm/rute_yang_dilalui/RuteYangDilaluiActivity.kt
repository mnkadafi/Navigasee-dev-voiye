package ac.id.unikom.codelabs.navigasee.mvvm.rute_yang_dilalui

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available.StepsItem
import ac.id.unikom.codelabs.navigasee.mvvm.popup.PopUpPerjalananSelesai
import ac.id.unikom.codelabs.navigasee.utilities.TUNANETRA_STEP_LIHAT_RUTE_SEKARANG
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_rute_yang_dilalui.*

class RuteYangDilaluiActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var adapter: RuteYangDilaluiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rute_yang_dilalui)
        setSupportActionBar(toolbar)

        preferences = Preferences.getInstance()

        preferences.setStatusTunanetra(TUNANETRA_STEP_LIHAT_RUTE_SEKARANG)

        val token = object : TypeToken<List<StepsItem?>?>() {}
        val step: List<StepsItem?>? =
            Gson().fromJson(preferences.getLangkahMenujuDestinasi(), token.type)

        // TODO: hapus tiap item dalam adapter ketika jarak sudah dekat dengan tiap item nya

        adapter = RuteYangDilaluiAdapter()
        rv_rute.adapter = adapter
        tv_berapa_menit.text = preferences.getMenitPerjalanan()
        tv_tujuan.text = preferences.getTempatTujuan()
        btn_akhiri_perjalanan.setOnClickListener {
            startActivity(Intent(this, PopUpPerjalananSelesai::class.java))
            finish()
        }

        if (!step.isNullOrEmpty())
            adapter.submitList(step)
        else
            finish()
    }

}


