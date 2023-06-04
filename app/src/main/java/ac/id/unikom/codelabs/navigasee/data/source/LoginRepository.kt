package ac.id.unikom.codelabs.navigasee.data.source

import ac.id.unikom.codelabs.navigasee.data.model.login.LoginResponse
import ac.id.unikom.codelabs.navigasee.data.model.login.User
import ac.id.unikom.codelabs.navigasee.data.source.remote.ApiService
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 * Repository module for handling data operations.
 */
class LoginRepository : BaseRepository() {

    private val TAG by lazy { LoginRepository::class.java.simpleName }

    suspend fun login(role: String, account: GoogleSignInAccount, fcm_token: String): LoginResponse? {
        try {
            val response = ApiService.loginApiService.login(
                    User(account.email!!, account.displayName!!, role, account.photoUrl.toString(), fcm_token)
            )
            if (response.token != null) {
                return response
            } else {
                return null
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }
    }

    suspend fun refreshFcm(email: String, fcm_token: String): BaseApiResponse<*>? {
        if (token != null) {
            try {
                val response = ApiService.loginApiService.updateFcm(token, email, fcm_token)
                return response
            } catch (e: Throwable) {
                e.printStackTrace()
                return null
            }
        } else {
            return null
        }
    }

    companion object {
        // For Singleton instantiation
        private var instance: LoginRepository? = null

        fun getInstance() = instance ?: LoginRepository().also { instance = it }
    }
}
