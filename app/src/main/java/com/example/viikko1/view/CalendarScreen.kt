package com.example.viikko1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import com.example.viikko1.model.Task
import com.example.viikko1.viewmodel.TaskViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: TaskViewModel,
    onTaskClick: (Int) -> Unit = {},
    onNavigateHome: () -> Unit,
    onBack: () -> Unit
) {
    val tasks by viewModel.allTasks.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState()
    val showAddDialog by viewModel.addTaskDialogVisible.collectAsState()

    //Group tasks by dueDate
    val grouped = tasks.groupBy { it.dueDate ?: "No date" }

    Column(modifier = Modifier.padding(16.dp)) {


        TopAppBar(
            title = { Text("Calendar") },

            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                }

            },
            actions = {
                IconButton(onClick = onNavigateHome) {
                    Icon(Icons.Filled.Home, contentDescription = "Go to list")
                }
                IconButton(onClick = { viewModel.addTaskDialogVisible.value = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }
            }
        )

        //LazyColumn for tasks grouped by date
        LazyColumn {
            grouped.forEach { (date, tasksOfDay) ->

                // Date header
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                // Tasks for this date
                items(tasksOfDay) { task ->
                    CalendarTaskCard(
                        task = task,
                        onTaskClick = onTaskClick
                    )
                }
            }
        }
    }

    //Add Task Dialog
    if (showAddDialog) {
        AddTaskDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.addTaskDialogVisible.value = false }
        )
    }
    //Edit dialog for selected task
    selectedTask?.let { task ->
        EditTaskDialog(
            task = task,
            onDismiss = { viewModel.closeTask() },
            onSave = { newTitle, newDescription, newDueDate, newDone ->
                viewModel.updateTask(
                    taskId = task.id,
                    newTitle = newTitle,
                    newDescription = newDescription,
                    newDueDate = newDueDate,
                    done = newDone
                )
                viewModel.closeTask()
            },
            onDelete = { id ->
                viewModel.deleteTask(id)
                viewModel.closeTask()
            }
        )
    }

}





@Composable
fun CalendarTaskCard(
    task: Task,
    onTaskClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onTaskClick(task.id) },
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (task.done) androidx.compose.ui.graphics.Color(0xFFD6FFD6) else androidx.compose.ui.graphics.Color.White
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(task.title, style = MaterialTheme.typography.titleMedium)
            if (task.description.isNotBlank()) {
                Text(task.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}