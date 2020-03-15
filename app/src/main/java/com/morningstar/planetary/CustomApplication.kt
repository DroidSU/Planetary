/*
 * Created by Sujoy Datta. Copyright (c) 2020. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.planetary

import android.app.Application
import com.morningstar.planetary.network.ApiService
import com.morningstar.planetary.network.interceptors.ConnectivityInterceptor
import com.morningstar.planetary.network.interceptors.ConnectivityInterceptorImpl
import com.morningstar.planetary.network.interceptors.ResponseInterceptor
import com.morningstar.planetary.network.interceptors.ResponseInterceptorImpl
import com.morningstar.planetary.repositories.MainRepository
import com.morningstar.planetary.repositories.MainRepositoryImpl
import com.morningstar.planetary.viewmodels.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CustomApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@CustomApplication))

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind<ResponseInterceptor>() with singleton { ResponseInterceptorImpl() }

        bind() from singleton { ApiService(instance(), instance()) }

        bind<MainRepository>() with singleton { MainRepositoryImpl(instance()) }

        bind() from provider {
            MainViewModelFactory(
                instance()
            )
        }
    }
}