package dev.thunderbolt.dogbreeds.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.thunderbolt.dogbreeds.data.local.DogBreedDb
import dev.thunderbolt.dogbreeds.data.remote.DogBreedApi
import dev.thunderbolt.dogbreeds.data.repository.DogBreedRepositoryImpl
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDogBreedDb(@ApplicationContext context: Context): DogBreedDb {
        return Room.databaseBuilder(
            context,
            DogBreedDb::class.java,
            "dog_breed.db",
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideDogBreedApi(client: OkHttpClient): DogBreedApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(DogBreedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDogBreedRepository(
        api: DogBreedApi,
        db: DogBreedDb,
    ): DogBreedRepository {
        return DogBreedRepositoryImpl(
            api = api,
            db = db,
        )
    }
}
