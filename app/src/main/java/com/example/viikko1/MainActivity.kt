package com.example.viikko1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.viikko1.ui.theme.Viikko1Theme
import com.example.viikko1.view.HomeScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.viikko1.routes.ROUTE_CALENDAR
import com.example.viikko1.routes.ROUTE_HOME
import com.example.viikko1.view.CalendarScreen
import com.example.viikko1.viewmodel.TaskViewModel
import com.example.viikko1.data.local.AppDatabase
import com.example.viikko1.data.repository.TaskRepository


class MainActivity : ComponentActivity() {

    // Luo tietokanta lazy-patternilla (vasta kun sitä tarvitaan)
    private val database by lazy {
        AppDatabase.getDatabase(applicationContext)
    }

    // Luo Repository, joka käyttää DAO:a tietokantaoperaatioihin
    private val repository by lazy {
        TaskRepository(database.taskDao())
    }

    // Luo ViewModel ViewModelProvider.Factory:n avulla
    private val viewModel: TaskViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle ?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

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
                                onNavigateToCalendar = { navController.navigate(ROUTE_CALENDAR) }
                            )
                        }

                        composable(ROUTE_CALENDAR) {
                            CalendarScreen(
                                viewModel = viewModel,
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
