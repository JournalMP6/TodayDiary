package com.mptsix.todaydiary.data.internal

import com.mptsix.todaydiary.data.response.JournalCategoryResponse

data class UserSealed(
    var userId: String,
    var userName: String,
    var journalCategoryList: List<JournalCategoryResponse>,
    var journalList: List<JournalSealed>
)