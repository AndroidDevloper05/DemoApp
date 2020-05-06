package  com.demo.api


import com.demo.BuildConfig
import com.demo.DemoApplication
import com.demo.R
import com.demo.model.UsersResponse
import com.demo.util.Utility
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * This class for RestClient api interface
 */
interface ApiInterface {

    //Get Data
    @GET("blogs?")
    fun getResponse(@Query("limit") limit:Int,
                    @Query("page")currentPage:Int): Call<MutableList<UsersResponse>>
}
@Suppress("UNCHECKED_CAST")
class RestClient {
    companion object {
        private const val DURATION = 100 // Seconds
        var apiInterface: ApiInterface? = null
        var objectCall:Call<Any>?=null
        fun getInstance(): ApiInterface {
            if (apiInterface == null) {
                setupRestClient()
            }
            return apiInterface as ApiInterface
        }
        private fun setupRestClient() {
            val builderOkHttp = OkHttpClient.Builder()
            builderOkHttp.connectTimeout(DURATION.toLong(), TimeUnit.SECONDS)
            builderOkHttp.readTimeout(DURATION.toLong(), TimeUnit.SECONDS)
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builderOkHttp.networkInterceptors().add(httpLoggingInterceptor)
            builderOkHttp.networkInterceptors().add(Interceptor { chain ->
                val request = chain.request().newBuilder().build()
                chain.proceed(request)
            })
            val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .connectTimeout(DURATION.toLong(), TimeUnit.SECONDS)
                .readTimeout(DURATION.toLong(), TimeUnit.SECONDS).build()
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()
            apiInterface = Retrofit.Builder().client(client).baseUrl(BuildConfig.DOMAIN_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
                .create(ApiInterface::class.java)
        }

        @Suppress("ConstantConditionIf")
        fun makeApiRequest(
            call: Any,
            reqCode: Int,
            apiResponseListener: ApiResponseListener
        ) {
            val activity=DemoApplication.instance
            try {
                if (Utility.hasInternet(DemoApplication.instance)) {
                    objectCall = call as Call<Any>
                    objectCall?.enqueue(object : Callback<Any> {
                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            when (t) {
                                is ConnectException, is SocketTimeoutException -> //Displaying no network connection error
                                    setError(false,
                                        activity.getString(R.string.time_out_error),reqCode,apiResponseListener
                                    )
                                else ->
                                    setError(false,
                                        t.toString(),reqCode,apiResponseListener
                                    )
                            }
                        }

                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful&&response.body()!=null)
                                apiResponseListener.onApiResponse(response.body()!!, reqCode)
                            else {
                                setError(false,
                                    activity.getString(R.string.server_error),reqCode,apiResponseListener
                                )

                            }
                        }
                    })
                } else {
                    setError(true,
                        activity.getString(R.string.net_message),reqCode,apiResponseListener
                    )
                }
            } catch (e: Exception) {
                setError(false,
                    activity.getString(R.string.server_error),reqCode,apiResponseListener

                )
                e.printStackTrace()
            }
        }

        private fun setError(isNetWork:Boolean,
                             message: String,reqCode: Int,onApiResponseListener: ApiResponseListener

        ) {
            try {
                if(isNetWork)
                    onApiResponseListener.onApiNetwork(message,reqCode)
                else {
                    onApiResponseListener.onApiError(message, reqCode)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                onApiResponseListener.onApiError(e.message!!, reqCode)
            }
        }
        fun getApiClient(): ApiInterface? {
            return apiInterface
        }

        fun cancelApiCall()
        {
            objectCall?.cancel()
        }
    }

}

