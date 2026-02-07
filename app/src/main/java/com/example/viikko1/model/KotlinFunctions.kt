package com.example.viikko1.model

fun addTask(list: List<Task>, newTask: Task): List<Task> {
    return list + newTask
}

fun toggleDone(list: List<Task>, id: Int): List<Task> {
    return list.map { task ->
        if (task.id == id) {
            task.copy(done = !task.done)
        } else {
            task
        }
    }
}

fun filterByDone(list: List<Task>, done: Boolean): List<Task> {
    return list.filter { it.done == done }
}

fun sortByDueDate(list: List<Task>): List<Task> {
    return list.sortedBy { it.dueDate }
}

fun deleteTask(list: List<Task>, id: Int): List<Task> {
    return list.filter { it.id != id }
}

fun updateTask(list: List<Task>, updatedTask: Task): List<Task> {
    return list.map { task ->
        if (task.id == updatedTask.id) {
            updatedTask
        } else {
            task
        }
    }
}
