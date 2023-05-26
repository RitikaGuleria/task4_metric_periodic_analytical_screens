package com.example.task_periodicscreen

import java.time.Month

data class UIData constructor(val month: String)

val uiList = arrayListOf<UIData>(
    UIData("January"),
    UIData("Februray"),
    UIData("March"),
    UIData("April")
)
