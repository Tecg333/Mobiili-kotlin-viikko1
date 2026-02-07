package com.example.viikko1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.viikko1.model.Task
import com.example.viikko1.viewmodel.TaskViewModel

@Composable
fun NameTextField(
    name: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel,
    onNavigateToCalendar: () -> Unit = {},
    onTaskClick: (Int) -> Unit = {},
    onAddClick: () -> Unit = {},
) {
    val allTasks by viewModel.allTasks.collectAsState()
    val showAddDialog by viewModel.addTaskDialogVisible.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState() // Unified state from ViewModel

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Tasks") },
                actions = {
                    IconButton(onClick = onNavigateToCalendar) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Calendar")
                    }
                    IconButton(onClick = { viewModel.addTaskDialogVisible.value = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Task")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { viewModel.filterType = "All" }) { Text("All") }
                Button(onClick = { viewModel.filterType = "Done" }) { Text("Done") }
                Button(onClick = { viewModel.filterType = "Todo" }) { Text("Todo") }
                Button(onClick = { viewModel.sortTasks() }) { Text("Sort") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val filteredTasks = when (viewModel.filterType) {
                "Done" -> allTasks.filter { it.done }
                "Todo" -> allTasks.filter { !it.done }
                else -> allTasks
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filteredTasks) { task: Task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                viewModel.openTask(task.id) // Opens via ViewModel
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (task.done) Color(0xFFD6FFD6) else Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = task.title)
                                Text(
                                    text = "${task.description} - Due: ${task.dueDate}",
                                    color = Color.Gray
                                )
                            }

                            Checkbox(
                                checked = task.done,
                                onCheckedChange = { viewModel.toggleDone(task.id) }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { viewModel.deleteTask(task.id) }
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }

            if (showAddDialog) {
                AddTaskDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.addTaskDialogVisible.value = false }
                )
            }

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
    }
}
