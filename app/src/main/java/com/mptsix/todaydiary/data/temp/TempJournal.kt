package com.mptsix.todaydiary.data.temp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_journal")
class TempJournal(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "mainJournalContent") var mainJournalContent: String,
    @ColumnInfo(name = "journalLocation") var journalLocation: String,
    @ColumnInfo(name = "journalCategory") var journalCategory: String,
    @ColumnInfo(name = "journalWeather") var journalWeather: String,
    @ColumnInfo(name = "journalDate") var journalDate: Long,
    @ColumnInfo(name = "journalImage") var journalImage: String?
)