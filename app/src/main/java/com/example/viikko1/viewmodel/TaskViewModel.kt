package com.example.viikko1.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.viikko1.model.Task
import com.example.viikko1.model.filterByDone
import com.example.viikko1.model.sortByDueDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.viikko1.model.mockTasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class TaskViewModel : ViewModel() {

    private val _allTasks = MutableStateFlow<List<Task>> (value = emptyList())

    val allTasks: StateFlow<List<Task>> = _allTasks

    init {
        _allTasks.value = mockTasks
    }
    private val _selectedTask = MutableStateFlow<Task?>(null)

    val selectedTask: StateFlow<Task?> = _selectedTask

    val addTaskDialogVisible = MutableStateFlow<Boolean>(value = false)


    var name by mutableStateOf("")
        private set

    var filterType by mutableStateOf("All")

    fun onNameChange(newName: String) {
        name = newName
    }

    fun addTask(title: String, description: String, dueDate: String) {
        if (name.isBlank()) return
        val newTask = Task(
            id = _allTasks.value.size + 1,
            title = name,
            description = description,
            priority = 1,
            dueDate = dueDate,
            done = false
        )
        _allTasks.value = com.example.viikko1.model.addTask(allTasks.value, newTask)
        name = ""
    }

    fun toggleDone(taskId: Int) {
        _allTasks.value = com.example.viikko1.model.toggleDone(allTasks.value, taskId)
    }

    fun sortTasks() {
        _allTasks.value = sortByDueDate(_allTasks.value)
    }

    fun filterTasks(filterType: String): List<Task> {
        return when (filterType) {
            "Done" -> filterByDone(_allTasks.value, true)
            "Todo" -> filterByDone(_allTasks.value, false)
            else -> _allTasks.value
        }
    }
    fun deleteTask(taskId: Int) {
        _allTasks.value = com.example.viikko1.model.deleteTask(_allTasks.value, taskId)
    }

    fun updateTask(taskId: Int, newTitle: String, newDescription: String, newDueDate: String, done: Boolean) {
        val updatedTask = _allTasks.value.firstOrNull { it.id == taskId }?.copy(
            title = newTitle,
            description = newDescription,
            dueDate = newDueDate,
            done = done
        ) ?: return
        _allTasks.value = com.example.viikko1.model.updateTask(_allTasks.value, updatedTask)
    }



    fun openTask(taskId: Int) {
        val task = _allTasks.value.find { it.id == taskId }
        _selectedTask.value = task
    }


    fun closeTask() {
        _selectedTask.value = null
    }

}
