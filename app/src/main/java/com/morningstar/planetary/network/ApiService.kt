package com.morningstar.planetary.network

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.morningstar.planetary.models.*
import com.morningstar.planetary.network.interceptors.ConnectivityInterceptor
import com.morningstar.planetary.network.interceptors.ResponseInterceptor
import com.morningstar.planetary.utilities.ConstantsManager.Companion.BASE_URL
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("api/Search/AutoCompleteSearchText?buyorrent=0")
    fun getLocations(@Query("txt") typedText  : String): Deferred<ArrayList<String>>

    @GET("api/LifeStyle/getSchools")
    fun getSchoolNames(@Query("input") text : String, @Query("buyorrent") buyorrent : String) : Deferred<ArrayList<SchoolNameResponse>>

    @GET("api/LifeStyle/getSchoolBoundary")
    fun getSchoolNameBoundary(@Query("id") id : String, @Query("type") type : String) : Deferred<SchoolNameBoundaryResponse>

    @GET("https://www.planetrecrm.biz/PlanetREAPI/api/LifeStyle/getSchoolDistricts")
    fun getSchoolDistrict(@Query("input") input : String, @Query("buyorrent") buyorrent: String) : Deferred<ArrayList<SchoolDistrictResponse>>

    @GET("api/LifeStyle/getSchoolBoundary")
    fun getSchoolDistrictBoundary(@Query("id") id : String, @Query("type") type : String) : Deferred<SchoolDistrictBoundaryResponse>

    @GET("api/LifeStyle/getDefaultLifeStyleSearchCriteria")
    fun getDropDowns(): Deferred<MainModel>

    @Headers("Content-Type: application/json")
    @POST("api/LifeStyle/getSearchResults")
    fun getSearchResult(@Body searchText: String): Deferred<ArrayList<SearchPropertyResponse>>


    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor,
            responseInterceptor: ResponseInterceptor
        ): ApiService {


            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)

            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(responseInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}