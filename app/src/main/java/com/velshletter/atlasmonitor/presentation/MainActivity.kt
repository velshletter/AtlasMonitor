package com.velshletter.atlasmonitor.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.data.ServiceStateCheckerImpl
import com.example.data.data.SharedPrefRepositoryImpl
import com.example.data.data.WebsiteRepositoryImpl
import com.example.domain.models.ResponseState
import com.example.domain.repository.SharedPrefRepository
import com.example.domain.usecase.StartMonitorUseCase
import com.velshletter.atlasmonitor.presentation.notification.AndroidNotificationSender
import com.velshletter.atlasmonitor.presentation.screens.MainView
import com.velshletter.atlasmonitor.presentation.screens.SecondScreen
import com.velshletter.atlasmonitor.presentation.service.AndroidServiceManager
import com.velshletter.atlasmonitor.presentation.viewmodels.MainViewModel
import com.velshletter.atlasmonitor.presentation.viewmodels.MainViewModelFactory
import com.velshletter.atlasmonitor.ui.theme.AtlasMonitorTheme

class MainActivity : ComponentActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val sharedPrefRepositoryImpl: SharedPrefRepository = SharedPrefRepositoryImpl(applicationContext)
        val serviceStateChecker = ServiceStateCheckerImpl(applicationContext)
        val websiteRepository = WebsiteRepositoryImpl()
        val notificationSender = AndroidNotificationSender(applicationContext)
        val serviceManager = AndroidServiceManager(applicationContext)
        val startMonitorUseCase = StartMonitorUseCase(
            websiteRepository = websiteRepository,
            notificationSender = notificationSender,
            serviceManager = serviceManager,
            serviceStateChecker = serviceStateChecker
        )

        val viewModelFactory = MainViewModelFactory(startMonitorUseCase, sharedPrefRepositoryImpl, websiteRepository)
        val mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        if (serviceStateChecker.isServiceRunning()) {
            mainViewModel.loadSavedData()
        }

        onCreateNotificationChannel()
        setContent {
            val navController = rememberNavController()
            AtlasMonitorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(mainViewModel, navController, serviceManager)
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
        lifecycleScope.launchWhenStarted {
            mainViewModel.showToast.collect { message ->
                message.let {
                    if (!message.isNullOrEmpty()) {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                        mainViewModel.clearToastMessage()
                    }
                }
            }
        }
    }

    private fun onCreateNotificationChannel() {
        val channel = NotificationChannel(
            "channel",
            "Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@Composable
fun Navigation(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    serviceManager: AndroidServiceManager,
    startDestination: String = "main_screen",
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("main_screen") { MainView(mainViewModel) }
        composable("sec_screen") { SecondScreen(mainViewModel, serviceManager) }
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