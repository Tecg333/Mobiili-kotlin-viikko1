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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.viikko1.domain.mockTasks
import com.example.viikko1.ui.theme.Viikko1Theme
import com.example.viikko1.domain.filterByDone
import com.example.viikko1.domain.sortByDueDate
import com.example.viikko1.domain.toggleDone

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
fun HomeScreen(modifier: Modifier = Modifier) {
    var allTasks by remember { mutableStateOf(mockTasks) }
    var filterType by remember { mutableStateOf("All") }

    val tasksToShow = when (filterType) {
        "Done" -> filterByDone(allTasks, true)
        "Todo" -> filterByDone(allTasks, false)
        else -> allTasks
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Current View: $filterType")
        
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { filterType = "All" }) { Text("All") }
            Button(onClick = { filterType = "Done" }) { Text("Done") }
            Button(onClick = { filterType = "Todo" }) { Text("Todo") }
            Button(onClick = { allTasks = sortByDueDate(allTasks) }) { Text("Sort") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        tasksToShow.forEach { task ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${task.title} (${task.dueDate}) - ${if(task.done) "Done" else "ToDo"}")
                Spacer(Modifier.weight(1f))
                Button(onClick = { 
                    allTasks = toggleDone(allTasks, task.id) 
                }) {
                    Text("Toggle")
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
