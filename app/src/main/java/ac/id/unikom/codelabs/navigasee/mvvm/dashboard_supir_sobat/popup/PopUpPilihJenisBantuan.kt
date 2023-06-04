package ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup.bantuan_datang.PopUpBantuanDatang.Companion.RESULT_BANTUAN
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup.bantuan_datang.PopUpBantuanDatang.Companion.RESULT_BANTUAN_SAMPAI_KENDARAAN
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup.bantuan_datang.PopUpBantuanDatang.Companion.RESULT_BANTUAN_SAMPAI_TUJUAN
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pop_up_pilih_jenis_bantuan.*

class PopUpPilihJenisBantuan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_pilih_jenis_bantuan)
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

        btn_sampai_tujuan.setOnClickListener {
            val i = Intent()
            i.putExtra(RESULT_BANTUAN, RESULT_BANTUAN_SAMPAI_TUJUAN)
            setResult(RESULT_OK, i)
            finish()
        }

        btn_sampai_kendaraan.setOnClickListener {
            val i = Intent()
            i.putExtra(RESULT_BANTUAN, RESULT_BANTUAN_SAMPAI_KENDARAAN)
            setResult(RESULT_OK, i)
            finish()
        }


    }

}
