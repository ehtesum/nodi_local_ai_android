package com.localai.nodi.core.di

import android.content.Context
import androidx.room.Room
import com.localai.nodi.data.storage.db.EncryptedDatabase
import com.localai.nodi.data.storage.db.NodiDao
import com.localai.nodi.engine.llm.LlamaCppEngineImpl
import com.localai.nodi.engine.llm.LlmEngine
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProvidesModule {

    @Provides
    @Singleton
    fun provideEncryptedDatabase(@ApplicationContext context: Context): EncryptedDatabase {
        val passPhrase = SQLiteDatabase.getBytes("nodi_offline_secure_key_256".toCharArray())
        val factory = SupportFactory(passPhrase)
        return Room.databaseBuilder(
            context,
            EncryptedDatabase::class.java,
            "nodi_encrypted.db"
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNodiDao(database: EncryptedDatabase): NodiDao {
        return database.nodiDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class EngineBindsModule {

    @Binds
    @Singleton
    abstract fun bindLlmEngine(impl: LlamaCppEngineImpl): LlmEngine
}
