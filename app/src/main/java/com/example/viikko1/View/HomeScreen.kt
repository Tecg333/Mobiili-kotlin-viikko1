package com.example.viikko1.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikko1.ui.theme.Viikko1Theme
import androidx.compose.ui.graphics.Color
import com.example.viikko1.ViewModel.TaskViewModel
import com.example.viikko1.domain.Model.Task


@Composable
fun NameTextField(
    name: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Name") },

        )
}



@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel()
) {
    val taskToEdit: MutableState<Task?> = remember { mutableStateOf(null) }

// Show dialog only if value is not null
    taskToEdit.value?.let { task ->
        EditTaskDialog(
            task = task,
            onDismiss = { taskToEdit.value = null },
            onSave = { newDescription, newDueDate ->
                viewModel.updateTask(task.id, newDescription, newDueDate)
                taskToEdit.value = null
            }
        )
    }

    Column(modifier = modifier.padding(16.dp)) {


        NameTextField(
            name = viewModel.name,
            onNameChange = viewModel:: onNameChange
        )

        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = { viewModel.addTask() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

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


        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(viewModel.filterTasks(viewModel.filterType)) {
                    task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),

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
                            Text(text = "${task.description} - Due: ${task.dueDate}", color = Color.Gray)
                            Text(text = if (task.done) "Done" else "Todo")
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.toggleDone(task.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (task.done) Color(0xFF4CAF50) else Color(0xFFFF9800)
                                )
                            ) {
                                Text(if (task.done) "Mark Todo" else "Mark Done")
                            }

                            Button(
                                onClick = { taskToEdit.value = task },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                            ) {
                                Text("Edit")
                            }



                            Button(
                                onClick = { viewModel.deleteTask(task.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Viikko1Theme {
        HomeScreen()
    }
}
