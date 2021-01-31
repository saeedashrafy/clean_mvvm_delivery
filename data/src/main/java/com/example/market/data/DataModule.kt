package com.example.market.data


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import com.example.market.data.app.AppPrefs
import com.example.market.data.app.STORE_SETTINGS
import com.example.market.data.local.MarketDao
import com.example.market.data.local.MarketDatabase
import com.example.market.data.mapper.*
import com.example.market.data.mapper.AuthResponseToAuthDomain
import com.example.market.data.mapper.CategoryProductResponseToDomain
import com.example.market.data.mapper.ConfirmDomainToConfirmBody
import com.example.market.data.mapper.ConfirmResponseToConfirmDomain
import com.example.market.data.remote.Api
import com.example.market.data.remote.MyCallAdapterFactory
import com.example.market.data.repositoryImpl.AuthRepositoryImpl
import com.example.market.data.repositoryImpl.FoodRepositoryImpl
import com.example.market.domain.repository.*


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext


import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.time.ExperimentalTime


private const val API_URL = "https://api.someShitApi.com/"

@ExperimentalTime
@ExperimentalCoroutinesApi
@FlowPreview
val dataModule = module {


    single { provideDatabase(androidApplication()) }
    single { provideMarketDao(get()) }

    single { provideApiService(get()) }


    single {
        provideRetrofit(
                baseUrl = API_URL,
                moshi = get(),
                client = get()
        )
    }

    single { provideMoshi() }

    single { provideOkHttpClient() }

    factory { ProductResponseToProductDomain() }
    factory { ConfirmDomainToConfirmBody() }
    factory { CategoryProductResponseToDomain() }
    factory { ConfirmResponseToConfirmDomain() }
    factory { AuthResponseToAuthDomain() }
    factory { FoodEntityToCartFoodDomain() }
    factory { RegisterDomainToBody() }
    factory { AppSpecificationDomainToBody() }
    factory { AppInitResponseToDomain() }
    factory { OrderResponseToDomain() }
    factory { CartDomainToBody() }
    //preferences
    single { provideDataStore(androidContext()) }
    single { AppPrefs(get()) }

    single<FoodRepository> {
        FoodRepositoryImpl(
                apiService = get(),
                dao = get(),
                dispatchers = get(),
                entityToDomain = get<FoodEntityToCartFoodDomain>(),
                responseToDomain = get<CategoryProductResponseToDomain>(),
                orderResponseToDomain = get<OrderResponseToDomain>(),
                cartFoodDomainToBody = get<CartDomainToBody>()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
                api = get(),
                dispatchers = get(),
                registerDomainToBody = get<RegisterDomainToBody>(),
                authResponseToAuthDomain = get<AuthResponseToAuthDomain>(),
                appInitResponseToDomain = get<AppInitResponseToDomain>(),
                confirmResponseToTokenDomain = get<ConfirmResponseToConfirmDomain>(),
                appSpecificationDomainToBody = get<AppSpecificationDomainToBody>(),
                appPrefs = get()
        )
    }



    single<SharedPreferences> { provideSessionPreferences(androidApplication()) }

}

fun provideDataStore(androidContext: Context): DataStore<Preferences> =
        androidContext.createDataStore(STORE_SETTINGS)


fun provideMarketDao(database: MarketDatabase): MarketDao = database.marketDao()

fun provideDatabase(application: Application): MarketDatabase =
        MarketDatabase.getInstance(application)


private fun provideOkHttpClient(): OkHttpClient {

    return OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()

}


private fun provideRetrofit(baseUrl: String, moshi: Moshi, client: OkHttpClient) =
        Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(MyCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(baseUrl)
                .build()


private fun provideMoshi(): Moshi {
    return Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
}


private fun provideApiService(retrofit: Retrofit): Api =
        retrofit.create(Api::class.java)


private const val PREFERENCES_FILE_KEY = "storage"
private fun provideSessionPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)



