package io.github.jwgibanez.contacts.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import io.github.jwgibanez.contacts.service.RqresService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class)
class ApiModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideOkHttpClientBuilder(interceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
        return builder
    }

    @Provides
    fun provideRetrofit(builder: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RqresService.API_HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }

    @Provides
    fun provideContactService(retrofit: Retrofit): RqresService {
        return retrofit.create(RqresService::class.java)
    }
}
