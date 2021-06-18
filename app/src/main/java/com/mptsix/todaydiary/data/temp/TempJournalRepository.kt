package com.mptsix.todaydiary.data.temp

import android.util.Log
import javax.inject.Inject

class TempJournalRepository @Inject constructor(
    private val tempJournalDao: TempJournalDao
) {
    private val logTag: String = this::class.java.simpleName

    suspend fun addTempJournal(inputJournal: TempJournal) {
        Log.d(logTag, "Inserting Journal ID: ${inputJournal.journalDate}")
        tempJournalDao.addTempJournal(inputJournal)
    }

    suspend fun removeSavedJournalByJournalDate(inputJournalDate: Long) {
        Log.d(logTag, "Removing Journal ID: $inputJournalDate")
        tempJournalDao.removeSavedJournalByJournalDate(inputJournalDate)
    }

    suspend fun getAllSavedJournal(): List<TempJournal> {
        Log.w(logTag, "Accessing all user's temp-saved journal..")
        return tempJournalDao.getAllSavedJournal()
    }

    suspend fun findSavedJournalByJournalDate(inputJournalDate: Long): List<TempJournal> {
        Log.d(logTag, "Finding journal, date: $inputJournalDate")
        return tempJournalDao.findSavedJournalByJournalDate(inputJournalDate)
    }

    suspend fun updateSavedJournal(inputJournal: TempJournal) {
        Log.d(logTag, "Updating journal: ${inputJournal.id}, Date: ${inputJournal.journalDate}")
        tempJournalDao.updateSavedJournal(inputJournal)
    }
}