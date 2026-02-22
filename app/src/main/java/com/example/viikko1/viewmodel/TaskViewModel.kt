package com.example.viikko1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viikko1.data.model.Task
import com.example.viikko1.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Automatically updates UI when database changes via the repository
    val allTasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    val addTaskDialogVisible = MutableStateFlow(false)

    var filterType = MutableStateFlow("All")

    fun addTask(title: String, description: String, dueDate: Long?) {
        viewModelScope.launch {
            val newTask = Task(
                title = title,
                description = description,
                dueDate = dueDate,
                isCompleted = false
            )
            repository.insertTask(newTask)
        }
    }

    fun toggleDone(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            if (task != null) {
                repository.deleteTask(task)
            }
        }
    }

    fun updateTask(taskId: Int, newTitle: String, newDescription: String, newDueDate: Long?, done: Boolean) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            if (task != null) {
                val updatedTask = task.copy(
                    title = newTitle,
                    description = newDescription,
                    dueDate = newDueDate,
                    isCompleted = done
                )
                repository.updateTask(updatedTask)
            }
        }
    }

    fun openTask(taskId: Int) {
        viewModelScope.launch {
            _selectedTask.value = repository.getTaskById(taskId)
        }
    }

    fun closeTask() {
        _selectedTask.value = null
    }
}
