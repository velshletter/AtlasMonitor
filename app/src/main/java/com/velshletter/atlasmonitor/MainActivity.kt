package com.velshletter.atlasmonitor

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.velshletter.atlasmonitor.domain.models.ResponseState
import com.velshletter.atlasmonitor.presentation.MainView
import com.velshletter.atlasmonitor.presentation.MainViewModel
import com.velshletter.atlasmonitor.presentation.SecondScreen
import com.velshletter.atlasmonitor.ui.theme.AtlasMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel: MainViewModel by viewModels();

        setContent {
            val navController = rememberNavController()
            AtlasMonitorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(mainViewModel, navController)
                }
            }

            val responseState by mainViewModel.responseState.collectAsState()
            when (responseState) {
                is ResponseState.Waiting -> {
                    if (navController.currentDestination?.route != "main_screen") {
                        navController.navigateUp()
                    }
                }
                is ResponseState.Loading -> {
                    LoadingIndicator()
                }
                is ResponseState.Success -> {
                    navController.navigate("sec_screen")
                }
                is ResponseState.Error -> {
                    Toast.makeText(
                        applicationContext,
                        (responseState as ResponseState.Error).description,
                        Toast.LENGTH_SHORT
                    ).show()
                    mainViewModel.updateResponseState(ResponseState.Waiting)
                }
            }
        }
    }
}

@Composable
fun Navigation(mainViewModel: MainViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") { MainView(mainViewModel, navController) }
        composable("sec_screen") { SecondScreen(mainViewModel) }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = Color.Blue
        )
    }
}