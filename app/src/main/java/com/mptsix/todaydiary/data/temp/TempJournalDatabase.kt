package com.mptsix.todaydiary.data.temp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TempJournal::class],
    version = 1
)
abstract class TempJournalDatabase: RoomDatabase() {
    abstract fun getTempJournalDao(): TempJournalDao
}