package ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.pilih_hadiah

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.utilities.extensions.showToast
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PilihHadiahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_hadiah)
        supportActionBar!!.title = "Pilih Hadiah"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun onClick() {
        showToast("Coming soon!", Toast.LENGTH_LONG)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
