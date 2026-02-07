package com.example.viikko1.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikko1.ui.DatePickerModal
import com.example.viikko1.viewmodel.TaskViewModel
import java.util.Calendar


@Composable
fun AddTaskDialog(
    viewModel: TaskViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { showDatePicker = true }) {
                    Text(if (dueDate.isEmpty()) "Select Due Date" else "Due Date: $dueDate")
                }

                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = { millis ->
                            if (millis != null) {
                                val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                                dueDate = "%02d.%02d.%04d".format(
                                    calendar.get(Calendar.DAY_OF_MONTH),
                                    calendar.get(Calendar.MONTH) + 1,
                                    calendar.get(Calendar.YEAR)
                                )
                            }
                            showDatePicker = false // Close date picker after selection
                        },
                        onDismiss = { showDatePicker = false }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank()) {
                    viewModel.onNameChange(title)
                    viewModel.addTask(title, description, dueDate)
                }
                onDismiss() // Close AddTaskDialog
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
