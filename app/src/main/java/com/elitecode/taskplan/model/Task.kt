package com.elitecode.taskplan.model

import androidx.compose.ui.graphics.Color

data class Task(
    val title : String,
    val description : String,
    val status : String,
    val priority : String,
    val time : String,
    val color : String,
    val deadline : String,
    val created_at : String,
    val user_id : String,
)