package hr.air_wheelly.ws.network
import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkService {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private fun getOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

    fun getRetrofitInstance(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun authService(context: Context): AuthenticationService {
        return getRetrofitInstance(context).create(AuthenticationService::class.java)
    }

    fun carService(context: Context): CarService {
        return getRetrofitInstance(context).create(CarService::class.java)
    }

    fun locationService(context: Context): LocationService {
        return getRetrofitInstance(context).create(LocationService::class.java)
    }

    fun paymentService(context: Context): PaymentService {
        return getRetrofitInstance(context).create(PaymentService::class.java)
    }
}