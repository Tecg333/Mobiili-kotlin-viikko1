package com.example.viikko1.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.viikko1.domain.Model.Task
import com.example.viikko1.domain.Model.filterByDone
import com.example.viikko1.domain.Model.sortByDueDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class TaskViewModel : ViewModel() {
    var allTasks: MutableState<List<Task>> = mutableStateOf(
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
            id = allTasks.value.size + 1,
            title = name,
            description = "description",
            priority = 1,
            dueDate = "2026-10-31",
            done = false
        )
        allTasks.value = com.example.viikko1.domain.Model.addTask(allTasks.value, newTask)
        name = "" // clear the input
    }

    fun toggleDone(taskId: Int) {
        allTasks.value = com.example.viikko1.domain.Model.toggleDone(allTasks.value, taskId)
    }

    fun sortTasks() {
        allTasks.value = sortByDueDate(allTasks.value)
    }

    fun filterTasks(filterType: String): List<Task> {
        return when (filterType) {
            "Done" -> filterByDone(allTasks.value, true)
            "Todo" -> filterByDone(allTasks.value, false)
            else -> allTasks.value
        }
    }
    fun deleteTask(taskId: Int) {
        allTasks.value = com.example.viikko1.domain.Model.deleteTask(allTasks.value, taskId)
    }

    fun updateTask(taskId: Int, newDescription: String, newDueDate: String) {
        val updatedTask = allTasks.value.firstOrNull { it.id == taskId }?.copy(
            description = newDescription,
            dueDate = newDueDate
        )?: return
            allTasks.value =
                com.example.viikko1.domain.Model.updateTask(allTasks.value, updatedTask)
    }
}