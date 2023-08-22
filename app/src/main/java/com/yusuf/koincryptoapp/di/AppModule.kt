package com.yusuf.koincryptoapp.di

import com.yusuf.koincryptoapp.repository.CryptoRepository
import com.yusuf.koincryptoapp.repository.CryptoRepositoryImpl
import com.yusuf.koincryptoapp.service.CryptoAPI
import com.yusuf.koincryptoapp.util.API.BASE_URL
import com.yusuf.koincryptoapp.viewmodel.CryptoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    //creates a singleton
    single {
            Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)
    }

    single<CryptoRepository> {
        CryptoRepositoryImpl(get())
    }

    viewModel {
        CryptoViewModel(get())
    }

    //creates a factory, everytime we inject a new instance is created.
    factory {

    }
}