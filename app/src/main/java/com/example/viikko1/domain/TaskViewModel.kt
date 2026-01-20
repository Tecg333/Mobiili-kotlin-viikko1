package com.example.viikko1.domain

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    var allTasks: List<Task> by mutableStateOf(
        listOf(
            Task(1, "Example 1", "Description 1", 1, "2026-10-31", false),
            Task(2, "Example 2", "Description 2", 1, "2026-11-01", true),
            Task(3, "Example 3", "Description 3", 2, "2026-12-05", false)
        )
    )
        private set

    var name by mutableStateOf("")
        private set

    var filterType by mutableStateOf("All")

    fun onNameChange(newName: String) {
        name = newName
    }

    fun addTask() {
        if (name.isBlank()) return
        val newTask = Task(
            id = allTasks.size + 1,
            title = name,
            description = "description",
            priority = 1,
            dueDate = "2026-10-31",
            done = false
        )
        allTasks = addTask(allTasks, newTask)
        name = "" // clear the input
    }

    fun toggleDone(taskId: Int) {
        allTasks = toggleDone(allTasks, taskId)
    }

    fun sortTasks() {
        allTasks = sortByDueDate(allTasks)
    }

    fun filterTasks(filterType: String): List<Task> {
        return when (filterType) {
            "Done" -> filterByDone(allTasks, true)
            "Todo" -> filterByDone(allTasks, false)
            else -> allTasks
        }
    }
    fun deleteTask(taskId: Int) {
        allTasks = deleteTask(allTasks, taskId)
    }
}


