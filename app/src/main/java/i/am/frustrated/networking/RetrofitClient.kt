package i.am.frustrated.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    private const val debug = false

    private val BASE_URL = if (debug) "https://bikitheman.pythonanywhere.com" else "https://isprivacyamyth.pythonanywhere.com"

    private fun retrofit(): Retrofit {

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
    }

    val dataService: DataService by lazy {
        retrofit().create(DataService::class.java)
    }
}