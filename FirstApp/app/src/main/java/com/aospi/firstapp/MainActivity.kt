package com.aospi.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aospi.firstapp.ui.theme.FirstAppTheme
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstAppTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    var showSecondScreen by remember { mutableStateOf(false) }

    if (showSecondScreen) {
        SecondScreen(onBack = { showSecondScreen = false })
    } else {
        MainScreen(onNavigate = { showSecondScreen = true })
    }
}

@Composable
fun MainScreen(onNavigate: () -> Unit) {
    var name by remember { mutableStateOf("Android") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color(0xFF7adfd1))
        ) {

            Spacer(modifier = Modifier.height(150.dp))

            Greeting(name = name)

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    name = if (name == "Android") "Victor" else "Android"
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Click Me", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = onNavigate,
                modifier = Modifier
                    .width(220.dp)
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "Let's control a led",
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    color = Color(0xFF150aff)

                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hello $name!",
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF000000)
        )
    }
}

@Composable
fun SecondScreen(onBack: () -> Unit) {
    var stateLed by remember { mutableStateOf("On") }
    var led by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFF7adfd1)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Second Screen",
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF310099)
                )
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .width(150.dp)
                        .height(100.dp))
                {
                    Text("Back",
                            fontSize = 30.sp)

                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(onClick = {
                    led = !led
                    stateLed = if (led) "On" else "Off"

                    val gpioState = if (led) 1 else 0
                    val command = "service call android.hardware.gpio.IGpio/default 1 i32 17 i32 $gpioState"

                    scope.launch {
                        val output = runShellCommand(command)
                        println("Shell Output:\n$output")
                    }

                },
                    modifier = Modifier
                        .width(150.dp)
                        .height(100.dp))
                {
                    Text(stateLed,
                        fontSize = 30.sp)
                }
            }
        }
    }
}

fun runShellCommand(command: String): String {
    return try {
        val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
        val output = process.inputStream.bufferedReader().use(BufferedReader::readText)
        val error = process.errorStream.bufferedReader().use(BufferedReader::readText)
        process.waitFor()
        output + if (error.isNotEmpty()) "\nError: $error" else ""
    } catch (e: Exception) {
        e.printStackTrace()
        "Error: ${e.message}"
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirstAppTheme {
        App()
    }
}
