package com.mptsix.todaydiary.data.temp

import android.util.Log
import javax.inject.Inject

class TempJournalRepository @Inject constructor(
    private val tempJournalDao: TempJournalDao
) {
    private val logTag: String = this::class.java.simpleName

    suspend fun saveOrUpdate(inputJournal: TempJournal) {
        val result: List<TempJournal> =
            tempJournalDao.findByJournalDateAndUserId(inputJournal.journalDate, inputJournal.userId)
        if (result.isEmpty()) {
            // Result is empty. Probably we need to insert
            addTempJournal(inputJournal)
        } else {
            // Result is non-empty. Update it
            inputJournal.id = result[0].id
            updateSavedJournal(inputJournal)
        }
    }

    suspend fun findByJournalDateAndUserId(date: Long, id: String): TempJournal {
        val result: List<TempJournal> =
            tempJournalDao.findByJournalDateAndUserId(date, id)

        if (result.size != 1) {
            throw IllegalStateException("Error: Find result is not exactly 1! Found ${result.size}")
        }
        return result[0]
    }

    suspend fun addTempJournal(inputJournal: TempJournal) {
        Log.d(logTag, "Inserting Journal ID: ${inputJournal.journalDate}")
        tempJournalDao.addTempJournal(inputJournal)
    }

    suspend fun removeByDateAndId(inputJournalDate: Long, id: String) {
        Log.d(logTag, "Removing Journal ID: $inputJournalDate")
        tempJournalDao.removeSavedJournalByJournalDate(inputJournalDate, id)
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