package com.example.quotesapp.di
import com.example.quotesapp.domain.api.ApiService
import com.example.quotesapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    //Retrofit Builder Class using base url and converter factory



    @Provides
    @Singleton
    fun retrofitInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("x-api-key", "Fm5XGDOb3h3sT2vZplVC8ZrJEYnguLf3MXKHDuiNELgFqjrAttgEoqgrnCoDv7bk1eKWQGoId1lTf2O7LrPrg50NDuuG3m29QY4BR8oIWjjr3QLL9oTUDoRB63Glrg4u")
                return@Interceptor chain.proceed(builder.build())
            }
        )
    }.build()
    )
        .build()

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}