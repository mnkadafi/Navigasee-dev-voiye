package ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.popup

import ac.id.unikom.codelabs.navigasee.R
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pop_up_terima_poin.*

class PopUpTerimaPoin : AppCompatActivity() {
    companion object {
        const val JUMLAH_POIN = "JUMLAH_POIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_terima_poin)

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

        val extras = intent.extras
        if (extras != null) {
            val poin = extras.getInt(JUMLAH_POIN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView7.text = Html.fromHtml(getString(R.string.popup_terima_poin, poin.toString()), Html.FROM_HTML_MODE_COMPACT)
            } else {
                textView7.text = Html.fromHtml(getString(R.string.popup_terima_poin, poin.toString()))
            }
//            textView7.text = HtmlgetString(R.string.popup_terima_poin, poin.toString())

            btn_ok.setOnClickListener {
                finish()
            }

            btn_close.setOnClickListener {
                finish()
            }
        } else {
            finish()
        }
    }
}
