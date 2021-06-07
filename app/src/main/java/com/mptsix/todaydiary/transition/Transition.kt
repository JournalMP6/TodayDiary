package com.mptsix.todaydiary.transition

data class Transition(
    var displayTransition: DisplayTransition,
    var data: Any? = null
)

enum class DisplayTransition {
    REQUEST_EDIT, REQUEST_SEARCH, REQUEST_DIARY, REQUEST_USERINFO
}