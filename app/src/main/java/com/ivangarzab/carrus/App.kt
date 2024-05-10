package com.ivangarzab.carrus

import android.app.Application
import com.ivangarzab.analytics.AnalyticsRepository
import com.ivangarzab.analytics.AnalyticsRepositoryImpl
import com.ivangarzab.analytics.BuildConfig
import com.ivangarzab.carrus.data.di.BuildVersionProvider
import com.ivangarzab.carrus.data.di.BuildVersionProviderImpl
import com.ivangarzab.carrus.data.di.DebugFlagProvider
import com.ivangarzab.carrus.data.di.DebugFlagProviderImpl
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepositoryImpl
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AlarmsRepositoryImpl
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepositoryImpl
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.repositories.CarRepositoryImpl
import com.ivangarzab.carrus.data.repositories.MessageQueueRepository
import com.ivangarzab.carrus.ui.compose.NavigationBottomBarViewModel
import com.ivangarzab.carrus.ui.create.CreateViewModel
import com.ivangarzab.carrus.ui.map.MapViewModel
import com.ivangarzab.carrus.ui.modals.ServiceModalViewModel
import com.ivangarzab.carrus.ui.overview.OverviewViewModel
import com.ivangarzab.carrus.ui.settings.SettingsViewModel
import com.ivangarzab.carrus.util.helpers.ContentResolverHelper
import com.ivangarzab.carrus.util.helpers.ContentResolverHelperImpl
import com.ivangarzab.carrus.util.managers.AlarmScheduler
import com.ivangarzab.carrus.util.managers.AlarmSchedulerImpl
import com.ivangarzab.carrus.util.managers.Analytics
import com.ivangarzab.carrus.util.managers.CarExporter
import com.ivangarzab.carrus.util.managers.CarImporter
import com.ivangarzab.carrus.util.managers.Coiler
import com.ivangarzab.carrus.util.managers.LeakUploader
import com.ivangarzab.carrus.util.managers.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

/**
 * Created by Ivan Garza Bermea.
 */
open class App : Application() {

    open val leakUploader: LeakUploader = LeakUploader()

    open val coiler: Coiler = Coiler()

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.v("Timber seed has been planted")
            setupLeakCanary()
        }
        setupCoil()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            modules(AppModule)
        }
    }

    open fun setupLeakCanary() {
        Timber.v("Setting up leak event listener")
        leakUploader.setupCrashlyticsLeakUploader()
    }

    open fun setupCoil() {
        Timber.v("Setting up Coil")
        coiler.setImageLoader(this)
    }
}

val AppModule = module {
    factory<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
    single {
        Preferences(get(), get())
    }
    single {
        Analytics(get())
    }
    single<AnalyticsRepository> {
        AnalyticsRepositoryImpl()
    }
    single<DebugFlagProvider> {
        DebugFlagProviderImpl()
    }
    single<BuildVersionProvider> {
        BuildVersionProviderImpl()
    }
    single<CarRepository> {
        CarRepositoryImpl(get(), get())
    }
    single {
        CarImporter()
    }
    single {
        CarExporter()
    }
    single<AlarmsRepository> {
        AlarmsRepositoryImpl(get(), get(), get())
    }
    single<AlarmScheduler> {
        AlarmSchedulerImpl(get(), get())
    }
    single<AlarmSettingsRepository> {
        AlarmSettingsRepositoryImpl(get(), get())
    }
    single {
        MessageQueueRepository()
    }
    single<AppSettingsRepository> {
        AppSettingsRepositoryImpl(get(), get())
    }
    single<ContentResolverHelper> {
        ContentResolverHelperImpl(get())
    }

    viewModel {
        CreateViewModel(get(), get(), get(), get(), get())
    }
    viewModel {
        OverviewViewModel(get(), get(), get(), get(), get(), get(), get())
    }
    viewModel {
        SettingsViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    viewModel {
        ServiceModalViewModel(get(), get())
    }
    viewModel {
        NavigationBottomBarViewModel(get())
    }
    viewModel {
        MapViewModel(get())
    }
}

