package com.mptsix.todaydiary.hilt

import android.content.Context
import androidx.room.Room
import com.mptsix.todaydiary.data.login.LoginSessionDao
import com.mptsix.todaydiary.data.login.LoginSessionDatabase
import com.mptsix.todaydiary.data.login.LoginSessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseProvider {
    @Singleton
    @Provides
    fun provideLoginSessionDatabase(@ApplicationContext context: Context): LoginSessionDatabase {
        return Room.databaseBuilder(
            context,
            LoginSessionDatabase::class.java,
            "login_session.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideLoginSessionDao(database: LoginSessionDatabase): LoginSessionDao {
        return database.getLoginSessionDao()
    }

    @Singleton
    @Provides
    fun provideLoginSessionRepository(loginSessionDao: LoginSessionDao): LoginSessionRepository {
        return LoginSessionRepository(loginSessionDao)
    }

}