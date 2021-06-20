package com.mptsix.todaydiary.data.response

import org.bson.types.Binary

data class Journal(
    var mainTitle: String,
    var mainJournalContent: String, // Journal Content[내용]
    var journalLocation: String, // "위도, 경도"
    var journalCategory: JournalCategory, // Category
    var journalWeather: String, // Weather
    var journalDate: Long, // Timestamp
    var journalImage: JournalImage = JournalImage() // Image attribute, default null
)

data class JournalImage(
    var imageFile: String? = null
)