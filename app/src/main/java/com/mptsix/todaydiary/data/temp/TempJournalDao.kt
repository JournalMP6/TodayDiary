package com.mptsix.todaydiary.data.temp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TempJournalDao {
    @Insert
    suspend fun addTempJournal(inputJournal: TempJournal)

    @Query("DELETE FROM saved_journal WHERE journalDate=:inputJournalDate")
    suspend fun removeSavedJournalByJournalDate(inputJournalDate: Long)

    @Query("SELECT * FROM saved_journal")
    suspend fun getAllSavedJournal(): List<TempJournal>

    @Query("SELECT * FROM saved_journal WHERE journalDate=:inputJournalDate")
    suspend fun findSavedJournalByJournalDate(inputJournalDate: Long): List<TempJournal>

    @Update
    suspend fun updateSavedJournal(inputJournal: TempJournal)
}