package com.elitecode.taskplan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elitecode.taskplan.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel: ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = listOf(
                Task("Constitution Day", "Feriado nacional", "1", "pendiente", "alta", "Todo el día", "#00FF00", "2025-02-05", "2025-02-05"),
                Task("Plan Telcel", "Pago mensual", "1", "completada", "media", "08:00 - 09:00", "#0000FF", "2025-02-17", "2025-02-17")
            )
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value + task
        }
    }
}