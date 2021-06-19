package com.mptsix.todaydiary.hilt

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.mptsix.todaydiary.data.login.LoginSessionDao
import com.mptsix.todaydiary.data.login.LoginSessionDatabase
import com.mptsix.todaydiary.data.login.LoginSessionRepository
import com.mptsix.todaydiary.data.temp.TempJournalDao
import com.mptsix.todaydiary.data.temp.TempJournalDatabase
import com.mptsix.todaydiary.data.temp.TempJournalRepository
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
class DatabaseProvider {

    private fun generateRandomPassword(): String {
        val availableString: String =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#%^&*0123456789"
        return (0..512).map { availableString.random() }.joinToString("")
    }

    private fun getPassword(context: Context): String {
        val genPassword: String = generateRandomPassword()

        val sharedPreferences: SharedPreferences = context.getSharedPreferences("com.mptsix.todaydiary", Context.MODE_PRIVATE)
        var keyExists: String = sharedPreferences.getString("com.mptsix.todaydiary.MAIN_KEY", "NON_EXISTS") ?: "NON_EXISTS"
        if (keyExists == "NON_EXISTS") {
            with (sharedPreferences.edit()) {
                putString("com.mptsix.todaydiary.MAIN_KEY", genPassword)
                commit()
            }
            keyExists = genPassword
        }

        return keyExists
    }

    @Singleton
    @Provides
    fun provideLoginSessionDatabase(@ApplicationContext context: Context): LoginSessionDatabase {
        val passwordObject: String = getPassword(context)

        val cipherFactory: SupportFactory = SupportFactory(SQLiteDatabase.getBytes(passwordObject.toCharArray()))
        return Room.databaseBuilder(
            context,
            LoginSessionDatabase::class.java,
            "login_session.db"
        )
        .openHelperFactory(cipherFactory)
        .build()
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

    @Singleton
    @Provides
    fun provideTempJournalDatabase(@ApplicationContext context: Context): TempJournalDatabase {
        val passwordObject: String = getPassword(context)
        val cipherFactory: SupportFactory = SupportFactory(SQLiteDatabase.getBytes(passwordObject.toCharArray()))
        return Room.databaseBuilder(
            context,
            TempJournalDatabase::class.java,
            "temp_journal.db"
        )
        .openHelperFactory(cipherFactory)
        .build()
    }

    @Singleton
    @Provides
    fun provideTempJournalDao(tempJournalDatabase: TempJournalDatabase): TempJournalDao {
        return tempJournalDatabase.getTempJournalDao()
    }

    @Singleton
    @Provides
    fun provideTempJournalRepository(tempJournalDao: TempJournalDao): TempJournalRepository {
        return TempJournalRepository(tempJournalDao)
    }
}