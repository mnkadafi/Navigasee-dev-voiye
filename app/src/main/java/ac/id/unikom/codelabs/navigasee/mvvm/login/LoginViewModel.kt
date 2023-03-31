package ac.id.unikom.codelabs.navigasee.mvvm.login

import ac.id.unikom.codelabs.navigasee.data.source.LoginRepository
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseViewModel
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.launch

class LoginViewModel internal constructor(val mRepository: LoginRepository) : BaseViewModel() {

    var gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    lateinit var googleSignInClient: GoogleSignInClient
    var loginSuccess = MutableLiveData<Event<Boolean>>()
    var preferences: Preferences? = null

    fun login(account: GoogleSignInAccount, role: String) {
        isRequesting.value = Event(true)

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.w("FirebaseInstanceId", "getInstanceId failed", it.exception)
                isRequesting.value = Event(false)
                showMessage.value = Event("Oops something went wrong...")
            } else {
                // Get new Instance ID token
                val fcm_token = it.result?.token

                Log.d("FirebaseInstanceId", "token : " + fcm_token)

                viewModelScope.launch {
//                    Log.wtf("mau login", "role = ${role}, account = ${account.displayName}, longitude = $longitude, latitude = $latitude, fcm_token = $fcm_token")
                    val login = mRepository.login(role, account, fcm_token!!)
                    isRequesting.value = Event(false)
                    if (login != null) {
                        preferences!!.saveLogin(account.displayName!!, account.photoUrl.toString(), login.token!!, account.email!!, role)
                        loginSuccess.value = Event(true)
                    }
                }
            }
        }
    }

}
