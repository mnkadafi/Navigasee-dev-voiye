package ac.id.unikom.codelabs.navigasee.utilities.helper

import androidx.appcompat.app.AppCompatActivity

class CloseActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        finish()
    }
}