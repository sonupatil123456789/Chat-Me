package com.example.smartjobreminder.core.rests//package com.novo.core.rests
//
//import com.novo.core.constants.ApiUrlConstants
//import kotlinx.serialization.json.Json
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import org.koin.dsl.module
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//
//val networkModule = module {
//
//    single {
//        Retrofit.Builder()
//            .baseUrl(ApiUrlConstants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//
//
//
//
//
//    single {
//        HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//    }
//
//    single {
//        OkHttpClient.Builder()
//            .addInterceptor(get<HttpLoggingInterceptor>())
//            .build()
//    }
//
//
//}
//
//
