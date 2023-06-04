package ac.id.unikom.codelabs.navigasee.data.source.remote

import ac.id.unikom.codelabs.navigasee.BuildConfig
import ac.id.unikom.codelabs.navigasee.MyApplication
import ac.id.unikom.codelabs.navigasee.data.source.remote.dashboard.DashboardApiService
import ac.id.unikom.codelabs.navigasee.data.source.remote.list_transportation_available.ListTransportationAvailableApiService
import ac.id.unikom.codelabs.navigasee.data.source.remote.login.LoginApiService
import ac.id.unikom.codelabs.navigasee.data.source.remote.popup.PopUpApiService
import ac.id.unikom.codelabs.navigasee.data.source.remote.search.SearchApiService
import ac.id.unikom.codelabs.navigasee.data.source.remote.update_status.UpdateStatusApiService
import ac.id.unikom.codelabs.navigasee.data.source.remote.waiting.WaitingApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface ApiService {
    companion object Factory {
        private val getApiService: Retrofit by lazy {
            val mLoggingInterceptor = HttpLoggingInterceptor()
            mLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val cacheSize = (5 * 1024 * 1024).toLong()
            val appCache = Cache(MyApplication.getContext().cacheDir, cacheSize)
            val mClient = if (BuildConfig.DEBUG) {
                OkHttpClient.Builder()
                        .cache(appCache)
                        .addInterceptor { chain ->
                            val request = chain.request().apply {
                                newBuilder().header("Cache-Control",
                                        "public, max-age=" + 5).build()
                            }
                            chain.proceed(request)
                        }
                        .addInterceptor(mLoggingInterceptor)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .build()
            } else {
                OkHttpClient.Builder()
                        .cache(appCache)
                        .addInterceptor { chain ->
                            val request = chain.request().apply {
                                newBuilder().header("Cache-Control",
                                        "public, max-age=" + 5).build()
                            }
                            chain.proceed(request)
                        }
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .build()
            }

            val mRetrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(mClient)
                    .build()

            return@lazy mRetrofit
        }

        val dashboardApiService: DashboardApiService = getApiService.create(DashboardApiService::class.java)
        val loginApiService: LoginApiService = getApiService.create(LoginApiService::class.java)
        val updateStatusApiService: UpdateStatusApiService = getApiService.create(UpdateStatusApiService::class.java)
        val listTransportationAvailableApiService: ListTransportationAvailableApiService = getApiService.create(ListTransportationAvailableApiService::class.java)
        val searchApiService: SearchApiService = getApiService.create(SearchApiService::class.java)
        val waitingApiService: WaitingApiService = getApiService.create(WaitingApiService::class.java)
        val popupApiService: PopUpApiService = getApiService.create(PopUpApiService::class.java)
    }
}
