package com.example.viikko1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.material3.TopAppBar
import com.example.viikko1.data.model.Task
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

    // 1️⃣ Group tasks by formatted dueDate
    val grouped = tasks.groupBy { task ->
        task.dueDate?.let { millis ->
            val cal = Calendar.getInstance().apply { timeInMillis = millis }
            "%02d.%02d.%04d".format(
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR)
            )
        } ?: "No date"
    }

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

        // 2️⃣ LazyColumn for tasks grouped by date
        LazyColumn {
            grouped.forEach { (date, tasksOfDay) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(tasksOfDay) { task ->
                    CalendarTaskCard(
                        task = task,
                        onTaskClick = { onTaskClick(task.id) }
                    )
                }
            }
        }
    }

    // 3️⃣ Add Task Dialog
    if (showAddDialog) {
        AddTaskDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.addTaskDialogVisible.value = false }
        )
    }

    // 4️⃣ Edit dialog for selected task
    selectedTask?.let { task ->
        EditTaskDialog(
            task = task,
            onDismiss = { viewModel.closeTask() },
            onSave = { title, desc, date, completed ->
                viewModel.updateTask(task.id, title, desc, date, completed)
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
    onTaskClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onTaskClick() },
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (task.isCompleted) androidx.compose.ui.graphics.Color(0xFFD6FFD6) else androidx.compose.ui.graphics.Color.White
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
