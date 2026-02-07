package com.example.viikko1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.viikko1.ui.theme.Viikko1Theme
import com.example.viikko1.view.HomeScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.viikko1.routes.ROUTE_CALENDAR
import com.example.viikko1.routes.ROUTE_HOME
import com.example.viikko1.view.CalendarScreen
import com.example.viikko1.viewmodel.TaskViewModel


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle ?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: TaskViewModel = viewModel()

            Viikko1Theme {
                Scaffold {
                        innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_HOME,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(ROUTE_HOME) {
                            HomeScreen(
                                viewModel = viewModel,
                                onTaskClick = { taskId ->
                                    viewModel.openTask(taskId)
                                },
                                onAddClick = {
                                    viewModel.addTaskDialogVisible.value = true
                                },
                                onNavigateToCalendar = { navController.navigate(ROUTE_CALENDAR) }
                            )
                        }

                        composable(ROUTE_CALENDAR) {
                            CalendarScreen(
                                viewModel = viewModel,
                                onTaskClick = { taskId ->
                                    viewModel.openTask(taskId)
                                },
                                onNavigateHome = { navController.navigate(ROUTE_HOME) },
                                onBack = { navController.popBackStack() }

                            )
                        }
                    }
                }
            }
        }
    }
}
