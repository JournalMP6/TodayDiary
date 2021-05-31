package com.mptsix.todaydiary.data.internal

data class DiaryWriteMode(
    var timeStamp: Long,
    var modeType: Boolean, // true for write, false for edit
)
