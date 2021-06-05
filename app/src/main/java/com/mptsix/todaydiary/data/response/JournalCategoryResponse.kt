package com.mptsix.todaydiary.data.response

import com.mptsix.todaydiary.data.internal.JournalCategory

data class JournalCategoryResponse(
    var category: JournalCategory,
    var count: Int
)