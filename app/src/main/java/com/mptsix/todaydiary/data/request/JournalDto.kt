package com.mptsix.todaydiary.data.request

data class JournalDto(
    var mainJournalContent: String, // Journal Content[내용]
    var journalLocation: String, // "위도, 경도"
    var journalCategory: String, // Category
    var journalWeather: String, // Weather
    var journalDate: Long // Timestamp
)