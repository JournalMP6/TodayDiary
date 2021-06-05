package com.mptsix.todaydiary.transition

data class Transition(
    var displayTransition: DisplayTransition,
    var data: Any? = null
)

enum class DisplayTransition {
    REQUEST_EDIT, REQUEST_ADD, REQUEST_DIARY, REQUEST_USERINFO
}