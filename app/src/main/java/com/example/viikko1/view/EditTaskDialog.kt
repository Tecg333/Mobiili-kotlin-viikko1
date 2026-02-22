package com.example.viikko1.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikko1.data.model.Task
import com.example.viikko1.ui.DatePickerModal
import java.util.Calendar

@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, dueDate: Long?, isCompleted: Boolean) -> Unit,
    onDelete: (taskId: Int) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var dueDateMillis by remember { mutableStateOf(task.dueDate) }
    var isCompleted by remember { mutableStateOf(task.isCompleted) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Format the date for display
    val dateDisplay = remember(dueDateMillis) {
        if (dueDateMillis != null) {
            val cal = Calendar.getInstance().apply { timeInMillis = dueDateMillis!! }
            "%02d.%02d.%04d".format(
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR)
            )
        } else "No date set"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(onClick = { showDatePicker = true }) {
                    Text("Due Date: $dateDisplay")
                }

                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = { millis ->
                            if (millis != null) {
                                dueDateMillis = millis
                            }
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false }
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Completed")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(title, description, dueDateMillis, isCompleted)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Row {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onDelete(task.id) }) {
                    Text("Delete")
                }
            }
        }
    )
}
