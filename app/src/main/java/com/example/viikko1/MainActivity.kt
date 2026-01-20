package com.example.viikko1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.viikko1.ui.theme.Viikko1Theme
import com.example.viikko1.domain.TaskViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items






class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Viikko1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
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
    Column(modifier = modifier.padding(16.dp)) {

        NameTextField(
            name = viewModel.name,
            onNameChange = viewModel::onNameChange
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
            items(viewModel.filterTasks(viewModel.filterType)) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${task.title} (${task.dueDate}) - ${if (task.done) "Done" else "ToDo"}"
                    )


                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Button(onClick = { viewModel.toggleDone(task.id) }) {
                            Text("Change Status")
                        }
                        Button(onClick = { viewModel.deleteTask(task.id) }) {
                            Text("Delete")
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
