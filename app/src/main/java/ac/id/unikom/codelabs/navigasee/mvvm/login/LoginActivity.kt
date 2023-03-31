package ac.id.unikom.codelabs.navigasee.mvvm.login

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.source.LoginRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivityLoginBinding
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.DashboardSupirSobatActivity
import ac.id.unikom.codelabs.navigasee.mvvm.my_location.MyLocationActivity
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.EventObserver
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import pub.devrel.easypermissions.EasyPermissions

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(R.layout.activity_login), LoginUserActionListener {

    private val TAG by lazy { LoginActivity::class.java.simpleName }
    private lateinit var role: String

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GOOGLE_SIGN_IN_RESULT_CODE -> {
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                        val account = task.getResult(ApiException::class.java)
                        //dari account harus nge post email, nama, role, foto, api_token
                        if (account != null) {
                            viewModel.login(account, role)
                        }
                    } catch (e: ApiException) {
                        Log.wtf(TAG, "signInResult:failed code=${e.statusCode}")
                    }
                }
            }
        }
    }

    override fun login(role: String) {
        this.role = role
        val i = viewModel.googleSignInClient.signInIntent
        startActivityForResult(i, GOOGLE_SIGN_IN_RESULT_CODE)
    }

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(LoginRepository.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        mParentVM = viewModel
        viewModel.preferences = Preferences.getInstance()

        if (!EasyPermissions.hasPermissions(this, *BegPermissionModal.PERMISSIONS_MANDATORY)) {
            val beg_permission_intent = Intent(this, BegPermissionModal::class.java)
            startActivity(beg_permission_intent)
        }

    }

    override fun onCreateObserver(viewModel: LoginViewModel) {
        viewModel.apply {
            loginSuccess.observe(this@LoginActivity, EventObserver {
                if (it) {
                    if (role.equals("Tunanetra")) {
                        val i = Intent(this@LoginActivity, MyLocationActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                    } else if (role.equals("Supir") || role.equals("Sahabat")) {
                        val i = Intent(this@LoginActivity, DashboardSupirSobatActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                    }
                }
            })
        }
    }

    override fun setContentData() {
        viewModel.googleSignInClient = GoogleSignIn.getClient(this, viewModel.gso)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun setMessageType(): String = MESSAGE_TYPE_TOAST

    companion object {
        private const val GOOGLE_SIGN_IN_RESULT_CODE = 101
    }

}
